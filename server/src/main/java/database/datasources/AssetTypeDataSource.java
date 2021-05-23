package database.datasources;

import database.DBConnection;
import models.Asset;
import models.AssetType;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class AssetTypeDataSource extends AbstractDataSource<AssetType> {

    private final Connection dbConnection = DBConnection.getInstance();

    protected AssetType resultSetToObject(ResultSet results) throws SQLException {
        return new AssetType(
                UUID.fromString(results.getString("assetTypeId")),
                results.getString("assetName")
        );
    }

    public AssetType getById(UUID id) throws SQLException {
        PreparedStatement getById = dbConnection.prepareStatement(
                "SELECT * FROM \"assetTypes\" WHERE \"assetTypeId\"::text = ?;"
        );

        getById.setString(1, id.toString());

        ResultSet results = getById.executeQuery();

        if (!results.next()) {
            throw new SQLException("AssetType does not exist");
        }

        return resultSetToObject(results);
    }

    public ArrayList<AssetType> getAll() throws SQLException {
        PreparedStatement getAll = dbConnection.prepareStatement(
                "SELECT * FROM \"assetTypes\";"
        );

        ArrayList<AssetType> assetTypes = new ArrayList<>();

        ResultSet results = getAll.executeQuery();
        while (results.next()) {
            AssetType assetType = resultSetToObject(results);
            assetTypes.add(assetType);
        }

        return assetTypes;
    }

    public void createNew(AssetType newObject) throws SQLException {
        PreparedStatement createNew = dbConnection.prepareStatement(
                "INSERT INTO \"assetTypes\" VALUES (uuid(?), ?);"
        );

        createNew.setString(1, newObject.getAssetTypeId().toString());
        createNew.setString(2, newObject.getAssetName());

        createNew.execute();
    }

    public void updateByAttribute(UUID id, String attribute, AssetType value) throws SQLException {
        Object attrValue;

        switch (attribute) {
            case "assetName":
                attrValue = value.getAssetName();
                break;
            default:
                throw new InvalidParameterException();
        }

        PreparedStatement updateStatement = dbConnection.prepareStatement(
                "UPDATE \"assetTypes\" SET \"" + attribute + "\" = ? WHERE \"assetTypeId\"::text = ?;"
        );
        updateStatement.setObject(1, attrValue);
        updateStatement.setString(2, id.toString());

        updateStatement.execute();
    }

    public boolean checkExistById(UUID id) throws SQLException {
        PreparedStatement checkExistsById = dbConnection.prepareStatement(
                "SELECT EXISTS(SELECT 1 FROM \"assetTypes\" WHERE \"assetTypeId\"::text = ?);"
        );

        checkExistsById.setString(1, id.toString());

        return checkExistsById.executeQuery().next();
    }

    public void deleteById(UUID id) throws SQLException {
        PreparedStatement deleteById = dbConnection.prepareStatement(
                "DELETE FROM \"assetTypes\" WHERE \"assetTypeId\"::text = ?;"
        );

        deleteById.setString(1, id.toString());

        deleteById.execute();
    }
}
