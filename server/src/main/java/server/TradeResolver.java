package server;

import database.datasources.*;
import exceptions.InvalidTransactionException;
import models.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.UUID;


public class TradeResolver extends TimerTask {

    /**
     * Trades are stored temporarily in HashMap, mapping UUIDs to ArrayLists. This
     * is a helper method for inserting a new entry in the HashMap if the AssetType UUID does
     * not yet exist in the map, OR, adding the new OpenTrade to an existing array if the
     * AssetType has been inserted previously.
     *
     * @param tradeMap    HashMap to insert OpenTrade into
     * @param insertTrade OpenTrade to insert into HashMap
     * @param assetTypeId AssetType UUID that the OpenTrade is concerned with
     */
    private void insertTradeInMap(HashMap<UUID, ArrayList<OpenTrade>> tradeMap, OpenTrade insertTrade, UUID assetTypeId) {
        if (tradeMap.get(assetTypeId) == null) {
            ArrayList<OpenTrade> newEntry = new ArrayList<>();
            newEntry.add(insertTrade);
            tradeMap.put(insertTrade.getAssetType(), newEntry);
        } else {
            tradeMap.get(assetTypeId).add(insertTrade);
        }
    }

    private OrganisationalUnit updateCreditBalance(OpenTrade trade, Float totalPrice, BatchedQuery batchedQuery) throws InvalidTransactionException, SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();

        // Get OrgUnit from the DB given the trade's OrgUnit ID
        OrganisationalUnit orgUnit = organisationalUnitDataSource.getById(trade.getOrganisationalUnit());

        Float newBalance = trade.getTradeType() == TradeType.BUY ?
                // If trade is a BUY trade, the OrgUnit will spend/lose credits
                orgUnit.getCreditBalance() - totalPrice :
                // If trade is a SELL trade, the OrgUnit will receive/gain credits
                orgUnit.getCreditBalance() + totalPrice;
        orgUnit.updateCreditBalance(newBalance);

        // Persist credit balance update to DB
        batchedQuery.addToBatch(
                organisationalUnitDataSource.getUpdateByAttributeQuery(orgUnit.getUnitId(), "creditBalance", orgUnit)
        );

