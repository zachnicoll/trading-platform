package handlers;

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
    protected void handlePost(HttpExchange exchange) throws IOException {

    }
}
