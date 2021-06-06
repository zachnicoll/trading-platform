import com.google.gson.Gson;
import data.TradesHandlerDataGenerator;
import errors.JsonError;
import models.OpenTrade;
import models.TradeType;
import models.partial.PartialOpenTrade;
import models.partial.PartialReadableOpenTrade;
import models.partial.PartialReadableResolvedTrade;
import org.junit.jupiter.api.*;
import server.RestApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradesHandlerTests {

    private static RestApi restApi;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    TradesHandlerDataGenerator tradesHandlerDataGenerator;
    private HttpRequest.Builder httpBuilder;
    private final String requestURL = "http://localhost:8000/trades/";

    @BeforeAll
    static void startApi() throws IOException {
        restApi = new RestApi();
        restApi.start();
    }

    @AfterAll
    static void stopApi() throws IOException {
        restApi.stop();
    }

    @BeforeEach
    public void setupHttpClient() throws IOException, InterruptedException, SQLException {
        tradesHandlerDataGenerator = new TradesHandlerDataGenerator();
        httpBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tradesHandlerDataGenerator.authenticationToken.toString());
    }

    /**
     * Test 1 - Create a BUY Trade without error
     */
    @Test
    public void createTrade() throws IOException, InterruptedException, SQLException {
        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.BUY,
                tradesHandlerDataGenerator.orgUnit1Id,
                tradesHandlerDataGenerator.assetType1Id,
                10,
                1.0f
        );

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialTrade))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned trade information is correct/reflects what was sent in request
        OpenTrade responseTrade = gson.fromJson(response.body(), OpenTrade.class);
        assertEquals(partialTrade.assetTypeId, responseTrade.getAssetType());
        assertEquals(partialTrade.organisationalUnitId, responseTrade.getOrganisationalUnit());
        assertEquals(partialTrade.quantity, responseTrade.getQuantity());
        assertEquals(partialTrade.pricePerAsset, responseTrade.getPricePerAsset());
    }

    /**
     * Test 2 - Create a BUY Trade with < 0 quantity of the AssetType
     */
    @Test
    public void createTradeInvalidQuantity() throws IOException, InterruptedException {
        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.BUY,
                tradesHandlerDataGenerator.orgUnit1Id,
                tradesHandlerDataGenerator.assetType1Id,
                -1,
                1.0f
        );

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialTrade))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed with correct response status code
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Quantity is less than or equal to 0").getError(), responseError.getError());
    }

    /**
     * Test 3 - Create a BUY Trade with PricePerAsset < 0
     */
    @Test
    public void createTradeInvalidPrice() throws IOException, InterruptedException {
        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.BUY,
                tradesHandlerDataGenerator.orgUnit1Id,
                tradesHandlerDataGenerator.assetType1Id,
                10,
                -1.0f
        );

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialTrade))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed with correct response status code
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("PricePerAsset is less than or equal to 0").getError(), responseError.getError());
    }

    /**
     * Test 5 - User does not belong to the OrgUnit they are creating the Trade for
     */
    @Test
    public void createTradeInvalidOrgUnit() throws IOException, InterruptedException {
        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.BUY,
                UUID.randomUUID(),
                tradesHandlerDataGenerator.assetType1Id,
                10,
                1.0f
        );

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialTrade))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed with correct response status code
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("You must belong to the Organisational Unit you are placing the Trade for").getError(), responseError.getError());
    }

    /**
     * Test 6 - OrgUnit does not have enough CreditBalance to place BUY order
     */
    @Test
    public void createTradeInvalidOrgUnitBalance() throws IOException, InterruptedException {
        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.BUY,
                tradesHandlerDataGenerator.orgUnit1Id,
                tradesHandlerDataGenerator.assetType1Id,
                1,
                1001.0f // test org unit balance = 1000
        );

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialTrade))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed with correct response status code
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Organisational Unit does not have enough credits to place this order").getError(), responseError.getError());
    }

    /**
     * Test 7 - OrgUnit does not have enough quantity of AssetType to place SELL order
     */
    @Test
    public void createTradeInvalidOrgUnitQuantity() throws IOException, InterruptedException {
        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.SELL,
                tradesHandlerDataGenerator.orgUnit1Id,
                tradesHandlerDataGenerator.assetType1Id, // test org unit does not have any assets
                10,
                1.0f
        );

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialTrade))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed with correct response status code
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Organisational Unit does not have enough of the given Asset Type to place this order").getError(), responseError.getError());
    }

    /**
     * Test 8 - Get all Trade history
     */
    @Test
    public void getAllTradeHistory() throws IOException, InterruptedException {
        HttpRequest request = httpBuilder.uri(URI.create(requestURL + "history/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        PartialReadableResolvedTrade[] resolvedTrades = gson.fromJson(response.body(), PartialReadableResolvedTrade[].class);
        Object[] buyUuidArr = Arrays.stream(resolvedTrades).map(PartialReadableResolvedTrade::getBuyTradeId).toArray();
        Object[] sellUuidArr = Arrays.stream(resolvedTrades).map(PartialReadableResolvedTrade::getSellTradeId).toArray();

        // History should contain the IDs of the resolved trades created by the data generator
        assertTrue(Arrays.asList(buyUuidArr).contains(tradesHandlerDataGenerator.buyTrade1Id));
        assertTrue(Arrays.asList(buyUuidArr).contains(tradesHandlerDataGenerator.buyTrade2Id));
        assertTrue(Arrays.asList(sellUuidArr).contains(tradesHandlerDataGenerator.sellTrade1Id));
        assertTrue(Arrays.asList(sellUuidArr).contains(tradesHandlerDataGenerator.sellTrade2Id));
    }

    /**
     * Test 9 - Get all Trade history for a particular AssetType
     */
    @Test
    public void getAllTradeHistoryForAssetType() throws IOException, InterruptedException {
        HttpRequest request = httpBuilder.uri(URI.create(requestURL + tradesHandlerDataGenerator.assetType1Id + "/history/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        PartialReadableResolvedTrade[] resolvedTrades = gson.fromJson(response.body(), PartialReadableResolvedTrade[].class);
        Object[] assetTypeIdArr = Arrays.stream(resolvedTrades).map(PartialReadableResolvedTrade::getAssetTypeName).toArray();

        // History should contain the IDs of the resolved trades created by the data generator
        assertTrue(Arrays.stream(assetTypeIdArr).allMatch(x -> x.equals("Test Asset Type " + tradesHandlerDataGenerator.assetType1Id)));
    }

    /**
     * Test 10 - Get all Open Trades
     */
    @Test
    public void getAllOpenTrades() throws IOException, InterruptedException {
        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.BUY,
                tradesHandlerDataGenerator.orgUnit1Id,
                tradesHandlerDataGenerator.assetType1Id, // test org unit does not have any assets
                10,
                1.0f
        );

        // Create new Open Trade
        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialTrade))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        OpenTrade createdTrade = gson.fromJson(response.body(), OpenTrade.class);

        // Get all Open Trades and expect to see newly-created trade with UUID
        request = httpBuilder.GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        PartialReadableOpenTrade[] openTrades = gson.fromJson(response.body(), PartialReadableOpenTrade[].class);
        Object[] tradeIds = Arrays.stream(openTrades).map(PartialReadableOpenTrade::getTradeId).toArray();

        assertTrue(Arrays.asList(tradeIds).contains(createdTrade.getTradeId()));
    }

    /**
     * Test 11 - Get all Trade history for an AssetType that doesn't exist
     */
    @Test
    public void historyForInvalidAssetType() throws IOException, InterruptedException {
        HttpRequest request = httpBuilder.uri(URI.create(requestURL + UUID.randomUUID() + "/history/")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

        JsonError jsonError = gson.fromJson(response.body(), JsonError.class);
        assertEquals("Selected assetTypeId does not exist, or there are no resolved trades involving the selected assetType", jsonError.getError());
    }

    /**
     * Test 12 - Invalid request URL for fetching history for particular AssetType
     */
    @Test
    public void invalidRequestUrlAssetTypeHistory() throws IOException, InterruptedException {
        HttpRequest request = httpBuilder.uri(URI.create(requestURL + tradesHandlerDataGenerator.assetType1Id)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Invalid URL response code, when ID is supplied, but not /history
        assertEquals(501, response.statusCode());
    }

    /**
     * Test 13 - Delete OpenTrade succeeds
     */
    @Test
    public void deleteOpenTrade() throws IOException, InterruptedException {
        // Create new OpenTrade to be deleted
        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.BUY,
                tradesHandlerDataGenerator.orgUnit1Id,
                tradesHandlerDataGenerator.assetType1Id, // test org unit does not have any assets
                10,
                1.0f
        );

        // Create new Open Trade
        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialTrade))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        OpenTrade createdTrade = gson.fromJson(response.body(), OpenTrade.class);
        request = httpBuilder.uri(URI.create(requestURL + createdTrade.getTradeId())).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Delete response succeeded, returns 200
        assertEquals(200, response.statusCode());
    }

    /**
     * Test 14 - Delete OpenTrade fails, does not exist
     */
    @Test
    public void deleteOpenTradeDoesNotExist() throws IOException, InterruptedException {
        HttpRequest request = httpBuilder.uri(URI.create(requestURL + UUID.randomUUID())).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // AssetType not found with that ID
        assertEquals(404, response.statusCode());

        JsonError jsonError = gson.fromJson(response.body(), JsonError.class);
        assertEquals("OpenTrade not found", jsonError.getError());
    }

    /**
     * Test 15 - Delete OpenTrade fails, no AssetType ID supplied
     */
    @Test
    public void deleteOpenTradeNoId() throws IOException, InterruptedException {
        HttpRequest request = httpBuilder.uri(URI.create(requestURL)).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // AssetType not found with that ID
        assertEquals(404, response.statusCode());

        JsonError jsonError = gson.fromJson(response.body(), JsonError.class);
        assertEquals("OpenTrade not found", jsonError.getError());
    }

    /**
     * Test 16 - Delete OpenTrade with User that does not belong to the correct OrgUnit
     */
    @Test
    public void deleteOpenUserFromWrongOrgUnit() throws IOException, InterruptedException {
        // Create new OpenTrade to be deleted
        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.BUY,
                tradesHandlerDataGenerator.orgUnit1Id,
                tradesHandlerDataGenerator.assetType1Id, // test org unit does not have any assets
                10,
                1.0f
        );

        // Create new Open Trade
        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialTrade))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        OpenTrade createdTrade = gson.fromJson(response.body(), OpenTrade.class);

        tradesHandlerDataGenerator.loginAsUser2();

        HttpRequest requestDelete = HttpRequest.newBuilder()
                        .uri(URI.create(requestURL + createdTrade.getTradeId()))
                        .timeout(Duration.ofSeconds(10))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + tradesHandlerDataGenerator.authenticationToken.toString())
                        .DELETE()
                        .build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, responseDelete.statusCode());

        JsonError jsonError = gson.fromJson(responseDelete.body(), JsonError.class);
        assertEquals("You do not belong to Organisational Unit that opened this trade.", jsonError.getError());
    }

    @AfterEach
    public void destroyTestData() throws SQLException {
        tradesHandlerDataGenerator.destroyTestData();
    }
}
