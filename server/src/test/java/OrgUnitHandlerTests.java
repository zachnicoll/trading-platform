import com.google.gson.Gson;
import com.google.gson.JsonElement;
import data.AssetsHandlerDataGenerator;
import data.OrgUnitHandlerDataGenerator;
import database.datasources.AssetDataSource;
import database.datasources.OrganisationalUnitDataSource;
import errors.JsonError;
import models.Asset;
import models.OpenTrade;
import models.OrganisationalUnit;
import models.partial.PartialOrganisationalUnit;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OrgUnitHandlerTests {

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;
    private OrgUnitHandlerDataGenerator dataGenerator;
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
    public void createOrganisationalUnit() throws IOException, InterruptedException, SQLException {

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

    }

    /**
     * Test 2 - Create Organisational Unit without name
     */

    @Test
    public void createOrganisationalUnitNoName() throws IOException, InterruptedException, SQLException {

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

    /**
     * Test 4 - Update Organisational Unit credit balance to 100
     */

    /**
     * Test 5 - Update Organisational Unit credit balance to < 0
     */

    /**
     * Test 6 - Update credit balance for Organisational Unit that does not exist in database
     */

    /**
     * Test 7 - Delete Organisational Unit
     */

    /**
     * Test 8 - Delete Organisational Unit that does not exist
     */

    /**
     * Test 9 - Get Organisational Unit by Id
     */

    /**
     * Test 10 - Get Organisational Unit by Id that does not exist
     */

    /**
     * Test 11 - Get all Organisational Units in the database
     */
}
