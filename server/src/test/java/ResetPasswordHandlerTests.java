import com.google.gson.Gson;
import data.RefreshHandlerDataGenerator;
import errors.JsonError;
import models.NewPassword;
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

public class ResetPasswordHandlerTests {
    private static RestApi restApi;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;

    // This data generator produces the same data necessary for these tests
    RefreshHandlerDataGenerator refreshHandlerDataGenerator;

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

        String requestURL = "http://localhost:8000/resetpassword/";
        httpBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + refreshHandlerDataGenerator.authenticationToken.toString());
    }

    @AfterEach
    public void destroyTestData() throws SQLException {
        refreshHandlerDataGenerator.destroyTestData();
    }

    /**
     * Test 1: Reset password with matching password and confirm password
     */
    @Test
    public void resetPasswordMatching() throws IOException, InterruptedException {
        NewPassword newPassword = new NewPassword("newPassword", "newPassword");

        HttpRequest resetPasswordRequest = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newPassword))).build();

        HttpResponse<String> response = client.send(resetPasswordRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    /**
     * Test 2: Reset password with non-matching password and confirm password
     */
    @Test
    public void resetPasswordNotMatching() throws IOException, InterruptedException {
        NewPassword newPassword = new NewPassword("newPassword", "notMatchingNewPassword");

        HttpRequest resetPasswordRequest = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newPassword))).build();

        HttpResponse<String> response = client.send(resetPasswordRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        JsonError jsonError = gson.fromJson(response.body(), JsonError.class);
        assertEquals("Password and ConfirmPassword do not match", jsonError.getError());
    }
}
