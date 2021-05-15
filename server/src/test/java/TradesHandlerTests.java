import com.google.gson.Gson;
import database.datasources.AssetTypeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import handlers.login.UsernamePassword;
import handlers.trades.NewOpenTrade;
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
        UsernamePassword credentials = new UsernamePassword("newUser", "password");
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

        NewOpenTrade partialTrade = new NewOpenTrade(
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

    public void createTradeInvalidQuantity() {

    }

    public void createTradeInvalidUserId() {

    }

    public void createTradeInvalidOrgUnit() {

    }

    public void createTradeInvalidPrice() {

    }

    public void createTradeInvalidOrgUnitBalance() {

    }

    public void createTradeInvalidOrgUnitQuantity() {

    }
}
