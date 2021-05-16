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
    private final Connection dbConnection = DBConnection.getInstance();

    protected ResolvedTrade resultSetToObject(ResultSet results) throws SQLException {
        return new ResolvedTrade(
                UUID.fromString(results.getString("buyTradeId")),
                UUID.fromString(results.getString("sellTradeId")),
                UUID.fromString(results.getString("buyOrgUnitId")),
                UUID.fromString(results.getString("sellOrgUnitId")),
                UUID.fromString(results.getString("assetTypeId")),
                results.getInt("quantity"),
                results.getFloat("price"),
                results.getTimestamp("dateResolved")
        );
    }

    public ResolvedTrade getById(UUID id) {
        return null;
    }

    public ArrayList<ResolvedTrade> getAll() throws SQLException {
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

    public void createNew(ResolvedTrade newObject) throws SQLException {
        PreparedStatement createResolvedTrade = dbConnection.prepareStatement(
                "INSERT INTO \"resolvedTrades\" VALUES (uuid(?), uuid(?), uuid(?), uuid(?), uuid(?), ?, ?, ?);"
        );
        createResolvedTrade.setString(1, newObject.getBuyTradeId().toString());
        createResolvedTrade.setString(2, newObject.getSellTradeId().toString());
        createResolvedTrade.setString(3, newObject.getBuyOrgUnitId().toString());
        createResolvedTrade.setString(4, newObject.getSellOrgUnitId().toString());
        createResolvedTrade.setString(5, newObject.getAssetTypeId().toString());
        createResolvedTrade.setInt(6, newObject.getQuantity());
        createResolvedTrade.setFloat(7, newObject.getPrice());
        createResolvedTrade.setTimestamp(8, newObject.getDateResolved());

        createResolvedTrade.execute();
    }

    public void updateByAttribute(UUID id, String attribute, ResolvedTrade value) {

    }

    public boolean checkExistById(UUID id) throws SQLException {
        return false;
    }
    public boolean checkExistById(UUID buyId, UUID sellId) throws SQLException {
        PreparedStatement createQueryTrade = dbConnection.prepareStatement(
                "SELECT * FROM \"resolvedTrades\" WHERE \"buyTradeId\" = uuid(?) AND \"sellTradeId\" = uuid(?);"
        );
        createQueryTrade.setString(1, buyId.toString());
        createQueryTrade.setString(2, sellId.toString());

        return createQueryTrade.executeQuery().next();
    }

    public void deleteById(UUID id) {

    }

    public String getCreateNewQuery(ResolvedTrade newObject) throws SQLException {
        PreparedStatement createResolvedTrade = dbConnection.prepareStatement(
                "INSERT INTO \"resolvedTrades\" VALUES (uuid(?), uuid(?), uuid(?), uuid(?), uuid(?), ?, ?, ?);"
        );
        createResolvedTrade.setString(1, newObject.getBuyTradeId().toString());
        createResolvedTrade.setString(2, newObject.getSellTradeId().toString());
        createResolvedTrade.setString(3, newObject.getBuyOrgUnitId().toString());
        createResolvedTrade.setString(4, newObject.getSellOrgUnitId().toString());
        createResolvedTrade.setString(5, newObject.getAssetTypeId().toString());
        createResolvedTrade.setInt(6, newObject.getQuantity());
        createResolvedTrade.setFloat(7, newObject.getPrice());
        createResolvedTrade.setTimestamp(8, newObject.getDateResolved());

        return createResolvedTrade.toString();
    }

}
