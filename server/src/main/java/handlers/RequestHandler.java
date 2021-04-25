package handlers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import errors.JsonError;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RequestHandler implements HttpHandler {
    protected boolean requiresAuth;
    private Pattern authorizationHeaderRegex = Pattern.compile("Bearer .+");
    private String AUTHORIZATION_HEADER_NAME = "Authorization";

    public RequestHandler(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public void handle(HttpExchange t) throws IOException {
        try {
            if (requiresAuth) {
                checkIsAuthorised(t);
            }

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
                default:
                    // Respond with 404 Not Found if unsupported method received
                    t.sendResponseHeaders(404, 0);
                    t.getResponseBody().close();
                    break;
            }
        } catch (Exception e) {
            /**
             * A 'catch-all' error message sent back to client if an uncaught exception occurs.
             * This can still throw an IOException while sending the message back - if this happens,
             * there's probably something wrong with the code.
             */
            JsonError jsonError = new JsonError("A server-side exception occurred while resolving this request: " + e.getMessage());
            writeResponseBody(t, jsonError, 500);
        }
    }

    private String getTokenFromHeader(HttpExchange t) throws JWTVerificationException {
        Headers headers = t.getRequestHeaders();

        if (headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            String rawToken = headers.get(AUTHORIZATION_HEADER_NAME).get(0);
            Matcher authHeaderMatcher = authorizationHeaderRegex.matcher(rawToken);

            if (authHeaderMatcher.find()) {
                // Remove 'Bearer' string;
                return rawToken.split("Bearer ")[1];
            } else {
                throw new JWTVerificationException("Authorisation header not formatted correctly!");
            }
        } else {
            throw new JWTVerificationException("Authorisation header not present!");
        }
    }

    private void checkIsAuthorised(HttpExchange t) throws IOException {
        try {
            String token = getTokenFromHeader(t);

            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("cab302-group10")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);

            /**
             * TODO: check that JWT token has not expired, should also potentially check if provided UserId in token actually exists
             */
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            JsonError jsonError = new JsonError("You are not authenticated");
            writeResponseBody(t, jsonError, 403);
        }
    }

    ;

    /**
     * TODO: Might be able to remove this once all methods have been implemented
     */
    protected void respondNotImplemented(HttpExchange t) throws IOException {
        t.sendResponseHeaders(501, 0);
        t.getResponseBody().close();
    }

    /**
     * Converts an Object to a JSON-formatted string.
     *
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
     *
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

    /**
     * Converts given Object to a JSON string and writes it to
     * the HttpExchange's response body, returning with a response code defined in
     * function parameters.
     *
     * @param t HttpExchange to write response body to
     * @param o Object to return in response body
     * @param code HTTP Status code to send with the response
     * @throws IOException If writing response body fails
     */
    protected void writeResponseBody(HttpExchange t, Object o, Integer code) throws IOException {
        String json = objectToJson(o);
        t.sendResponseHeaders(code, json.length());
        OutputStream os = t.getResponseBody();
        os.write(json.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    protected abstract void handleGet(HttpExchange t) throws IOException;

    protected abstract void handlePost(HttpExchange t) throws IOException;

    protected abstract void handlePut(HttpExchange t) throws IOException;

    protected abstract void handleDelete(HttpExchange t) throws IOException;
}