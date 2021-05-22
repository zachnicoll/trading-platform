package handlers;

import com.sun.net.httpserver.HttpExchange;
import database.datasources.AssetTypeDataSource;
import handlers.AbstractRequestHandler;
import models.AssetType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    protected void handleGet(HttpExchange exchange) throws IOException, SQLException {
        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
        ArrayList<AssetType> assetTypes = assetTypeDataSource.getAll();
        writeResponseBody(exchange, assetTypes);
    }
}
