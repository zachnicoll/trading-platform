import com.google.gson.Gson;
import data.LoginHandlerDataGenerator;
import errors.JsonError;
import helpers.PasswordHasher;
import models.AuthenticationToken;
import models.Credentials;
import org.junit.jupiter.api.*;
import server.RestApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginHandlerTests {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    LoginHandlerDataGenerator loginHandlerDataGenerator;
    private HttpRequest.Builder httpBuilder;
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
    public void setupHttpClient() throws SQLException {
        loginHandlerDataGenerator = new LoginHandlerDataGenerator();

        String requestURL = "http://localhost:8000/login/";
        httpBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json");
    }

    /**
     * Test 1: Login with correct username and password
     */
    @Test
    public void loginWithCorrectCredentials() throws IOException, InterruptedException {
        Credentials credentials = new Credentials(loginHandlerDataGenerator.username, loginHandlerDataGenerator.password);

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(credentials))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200);

        // This should NOT throw an error
        gson.fromJson(response.body(), AuthenticationToken.class);
    }

    /**
     * Test 2: Login with incorrect username and password
     */
    @Test
    public void loginWithInCorrectCredentials() throws IOException, InterruptedException {
        Credentials credentials = new Credentials(loginHandlerDataGenerator.username, "incorrect password");

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(credentials))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 403);

        // This should NOT throw an error
        JsonError jsonError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(jsonError.getError(), "Username or password incorrect");
    }

    /**
     * Test 3: Send request to login endpoint with request body
     */
    @Test
    public void loginNoBody() throws IOException, InterruptedException {
        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString("")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 403);

        // This should NOT throw an error
        JsonError jsonError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(jsonError.getError(), "Username or password incorrect");
    }

    /**
     * Test 4: Login with username that does not exist
     */
    @Test
    public void loginUsernameDoesNotExist() throws IOException, InterruptedException {
        Credentials credentials = new Credentials("non-existent user", loginHandlerDataGenerator.password);


        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(credentials))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 403);

        // This should NOT throw an error
        JsonError jsonError = gson.fromJson(response.body(), JsonError.class);
        assertEquals(jsonError.getError(), "Username or password incorrect");
    }


    @AfterEach
    public void destroyTestData() throws SQLException {
        loginHandlerDataGenerator.destroyTestData();
    }
}
