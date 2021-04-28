package handlers.refresh;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;

import java.io.IOException;

/**
 * Route: /refresh/
 *
 * Supported Methods:
 *
 */
public class RefreshHandler extends RequestHandler {

    public RefreshHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        writeResponseBody(exchange, null);
    }
}
