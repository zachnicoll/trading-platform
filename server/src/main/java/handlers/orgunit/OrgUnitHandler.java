package handlers.orgunit;

import com.sun.net.httpserver.HttpExchange;
import handlers.RequestHandler;
import models.Asset;
import models.OrganisationalUnit;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Route: /orgunit/
 *
 * Supported Methods:
 *
 */
public class OrgUnitHandler extends RequestHandler {
    public OrgUnitHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        ArrayList<Asset> assets = new ArrayList<>();
        OrganisationalUnit organisationalUnit = new OrganisationalUnit("org-unit-id", "Org Unit Name", 1000.0f, assets);
        writeResponseBody(exchange, organisationalUnit);
    }
}