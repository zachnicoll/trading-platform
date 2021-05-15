package handlers.trades;

import com.sun.net.httpserver.HttpExchange;
import database.datasources.AssetTypeDataSource;
import database.datasources.OpenTradeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import database.datasources.UserDataSource;
import errors.JsonError;
import handlers.AbstractRequestHandler;
import models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

/**
 * Route: /trades/
 * <p>
 * Supported Methods:
 */
public class TradesHandler extends AbstractRequestHandler {

    public TradesHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        /*
         * Example of how to block route method if Admin AccountType is required.
         * TODO: remove this when route is implemented correctly.
         */
        checkIsAdmin(exchange);

        OpenTrade trade = new OpenTrade(UUID.randomUUID(), TradeType.BUY, UUID.randomUUID(), UUID.randomUUID(), 100, 1.045f, Timestamp.from(Instant.now()));
        writeResponseBody(exchange, trade);
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, SQLException {
        // Make new OpenTrade object from json in request body
        OpenTrade newTrade = (OpenTrade) readRequestBody(exchange, OpenTrade.class);

        /*
         * Check that new trade is valid
         */

        if (newTrade.getQuantity() <= 0) {
            writeResponseBody(exchange, new JsonError("Quantity is less than or equal to 0"), 400);
            return;
        }

        if (newTrade.getPricePerAsset() <= 0) {
            writeResponseBody(exchange, new JsonError("PricePerAsset is less than or equal to 0"), 400);
            return;
        }

        /*
         * Check that the provided AssetType exists in the DB
         */

        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
        // This will throw SQLException if the AssetType does not exist
        assetTypeDataSource.getById(newTrade.getAssetType());


        /*
         * Check that provided UserId is valid
         */
        String userId = getUserId(exchange);

        if (userId == null) {
            writeResponseBody(exchange, new JsonError("UserId invalid"), 400);
            return;
        }

        /*
         * Check that User belongs to the OrgUnit they are placing the order for
         */
        UserDataSource userDataSource = new UserDataSource();
        User user = userDataSource.getById(UUID.fromString(userId));

        if (user.getOrganisationalUnitId() != newTrade.getOrganisationalUnit()) {
            writeResponseBody(exchange, new JsonError("You must belong to the Organisational Unit you are placing the Trade for"), 400);
            return;
        }

        /*
         * Check that the OrgUnit has enough credits/quantity of AssetType
         */

        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        OrganisationalUnit orgUnit = organisationalUnitDataSource.getById(user.getOrganisationalUnitId());
        Float totalPrice = newTrade.getPricePerAsset() * newTrade.getQuantity();

        if (newTrade.getTradeType() == TradeType.BUY && orgUnit.getCreditBalance() < totalPrice) {
            writeResponseBody(exchange, new JsonError("Organisational Unit does not have enough credits to place this order"), 400);
            return;
        } else {
            Asset existingAsset = orgUnit.findExistingAsset(newTrade.getAssetType());

            if (existingAsset == null || existingAsset.getQuantity() < newTrade.getQuantity()) {
                writeResponseBody(exchange, new JsonError("Organisational Unit does not have enough of the given Asset Type to place this order"), 400);
                return;
            }
        }

        // Create new trade in DB
        OpenTradeDataSource openTradeDataSource = new OpenTradeDataSource();
        openTradeDataSource.createNew(newTrade);

        // Respond with created Object
        writeResponseBody(exchange, newTrade);
    }
}
