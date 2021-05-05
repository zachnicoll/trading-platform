package server;

import database.datasources.OpenTradeDataSource;
import models.OpenTrade;
import models.TradeType;

import java.sql.SQLException;
import java.util.*;

public class TradeResolver extends TimerTask {

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
        /*
         *  Fetch all trades from DB that are unresolved
         */
        OpenTradeDataSource tradeDataSource = new OpenTradeDataSource();
        ArrayList<OpenTrade> unresolvedTrades = null;

        try {
            unresolvedTrades = tradeDataSource.getAll();

            HashMap<UUID, ArrayList<OpenTrade>> buyTradesMap = new HashMap<>();
            HashMap<UUID, ArrayList<OpenTrade>> sellTradesMap = new HashMap<>();

            // Separate unresolved trades into separate hashmaps, stored at the UUID
            // of the AssetType.
            for (OpenTrade trade: unresolvedTrades) {
                UUID assetTypeId = trade.getAssetType();

                if (trade.getTradeType() == TradeType.BUY) {
                    insertTradeInMap(buyTradesMap, trade, assetTypeId);
                } else {
                    insertTradeInMap(sellTradesMap, trade, assetTypeId);
                }
            }

            for (UUID assetTypeId: buyTradesMap.keySet()) {
                buyTradesMap.get(assetTypeId).sort(OpenTrade.tradeDateComparator);
                sellTradesMap.get(assetTypeId).sort(OpenTrade.tradeDateComparator);

                for(OpenTrade selectedTrade: buyTradesMap.get(assetTypeId)) {
                    System.out.println(selectedTrade);
                    ArrayList<OpenTrade> sellTradesOfSameAssetType = sellTradesMap.get(assetTypeId);
                    for(int i = 0; i < sellTradesOfSameAssetType.size(); i++) {
                        // Find sell trade that:
                    }
                }
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        /*
         *  Trade, find all other-type (BUY/SELL) trades of the same AssetType with maximum unit price
         *  <= the BUY / >= the SELL trade's set unit price.
         */

        /*
         *  Resolve the selected trade by choosing another trade with the unit price that is closest to the selected.
         *  If there are multiple, the earliest trade takes priority.
         */

        System.out.println(UUID.randomUUID() + "\n");
    }
}
