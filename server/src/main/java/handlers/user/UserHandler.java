package handlers.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sun.net.httpserver.HttpExchange;
import database.datasources.UserDataSource;
import handlers.AbstractRequestHandler;
import models.AccountType;
import models.User;
import models.partial.PartialUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Route: /user/
 * <p>
 * Supported Methods:
 */
public class UserHandler extends AbstractRequestHandler {

    public UserHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        User user = new User(UUID.randomUUID(), "user_123", AccountType.USER, UUID.randomUUID());
        writeResponseBody(exchange, user);
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, SQLException {
        PartialUser partialUser = (PartialUser) readRequestBody(exchange, PartialUser.class);
        User fullUser = new User(
                UUID.randomUUID(),
                partialUser.username,
                partialUser.accountType,
                partialUser.organisationalUnitId
        );

        String hashedPassword = BCrypt.withDefaults().hashToString(12, partialUser.password.toCharArray());

        UserDataSource userDataSource = new UserDataSource();

        userDataSource.createNew(fullUser, hashedPassword);

        writeResponseBody(exchange, fullUser);
    }
}
