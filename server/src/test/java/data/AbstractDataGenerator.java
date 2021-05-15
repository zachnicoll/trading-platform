package data;

import com.google.gson.Gson;
import models.AuthenticationToken;
import models.Credentials;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;

/**
 * Interface that should be implemented by all test Data Generators.
 * Ensures that the generators are destroying the test data that they created.
 */
public abstract class AbstractDataGenerator {
    public AuthenticationToken authenticationToken;

    /**
     * Sends a request to the /login/ endpoint to retrieve an Authentication Token that
     * can be used in subsequent requests to the API.
     *
     * @param username Username of User to login as
     * @throws IOException
     * @throws InterruptedException
     */
    protected void login(String username) throws IOException, InterruptedException {
        /*
         * Login as new User and retrieve an auth token to be used in subsequent test API calls
         */
        Credentials credentials = new Credentials(username, "password");
        Gson gson = new Gson();
        String credentialsJson = gson.toJson(credentials);

        String requestLoginURL = "http://localhost:8000/login/";
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(requestLoginURL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(credentialsJson))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        // Login
        HttpResponse<String> response = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

        authenticationToken = gson.fromJson(response.body(), AuthenticationToken.class);
    }

    /**
     * Create all fake data in this method.
     *
     * @throws SQLException
     */
    protected abstract void generateData() throws SQLException;

    /**
     * All entries in tables that were created with particular IDs should be deleted from the database here.
     *
     * @throws SQLException
     */
    abstract void destroyTestData() throws SQLException;
}
