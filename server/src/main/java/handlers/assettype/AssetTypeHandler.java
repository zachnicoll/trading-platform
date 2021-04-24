package handlers.assettype;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import models.AccountType;
import models.AssetType;
import models.User;

import java.io.IOException;
import java.util.UUID;

public class AssetTypeHandler extends RequestHandler {

    @Override
    protected void handleGet(HttpExchange t) throws IOException {
        AssetType assetType = new AssetType(UUID.randomUUID().toString(), "some_asset_name");
        writeResponseBody(t, assetType);
    }

    @Override
    protected void handlePost(HttpExchange t) throws IOException {
        respondNotImplemented(t);
    }

    @Override
    protected void handlePut(HttpExchange t) throws IOException {
        respondNotImplemented(t);
    }

    @Override
    protected void handleDelete(HttpExchange t) throws IOException {
        respondNotImplemented(t);
    }
}
