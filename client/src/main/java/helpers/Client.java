package helpers;

import com.google.gson.Gson;
import models.AuthenticationToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {


    private static String http;

    public static HttpResponse<String> clientPost(String url, Object object) throws IOException, InterruptedException {

        http = "http://localhost:8000"; //TODO CHANGE ONCE TXT READ IS READY

        Gson gson = new Gson();

        String requestUrl = http + url;

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(object)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public static HttpResponse<String> clientGet(String url, AuthenticationToken authToken) throws IOException, InterruptedException {

        http = "http://localhost:8000"; //TODO CHANGE ONCE TXT READ IS READY

        String requestUrl = http + url;

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET().setHeader("Authorization", "Bearer "+ authToken.toString())
                .build();

        HttpResponse<String> userResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        return userResponse;
    }


}
