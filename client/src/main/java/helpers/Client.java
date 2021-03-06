package helpers;

import com.google.gson.Gson;
import models.AuthenticationToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

/**
 * Static class containing methods for performing HTTP requests and returning the response.
 */
public class Client {
    private static String baseUrl = "http://localhost:8000";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    // Build header with client JWT token
    private static HttpRequest.Builder builderWithHeaders() {
        ClientInfo clientInfo = ClientInfo.getInstance();
        return HttpRequest.newBuilder()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer " + clientInfo.getAuthToken());
    }

    private static String makeUrl(String route) {
        return baseUrl + route;
    }

    public static void setBaseUrl(String url){
        baseUrl = url;
    }


    // POST Request to REST API - accepts endpoint and body object
    public static HttpResponse<String> clientPost(String route, Object object) throws IOException, InterruptedException {
        String requestUrl = makeUrl(route);
        HttpRequest request = builderWithHeaders()
                .uri(URI.create(requestUrl))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(object)))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    // GET Request to REST API - accepts endpoint
    public static HttpResponse<String> clientGet(String route) throws IOException, InterruptedException {
        String requestUrl = makeUrl(route);
        HttpRequest request = builderWithHeaders()
                .uri(URI.create(requestUrl))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // PUT Request to REST API - accepts endpoint and body object
    public static HttpResponse<String> clientPut(String route, Object object) throws IOException, InterruptedException {
        String requestUrl = makeUrl(route);
        HttpRequest request = builderWithHeaders()
                .uri(URI.create(requestUrl))
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(object)))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // DELETE Request to REST API - accepts endpoint
    public static HttpResponse<String> clientDelete(String route) throws IOException, InterruptedException {
        String requestUrl = makeUrl(route);
        HttpRequest request = builderWithHeaders()
                .uri(URI.create(requestUrl))
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


}
