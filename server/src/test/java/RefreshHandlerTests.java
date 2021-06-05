import com.google.gson.Gson;
import data.RefreshHandlerDataGenerator;
import errors.JsonError;
import models.AuthenticationToken;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RefreshHandlerTests {
    private static RestApi restApi;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    RefreshHandlerDataGenerator refreshHandlerDataGenerator;
    private HttpRequest.Builder httpBuilder;

    @BeforeAll
    static void startApi() throws IOException {
        restApi = new RestApi();
        restApi.start();
    }

    @AfterAll
    static void stopApi() {
        restApi.stop();
    }

    @BeforeEach
    public void setupHttpClient() throws SQLException, IOException, InterruptedException {
        refreshHandlerDataGenerator = new RefreshHandlerDataGenerator();

        String requestURL = "http://localhost:8000/refresh/";
        httpBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json");
    }

    /**
     * Test 1: Refresh the current User's auth token by providing a valid auth token in the header
     */
    @Test
    public void refreshTokenValid() throws IOException, InterruptedException {
        HttpRequest refreshTokenRequest = httpBuilder
                .header("Authorization", "Bearer " + refreshHandlerDataGenerator.authenticationToken.toString())
                .GET()
                .build();

        HttpResponse<String> response = client.send(refreshTokenRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        AuthenticationToken authenticationToken = gson.fromJson(response.body(), AuthenticationToken.class);
        assertNotNull(authenticationToken);
    }

    /**
     * Test 2: Attempt to refresh token without providing a valid auth token in the header
     */
    @Test
    public void refreshTokenInvalid() throws IOException, InterruptedException {
        HttpRequest refreshTokenRequest = httpBuilder
                .GET()
                .build();

        HttpResponse<String> response = client.send(refreshTokenRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(403, response.statusCode());

        JsonError jsonError = gson.fromJson(response.body(), JsonError.class);
        assertEquals("You are not authenticated", jsonError.getError());
    }

    @AfterEach
    public void destroyTestData() throws SQLException {
        refreshHandlerDataGenerator.destroyTestData();
    }
}
