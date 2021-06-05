import com.google.gson.Gson;
import data.OrgUnitHandlerDataGenerator;
import database.datasources.OrganisationalUnitDataSource;
import errors.JsonError;
import models.Asset;
import models.OrganisationalUnit;
import models.partial.PartialOrganisationalUnit;
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

public class OrgUnitHandlerTests {

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;
    private OrganisationalUnitDataSource orgUnitDataSource = new OrganisationalUnitDataSource();
    private OrgUnitHandlerDataGenerator dataGenerator;
    private String requestURL;

    @BeforeAll
    static void startApi() throws IOException {
        RestApi restApi = new RestApi();
        restApi.start();
    }

    @BeforeEach
    public void setupHttpClient() throws IOException, InterruptedException, SQLException {
        dataGenerator = new OrgUnitHandlerDataGenerator();

        requestURL = "http://localhost:8000/orgunit/";
        httpBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + dataGenerator.authenticationToken.toString());
    }



    /**
     * Test 1 - Create Organisational Unit
     */
    @Test
    public void createOrgUnit() throws IOException, InterruptedException, SQLException {

        PartialOrganisationalUnit orgUnit = new PartialOrganisationalUnit("TestUnit",100f);

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(orgUnit))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned orgUnit information is correct/reflects what was sent in request
        OrganisationalUnit createdOrgUnit = gson.fromJson(response.body(), OrganisationalUnit.class);
        assertNotNull(createdOrgUnit);
        assertEquals(orgUnit.unitName, createdOrgUnit.getUnitName());
        assertEquals(orgUnit.creditBalance, createdOrgUnit.getCreditBalance());
        dataGenerator.fullOrgUnit = createdOrgUnit;

    }

    /**
     * Test 2 - Create Organisational Unit without name
     */

    @Test
    public void createOrgUnitInvalidNoName() throws IOException, InterruptedException, SQLException {

        PartialOrganisationalUnit orgUnit = new PartialOrganisationalUnit("",100f);

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(orgUnit))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(responseError.getError(), new JsonError("Organisational Unit does not have name").getError());

    }

    /**
     * Test 3 - Create Organisational Unit with credit balance < 0
     */
    @Test
    public void createOrgUnitInvalidBalance() throws IOException, InterruptedException, SQLException {

        PartialOrganisationalUnit orgUnit = new PartialOrganisationalUnit("TestOrgUnit",-1.0f);

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(orgUnit))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Organisational Unit has credit balance less than zero").getError(), responseError.getError());
    }

    /**
     * Test 4 - Update Organisational Unit credit balance to 100
     */
    @Test
    public void updateOrgUnitBalance() throws IOException, InterruptedException{

        PartialOrganisationalUnit orgUnit = new PartialOrganisationalUnit("Test Org Unit " + dataGenerator.orgUnit1Id, 100f);

        HttpRequest request = httpBuilder.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(orgUnit))).uri(URI.create(requestURL + dataGenerator.orgUnit1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned orgUnit information is correct/reflects what was sent in request
        assertEquals("null", response.body());

    }

    /**
     * Test 5 - Update Organisational Unit credit balance to < 0
     */
    @Test
    public void updateOrgUnitInvalidBalanceLessThanZero() throws IOException, InterruptedException{

        PartialOrganisationalUnit orgUnit = new PartialOrganisationalUnit("Test Org Unit " + dataGenerator.orgUnit1Id, -1.0f);

        HttpRequest request = httpBuilder.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(orgUnit))).uri(URI.create(requestURL + dataGenerator.orgUnit1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("New credit balance is less than zero").getError(), responseError.getError());

    }

    /**
     * Test 6 - Update credit balance for Organisational Unit that does not exist in database
     */
    @Test
    public void updateOrgUnitInvalidDoesNotExist() throws IOException, InterruptedException {

        PartialOrganisationalUnit orgUnit = new PartialOrganisationalUnit("Test Org Unit " + dataGenerator.orgUnit1Id, 100f);

        HttpRequest request = httpBuilder.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(orgUnit))).uri(URI.create(requestURL + UUID.randomUUID())).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(404, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Organisational Unit does not exist").getError(), responseError.getError());

    }

    /**
     * Test 7 - Delete Organisational Unit
     */
    @Test
    public void deleteOrgUnitSuccess() throws IOException, InterruptedException, SQLException {

        // Test if orgUnit is in database
        assertTrue(orgUnitDataSource.checkExistById(dataGenerator.orgUnit1Id));

        HttpRequest request = httpBuilder.DELETE().uri(URI.create(requestURL + dataGenerator.orgUnit1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned orgUnit information is correct/reflects what was sent in request
        assertEquals("null", response.body());
    }

    /**
     * Test 8 - Delete Organisational Unit that does not exist
     */
    @Test
    public void deleteOrgUnitInvalidUnitNonExistent() throws IOException, InterruptedException, SQLException {

        // Test if orgUnit is in database
        assertTrue(orgUnitDataSource.checkExistById(dataGenerator.orgUnit1Id));

        HttpRequest request = httpBuilder.DELETE().uri(URI.create(requestURL + UUID.randomUUID())).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(404, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Organisational Unit does not exist").getError(), responseError.getError());
    }

    /**
     * Test 9 - Get Organisational Unit by Id
     */
    @Test
    public void getOrgUnitById() throws IOException, InterruptedException {

        HttpRequest request = httpBuilder.uri(URI.create(requestURL + dataGenerator.orgUnit1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());


        // Test that returned asset information is correct/reflects what was sent in request
        OrganisationalUnit retrievedOrgUnit = gson.fromJson(response.body(), OrganisationalUnit.class);
        assertNotNull(retrievedOrgUnit);
        assertEquals(dataGenerator.orgUnit1Id, retrievedOrgUnit.getUnitId());
    }

    /**
     * Test 10 - Get Organisational Unit by Id that does not exist
     */
    @Test
    public void getOrgUnitInvalidUnitNonExistent() throws IOException, InterruptedException {

        HttpRequest request = httpBuilder.uri(URI.create(requestURL + UUID.randomUUID())).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(404, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("Organisational Unit does not exist").getError(), responseError.getError());
    }

    /**
     * Test 11 - Get all Organisational Units in the database
     */
    @Test
    public void getOrgUnits() throws IOException, InterruptedException, SQLException {

        HttpRequest request = httpBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        OrganisationalUnit[] orgUnits = gson.fromJson(response.body(), OrganisationalUnit[].class);
        OrganisationalUnit[] actualOrgUnits = orgUnitDataSource.getAll().toArray(new OrganisationalUnit[0]);

        // Test that returned orgUnit information is correct/reflects what was sent in request
        assertArrayEquals(Stream.of(actualOrgUnits).map(OrganisationalUnit::getUnitId).toArray(UUID[]::new),
                Stream.of(orgUnits).map(OrganisationalUnit::getUnitId).toArray(UUID[]::new));
    }

    @AfterEach
    public void destroyTestData() throws SQLException {
        dataGenerator.destroyTestData();
    }
}
