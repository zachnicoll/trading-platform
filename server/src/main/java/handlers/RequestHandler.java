package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

public abstract class RequestHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
        switch (t.getRequestMethod()) {
            case "GET":
                handleGet(t);
                break;
            case "POST":
                handlePost(t);
                break;
            case "PUT":
                handlePut(t);
                break;
            case "DELETE":
                handleDelete(t);
                break;
        }
    }

    /**
     * Converts an Object to a JSON-formatted string.
     * @param o Object to convert to JSON
     * @return JSON String representation of the Object
     */
    protected String objectToJson(Object o) {
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return json;
    }

    /**
     * Converts given Object to a JSON string and writes it to
     * the HttpExchange's response body, returning with a 200 response code.
     * @param t HttpExchange to write response body to
     * @param o Object to return in response body
     * @throws IOException If writing response body fails
     */
    protected void writeResponseBody(HttpExchange t, Object o) throws IOException {
        String json = objectToJson(o);
        t.sendResponseHeaders(200, json.length());
        OutputStream os = t.getResponseBody();
        os.write(json.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    protected abstract void handleGet(HttpExchange t) throws IOException;
    protected abstract void handlePost(HttpExchange t) throws IOException;
    protected abstract void handlePut(HttpExchange t) throws IOException;
    protected abstract void handleDelete(HttpExchange t) throws IOException;
}


//        AssetType assetType = new AssetType("thisisanID", "Some AssetType Name");
//        Gson gson = new Gson();
//        String json = gson.toJson(assetType);
//        t.sendResponseHeaders(200, json.length());
//        OutputStream os = t.getResponseBody();
//        os.write(json.getBytes(StandardCharsets.UTF_8));
//        os.close();