package server;

import database.datasources.OpenTradeDataSource;
import database.datasources.ResolvedTradeDataSource;
import models.OpenTrade;
import models.ResolvedTrade;
import models.TradeType;

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
     * not yet exist in the map, OR, adding the new OpenTrade to an existing array in the
     * AssetType has been inserted previously.
     * @param tradeMap HashMap to insert OpenTrade into
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

    public void run() {
        System.out.println("RESOLVING TRADES...");

        /*
         *  Fetch all trades from DB that are unresolved
         */
        OpenTradeDataSource openTradeDataSource = new OpenTradeDataSource();
        ResolvedTradeDataSource resolvedTradeDataSource = new ResolvedTradeDataSource();
        ArrayList<OpenTrade> openTrades;

        try {
            openTrades = openTradeDataSource.getAll();

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

            for (UUID assetTypeId : buyTradesMap.keySet()) {
                // Make sure that the lists at each key are sorted by date
                buyTradesMap.get(assetTypeId).sort(OpenTrade.tradeDateComparator);
                sellTradesMap.get(assetTypeId).sort(OpenTrade.tradeDateComparator);

                for (OpenTrade buyTrade : buyTradesMap.get(assetTypeId)) {
                    ArrayList<OpenTrade> sellTradesOfSameAssetType = sellTradesMap.get(assetTypeId);

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

                        if (buyTrade.getPricePerAsset() >= sellTrade.getPricePerAsset()) {
                            // Conditions have been satisfied for BUY trade, resolve it
                            Integer resolvedQuantity;

                            if (buyTrade.getQuantity() > sellTrade.getQuantity()) {
                                resolvedQuantity = sellTrade.getQuantity();

                                // Buying more than is available, SELL OpenTrade gets deleted
                                openTradeDataSource.deleteById(sellTrade.getTradeId());

                                // Update BUY OpenTrade's quantity
                                buyTrade.setQuantity(buyTrade.getQuantity() - sellTrade.getQuantity());
                                openTradeDataSource.updateByAttribute(buyTrade.getTradeId(), "quantity", buyTrade);

                            } else if (buyTrade.getQuantity() < sellTrade.getQuantity()) {
                                resolvedQuantity = buyTrade.getQuantity();

                                // Buying less than is available, BUY OpenTrade gets deleted
                                openTradeDataSource.deleteById(buyTrade.getTradeId());

                                // Update SELL OpenTrade's quantity
                                sellTrade.setQuantity(sellTrade.getQuantity() - buyTrade.getQuantity());
                                openTradeDataSource.updateByAttribute(sellTrade.getTradeId(), "quantity", sellTrade);

                            } else {
                                resolvedQuantity = sellTrade.getQuantity();

                                // Quantities are equal, both OpenTrades get deleted
                                openTradeDataSource.deleteById(buyTrade.getTradeId());
                                openTradeDataSource.deleteById(sellTrade.getTradeId());
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
                            resolvedTradeDataSource.createNew(resolvedTrade);

                            break;
                        }
                    }
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        System.out.println("RESOLVING TRADES COMPLETE!");
    }
}
