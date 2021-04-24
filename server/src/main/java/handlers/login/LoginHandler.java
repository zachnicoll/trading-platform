package handlers.login;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import models.AccountType;
import models.AuthenticationToken;
import models.User;

import java.io.IOException;
import java.util.UUID;

public class LoginHandler extends RequestHandler {

    @Override
    protected void handleGet(HttpExchange t) throws IOException {
        AuthenticationToken authenticationToken = new AuthenticationToken("aaaabbbbccccdddd--ffff");
        writeResponseBody(t, authenticationToken);
    }

    @Override
    protected void handlePost(HttpExchange t) throws IOException {
        respondNotImplemented(t);
    }

    @Override
    protected void handlePut(HttpExchange t) throws IOException {
        respondNotImplemented(t);
    }

    @Override
    protected void handleDelete(HttpExchange t) throws IOException {
        respondNotImplemented(t);
    }
}
