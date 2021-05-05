package server;

import database.datasources.TradeDataSource;
import models.OpenTrade;
import models.TradeType;

import java.sql.SQLException;
import java.util.*;

public class TradeResolver extends TimerTask {
    public void run() {
        /*
         *  Fetch all trades from DB that are unresolved
         */
        TradeDataSource tradeDataSource = new TradeDataSource();
        ArrayList<OpenTrade> unresolvedTrades = null;

        try {
            unresolvedTrades = tradeDataSource.getAll();

            HashMap<UUID, ArrayList<OpenTrade>> buyTrades = new HashMap<>();
            HashMap<UUID, ArrayList<OpenTrade>> sellTrades = new HashMap<>();

            // Separate unresolved trades into separate hashmaps, stored at the UUID
            // of the AssetType.
            for (OpenTrade trade: unresolvedTrades) {
                UUID assetTypeId = trade.getAssetType();

                if (trade.getTradeType() == TradeType.BUY) {
                    if (buyTrades.get(assetTypeId) == null) {
                        ArrayList<OpenTrade> newEntry = new ArrayList<>();
                        newEntry.add(trade);
                        buyTrades.put(trade.getAssetType(), newEntry);
                    } else {
                        buyTrades.get(assetTypeId).add(trade);
                    }
                } else {
                    if (sellTrades.get(assetTypeId) == null) {
                        ArrayList<OpenTrade> newEntry = new ArrayList<>();
                        newEntry.add(trade);
                        sellTrades.put(trade.getAssetType(), newEntry);
                    } else {
                        sellTrades.get(assetTypeId).add(trade);
                    }
                }
            }

            for (UUID assetTypeId: buyTrades.keySet()) {
                buyTrades.get(assetTypeId).sort(OpenTrade.tradeDateComparator);
                sellTrades.get(assetTypeId).sort(OpenTrade.tradeDateComparator);

                for(OpenTrade selectedTrade: buyTrades.get(assetTypeId)) {
                    System.out.println(selectedTrade);
                    ArrayList<OpenTrade> sellTradesOfSameAssetType = sellTrades.get(assetTypeId);
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
