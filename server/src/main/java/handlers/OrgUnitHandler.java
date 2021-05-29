package handlers;

import com.sun.net.httpserver.HttpExchange;
import database.datasources.AssetDataSource;
import database.datasources.OpenTradeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import errors.JsonError;
import handlers.AbstractRequestHandler;
import models.Asset;
import models.OpenTrade;
import models.OrganisationalUnit;
import models.partial.PartialOrganisationalUnit;

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
    protected void handleGet(HttpExchange exchange) throws IOException, SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        String[] params = exchange.getRequestURI().getRawPath().split("/");
        OrganisationalUnit organisationalUnit;

        if (params.length == 3) {
            // Organisational Unit Id is present in URL, use it to filter assets
            UUID orgUnitId = UUID.fromString(params[2]);
            organisationalUnit = organisationalUnitDataSource.getById(orgUnitId);
        }else{
            //other get endpoints
            organisationalUnit = null;
        }

        writeResponseBody(exchange, organisationalUnit);
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, SQLException {

        //checks if user has admin privileges
        checkIsAdmin(exchange);

        // Make new OrganisationalUnit object from json in request body
        PartialOrganisationalUnit partialOrganisationalUnit = (PartialOrganisationalUnit) readRequestBody(exchange, OrganisationalUnit.class);
        OrganisationalUnit fullOrganisationalUnit =
                new OrganisationalUnit(
                        UUID.randomUUID(),
                        partialOrganisationalUnit.unitName,
                        partialOrganisationalUnit.creditBalance,
                        new ArrayList<Asset>()
                );

        //check if unit name is not null
        if(fullOrganisationalUnit.getUnitName() == null){
            writeResponseBody(exchange, new JsonError("Organisational Unit does not have name"));
            return;
        }

        //check if unit's credit balance is less than zero
        if(fullOrganisationalUnit.getCreditBalance() < 0){
            writeResponseBody(exchange, new JsonError("Organisational Unit has credit balance less than zero"));
            return;
        }


        // Create new OrganisationalUnit in DB
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        organisationalUnitDataSource.createNew(fullOrganisationalUnit);

        // Respond with created Object
        writeResponseBody(exchange, fullOrganisationalUnit);
    }
}

