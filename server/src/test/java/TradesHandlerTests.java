import com.google.gson.Gson;
import data.TradesHandlerDataGenerator;
import errors.JsonError;
import jdk.jshell.spi.ExecutionControl;
import models.*;
import models.partial.PartialOpenTrade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.RestApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradesHandlerTests {

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;
    TradesHandlerDataGenerator tradesHandlerDataGenerator;

    @BeforeAll
    @Test
    static void startApi() throws IOException {
        RestApi restApi = new RestApi();
        restApi.start();
    }

    @BeforeEach
    @Test
    public void setupHttpClient() throws IOException, InterruptedException, SQLException {
        tradesHandlerDataGenerator = new TradesHandlerDataGenerator();

        String requestURL = "http://localhost:8000/trades/";
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
        assertEquals(response.statusCode(), 200);

        // Test that returned trade information is correct/reflects what was sent in request
        OpenTrade responseTrade = gson.fromJson(response.body(), OpenTrade.class);
        assertEquals(responseTrade.getAssetType(), partialTrade.assetTypeId);
        assertEquals(responseTrade.getOrganisationalUnit(), partialTrade.organisationalUnitId);
        assertEquals(responseTrade.getQuantity(), partialTrade.quantity);
        assertEquals(responseTrade.getPricePerAsset(), partialTrade.pricePerAsset);
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
        assertEquals(response.statusCode(), 400);

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(responseError.getError(), new JsonError("Quantity is less than or equal to 0").getError());
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
        assertEquals(response.statusCode(), 400);

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(responseError.getError(), new JsonError("PricePerAsset is less than or equal to 0").getError());
    }

    /**
     * Test 4 - UserId is not present in the JWT token
     */
    @Test
    public void createTradeInvalidUserId() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
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
        assertEquals(response.statusCode(), 400);

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(responseError.getError(), new JsonError("You must belong to the Organisational Unit you are placing the Trade for").getError());
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
        assertEquals(response.statusCode(), 400);

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(responseError.getError(), new JsonError("Organisational Unit does not have enough credits to place this order").getError());
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
        assertEquals(response.statusCode(), 400);

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(responseError.getError(),  new JsonError("Organisational Unit does not have enough of the given Asset Type to place this order").getError());
    }

    @AfterEach
    @Test
    public void destroyTestData() throws SQLException {
        tradesHandlerDataGenerator.destroyTestData();
    }
}
