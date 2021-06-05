package handlers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import errors.JsonError;
import models.AccountType;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Abstract class for handling requests to particular routes, e.g. /user/.
 * This class SHOULD be inherited from, with handleGet, handlePost, handlePut, and handleDelete
 * overridden depending on the implemented methods for a given route. If desired, authentication is
 * handle automatically by extracting and validating the JWT token from request headers.
 * Also contains methods for converting an Object to a JSON string and returning the string in the
 * response's body.
 */
public abstract class AbstractRequestHandler implements HttpHandler {
    private final Pattern authorizationHeaderRegex = Pattern.compile("Bearer .+");
    protected boolean requiresAuth;

    public AbstractRequestHandler(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    /**
     * The first method that is called when a request is received that matches the specified
     * route. Passes the HttpExchange (request data) to a corresponding handler based on the
     * request's HTTP method. Will reject requests that do not contain a JWT token when requiresAuth = true.
     *
     * @param exchange HttpExchange containing request data
     * @throws IOException If error occurs writing response
     */
    public void handle(HttpExchange exchange) throws IOException {
        try {
            /*
             * If this AbstractRequestHandler requires authentication (JWT token in request header),
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
                    // Respond with 404 - Not Found if unsupported method received
                    exchange.sendResponseHeaders(404, 0);
                    exchange.getResponseBody().close();
                }
            }
        } catch (JWTVerificationException exception) {
            // User is not authenticated
            JsonError jsonError = new JsonError("You are not authenticated");
            writeResponseBody(exchange, jsonError, 403);
        } catch (Exception e) {
            /*
             * A 'catch-all' error message sent back to client if an uncaught exception occurs.
             * This can still throw an IOException while sending the message back - if this happens,
             * there's probably something wrong with the code.
             */
            JsonError jsonError = new JsonError(
                    "A server-side exception occurred while resolving this request: " +
                            e.getMessage()
            );
            writeResponseBody(exchange, jsonError, 500);
        }
    }

    /**
     * Extract the token string from the Authorization header in the request. As the header's value
     * should be formatted like 'Bearer eyJ0eX...', the 'Bearer ' part is removed. If the
     * header is not formatted like this, or the header does not exist, an exception will be thrown.
     *
     * @param exchange HttpExchange to extract header from
     * @return JWT token as-a-string
     * @throws JWTVerificationException If Authorization header is not present or is not formatted correctly
     */
    private String getTokenFromHeader(HttpExchange exchange) throws JWTVerificationException {
        Headers headers = exchange.getRequestHeaders();
        String AUTHORIZATION_HEADER_NAME = "Authorization";

        if (headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            String rawToken = headers.get(AUTHORIZATION_HEADER_NAME).get(0);
            Matcher authHeaderMatcher = authorizationHeaderRegex.matcher(rawToken);

            if (authHeaderMatcher.find()) {
                // Remove 'Bearer ' from token string and return it
                return rawToken.split("Bearer ")[1];
            } else {
                throw new JWTVerificationException("Authorisation header not formatted correctly!");
            }
        } else {
            throw new JWTVerificationException("Authorisation header not present!");
        }
    }

    /**
     * Extract the Authorization header from the request and decode it as a JWT token.
     * If the token is not valid or has expired, the request will be returned with code
     * 403 - Not Authenticated.
     *
     * @param exchange HttpExchange to extract token from
     * @throws IOException If writing response body fails
     */
    private void checkIsAuthorised(HttpExchange exchange) throws IOException {
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
         * TODO: Potentially check if provided UserId in token exists, and 'type' claim matches the User's
         *      AccountType - when DB is implemented
         */
    }

    /**
     * Similar to checkIsAuthorised(), however this method will return a 403 - Not Authenticated
     * if the claim 'type' does not equal 'ADMIN' in the supplied JWT token. This should only be
     * used inside routes requiring authentication to ensure that the JWT token has been
     * verified previously.
     *
     * @param exchange HttpExchange to extract token from
     * @throws IOException If writing response body fails
     */
    protected void checkIsAdmin(HttpExchange exchange) throws IOException {
        // Extract token string from header - Authorization: "Bearer eyJ0eX..."
        String token = getTokenFromHeader(exchange);

        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm)
                // Expect claim 'type' to equal 'ADMIN'
                .withClaim("type", AccountType.ADMIN.toString())
                .build();
        verifier.verify(token);
    }

    /**
     * Extract the UserId from the JWt token provided in the header of the request
     *
     * @param exchange HttpExchange to extract the UserId from
     * @return UserId as a String, or null if extraction fails
     */
    protected String getUserId(HttpExchange exchange) {
        try {
            // Extract token string from header - Authorization: "Bearer eyJ0eX..."
            String token = getTokenFromHeader(exchange);

            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims().get("sub").asString();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

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
     * Converts the body of the request from a JSON string to an object of type T.
     * The object is not cast to T, however, so this will need to be done manually.
     *
     * @param exchange HttpExchange to read request body from
     * @param T        Type to convert the JSON object to
     * @return Object of type T, although not explicitly casted
     */
    protected Object readRequestBody(HttpExchange exchange, Type T) {
        InputStream bodyStream = exchange.getRequestBody();
        String json = new BufferedReader(
                new InputStreamReader(bodyStream, StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, T);
    }

    /**
     * Converts given Object to a JSON string and writes it to
     * the HttpExchange's response body, returning with a 200 response code.
     *
     * @param exchange HttpExchange to write response body to
     * @param o        Object to return in response body
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
     * @param o        Object to return in response body
     * @param code     HTTP Status code to send with the response
     * @throws IOException If writing response body fails
     */
    protected void writeResponseBody(HttpExchange exchange, Object o, Integer code) throws IOException {
        String json = objectToJson(o);
        exchange.sendResponseHeaders(code, json.length());
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    /**
     * Handles GET method of route. Return 501 - Not Implemented by default.
     */
    protected void handleGet(HttpExchange exchange) throws IOException, SQLException {
        respondNotImplemented(exchange);
    }

    /**
     * Handles POST method of route. Return 501 - Not Implemented by default.
     */
    protected void handlePost(HttpExchange exchange) throws IOException, SQLException {
        respondNotImplemented(exchange);
    }

    /**
     * Handles PUT method of route. Return 501 - Not Implemented by default.
     */
    protected void handlePut(HttpExchange exchange) throws IOException, SQLException {
        respondNotImplemented(exchange);
    }

    /**
     * Handles DELETE method of route. Return 501 - Not Implemented by default.
     */
    protected void handleDelete(HttpExchange exchange) throws IOException, SQLException {
        respondNotImplemented(exchange);
    }
}