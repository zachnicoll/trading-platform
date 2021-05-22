package handlers;

import com.sun.net.httpserver.HttpExchange;
import database.datasources.AssetDataSource;
import handlers.AbstractRequestHandler;
import models.Asset;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Route: /assets/
 * <p>
 * Supported Methods:
 */
public class AssetsHandler extends AbstractRequestHandler {

    public AssetsHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException, SQLException {
        AssetDataSource assetDataSource = new AssetDataSource();
        String[] params = exchange.getRequestURI().getRawPath().split("/");
        ArrayList<Asset>  assets;

        if (params.length == 3) {
            // Organisational Unit Id is present in URL, use it to filter assets
            UUID orgUnitId = UUID.fromString(params[2]);
            assets = assetDataSource.getByOrgUnitId(orgUnitId);
        } else {
            // Otherwise just get all
            assets = assetDataSource.getAll();
        }

        writeResponseBody(exchange, assets);
    }
}
