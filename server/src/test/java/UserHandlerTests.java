import com.google.gson.Gson;
import data.UserHandlerDataGenerator;
import database.datasources.UserDataSource;
import models.AccountType;
import models.User;
import models.partial.PartialUser;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserHandlerTests {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;
    private UserDataSource userDataSource = new UserDataSource();
    private UserHandlerDataGenerator userDataGenerator;
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
        userDataGenerator = new UserHandlerDataGenerator();

        requestURL = "http://localhost:8000/user/";
        httpBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + userDataGenerator.authenticationToken.toString());
    }



    /**
     * Test 1 - Create New User
     */
    @Test
    public void createUser() throws IOException, InterruptedException {
        PartialUser partialUser = new PartialUser("TestUser", AccountType.USER, DigestUtils.sha256Hex(password));

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userDataGenerator.user))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned user information is correct/reflects what was sent in request
        User createdUser = gson.fromJson(response.body(), User.class);
        assertNotNull(createdUser);
        assertEquals(userDataGenerator.user.getUserId(), createdUser.getUserId());
        assertEquals(userDataGenerator.user.getOrganisationalUnitId(), createdUser.getOrganisationalUnitId());
    }


    /**
     * Test 2 - Create New User does not contain password
     */

    /**
     * Test 3 - Create New User does not contain username
     */

    /**
     * Test 4 - Create New User does not contain Organisational Unit Id
     */

    /**
     * Test 5 - Get User by Id
     */

    /**
     * Test 6 - Get all Users
     */

    /**
     * Test 7 - Delete User by Id
     */

    /**
     * Test 8 - Delete User without Id
     */
}
