import com.google.gson.Gson;
import data.TradeHandlerAbstractDataGenerator;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradesHandlerTests {

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;
    TradeHandlerAbstractDataGenerator tradeHandlerDataGenerator;

    @BeforeAll
    @Test
    static void startApi() throws IOException {
        RestApi restApi = new RestApi();
        restApi.start();
    }

    @BeforeEach
    @Test
    public void setupHttpClient() throws IOException, InterruptedException, SQLException {
        tradeHandlerDataGenerator = new TradeHandlerAbstractDataGenerator();

        String requestURL = "http://localhost:8000/trades/";
        httpBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tradeHandlerDataGenerator.authenticationToken.toString());
    }

    /**
     * Test 1 - Create a BUY Trade without error
     */
    @Test
    public void createTrade() throws IOException, InterruptedException, SQLException {
        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.BUY,
                tradeHandlerDataGenerator.orgUnit1Id,
                tradeHandlerDataGenerator.assetType1Id,
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
    public void createTradeInvalidQuantity() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    /**
     * Test 2 - Create a BUY Trade with PricePerAsset < 0
     */
    @Test
    public void createTradeInvalidPrice() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    /**
     * Test 3 - UserId is not present in the JWT token
     */
    @Test
    public void createTradeInvalidUserId() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    /**
     * Test 4 - User does not belong to the OrgUnit they are creating the Trade for
     */
    @Test
    public void createTradeInvalidOrgUnit() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    /**
     * Test 5 - OrgUnit does not have enough CreditBalance to place BUY order
     */
    @Test
    public void createTradeInvalidOrgUnitBalance() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    /**
     * Test 6 - OrgUnit does not have enough quantity of AssetType to place SELL order
     */
    @Test
    public void createTradeInvalidOrgUnitQuantity() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("THIS TEST NEEDS TO BE WRITTEN");
    }

    @AfterEach
    @Test
    public void destroyTestData() throws SQLException {
        tradeHandlerDataGenerator.destroyTestData();
    }
}
