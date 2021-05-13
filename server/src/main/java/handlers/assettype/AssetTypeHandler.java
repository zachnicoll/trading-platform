package handlers.assettype;

import com.sun.net.httpserver.HttpExchange;
import handlers.AbstractRequestHandler;
import models.AssetType;

import java.io.IOException;
import java.util.UUID;

/**
 * Route: /assettype/
 *
 * Supported Methods:
 *
 */
public class AssetTypeHandler extends AbstractRequestHandler {

    public AssetTypeHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        AssetType assetType = new AssetType(UUID.randomUUID(), "some_asset_name");
        writeResponseBody(exchange, assetType);
    }
}
