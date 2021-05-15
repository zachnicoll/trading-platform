package data;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import database.datasources.AssetTypeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import database.datasources.UserDataSource;
import models.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Class for generating fake data for TradeHandlerTests. Contains publicly
 * accessible fields that can be referenced in Tests (like IDs) to make valid API calls.
 */
public class TradeHandlerDataGenerator implements DataGenerator {

    /*
     * Publicly accessible fields to reference in tests
     */
    public final UUID orgUnit1Id = UUID.randomUUID();
    public final UUID assetType1Id = UUID.randomUUID();
    public final UUID user1Id = UUID.randomUUID();
    public AuthenticationToken authenticationToken;

    private void createTestOrgUnits() throws SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        OrganisationalUnit organisationalUnit = new OrganisationalUnit(
                orgUnit1Id,
                "Test Org Unit " + orgUnit1Id,
                1000.0f,
                new ArrayList<>()
        );
        organisationalUnitDataSource.createNew(organisationalUnit);
    }

    private void createTestAssetTypes() throws SQLException {
        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
        AssetType assetType = new AssetType(
                assetType1Id,
                "Test Asset Type " + assetType1Id
        );
        assetTypeDataSource.createNew(assetType);
    }

    private void createTestUser() throws SQLException {
        UserDataSource userDataSource = new UserDataSource();
        User user = new User(
                user1Id,
                "Test User " + user1Id,
                AccountType.USER,
                orgUnit1Id
        );
        userDataSource.createNew(user, BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
    }

    public TradeHandlerDataGenerator() throws IOException, InterruptedException, SQLException {
        /*
         * Create data in DB
         */
        createTestOrgUnits();
        createTestAssetTypes();
        createTestUser();

        /*
         * Login as new User and retrieve an auth token to be used in subsequent test API calls
         */
        Credentials credentials = new Credentials("Test User " + user1Id, "password");
        Gson gson = new Gson();
        String credentialsJson = gson.toJson(credentials);

        String requestLoginURL = "http://localhost:8000/login/";
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(requestLoginURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(credentialsJson))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        // Login
        HttpResponse<String> response = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

        authenticationToken = gson.fromJson(response.body(), AuthenticationToken.class);
    }

    public void destroyTestData() throws SQLException {
        UserDataSource userDataSource = new UserDataSource();
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();

        organisationalUnitDataSource.deleteById(orgUnit1Id);
        assetTypeDataSource.deleteById(assetType1Id);
        userDataSource.deleteById(user1Id);
    }
}
