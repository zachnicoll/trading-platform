package helpers;

import com.google.gson.Gson;
import models.AuthenticationToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    // TODO: Retrieve this from config file
    private static final String baseUrl = "http://localhost:8000";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    private static HttpRequest.Builder builderWithHeaders() {
        ClientInfo clientInfo = ClientInfo.getInstance();
        return HttpRequest.newBuilder()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer " + clientInfo.authToken);
    }

    public static HttpResponse<String> clientPost(String route, Object object) throws IOException, InterruptedException {
        String requestUrl = baseUrl + route;
        HttpRequest request = builderWithHeaders()
                .uri(URI.create(requestUrl))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(object)))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> clientGet(String route) throws IOException, InterruptedException {
        String requestUrl = baseUrl + route;
        HttpRequest request = builderWithHeaders()
                .uri(URI.create(requestUrl))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


}
