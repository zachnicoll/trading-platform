package handlers;

import com.sun.net.httpserver.HttpExchange;
import database.datasources.AssetTypeDataSource;
import models.AssetType;
import models.partial.PartialAssetType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Route: /assettype/
 * <p>
 * Supported Methods:
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

    @Override
    protected void handlePost(HttpExchange exchange) throws SQLException, IOException {
        PartialAssetType partialAssetType = (PartialAssetType) readRequestBody(exchange, PartialAssetType.class);
        AssetType newAssetType = new AssetType(
                UUID.randomUUID(),
                partialAssetType.assetName
        );

        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
        assetTypeDataSource.createNew(newAssetType);

        writeResponseBody(exchange, newAssetType);
    }
}
