package handlers.resetpassword;

import com.sun.net.httpserver.HttpExchange;
import handlers.AbstractRequestHandler;

import java.io.IOException;

/**
 * Route: /resetpassword/
 *
 * Supported Methods:
 *
 */
public class ResetPasswordHandler extends AbstractRequestHandler {

    public ResetPasswordHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        writeResponseBody(exchange, null);
    }
}
