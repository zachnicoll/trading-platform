package handlers.login;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.sun.net.httpserver.HttpExchange;
import database.datasources.UserDataSource;
import errors.JsonError;
import handlers.AbstractRequestHandler;
import models.AccountType;
import models.AuthenticationToken;
import models.User;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Route: /login/
 * <p>
 * Supported Methods:
 * [POST] Returns a JWT authentication token if provided credentials are correct
 */
public class LoginHandler extends AbstractRequestHandler {

    public LoginHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    /**
     * Creates a Date object exactly 24 hours after right now. This is used
     * to create the expiry DateTime for the JWT token's 'exp' claim.
     *
     * @return A Date object, set to 24 hours from now
     */
    private Date tokenExpiryTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        return calendar.getTime();
    }

    /**
     * Method: POST
     * Takes a username and password defined in the request body and
     * returns a JWT authentication token as a String if the credentials
     * are correct.
     */
    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        try {
            UsernamePassword providedCredentials = (UsernamePassword) readRequestBody(exchange, UsernamePassword.class);

            // Check that username and password was actually supplied in the request
            if (providedCredentials == null) {
                throw new AuthenticationException();
            }

            // Get the existing credentials that match the User with corresponding username
            UserDataSource userDataSource = new UserDataSource();
            UsernamePassword existingCredentials = userDataSource.getExistingCredentials(providedCredentials.username);

            // Compare passwords
            BCrypt.Result result = BCrypt.verifyer().verify(providedCredentials.password.toCharArray(), existingCredentials.password);

            // Reject if passwords did not match
            if (!result.verified) {
                throw new AuthenticationException();
            }

            User loggedInUser = userDataSource.getByUsername(providedCredentials.username);

            Algorithm algorithm = Algorithm.HMAC256("secret");

            // Encode claims (meta-data) and create a JWT authentication token
            String token = JWT.create()
                    .withIssuer("cab302-group10")
                    .withIssuedAt(new Date()) // Issued right now
                    .withExpiresAt(tokenExpiryTime()) // Expires in 24 hours
                    .withSubject(loggedInUser.getUserId().toString())
                    .withClaim("type", AccountType.USER.toString())
                    .sign(algorithm);

            AuthenticationToken authenticationToken = new AuthenticationToken(token);
            writeResponseBody(exchange, authenticationToken);
        } catch (JWTCreationException jwtException) {
            // Invalid Signing configuration / Couldn't convert Claims.
            JsonError jsonError = new JsonError("Failed to login");
            writeResponseBody(exchange, jsonError, 500);
        } catch (AuthenticationException | SQLException authException) {
            // Invalid credentials / user doesn't exist with that username
            JsonError jsonError = new JsonError("Username or password incorrect");
            writeResponseBody(exchange, jsonError, 403);
        }

    }
}
