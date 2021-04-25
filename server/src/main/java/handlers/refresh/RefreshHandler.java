package handlers.refresh;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import java.io.IOException;

public class RefreshHandler extends RequestHandler {

    public RefreshHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

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
