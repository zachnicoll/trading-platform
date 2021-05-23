package handlers;

import com.sun.net.httpserver.HttpExchange;
import database.datasources.AssetTypeDataSource;
import errors.JsonError;
import models.Asset;
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
        checkIsAdmin(exchange);

        PartialAssetType partialAssetType = (PartialAssetType) readRequestBody(exchange, PartialAssetType.class);
        AssetType newAssetType = new AssetType(
                UUID.randomUUID(),
                partialAssetType.assetName
        );

        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
        assetTypeDataSource.createNew(newAssetType);

        writeResponseBody(exchange, newAssetType);
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws SQLException, IOException {
        checkIsAdmin(exchange);
        String[] params = exchange.getRequestURI().getRawPath().split("/");

        if (params.length == 3) {
            // AssetType ID is present in the URL, use it to delete AssetType
            AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
            UUID assetTypeId = UUID.fromString(params[2]);
            assetTypeDataSource.deleteById(assetTypeId);
            writeResponseBody(exchange, null);
        } else {
            JsonError jsonError = new JsonError("AssetType not found");
            writeResponseBody(exchange, jsonError,404);
        }
    }
}
