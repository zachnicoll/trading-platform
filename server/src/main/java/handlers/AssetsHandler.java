package handlers;

import com.sun.net.httpserver.HttpExchange;
import database.datasources.AssetDataSource;
import database.datasources.AssetTypeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import errors.JsonError;
import handlers.AbstractRequestHandler;
import models.Asset;
import models.AssetType;
import models.OrganisationalUnit;
import models.partial.PartialAssetType;

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
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        String[] params = exchange.getRequestURI().getRawPath().split("/");
        ArrayList<Asset>  assets;
        if (params.length == 3) {
            // Organisational Unit Id is present in URL, use it to filter assets
            UUID orgUnitId = UUID.fromString(params[2]);
            if(organisationalUnitDataSource.checkExistById(orgUnitId)){
                assets = assetDataSource.getByOrgUnitId(orgUnitId);
                writeResponseBody(exchange, assets);
            }else{
                writeResponseBody(exchange, new JsonError("Organisational Unit does not exist"), 404);
            }

        } else {
            // Otherwise just get all
            assets = assetDataSource.getAll();
            writeResponseBody(exchange, assets);
        }
    }
    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException, SQLException {

        AssetDataSource assetDataSource = new AssetDataSource();
        OrganisationalUnitDataSource orgUnitDataSource = new OrganisationalUnitDataSource();
        String[] params = exchange.getRequestURI().getRawPath().split("/");

        UUID orgUnitId = UUID.fromString(params[2]);
        UUID assetTypeId = UUID.fromString(params[3]);

        if (!orgUnitDataSource.checkExistById(orgUnitId)) {
            JsonError jsonError = new JsonError("Organisational Unit does not exist");
            writeResponseBody(exchange, jsonError, 404);
        }
        else if (!assetDataSource.checkExistById(assetTypeId, orgUnitId)){
            JsonError jsonError = new JsonError("Organisational Unit does not own any of the given Asset Type");
            writeResponseBody(exchange, jsonError, 400);
        }
        else{
            assetDataSource.deleteById(assetTypeId, orgUnitId);
            writeResponseBody(exchange, null, 200);
        }
    }

    @Override
    protected void handlePut(HttpExchange exchange) throws IOException, SQLException {

        AssetDataSource assetDataSource = new AssetDataSource();
        String[] params = exchange.getRequestURI().getRawPath().split("/");

        UUID orgUnitId = UUID.fromString(params[2]);

        Asset asset = (Asset) readRequestBody(exchange, Asset.class);

        if (asset.getQuantity() < 1)
        {
            JsonError jsonError = new JsonError("Provided Quantity is less than 1");
            writeResponseBody(exchange, jsonError, 400);
        }
        else {
            if (assetDataSource.checkExistById(asset.getAssetTypeId(), orgUnitId)) {
                assetDataSource.updateAssetQuantity(orgUnitId, asset.getAssetTypeId(), asset.getQuantity());
            } else {
                assetDataSource.createNew(asset, orgUnitId);
            }
            writeResponseBody(exchange, null, 200);
        }
    }

}
