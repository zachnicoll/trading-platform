package database.datasources;

import database.DBConnection;
import models.OpenTrade;
import models.TradeType;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class OpenTradeDataSource extends AbstractDataSource<OpenTrade> {

    protected Connection dbConnection = DBConnection.getInstance();

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

    public OpenTrade getById(String id) {
        return null;
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

    public void createNew(OpenTrade newObject) throws SQLException {
        PreparedStatement createNew = dbConnection.prepareStatement(
                "INSERT INTO \"openTrades\" VALUES (uuid(?),uuid(?),uuid(?),uuid(?),?,?,?);"
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

    public boolean checkExistById(UUID id) {
        return false;
    }

    public void deleteById(UUID id) throws SQLException {
        PreparedStatement deleteStatement = dbConnection.prepareStatement(
                "DELETE FROM \"openTrades\" WHERE \"tradeId\"::text = ?;"
        );
        deleteStatement.setString(1, id.toString());
        deleteStatement.execute();
    }
}
