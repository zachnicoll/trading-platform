package handlers.orgunit;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import models.Asset;
import models.OrganisationalUnit;

import java.io.IOException;
import java.util.ArrayList;

public class OrgUnitHandler extends RequestHandler {
    public OrgUnitHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange t) throws IOException {
        ArrayList<Asset> assets = new ArrayList<>();
        OrganisationalUnit organisationalUnit = new OrganisationalUnit("org-unit-id", "Org Unit Name", 1000.0f, assets);
        writeResponseBody(t, organisationalUnit);
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