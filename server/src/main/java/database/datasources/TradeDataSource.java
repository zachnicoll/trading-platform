package database.datasources;

import database.DBConnection;
import models.OpenTrade;
import models.TradeType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class TradeDataSource extends AbstractDataSource<OpenTrade> {

    protected OpenTrade resultSetToObject(ResultSet results) throws SQLException {
        return new OpenTrade(
                UUID.fromString(results.getString("tradeId")),
                TradeType.valueOf(results.getString("tradeType")),
                UUID.fromString(results.getString("organisationalUnitId")),
                UUID.fromString(results.getString("assetTypeId")),
                results.getInt("quantity"),
                results.getFloat("price"),
                results.getDate("dateOpened")
        );
    }

    public OpenTrade getById(String id) {
        return null;
    }

    public ArrayList<OpenTrade> getAll() throws SQLException {
        Connection dbConnection = DBConnection.getInstance();
        PreparedStatement getAllUnresolved = dbConnection.prepareStatement(
                "SELECT * FROM \"openTrades\" ORDER BY \"dateOpened\" ASC;"
        );
        ResultSet results = getAllUnresolved.executeQuery();

        ArrayList<OpenTrade> unresolvedTrades = new ArrayList<>();
        while (results.next()) {
            OpenTrade trade = resultSetToObject(results);
            unresolvedTrades.add(trade);
        }

        return unresolvedTrades;
    }

    public boolean createNew(OpenTrade newObject) {
        return false;
    }

    public boolean updateByAttribute(String id, String attribute, OpenTrade value) {
        return false;
    }

    public boolean checkExistById(String id) {
        return false;
    }

    public void deleteById(String id) {

    }
}
