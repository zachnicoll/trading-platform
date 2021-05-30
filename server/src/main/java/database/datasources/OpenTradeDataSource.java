package database.datasources;

import database.DBConnection;
import models.OpenTrade;
import models.TradeType;
import models.partial.PartialReadableOpenTrade;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class OpenTradeDataSource extends AbstractDataSource<OpenTrade> {

    private final Connection dbConnection = DBConnection.getInstance();

    protected OpenTrade resultSetToObject(ResultSet results) throws SQLException {
        return new OpenTrade(
                UUID.fromString(results.getString("tradeId")),
                TradeType.valueOf(results.getString("tradeType")),
                UUID.fromString(results.getString("organisationalUnitId")),
                UUID.fromString(results.getString("assetTypeId")),
                results.getInt("quantity"),
                results.getFloat("price"),
                results.getTimestamp("dateOpened")
        );
    }

    protected PartialReadableOpenTrade resultSetToReadableObject(ResultSet results) throws SQLException {
        return new PartialReadableOpenTrade(
                UUID.fromString(results.getString("tradeId")),
                TradeType.valueOf(results.getString("tradeType")),
                String.valueOf(results.getString("organisationalUnitName")),
                String.valueOf(results.getString("assetName")),
                results.getInt("quantity"),
                results.getFloat("price"),
                results.getTimestamp("dateOpened"),
                UUID.fromString(results.getString("organisationalUnitId"))
        );
    }

    public OpenTrade getById(UUID id) throws SQLException {
        PreparedStatement getById = dbConnection.prepareStatement(
                "SELECT * FROM \"openTrades\" WHERE \"tradeId\"::text = ?;"
        );

        getById.setString(1, id.toString());

        ResultSet results = getById.executeQuery();

        if (!results.next()) {
            throw new SQLException("OpenTrade does not exist");
        }

        return resultSetToObject(results);
    }

    public ArrayList<OpenTrade> getAll() throws SQLException {
        PreparedStatement getAllUnresolved = dbConnection.prepareStatement(
                "SELECT * FROM \"openTrades\" ORDER BY \"dateOpened\" ASC;"
        );
        ResultSet results = getAllUnresolved.executeQuery();

        ArrayList<OpenTrade> allOpenTrades = new ArrayList<>();
        while (results.next()) {
            OpenTrade trade = resultSetToObject(results);
            allOpenTrades.add(trade);
        }

        return allOpenTrades;
    }

    public ArrayList<PartialReadableOpenTrade> getAllReadable() throws SQLException {
        PreparedStatement getAllUnresolved = dbConnection.prepareStatement(
                """
                        SELECT
                                      ot."tradeId",
                                      ot."tradeType",
                                      ou."organisationalUnitName",
                                      ou."organisationalUnitId",
                                      at2."assetName",
                                      ot.quantity,
                                      ot.price,
                                      ot.quantity,
                                      ot."dateOpened"\s
                                      FROM
                                      "openTrades" as ot\s
                                      JOIN "organisationalUnits" ou ON ou."organisationalUnitId" = ot."organisationalUnitId"
                                      JOIN "assetTypes" at2 ON at2."assetTypeId" = ot."assetTypeId";""");

        ResultSet results = getAllUnresolved.executeQuery();

        ArrayList<PartialReadableOpenTrade> allOpenTrades = new ArrayList<>();
        while (results.next()) {
            PartialReadableOpenTrade trade = resultSetToReadableObject(results);
            allOpenTrades.add(trade);
        }

        return allOpenTrades;
    }



    public void createNew(OpenTrade newObject) throws SQLException {
        PreparedStatement createNew = dbConnection.prepareStatement(
                "INSERT INTO \"openTrades\" VALUES (uuid(?),uuid(?),uuid(?),?::\"tradeType\",?,?,?);"
        );

        createNew.setString(1, newObject.getTradeId().toString());
        createNew.setString(2, newObject.getAssetType().toString());
        createNew.setString(3, newObject.getOrganisationalUnit().toString());
        createNew.setString(4, newObject.getTradeType().toString());
        createNew.setInt(5, newObject.getQuantity());
        createNew.setFloat(6, newObject.getPricePerAsset());
        createNew.setTimestamp(7, newObject.getDate());

        createNew.execute();
    }

    public void updateByAttribute(UUID id, String attribute, OpenTrade value) throws SQLException, InvalidParameterException {
        Object attrValue;

        switch (attribute) {
            case "quantity":
                attrValue = value.getQuantity();
                break;
            default:
                throw new InvalidParameterException();
        }

        PreparedStatement updateStatement = dbConnection.prepareStatement(
                "UPDATE \"openTrades\" SET \"" + attribute + "\" = ? WHERE \"tradeId\"::text = ?;"
        );
        updateStatement.setObject(1, attrValue);
        updateStatement.setString(2, id.toString());

        updateStatement.execute();
    }

    public boolean checkExistById(UUID id) throws SQLException {
        PreparedStatement checkIfExist = dbConnection.prepareStatement(
                "SELECT EXISTS(SELECT 1 FROM \"openTrades\" WHERE \"tradeId\"::text = ?);"
        );

        checkIfExist.setString(1, id.toString());

        //checks if query returns a result set with at least one element, indicating a row exists with the given id
        return checkIfExist.executeQuery().next();
    }

    public void deleteById(UUID id) throws SQLException {
        PreparedStatement deleteStatement = dbConnection.prepareStatement(
                "DELETE FROM \"openTrades\" WHERE \"tradeId\"::text = ?;"
        );
        deleteStatement.setString(1, id.toString());
        deleteStatement.execute();
    }

    public String getDeleteByIdQuery(UUID id) throws SQLException{
        PreparedStatement deleteStatement = dbConnection.prepareStatement(
                "DELETE FROM \"openTrades\" WHERE \"tradeId\"::text = ?;"
        );
        deleteStatement.setString(1, id.toString());
        return deleteStatement.toString();
    }

    public String getUpdateByAttributeQuery(UUID id, String attribute, OpenTrade value) throws SQLException, InvalidParameterException {
        Object attrValue;

        switch (attribute) {
            case "quantity":
                attrValue = value.getQuantity();
                break;
            default:
                throw new InvalidParameterException();
        }

        PreparedStatement updateStatement = dbConnection.prepareStatement(
                "UPDATE \"openTrades\" SET \"" + attribute + "\" = ? WHERE \"tradeId\"::text = ?;"
        );
        updateStatement.setObject(1, attrValue);
        updateStatement.setString(2, id.toString());

        return updateStatement.toString();
    }
}
