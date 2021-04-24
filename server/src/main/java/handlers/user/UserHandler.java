package handlers.user;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import models.AccountType;
import models.User;

import java.io.IOException;
import java.util.UUID;

public class UserHandler extends RequestHandler {

    @Override
    protected void handleGet(HttpExchange t) throws IOException {
        User user = new User(UUID.randomUUID(), "user_123", AccountType.USER, UUID.randomUUID());
        writeResponseBody(t, user);
    }

    @Override
    protected void handlePost(HttpExchange t) throws IOException {

    }

    @Override
    protected void handlePut(HttpExchange t) throws IOException {

    }

    @Override
    protected void handleDelete(HttpExchange t) throws IOException {

    }
}
