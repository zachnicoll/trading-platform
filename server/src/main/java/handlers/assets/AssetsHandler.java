package handlers.assets;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import models.AccountType;
import models.Asset;
import models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AssetsHandler extends RequestHandler {

    @Override
    protected void handleGet(HttpExchange t) throws IOException {
        ArrayList<Asset> assets = new ArrayList<>();
        writeResponseBody(t, assets);
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
