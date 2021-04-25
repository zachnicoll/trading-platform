package handlers.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.sun.net.httpserver.HttpExchange;
import errors.JsonError;
import handlers.RequestHandler;
import models.AuthenticationToken;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Route: /login/
 *
 * Supported Methods:
 * [POST] Returns a JWT authentication token if provided credentials are correct
 */
public class LoginHandler extends RequestHandler {

    public LoginHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    /**
     * Creates a Date object exactly 24 hours after right now. This is used
     * to create the expiry DateTime for the JWT token's 'exp' claim.
     * @return A Date object, set to 24 hours from now
     */
    private Date tokenExpiryTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        return  calendar.getTime();
    }

    /**
     *  Method: POST
     *  Takes a username and password defined in the request body and
     *  returns a JWT authentication token as a String if the credentials
     *  are correct.
     */
    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");

            // Encode claims (meta-data) and create a JWT authentication token
            String token = JWT.create()
                    .withIssuer("cab302-group10")
                    .withIssuedAt(new Date()) // Issued right now
                    .withExpiresAt(tokenExpiryTime()) // Expires in 24 hours
                    // TODO: Use actual User's UUID for this when DB setup
                    .withSubject(UUID.randomUUID().toString())
                    .sign(algorithm);

            AuthenticationToken authenticationToken = new AuthenticationToken(token);
            writeResponseBody(exchange, authenticationToken);
        } catch (JWTCreationException jwtException) {
            // Invalid Signing configuration / Couldn't convert Claims.
            JsonError jsonError = new JsonError("Failed to login");
            writeResponseBody(exchange, jsonError, 500);
        }
        /*
         * TODO: Uncomment this when User from DB is implemented
         *         catch (AuthenticationException authException) {
         *             // Invalid credentials / user doesn't exist with that username
         *             JsonError jsonError = new JsonError("Username or password incorrect");
         *             writeResponseBody(exchange, jsonError, 403);
         *         }
         */
    }
}
