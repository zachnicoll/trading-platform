package database.datasources;

import database.DBConnection;
import models.ResolvedTrade;
import models.partial.PartialReadableResolvedTrade;

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

    protected PartialReadableResolvedTrade resultSetToReadableObject(ResultSet results) throws SQLException {
        return new PartialReadableResolvedTrade(
                UUID.fromString(results.getString("buyTradeId")),
                UUID.fromString(results.getString("sellTradeId")),
                results.getString("assetName"),
                results.getInt("quantity"),
                results.getFloat("price"),
                results.getTimestamp("dateResolved"),
                results.getString("buyOrgName"),
                results.getString("sellOrgName")
        );
    }

    public ResolvedTrade getById(UUID buyId) {
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

    public ArrayList<PartialReadableResolvedTrade> getAllReadable() throws SQLException {
        PreparedStatement getAllUnresolved = dbConnection.prepareStatement(
                """
                         select\s
                        	    rt."buyTradeId",
                        	    rt."sellTradeId",
                        	    sellOrg."sellorgname",
                        	    buyOrg."buyorgname",
                        	    rt.price,
                        	    rt.quantity,
                        	    rt."dateResolved",
                        	    at2."assetName"
                        from "resolvedTrades" rt
                        join "assetTypes" at2 on
                        	    rt."assetTypeId" = at2."assetTypeId"
                             join (select\s
                         	    rt."buyTradeId",
                        	        rt."sellTradeId",
                        	        rt."buyOrgUnitId",\s
                        	        ou."organisationalUnitName" as buyOrgName
                             from "resolvedTrades" rt
                             join "organisationalUnits" ou on rt."buyOrgUnitId" = ou."organisationalUnitId") buyOrg
                        on rt."sellTradeId" = buyOrg."sellTradeId" and rt."buyTradeId" = buyOrg."buyTradeId"\s
                             join (select\s
                        	        rt."buyTradeId",
                        	        rt."sellTradeId",
                             	rt."sellOrgUnitId",\s
                        	        ou."organisationalUnitName" as sellOrgName
                             from "resolvedTrades" rt
                             join "organisationalUnits" ou on rt."sellOrgUnitId" = ou."organisationalUnitId") sellOrg
                        on rt."sellTradeId" = sellOrg."sellTradeId" and rt."buyTradeId" = sellOrg."buyTradeId"
                        order by rt."dateResolved" asc;"""
        );
        ResultSet results = getAllUnresolved.executeQuery();

        ArrayList<PartialReadableResolvedTrade> allResolvedTrades = new ArrayList<>();
        while (results.next()) {
            PartialReadableResolvedTrade trade = resultSetToReadableObject(results);
            allResolvedTrades.add(trade);
        }

        return allResolvedTrades;
    }

    public ArrayList<PartialReadableResolvedTrade> getAllByAssetReadable(UUID assetTypeId) throws SQLException {
        PreparedStatement getAllUnresolved = dbConnection.prepareStatement(
                """
                         select\s
                        	    rt."buyTradeId",
                        	    rt."sellTradeId",
                        	    sellOrg."sellorgname",
                        	    buyOrg."buyorgname",
                        	    rt.price,
                        	    rt.quantity,
                        	    rt."dateResolved",
                        	    at2."assetName"
                        from "resolvedTrades" rt
                        join "assetTypes" at2 on
                        	    rt."assetTypeId" = at2."assetTypeId"
                             join (select\s
                         	    rt."buyTradeId",
                        	        rt."sellTradeId",
                        	        rt."buyOrgUnitId",\s
                        	        ou."organisationalUnitName" as buyOrgName
                             from "resolvedTrades" rt
                             join "organisationalUnits" ou on rt."buyOrgUnitId" = ou."organisationalUnitId") buyOrg
                        on rt."sellTradeId" = buyOrg."sellTradeId" and rt."buyTradeId" = buyOrg."buyTradeId"\s
                             join (select\s
                        	        rt."buyTradeId",
                        	        rt."sellTradeId",
                             	rt."sellOrgUnitId",\s
                        	        ou."organisationalUnitName" as sellOrgName
                             from "resolvedTrades" rt
                             join "organisationalUnits" ou on rt."sellOrgUnitId" = ou."organisationalUnitId") sellOrg
                        on rt."sellTradeId" = sellOrg."sellTradeId" and rt."buyTradeId" = sellOrg."buyTradeId"
                        where at2."assetTypeId"::text = ? order by rt."dateResolved" asc;"""
        );
        getAllUnresolved.setString(1, assetTypeId.toString());
        ResultSet results = getAllUnresolved.executeQuery();

        ArrayList<PartialReadableResolvedTrade> allResolvedTrades = new ArrayList<>();
        while (results.next()) {
            PartialReadableResolvedTrade trade = resultSetToReadableObject(results);
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

    public void updateByAttribute(UUID buyId, UUID sellId, String attribute, ResolvedTrade value) throws SQLException, InvalidParameterException {

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

    public boolean checkExistById(UUID assetTypeId) throws SQLException {
        PreparedStatement checkTradeExistByAsset = dbConnection.prepareStatement(
                "SELECT EXISTS(SELECT 1 FROM \"resolvedTrades\" WHERE \"assetTypeId\" = uuid(?));"
        );
        checkTradeExistByAsset.setString(1, assetTypeId.toString());

        //checks if first element is either 't' or 'f' indicating if the row exists in the database
        ResultSet check = checkTradeExistByAsset.executeQuery();
        check.next(); // moves cursor to the next row
        String confirm = check.getString("exists");

        return confirm.equals("t") ? true : false;
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
