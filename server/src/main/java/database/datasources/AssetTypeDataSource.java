package database.datasources;

import database.DBConnection;
import models.AssetType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class AssetTypeDataSource extends AbstractDataSource<AssetType> {

    private Connection dbConnection = DBConnection.getInstance();

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

    public ArrayList<AssetType> getAll() {
        return null;
    }

    public void createNew(AssetType newObject) {

    }

    public void updateByAttribute(UUID id, String attribute, AssetType value) throws SQLException {

    }

    public boolean checkExistById(UUID id) throws SQLException {
        return false;
    }

    public void deleteById(UUID id) throws SQLException {

    }
}
