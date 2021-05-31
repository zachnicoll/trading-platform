package handlers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.net.httpserver.HttpExchange;
import database.datasources.UserDataSource;
import handlers.AbstractRequestHandler;
import models.AccountType;
import models.AuthenticationToken;
import models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Route: /refresh/
 *
 * Supported Methods:
 *
 */
public class RefreshHandler extends AbstractRequestHandler {

    public RefreshHandler(boolean requiresAuth) {
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

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException, SQLException {

        UserDataSource userDataSource = new UserDataSource();

        Algorithm algorithm = Algorithm.HMAC256("secret");

        String userId = getUserId(exchange);
        User theUser = userDataSource.getById(UUID.fromString(userId));

        // Encode claims (meta-data) and create a JWT authentication token
        String token = JWT.create()
                .withIssuer("cab302-group10")
                .withIssuedAt(new Date()) // Issued right now
                .withExpiresAt(tokenExpiryTime()) // Expires in 24 hours
                .withSubject(userId)
                .withClaim("type", theUser.getAccountType().toString())
                .sign(algorithm);

        AuthenticationToken authenticationToken = new AuthenticationToken(token);

        writeResponseBody(exchange, authenticationToken, 200);
    }


}
