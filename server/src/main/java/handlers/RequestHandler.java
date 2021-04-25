package handlers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import errors.JsonError;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class RequestHandler implements HttpHandler {
    protected boolean requiresAuth;
    private final Pattern authorizationHeaderRegex = Pattern.compile("Bearer .+");

    public RequestHandler(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public void handle(HttpExchange exchange) throws IOException {
        try {
            /*
             * If this RequestHandler requires authentication (JWT token in request header),
             * check if the token is valid before continuing. If the token is invalid, the request will
             * be resolved with a 403 - Not Authorised response code.
             */
            if (requiresAuth) {
                checkIsAuthorised(exchange);
            }

            /*
             * Based on the request's HTTP method, pass the HttpExchange to the correct handler.
             */
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGet(exchange);
                case "POST" -> handlePost(exchange);
                case "PUT" -> handlePut(exchange);
                case "DELETE" -> handleDelete(exchange);
                default -> {
                    // Respond with 404 Not Found if unsupported method received
                    exchange.sendResponseHeaders(404, 0);
                    exchange.getResponseBody().close();
                }
            }
        } catch (Exception e) {
            /*
             * A 'catch-all' error message sent back to client if an uncaught exception occurs.
             * This can still throw an IOException while sending the message back - if this happens,
             * there's probably something wrong with the code.
             */
            JsonError jsonError = new JsonError("A server-side exception occurred while resolving this request: " + e.getMessage());
            writeResponseBody(exchange, jsonError, 500);
        }
    }

    private String getTokenFromHeader(HttpExchange exchange) throws JWTVerificationException {
        Headers headers = exchange.getRequestHeaders();
        String AUTHORIZATION_HEADER_NAME = "Authorization";

        if (headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            String rawToken = headers.get(AUTHORIZATION_HEADER_NAME).get(0);
            Matcher authHeaderMatcher = authorizationHeaderRegex.matcher(rawToken);

            if (authHeaderMatcher.find()) {
                // Remove 'Bearer ' from token string
                return rawToken.split("Bearer ")[1];
            } else {
                throw new JWTVerificationException("Authorisation header not formatted correctly!");
            }
        } else {
            throw new JWTVerificationException("Authorisation header not present!");
        }
    }

    private void checkIsAuthorised(HttpExchange exchange) throws IOException {
        try {
            // Extract token string from header - Authorization: "Bearer eyJ0eX..."
            String token = getTokenFromHeader(exchange);

            // Construct verifier, expect our issuer 'cab302-group10' to be present
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("cab302-group10")
                    .build();

            /*
             * Decode JWT token string - this will throw a JWTVerificationException if:
             * - The token has expired
             * - The token is invalid
             * - The token's secret is incorrect
             *
             * If an error is not thrown, this JWT token is valid.
             */
            DecodedJWT jwt = verifier.verify(token);

            /**
             * TODO: Potentially check if provided UserId in token actually exists, when DB implemented
             */
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            JsonError jsonError = new JsonError("You are not authenticated");
            writeResponseBody(exchange, jsonError, 403);
        }
    }

    ;

    /**
     * Responds with status code 501 - Not Implemented. Used as default response for methods
     * that are not overwritten in inherited classes.
     */
    private void respondNotImplemented(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(501, 0);
        exchange.getResponseBody().close();
    }

    /**
     * Converts an Object to a JSON-formatted string.
     *
     * @param o Object to convert to JSON
     * @return JSON String representation of the Object
     */
    private String objectToJson(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    /**
     * Converts given Object to a JSON string and writes it to
     * the HttpExchange's response body, returning with a 200 response code.
     *
     * @param exchange HttpExchange to write response body to
     * @param o Object to return in response body
     * @throws IOException If writing response body fails
     */
    protected void writeResponseBody(HttpExchange exchange, Object o) throws IOException {
        String json = objectToJson(o);
        exchange.sendResponseHeaders(200, json.length());
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    /**
     * Converts given Object to a JSON string and writes it to
     * the HttpExchange's response body, returning with a response code defined in
     * function parameters.
     *
     * @param exchange HttpExchange to write response body to
     * @param o Object to return in response body
     * @param code HTTP Status code to send with the response
     * @throws IOException If writing response body fails
     */
    protected void writeResponseBody(HttpExchange exchange, Object o, Integer code) throws IOException {
        String json = objectToJson(o);
        exchange.sendResponseHeaders(code, json.length());
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    protected void handleGet(HttpExchange exchange) throws IOException {
        respondNotImplemented(exchange);
    };

    protected void handlePost(HttpExchange exchange) throws IOException {
        respondNotImplemented(exchange);
    };

    protected void handlePut(HttpExchange exchange) throws IOException {
        respondNotImplemented(exchange);
    };

    protected void handleDelete(HttpExchange exchange) throws IOException {
        respondNotImplemented(exchange);
    };
}