        // Return OrgUnit so we don't have to fetch it from the DB again
        return orgUnit;
    }

    private Integer updateTradeQuantity(OpenTrade smallerQuantityTrade, OpenTrade largerQuantityTrade, BatchedQuery batchedQuery) throws SQLException {
        OpenTradeDataSource openTradeDataSource = new OpenTradeDataSource();

        // Trade with smaller quantity will always be the resolved quantity
        int resolvedQuantity = smallerQuantityTrade.getQuantity();

        // Delete the smaller quantity trade as its quantity will be reduced to 0
        batchedQuery.addToBatch(
                openTradeDataSource.getDeleteByIdQuery(smallerQuantityTrade.getTradeId())
        );

        // Set the larger quantity trade's new quantity to the difference in trade quantities
        largerQuantityTrade.setQuantity(largerQuantityTrade.getQuantity() - smallerQuantityTrade.getQuantity());
        batchedQuery.addToBatch(
                openTradeDataSource.getUpdateByAttributeQuery(largerQuantityTrade.getTradeId(), "quantity", largerQuantityTrade)
        );

        // Return resolved quantity for convenience
        return resolvedQuantity;
    }

    /**
     * Update both of the OrganisationalUnits that associated with resolving a trade.
     * E.g. the OrgUnit that placed the BUY OpenTrade, and the OrgUnit that placed the
     * SELL OpenTrade. Both OrgUnits' credit balances and quantities of the given AssetType
     * are updated.
     *
     * @param assetTypeId      UUID of AssetType associated with trades
     * @param buyTrade         OpenTrade object for the BUY trade
     * @param sellTrade        OpenTrade object for the SELL trade
     * @param resolvedQuantity Quantity of AssetType that was exchanged
     */
    private void updateOrgUnits(UUID assetTypeId, OpenTrade buyTrade, OpenTrade sellTrade, Integer resolvedQuantity, BatchedQuery batchedQuery) throws SQLException, InvalidTransactionException {

        AssetDataSource assetDataSource = new AssetDataSource();

        Float totalPrice = resolvedQuantity * sellTrade.getPricePerAsset();

        // Update creditBalance of the OrgUnit that placed the BUY order
        OrganisationalUnit buyOrgUnit = updateCreditBalance(buyTrade, totalPrice, batchedQuery);

        // Update creditBalance of the OrgUnit that placed the SELL order
        OrganisationalUnit sellOrgUnit = updateCreditBalance(sellTrade, totalPrice, batchedQuery);

        // Get the Asset from each OrgUnit with assetTypeId so we can update quantities
        Asset buyOrgUnitAsset = buyOrgUnit.findExistingAsset(assetTypeId);
        Asset sellOrgUnitAsset = sellOrgUnit.findExistingAsset(assetTypeId);

        // The BUYing OrgUnit may not own any of this AssetType yet, so create a new Asset if not
        if (buyOrgUnitAsset != null) {
            // Update if already owns AssetType
            int buyOrgUnitAssetQuantity = buyOrgUnitAsset.getQuantity() + resolvedQuantity;
            batchedQuery.addToBatch(
                    assetDataSource.getUpdateAssetQuantityQuery(buyOrgUnit.getUnitId(), assetTypeId, buyOrgUnitAssetQuantity)
            );
        } else {
            // Create new if does not own AssetType
            Asset asset = new Asset(assetTypeId, resolvedQuantity);
            batchedQuery.addToBatch(
                    assetDataSource.getCreateNewQuery(asset, buyOrgUnit.getUnitId())
            );
        }

        // The SELLing OrgUnit should always have some of the AssetType already
        int sellOrgUnitAssetQuantity = sellOrgUnitAsset.getQuantity() - resolvedQuantity;

        if (sellOrgUnitAssetQuantity < 0) {
            throw new InvalidTransactionException();
        }

        batchedQuery.addToBatch(
                assetDataSource.getUpdateAssetQuantityQuery(sellOrgUnit.getUnitId(), assetTypeId, sellOrgUnitAssetQuantity)
        );
    }

    public void run() {
        /*
         *  Fetch all trades from DB that are unresolved
         */
        OpenTradeDataSource openTradeDataSource = new OpenTradeDataSource();
        ResolvedTradeDataSource resolvedTradeDataSource = new ResolvedTradeDataSource();

        ArrayList<OpenTrade> openTrades;
        BatchedQuery batchedQuery;

        try {
            batchedQuery = new BatchedQuery();
            openTrades = openTradeDataSource.getAll();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return;
        }

        System.out.println("RESOLVING TRADES...");

        HashMap<UUID, ArrayList<OpenTrade>> buyTradesMap = new HashMap<>();
        HashMap<UUID, ArrayList<OpenTrade>> sellTradesMap = new HashMap<>();

        // Separate unresolved trades into separate hashmaps, stored at the UUID of the AssetType.
        for (OpenTrade trade : openTrades) {
            UUID assetTypeId = trade.getAssetType();

            if (trade.getTradeType() == TradeType.BUY) {
                insertTradeInMap(buyTradesMap, trade, assetTypeId);
            } else {
                insertTradeInMap(sellTradesMap, trade, assetTypeId);
            }
        }

        // Only proceed if there are trades to resolve
        if (buyTradesMap.size() <= 0 || sellTradesMap.size() <= 0) {
            return;
        }

        // For each AssetType...
        for (UUID assetTypeId : buyTradesMap.keySet()) {
            // Make sure that the lists at each key are sorted by date
            buyTradesMap.get(assetTypeId).sort(OpenTrade.tradeDateComparator);

            if (sellTradesMap.get(assetTypeId) == null) {
                // No SELL orders with this assetTypeId, skip to next id
                continue;
            }

            sellTradesMap.get(assetTypeId).sort(OpenTrade.tradeDateComparator);
            ArrayList<OpenTrade> sellTradesOfSameAssetType = sellTradesMap.get(assetTypeId);

            // For each BUY trade...
            for (OpenTrade buyTrade : buyTradesMap.get(assetTypeId)) {

                // For each SELL trade of same AssetType...
                for (OpenTrade sellTrade : sellTradesOfSameAssetType) {
                        /*
                            Find sell trade that satisfies:
                            -> BUY unit price >= SELL unit price

                            Break loop if condition is met, set OpenTrade to be deleted from the openTrades
                            table, and create a new ResolvedTrade to be added to the resolvedTrades table.

                            If both BUY & SELL trades' quantities satisfy each other exactly, then both OpenTrades
                            will be deleted. Otherwise, only one of the OpenTrades will be deleted (the fully satisfied
                            trade) and the other will be updated to reflect the new quantity.
                         */
                    if (
                            buyTrade.getPricePerAsset() >= sellTrade.getPricePerAsset() &&
                                    // Cannot trade within an Organisational Unit
                                    buyTrade.getOrganisationalUnit() != sellTrade.getOrganisationalUnit()
                    ) {
                        // Conditions have been satisfied for BUY trade, resolve it
                        Integer resolvedQuantity;
                        try {
                            // Make sure we have a fresh query batch
                            batchedQuery.clearBatch();

                            if (buyTrade.getQuantity() > sellTrade.getQuantity()) {
                                // Buying more than is available, SELL trade gets consumed entirely and deleted
                                resolvedQuantity = updateTradeQuantity(sellTrade, buyTrade, batchedQuery);
                            } else if (buyTrade.getQuantity() < sellTrade.getQuantity()) {
                                // Buying less than is available, BUY trade gets consumed entirely and deleted
                                resolvedQuantity = updateTradeQuantity(buyTrade, sellTrade, batchedQuery);
                            } else {
                                // Quantities are equal, both OpenTrades get deleted
                                resolvedQuantity = sellTrade.getQuantity();

                                batchedQuery.addToBatch(
                                        openTradeDataSource.getDeleteByIdQuery(buyTrade.getTradeId())
                                );
                                batchedQuery.addToBatch(
                                        openTradeDataSource.getDeleteByIdQuery(sellTrade.getTradeId())
                                );
                            }

                            // Create a ResolvedTrade to signify resolution
                            ResolvedTrade resolvedTrade = new ResolvedTrade(
                                    buyTrade.getTradeId(),
                                    sellTrade.getTradeId(),
                                    buyTrade.getOrganisationalUnit(),
                                    sellTrade.getOrganisationalUnit(),
                                    assetTypeId,
                                    resolvedQuantity,
                                    sellTrade.getPricePerAsset(),
                                    Timestamp.from(Instant.now())
                            );
                            batchedQuery.addToBatch(
                                    resolvedTradeDataSource.getCreateNewQuery(resolvedTrade)
                            );

                            /*
                             * Update the credit balance and quantities of AssetType for each
                             * OrgUnit associated with this trade.
                             */
                            updateOrgUnits(assetTypeId, buyTrade, sellTrade, resolvedQuantity, batchedQuery);

                            batchedQuery.executeBatch();

                            // Exit for-loop of SELL trades as a resolving SELL trade has been found
                            break;
                        } catch (SQLException | InvalidTransactionException throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }
            }
        }
        System.out.println("RESOLVING TRADES COMPLETE!");
    }
}
