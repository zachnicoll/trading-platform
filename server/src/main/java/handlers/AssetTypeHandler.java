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
import java.util.Objects;
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

        if(Objects.nonNull(partialAssetType) && Objects.nonNull(partialAssetType.assetName)){
            AssetType newAssetType = new AssetType(
                    UUID.randomUUID(),
                    partialAssetType.assetName
            );

            AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
            assetTypeDataSource.createNew(newAssetType);

            writeResponseBody(exchange, newAssetType);

        }else{
            writeResponseBody(exchange, new JsonError("AssetType does not contain asset name"),400);
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws SQLException, IOException {
        checkIsAdmin(exchange);
        String[] params = exchange.getRequestURI().getRawPath().split("/");

        if (params.length == 3) {
            // AssetType ID is present in the URL, use it to delete AssetType
            AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
            UUID assetTypeId = UUID.fromString(params[2]);
            if(assetTypeDataSource.checkExistById(assetTypeId)){
                assetTypeDataSource.deleteById(assetTypeId);
                writeResponseBody(exchange, null);
            }else{
                writeResponseBody(exchange, new JsonError("AssetType not found"), 404);
            }

        } else {
            JsonError jsonError = new JsonError("AssetType Id not present");
            writeResponseBody(exchange, jsonError,400);
        }
    }
}
