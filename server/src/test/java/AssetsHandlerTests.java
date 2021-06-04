import com.google.gson.Gson;
import data.AssetsHandlerDataGenerator;
import data.TradesHandlerDataGenerator;
import database.datasources.AssetDataSource;
import errors.JsonError;
import models.Asset;
import models.OpenTrade;
import models.TradeType;
import models.partial.PartialOpenTrade;
import models.partial.PartialReadableOpenTrade;
import org.junit.jupiter.api.*;
import server.RestApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AssetsHandlerTests {

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;
    private AssetsHandlerDataGenerator assetsHandlerDataGenerator;
    private AssetDataSource assetDataSource = new AssetDataSource();
    private String requestURL;

    @BeforeAll
    @Test
    static void startApi() throws IOException {
        RestApi restApi = new RestApi();
        restApi.start();
    }

    @BeforeEach
    @Test
    public void setupHttpClient() throws IOException, InterruptedException, SQLException {
        assetsHandlerDataGenerator = new AssetsHandlerDataGenerator();

        requestURL = "http://localhost:8000/assets/";
        httpBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + assetsHandlerDataGenerator.authenticationToken.toString());
    }



    /**
     * Test 1 - Get All assets
     */
    @Test
    public void getAllAssetsSuccess() throws IOException, InterruptedException, SQLException {

        HttpRequest request = httpBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        Asset[] assets = gson.fromJson(response.body(), Asset[].class);
        Asset[] actualAssets = assetDataSource.getAll().toArray(new Asset[0]);

        // Test that returned asset information is correct/reflects what was sent in request
        // Converts from arrays of Asset objects to UUID[] of assetTypeId
        assertArrayEquals(Stream.of(actualAssets).map(Asset::getAssetTypeId).toArray(UUID[]::new),
                Stream.of(assets).map(Asset::getAssetTypeId).toArray(UUID[]::new));

    }

    /**
     * Test 2 - Get all assets belonging to an organisational unit
     */
    @Test
    public void getAllOrgAssetsSuccess() throws IOException, InterruptedException, SQLException {

        HttpRequest request = httpBuilder.uri(URI.create(requestURL + assetsHandlerDataGenerator.orgUnit1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        Asset[] assets = gson.fromJson(response.body(), Asset[].class);
        Asset[] actualAssets = assetDataSource.getByOrgUnitId(assetsHandlerDataGenerator.orgUnit1Id).toArray(new Asset[0]);

        // Test that returned asset information is correct/reflects what was sent in request
        assertArrayEquals(Stream.of(actualAssets).map(Asset::getAssetTypeId).toArray(UUID[]::new),
                Stream.of(assets).map(Asset::getAssetTypeId).toArray(UUID[]::new));
    }

    /**
     * Test 3 - Get all assets belonging to a non-existent organisational Unit
     */
    @Test
    public void getAllOrgAssetsInvalidOrgUnitNotExist() throws IOException, InterruptedException {

        HttpRequest request = httpBuilder.uri(URI.create(requestURL + UUID.randomUUID())).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(404, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Organisational Unit does not exist").getError(), responseError.getError());
    }

    /**
     * Test 4 - Delete asset from Organisational unit successfully
     */
    @Test
    public void deleteAssetSuccess() throws IOException, InterruptedException, SQLException {

        // Test if asset is in database
        assertTrue(assetDataSource.checkExistById(assetsHandlerDataGenerator.assetType1Id,assetsHandlerDataGenerator.orgUnit1Id));

        HttpRequest request = httpBuilder.DELETE().uri(URI.create(requestURL + assetsHandlerDataGenerator.orgUnit1Id +
                "/" + assetsHandlerDataGenerator.assetType1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned asset information is correct/reflects what was sent in request
        assertEquals("null", response.body());

    }

    /**
     * Test 5 - Delete asset type from non-existent Organisational unit
     */
    @Test
    public void deleteAssetInvalidOrgUnitNotExist() throws IOException, InterruptedException, SQLException {

        // Test if asset is in database
        assertTrue(assetDataSource.checkExistById(assetsHandlerDataGenerator.assetType1Id,assetsHandlerDataGenerator.orgUnit1Id));

        HttpRequest request = httpBuilder.DELETE().uri(URI.create(requestURL + UUID.randomUUID() +
                "/" + assetsHandlerDataGenerator.assetType1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(404, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Organisational Unit does not exist").getError(), responseError.getError());

    }

    /**
     * Test 6 - Delete asset type from Organisational unit that does not own any of the asset type
     */
    @Test
    public void deleteAssetInvalidOrgUnitDoesNotOwn() throws IOException, InterruptedException, SQLException {

        AssetsHandlerDataGenerator additionalData = new AssetsHandlerDataGenerator();
        // Test if asset is in database
        assertTrue(assetDataSource.checkExistById(assetsHandlerDataGenerator.assetType1Id, assetsHandlerDataGenerator.orgUnit1Id));

        HttpRequest request = httpBuilder.DELETE().uri(URI.create(requestURL + additionalData.orgUnit1Id +
                "/" + assetsHandlerDataGenerator.assetType1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Organisational Unit does not own any of the given Asset Type").getError(), responseError.getError());

    }

    /**
     * Test 7 - Update quantity of asset in Organisational unit to < 1
     */
    @Test
    public void updateQuantityInvalidToZero() throws IOException, InterruptedException, SQLException {

        // Test if asset quantity is 10 in database before updating
        assertTrue(assetDataSource.checkExistById(assetsHandlerDataGenerator.assetType1Id, assetsHandlerDataGenerator.orgUnit1Id));
        assertEquals(10, assetDataSource.getById(assetsHandlerDataGenerator.assetType1Id, assetsHandlerDataGenerator.orgUnit1Id).getQuantity());

        Asset asset = new Asset(assetsHandlerDataGenerator.assetType1Id, 0);

        HttpRequest request = httpBuilder.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(asset))).uri(URI.create(requestURL + assetsHandlerDataGenerator.orgUnit1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Provided Quantity is less than 1").getError(), responseError.getError());

    }

    /**
     * Test 8 - Update quantity of asset in Organisational unit to 5
     */
    @Test
    public void updateQuantitySuccessToFive() throws IOException, InterruptedException, SQLException {

        // Test if asset quantity is 10 in database before updating
        assertTrue(assetDataSource.checkExistById(assetsHandlerDataGenerator.assetType1Id, assetsHandlerDataGenerator.orgUnit1Id));
        assertEquals(10, assetDataSource.getById(assetsHandlerDataGenerator.assetType1Id, assetsHandlerDataGenerator.orgUnit1Id).getQuantity());

        Asset asset = new Asset(assetsHandlerDataGenerator.assetType1Id, 5);

        HttpRequest request = httpBuilder.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(asset))).uri(URI.create(requestURL + assetsHandlerDataGenerator.orgUnit1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned asset information is correct/reflects what was sent in request
        assertEquals(5, assetDataSource.getById(assetsHandlerDataGenerator.assetType1Id, assetsHandlerDataGenerator.orgUnit1Id).getQuantity());
        assertEquals("null", response.body());

    }


    /**
     * Test 9 - Update quantity of asset in Organisational unit that does not exist
     */

    @Test
    public void updateQuantityInvalidOrgUnitNotExist() throws IOException, InterruptedException, SQLException {

        Asset asset = new Asset(assetsHandlerDataGenerator.assetType1Id, 5);

        HttpRequest request = httpBuilder.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(asset))).uri(URI.create(requestURL + UUID.randomUUID())).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(404, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Organisational Unit does not exist").getError(), responseError.getError());

    }

    @AfterEach
    @Test
    public void destroyTestData() throws SQLException {
        assetsHandlerDataGenerator.destroyTestData();
    }

}
