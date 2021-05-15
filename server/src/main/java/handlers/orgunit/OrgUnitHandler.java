package handlers.orgunit;

import com.sun.net.httpserver.HttpExchange;
import database.datasources.OpenTradeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import handlers.AbstractRequestHandler;
import models.Asset;
import models.OpenTrade;
import models.OrganisationalUnit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Route: /orgunit/
 *
 * Supported Methods:
 *
 */
public class OrgUnitHandler extends AbstractRequestHandler {
    public OrgUnitHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        ArrayList<Asset> assets = new ArrayList<>();
        OrganisationalUnit organisationalUnit = new OrganisationalUnit(UUID.randomUUID(), "Org Unit Name", 1000.0f, assets);
        writeResponseBody(exchange, organisationalUnit);
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, SQLException {


        // Make new OrganisationalUnit object from json in request body
        DBOrganisationalUnit newOrganisationalUnit = (DBOrganisationalUnit) readRequestBody(exchange, OrganisationalUnit.class);

        // Create new OrganisationalUnit in DB
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        organisationalUnitDataSource.createNew(newOrganisationalUnit);

        // Respond with created Object
        writeResponseBody(exchange, newOrganisationalUnit);
    }
}

