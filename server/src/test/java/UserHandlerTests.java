import com.google.gson.Gson;
import data.UserHandlerDataGenerator;
import database.datasources.UserDataSource;
import errors.JsonError;
import helpers.PasswordHasher;
import models.AccountType;
import models.OrganisationalUnit;
import models.User;
import models.partial.PartialUser;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UserHandlerTests {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;
    private UserDataSource userDataSource = new UserDataSource();
    private UserHandlerDataGenerator userDataGenerator;
    private String requestURL;

    @BeforeAll
    static void startApi() throws IOException {
        RestApi restApi = new RestApi();
        restApi.start();
    }

    @BeforeEach
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
        PartialUser partialUser = new PartialUser("TestUser", AccountType.USER, userDataGenerator.orgUnit1Id, PasswordHasher.hashPassword("password"));

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialUser))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned user information is correct/reflects what was sent in request
        User createdUser = gson.fromJson(response.body(), User.class);
        assertNotNull(createdUser);
        assertTrue(createdUser.getUserId() instanceof UUID);
        assertEquals(userDataGenerator.orgUnit1Id, createdUser.getOrganisationalUnitId());
        userDataGenerator.user = createdUser;
    }


    /**
     * Test 2 - Create New User does not contain password
     */
    @Test
    public void createUserInvalidNoPassword() throws IOException, InterruptedException {
        PartialUser partialUser = new PartialUser("TestUser", AccountType.USER, userDataGenerator.orgUnit1Id, null);

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialUser))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("User does not contain a password").getError(), responseError.getError());
    }

    /**
     * Test 3 - Create New User does not contain username
     */
    @Test
    public void createUserInvalidNoUsername() throws IOException, InterruptedException {
        PartialUser partialUser = new PartialUser(null, AccountType.USER, userDataGenerator.orgUnit1Id, PasswordHasher.hashPassword("password"));

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialUser))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("User does not contain a username").getError(), responseError.getError());
    }

    /**
     * Test 4 - Create New User does not contain Organisational Unit Id
     */
    @Test
    public void createUserInvalidNoOrgUnitId() throws IOException, InterruptedException {
        PartialUser partialUser = new PartialUser("TestUser", AccountType.USER, null, PasswordHasher.hashPassword("password"));

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(partialUser))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(400, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("User does not contain an organisational unit Id").getError(), responseError.getError());
    }

    /**
     * Test 5 - Get User by Id
     */
    @Test
    public void getUserById() throws IOException, InterruptedException {

        HttpRequest request = httpBuilder.uri(URI.create(requestURL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());


        // Test that returned user information is correct/reflects what was sent in request
        User retrievedUser = gson.fromJson(response.body(), User.class);
        assertNotNull(retrievedUser);
        assertEquals(userDataGenerator.user1Id, retrievedUser.getUserId());
    }

    /**
     * Test 6 - Get all Users
     */
    @Test
    public void getAllUsers() throws IOException, InterruptedException, SQLException {

        HttpRequest request = httpBuilder.uri(URI.create(requestURL + "all")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        User[] users = gson.fromJson(response.body(), User[].class);
        User[] actualUsers = userDataSource.getAll().toArray(new User[0]);

        // Test that returned user information is correct/reflects what was sent in request
        assertArrayEquals(Stream.of(actualUsers).map(User::getUserId).toArray(UUID[]::new),
                Stream.of(users).map(User::getUserId).toArray(UUID[]::new));
    }

    /**
     * Test 7 - Delete User by Id
     */
    @Test
    public void deleteUser() throws IOException, InterruptedException, SQLException {

        // Test if orgUnit is in database
        assertTrue(userDataSource.checkExistById(userDataGenerator.user1Id));

        HttpRequest request = httpBuilder.DELETE().uri(URI.create(requestURL + userDataGenerator.user1Id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request was successful
        assertEquals(200, response.statusCode());

        // Test that returned user information is correct/reflects what was sent in request
        assertEquals("null", response.body());
    }

    /**
     * Test 8 - Delete User without Id
     */
    @Test
    public void deleteUserInvalidIdNonExistent() throws IOException, InterruptedException, SQLException {

        // Test if orgUnit is in database
        assertTrue(userDataSource.checkExistById(userDataGenerator.user1Id));

        HttpRequest request = httpBuilder.DELETE().uri(URI.create(requestURL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Test that request failed
        assertEquals(404, response.statusCode());

        // Test that returned error information is correct/reflects what was sent in request
        JsonError responseError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(new JsonError("User not found").getError(), responseError.getError());
    }


    @AfterEach
    public void destroyTestData() throws SQLException {
        userDataGenerator.destroyTestData();
    }
}
