package handlers.user;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import models.AccountType;
import models.User;

import java.io.IOException;
import java.util.UUID;

/**
 * Route: /user/
 *
 * Supported Methods:
 *
 */
public class UserHandler extends RequestHandler {

    public UserHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        User user = new User(UUID.randomUUID(), "user_123", AccountType.USER, UUID.randomUUID());
        writeResponseBody(exchange, user);
    }
}
