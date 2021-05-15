import com.google.gson.Gson;
import database.datasources.AssetTypeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import models.Credentials;
import models.partial.PartialOpenTrade;
import models.AssetType;
import models.AuthenticationToken;
import models.OrganisationalUnit;
import models.TradeType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.RestApi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;

public class TradesHandlerTests {

    private final String requestURL = "http://localhost:8000/trades/";
    private final String requestLoginURL = "http://localhost:8000/login/";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;

    @BeforeAll
    @Test
    static void startApi() throws IOException {
        RestApi restApi = new RestApi();
        restApi.start();
    }

    @BeforeEach
    @Test
    public void setupHttpClient() throws IOException, InterruptedException {
        Credentials credentials = new Credentials("newUser", "password");
        String credentialsJson = gson.toJson(credentials);

        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(requestLoginURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(credentialsJson))
                .build();

        HttpResponse<String> response = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        AuthenticationToken authenticationToken = gson.fromJson(response.body(), AuthenticationToken.class);

        httpBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authenticationToken.toString());
    }

    /**
     * Test 1 - Create a BUY Trade without error
     */
    @Test
    public void createTrade() throws IOException, InterruptedException, SQLException {
        UUID orgUnitId = UUID.randomUUID();
        UUID assetTypeId = UUID.randomUUID();

        OrganisationalUnit organisationalUnit = new OrganisationalUnit(
                orgUnitId,
                "Test Unit " + orgUnitId,
                1000.0f,
                new ArrayList<>()
        );

        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        organisationalUnitDataSource.createNew(organisationalUnit);

        AssetType assetType = new AssetType(
                assetTypeId,
                "Test AssetType " + assetTypeId
        );

        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
        assetTypeDataSource.createNew(assetType);

        PartialOpenTrade partialTrade = new PartialOpenTrade(
                TradeType.BUY,
                orgUnitId,
                assetTypeId,
                10,
                1.0f
        );

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialTrade))).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200);
    }

    /**
     * Test 2 - Create a BUY Trade with < 0 quantity of the AssetType
     */
    @Test
    public void createTradeInvalidQuantity() {

    }

    /**
     * Test 2 - Create a BUY Trade with PricePerAsset < 0
     */
    @Test
    public void createTradeInvalidPrice() {

    }

    /**
     * Test 3 - UserId is not present in the JWT token
     */
    @Test
    public void createTradeInvalidUserId() {

    }

    /**
     * Test 4 - User does not belong to the OrgUnit they are creating the Trade for
     */
    @Test
    public void createTradeInvalidOrgUnit() {

    }

    /**
     * Test 5 - OrgUnit does not have enough CreditBalance to place BUY order
     */
    @Test
    public void createTradeInvalidOrgUnitBalance() {

    }

    /**
     * Test 6 - OrgUnit does not have enough quantity of AssetType to place SELL order
     */
    @Test
    public void createTradeInvalidOrgUnitQuantity() {

    }
}
