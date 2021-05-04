package server;

import database.datasources.TradeDataSource;
import models.Trade;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

public class TradeResolver extends TimerTask {
    public void run() {
        /*
         *  Fetch all trades from DB that are unresolved
         */
        TradeDataSource tradeDataSource = new TradeDataSource();
        ArrayList<Trade> unresolvedTrades = null;

        try {
            unresolvedTrades = tradeDataSource.getAllUnresolved();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        if (unresolvedTrades != null) {
            for (Trade trade:
                 unresolvedTrades) {
                System.out.println(trade.getTradeId());
            }
        }

        /*
         *  Sort trades by DateTime, earliest first
         */

        /*
         *  Trade, find all other-type (BUY/SELL) trades of the same AssetType with maximum unit price
         *  <= the BUY / >= the SELL trade's set unit price.
         */

        /*
         *  Resolve the selected trade by choosing another trade with the unit price that is closest to the selected.
         *  If there are multiple, the earliest trade takes priority.
         */

        System.out.println("Time is:" + new Date());
    }
}
