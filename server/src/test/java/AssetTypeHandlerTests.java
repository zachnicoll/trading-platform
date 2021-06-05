import com.google.gson.Gson;
import data.AssetTypeHandlerDataGenerator;
import data.AssetsHandlerDataGenerator;
import database.datasources.AssetDataSource;
import database.datasources.AssetTypeDataSource;
import errors.JsonError;
import helpers.PasswordHasher;
import models.AccountType;
import models.AssetType;
import models.User;
import models.partial.PartialAssetType;
import models.partial.PartialUser;
import org.junit.jupiter.api.*;
import server.RestApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssetTypeHandlerTests {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;
    private AssetTypeHandlerDataGenerator assetTypeDataGenerator;
    private AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
    private String requestURL;
    private static RestApi restApi;

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
        assetTypeDataGenerator = new AssetTypeHandlerDataGenerator();

        requestURL = "http://localhost:8000/assettype/";
        httpBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + assetTypeDataGenerator.authenticationToken.toString());
    }



    /**
     * Test 1 - Create assetType
     */
    @Test
    public void createAssetType() throws IOException, InterruptedException {
        PartialAssetType partialAssetType = new PartialAssetType("TestAssetType");

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialAssetType))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned assetType information is correct/reflects what was sent in request
        AssetType createdAssetType = gson.fromJson(response.body(), AssetType.class);
        assertNotNull(createdAssetType);
        assertTrue(createdAssetType.getAssetTypeId() instanceof UUID);
        assertEquals("TestAssetType", createdAssetType.getAssetName());
        assetTypeDataGenerator.assetType = createdAssetType;
    }

    /**
     * Test 2 - Create assetType without asset name
     */
    @Test
    public void createAssetTypeInvalidNoAssetName() throws IOException, InterruptedException {
        PartialAssetType partialAssetType = new PartialAssetType(null);

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialAssetType))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("AssetType does not contain asset name").getError(), responseError.getError());

    }

    /**
     * Test 3 - Get all assetTypes
     */
    @Test
    public void getAllAssetTypes() throws IOException, InterruptedException, SQLException {

        HttpRequest request = httpBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        AssetType[] assetTypes = gson.fromJson(response.body(), AssetType[].class);
        AssetType[] actualAssetTypes = assetTypeDataSource.getAll().toArray(new AssetType[0]);

        // Test that returned user information is correct/reflects what was sent in request
        assertArrayEquals(Stream.of(actualAssetTypes).map(AssetType::getAssetTypeId).toArray(UUID[]::new),
                Stream.of(assetTypes).map(AssetType::getAssetTypeId).toArray(UUID[]::new));
    }

    /**
     * Test 4 - Delete assetType by Id
     */
    @Test
    public void deleteAssetTypeById() throws IOException, InterruptedException, SQLException {

        // Test if assetType is in database
        assertTrue(assetTypeDataSource.checkExistById(assetTypeDataGenerator.assetType1Id));

        HttpRequest request = httpBuilder.DELETE().uri(URI.create(requestURL + assetTypeDataGenerator.assetType1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned user information is correct/reflects what was sent in request
        assertEquals("null", response.body());
    }

    /**
     * Test 5 - Delete assetType without Id
     */
    @Test
    public void deleteAssetTypeInvalidIdNonExistent() throws IOException, InterruptedException, SQLException {

        // Test if assetType is in database
        assertTrue(assetTypeDataSource.checkExistById(assetTypeDataGenerator.assetType1Id));

        HttpRequest request = httpBuilder.DELETE().uri(URI.create(requestURL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("AssetType Id not present").getError(), responseError.getError());
    }

    /**
     * Test 6 - Delete assetType with invalid Id
     */
    @Test
    public void deleteAssetTypeInvalidId() throws IOException, InterruptedException, SQLException {

        // Test if assetType is in database
        assertTrue(assetTypeDataSource.checkExistById(assetTypeDataGenerator.assetType1Id));

        HttpRequest request = httpBuilder.DELETE().uri(URI.create(requestURL + UUID.randomUUID())).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(404, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("AssetType not found").getError(), responseError.getError());
    }

    @AfterEach
    public void destroyTestData() throws SQLException {
        assetTypeDataGenerator.destroyTestData();
    }
}
