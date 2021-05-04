package database.datasources;

import database.DBConnection;
import models.Trade;
import models.TradeStatus;
import models.TradeType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradeDataSource implements TradingPlatformDataSource<Trade> {

    @Override
    public Trade getById(String id) {
        return null;
    }

    public ArrayList<Trade> getAllUnresolved() throws SQLException {
        Connection dbConnection = DBConnection.getInstance();
        PreparedStatement getAllUnresolved = dbConnection.prepareStatement("SELECT * FROM trades t WHERE t.status = 'UNRESOLVED';");
        ResultSet results = getAllUnresolved.executeQuery();

        ArrayList<Trade> unresolvedTrades = new ArrayList<>();
        while (results.next()) {
            Trade trade = new Trade(
                    UUID.fromString(results.getString("tradeId")),
                    TradeType.valueOf(results.getString("tradeType")),
                    UUID.fromString(results.getString("organisationalUnitId")),
                    UUID.fromString(results.getString("assetTypeId")),
                    results.getInt("quantity"),
                    results.getFloat("price"),
                    results.getDate("date"),
                    TradeStatus.valueOf(results.getString("status"))
            );
            unresolvedTrades.add(trade);
        }

        return unresolvedTrades;
    }

    @Override
    public List<Trade> getAll() {
        return null;
    }

    @Override
    public boolean createNew(Trade newObject) {
        return false;
    }

    @Override
    public boolean updateByAttribute(String id, String attribute, Trade value) {
        return false;
    }

    @Override
    public boolean checkExistById(String id) {
        return false;
    }

    @Override
    public void deleteById(String id) {

    }
}
