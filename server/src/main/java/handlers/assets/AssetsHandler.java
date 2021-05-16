package handlers.assets;

import com.sun.net.httpserver.HttpExchange;
import handlers.AbstractRequestHandler;
import models.Asset;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Route: /assets/
 *
 * Supported Methods:
 *
 */
public class AssetsHandler extends AbstractRequestHandler {

    public AssetsHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        ArrayList<Asset> assets = new ArrayList<>();
        writeResponseBody(exchange, assets);
    }
}
