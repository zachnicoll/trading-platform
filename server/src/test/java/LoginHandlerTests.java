import com.google.gson.Gson;
import data.LoginHandlerDataGenerator;
import data.TradesHandlerDataGenerator;
import helpers.PasswordHasher;
import models.AuthenticationToken;
import models.Credentials;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginHandlerTests {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private HttpRequest.Builder httpBuilder;
    LoginHandlerDataGenerator loginHandlerDataGenerator;

    @BeforeAll
    static void startApi() throws IOException {
        RestApi restApi = new RestApi();
        restApi.start();
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

    @Test
    public void loginWithCorrectCredentials() throws SQLException, IOException, InterruptedException {
        Credentials credentials = new Credentials(loginHandlerDataGenerator.username, PasswordHasher.hashPassword(loginHandlerDataGenerator.password));

        HttpRequest request = httpBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(credentials))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200);

        // This should NOT throw an error
        gson.fromJson(response.body(), AuthenticationToken.class);
    }

    @AfterEach
    @Test
    public void destroyTestData() throws SQLException {
        loginHandlerDataGenerator.destroyTestData();
    }
}
