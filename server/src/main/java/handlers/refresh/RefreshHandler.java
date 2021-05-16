package handlers.refresh;

import com.sun.net.httpserver.HttpExchange;
import handlers.AbstractRequestHandler;

import java.io.IOException;

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

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        writeResponseBody(exchange, null);
    }
}
