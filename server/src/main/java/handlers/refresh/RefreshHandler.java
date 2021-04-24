package handlers.refresh;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import models.AccountType;
import models.User;

import java.io.IOException;
import java.util.UUID;

public class RefreshHandler extends RequestHandler {

    @Override
    protected void handleGet(HttpExchange t) throws IOException {
        writeResponseBody(t, null);
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
