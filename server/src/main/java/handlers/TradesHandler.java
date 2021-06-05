package handlers;

import com.sun.net.httpserver.HttpExchange;
import database.datasources.*;
import errors.JsonError;
import models.*;
import models.partial.PartialOpenTrade;
import models.partial.PartialReadableOpenTrade;
import models.partial.PartialReadableResolvedTrade;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Route: /trades/
 * <p>
 * Supported Methods:
 * [POST] Create a new OpenTrade
 */
public class TradesHandler extends AbstractRequestHandler {

    public TradesHandler(boolean requiresAuth) {
        super(requiresAuth);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException, SQLException {

        String[] params = exchange.getRequestURI().getRawPath().split("/");

        if (params.length == 3 && params[2].equals("history")) {
            // URL equals /trades/history
            ResolvedTradeDataSource resolvedTradeDataSource = new ResolvedTradeDataSource();
            ArrayList<PartialReadableResolvedTrade> readableResolvedTrades;
            readableResolvedTrades = resolvedTradeDataSource.getAllReadable();
            writeResponseBody(exchange, readableResolvedTrades);
        } else if (params.length == 4 && params[3].equals("history")) {
            // URL equals /trades/<assetTypeId>/history
            ResolvedTradeDataSource resolvedTradeDataSource = new ResolvedTradeDataSource();
            UUID assetTypeId = UUID.fromString(params[2]);

            if (resolvedTradeDataSource.checkExistById(assetTypeId)) {
                //get all resolved trades by assetTypeId
                ArrayList<PartialReadableResolvedTrade> readableResolvedTrades;
                readableResolvedTrades = resolvedTradeDataSource.getAllByAssetReadable(assetTypeId);
                if (readableResolvedTrades.size() > 0) {
                    writeResponseBody(exchange, readableResolvedTrades);
                } else {
                    writeResponseBody(exchange, new JsonError("There are no resolved trades involving the selected assetType"), 400);
                }
            } else {
                writeResponseBody(exchange, new JsonError("Selected assetTypeId does not exist"), 404);
            }
        } else if (params.length == 3) {
            //get all current trades by assetTypeId (params[2] = assetTypeId) -- not sure if we need this
            exchange.sendResponseHeaders(501, 0);
            exchange.getResponseBody().close();
        } else {
            OpenTradeDataSource openTradeDataSource = new OpenTradeDataSource();
            ArrayList<PartialReadableOpenTrade> readableOpenTrades;

            readableOpenTrades = openTradeDataSource.getAllReadable();
            writeResponseBody(exchange, readableOpenTrades);
        }


    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, SQLException {
        // Make new OpenTrade object from json in request body
        PartialOpenTrade partialTrade = (PartialOpenTrade) readRequestBody(exchange, PartialOpenTrade.class);
        OpenTrade fullTrade = new OpenTrade(
                UUID.randomUUID(),
                partialTrade.tradeType,
                partialTrade.organisationalUnitId,
                partialTrade.assetTypeId,
                partialTrade.quantity,
                partialTrade.pricePerAsset,
                Timestamp.from(Instant.now())
        );

        /*
         * Check that new trade is valid
         */

        if (fullTrade.getQuantity() <= 0) {
            writeResponseBody(exchange, new JsonError("Quantity is less than or equal to 0"), 400);
            return;
        }

        if (fullTrade.getPricePerAsset() <= 0) {
            writeResponseBody(exchange, new JsonError("PricePerAsset is less than or equal to 0"), 400);
            return;
        }

        /*
         * Check that the provided AssetType exists in the DB
         */

        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
        // This will throw SQLException if the AssetType does not exist
        assetTypeDataSource.getById(fullTrade.getAssetType());


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

        if (!user.getOrganisationalUnitId().equals(fullTrade.getOrganisationalUnit())) {
            writeResponseBody(exchange, new JsonError("You must belong to the Organisational Unit you are placing the Trade for"), 400);
            return;
        }

        /*
         * Check that the OrgUnit has enough credits/quantity of AssetType
         */

        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        OrganisationalUnit orgUnit = organisationalUnitDataSource.getById(user.getOrganisationalUnitId());
        Float totalPrice = fullTrade.getPricePerAsset() * fullTrade.getQuantity();

        if (fullTrade.getTradeType() == TradeType.BUY && orgUnit.getCreditBalance() < totalPrice) {
            writeResponseBody(exchange, new JsonError("Organisational Unit does not have enough credits to place this order"), 400);
            return;
        } else if (fullTrade.getTradeType() == TradeType.SELL) {
            Asset existingAsset = orgUnit.findExistingAsset(fullTrade.getAssetType());

            if (existingAsset == null || existingAsset.getQuantity() < fullTrade.getQuantity()) {
                writeResponseBody(exchange, new JsonError("Organisational Unit does not have enough of the given Asset Type to place this order"), 400);
                return;
            }
        }

        // Create new trade in DB
        OpenTradeDataSource openTradeDataSource = new OpenTradeDataSource();
        openTradeDataSource.createNew(fullTrade);

        // Respond with created Object
        writeResponseBody(exchange, fullTrade);
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws SQLException, IOException {
        String[] params = exchange.getRequestURI().getRawPath().split("/");

        if (params.length == 3) {
            // AssetType ID is present in the URL, use it to delete AssetType
            OpenTradeDataSource openTradeDataSource = new OpenTradeDataSource();
            UUID openTradeId = UUID.fromString(params[2]);

            // Get organisational unit for the given open trade
            OpenTrade openTrade = openTradeDataSource.getById(openTradeId);

            OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
            OrganisationalUnit organisationalUnit = organisationalUnitDataSource.getById(openTrade.getOrganisationalUnit());

            // Check that the User making the request belongs to the Organisational Unit
            UserDataSource userDataSource = new UserDataSource();
            UUID userOrgUnitId = userDataSource.getById(UUID.fromString(getUserId(exchange))).getOrganisationalUnitId();

            if (organisationalUnit.getUnitId().equals(userOrgUnitId)) {
                openTradeDataSource.deleteById(openTradeId);
                writeResponseBody(exchange, null);
            } else {
                JsonError jsonError = new JsonError("You do not belong to Organisational Unit that opened this trade.");
                writeResponseBody(exchange, jsonError, 400);
            }
        } else {
            JsonError jsonError = new JsonError("OpenTrade not found");
            writeResponseBody(exchange, jsonError, 404);
        }
    }
}
