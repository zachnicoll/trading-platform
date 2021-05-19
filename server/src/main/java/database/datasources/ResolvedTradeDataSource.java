package database.datasources;

import database.DBConnection;
import models.Asset;
import models.ResolvedTrade;

import java.security.InvalidParameterException;
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
    public ResolvedTrade getById(UUID buyId)
    {
        //TODO: DELETE ONCE FIXED
        return null;
    }


    public ResolvedTrade getById(UUID buyId, UUID sellId) throws SQLException {

        PreparedStatement getById = dbConnection.prepareStatement(
                "SELECT * FROM \"resolvedTrades\" WHERE \"buyTradeId\" = uuid(?) AND \"sellTradeId\" = uuid(?);"
        );
        getById.setString(1, buyId.toString());
        getById.setString(2, sellId.toString());

        ResultSet results = getById.executeQuery();

        if (!results.next()) {
            throw new SQLException("ResolvedTrade does not exist");
        }

        return resultSetToObject(results);

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

    public void updateByAttribute(UUID buyId, String attribute, ResolvedTrade value) throws SQLException {
        //TODO: DELETE ONCE FIXED
    }

    public void updateByAttribute(UUID buyId, UUID sellId, String attribute, ResolvedTrade value) throws SQLException, InvalidParameterException  {

        Object attrValue;

        switch (attribute) {
            case "quantity":
                attrValue = value.getQuantity();
                break;
            case "price":
                attrValue = value.getPrice();
                break;
            default:
                throw new InvalidParameterException();
        }

        PreparedStatement updateByAttribute = dbConnection.prepareStatement(
                    "UPDATE \"resolvedTrades\" SET \"?\" = ? WHERE \"buyTradeId\" = uuid(?) AND \"sellTradeId\" = uuid(?);"
            );


        updateByAttribute.setString(1, attribute);
        updateByAttribute.setObject(2, attrValue);
        updateByAttribute.setString(3, buyId.toString());
        updateByAttribute.setString(4, sellId.toString());

        updateByAttribute.execute();

    }

    public boolean checkExistById(UUID id) throws SQLException {
        //TODO: DELETE ONCE FIXED
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

    public void deleteById(UUID Id) throws SQLException {
        //TODO: DELETE ONCE FIXED
    }

    public void deleteById(UUID buyId, UUID sellId) throws SQLException {
        PreparedStatement createDeleteTrade = dbConnection.prepareStatement(
                "DELETE FROM \"resolvedTrades\" WHERE \"buyTradeId\" = uuid(?) AND \"sellTradeId\" = uuid(?);"
        );
        createDeleteTrade.setString(1, buyId.toString());
        createDeleteTrade.setString(2, sellId.toString());

        createDeleteTrade.execute();
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
