package database.datasources;

import database.DBConnection;
import models.ResolvedTrade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class ResolvedTradeDataSource extends AbstractDataSource<ResolvedTrade> {

    protected ResolvedTrade resultSetToObject(ResultSet results) throws SQLException {
        return new ResolvedTrade(
                UUID.fromString(results.getString("buyTradeId")),
                UUID.fromString(results.getString("buyOrgUnitId")),
                UUID.fromString(results.getString("sellTradeId")),
                UUID.fromString(results.getString("sellOrgUnitId")),
                results.getInt("quantity"),
                results.getFloat("price"),
                results.getDate("dateResolved")
        );
    }

    public ResolvedTrade getById(String id) {
        return null;
    }

    public ArrayList<ResolvedTrade> getAll() throws SQLException {
        Connection dbConnection = DBConnection.getInstance();
        PreparedStatement getAllUnresolved = dbConnection.prepareStatement(
                "SELECT * FROM \"resolvedTrades\" ORDER BY \"dateOpened\" ASC;"
        );
        ResultSet results = getAllUnresolved.executeQuery();

        ArrayList<ResolvedTrade> allResolvedTrades = new ArrayList<>();
        while (results.next()) {
            ResolvedTrade trade = resultSetToObject(results);
            allResolvedTrades.add(trade);
        }

        return allResolvedTrades;
    }

    public boolean createNew(ResolvedTrade newObject) {
        return false;
    }

    public boolean updateByAttribute(String id, String attribute, ResolvedTrade value) {
        return false;
    }

    public boolean checkExistById(String id) {
        return false;
    }

    public void deleteById(String id) {

    }
}
