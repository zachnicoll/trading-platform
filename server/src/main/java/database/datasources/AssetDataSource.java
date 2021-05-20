package database.datasources;

import database.DBConnection;
import models.Asset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class AssetDataSource extends AbstractDataSource<Asset> {

    private final Connection dbConnection = DBConnection.getInstance();

    protected Asset resultSetToObject(ResultSet results) throws SQLException {
        boolean assetNameExists;
        try {
            results.findColumn("assetName");
            assetNameExists = true;
        } catch (Exception e) {
            assetNameExists = false;
        }

        return new Asset(
                UUID.fromString(results.getString("assetTypeId")),
                results.getInt("quantity"),
                assetNameExists ? results.getString("assetName") : null
        );
    }

    public Asset getById(UUID id) {
        return null;
    }

    public Asset getById(UUID assetTypeId, UUID orgUnitId) throws SQLException {
        PreparedStatement getById = dbConnection.prepareStatement(
                "SELECT * FROM \"organisationalUnitAssets\" WHERE" +
                        "\"organisationalUnitId\"::text = ? AND" +
                        "\"assetTypeId\"::text = ?;"
        );

        getById.setString(1, orgUnitId.toString());
        getById.setString(2, assetTypeId.toString());

        ResultSet results = getById.executeQuery();

        if (!results.next()) {
            throw new SQLException("Organisational Unit does not own any of this AssetType");
        }

        return resultSetToObject(results);
    }

    public ArrayList<Asset> getByOrgUnitId(UUID organisationalUnitId) throws SQLException {
        // Need to join assetTypes table to get assetName for displaying on frontend
        PreparedStatement getAllByOrgUnitId = dbConnection.prepareStatement(
                """
                             SELECT\s
                             oua."assetTypeId",\s
                             oua."quantity",
                             ast."assetName" FROM\s
                             "organisationalUnitAssets" oua JOIN\s
                             "assetTypes" ast ON (oua."assetTypeId" = ast."assetTypeId")
                             WHERE "organisationalUnitId"::text = ?;
                        """
        );

        getAllByOrgUnitId.setString(1, organisationalUnitId.toString());

        ArrayList<Asset> assets = new ArrayList<>();

        ResultSet results = getAllByOrgUnitId.executeQuery();
        while (results.next()) {
            Asset asset = resultSetToObject(results);
            assets.add(asset);
        }

        return assets;
    }

    public ArrayList<Asset> getAll() throws SQLException {
        // Join asset names
        PreparedStatement getAll = dbConnection.prepareStatement(
                """
                             SELECT\s
                             oua."assetTypeId",\s
                             oua."quantity",
                             ast."assetName" FROM\s
                             "organisationalUnitAssets" oua JOIN\s
                             "assetTypes" ast ON (oua."assetTypeId" = ast."assetTypeId");
                        """
        );

        ArrayList<Asset> assets = new ArrayList<>();

        ResultSet results = getAll.executeQuery();
        while (results.next()) {
            Asset asset = resultSetToObject(results);
            assets.add(asset);
        }

        return assets;
    }

    public void createNew(Asset newObject) {
    }

    public void createNew(Asset newObject, UUID organisationalUnitId) throws SQLException {
        PreparedStatement createStatement = dbConnection.prepareStatement(
                "INSERT INTO \"organisationUnitAssets\" VALUES (uuid(?), uuid(?), ?);"
        );

        createStatement.setString(1, organisationalUnitId.toString());
        createStatement.setString(2, newObject.getAssetTypeId().toString());
        createStatement.setInt(3, newObject.getQuantity());

        createStatement.execute();
    }

    public void updateByAttribute(UUID id, String attribute, Asset value) throws SQLException {
    }

    public void updateAssetQuantity(UUID organisationalUnitId, UUID assetTypeId, Integer quantity) throws SQLException {
        PreparedStatement updateStatement = dbConnection.prepareStatement(
                "UPDATE \"organisationalUnitAssets\" SET quantity = ? WHERE" +
                        "\"organisationalUnitId\"::text = ? AND" +
                        "\"assetTypeId\"::text = ?;"
        );
        updateStatement.setInt(1, quantity);
        updateStatement.setString(2, organisationalUnitId.toString());
        updateStatement.setString(3, assetTypeId.toString());

        updateStatement.execute();
    }

    public boolean checkExistById(UUID id) throws SQLException {
        return false;
    }

    public void deleteById(UUID id) throws SQLException {
    }

    public boolean checkExistById(UUID assetTypeId, UUID orgUnitId) throws SQLException {
        PreparedStatement checkExistsById = dbConnection.prepareStatement(
                "SELECT EXISTS(SELECT 1 FROM \"organisationalUnitAssets\" WHERE " +
                        "\"organisationalUnitId\"::text = ? AND" +
                        "\"assetTypeId\"::text = ?);"
        );

        checkExistsById.setString(1, orgUnitId.toString());
        checkExistsById.setString(2, assetTypeId.toString());

        return checkExistsById.executeQuery().next();
    }

    public void deleteById(UUID assetTypeId, UUID orgUnitId) throws SQLException {
        PreparedStatement deleteById = dbConnection.prepareStatement(
                "DELETE FROM \"organisationalUnitAssets\" WHERE " +
                        "\"organisationalUnitId\"::text = ? AND" +
                        "\"assetTypeId\"::text = ?;"
        );

        deleteById.setString(1, orgUnitId.toString());
        deleteById.setString(2, assetTypeId.toString());

        deleteById.execute();
    }

    public String getUpdateAssetQuantityQuery(UUID organisationalUnitId, UUID assetTypeId, Integer quantity) throws SQLException {
        PreparedStatement updateStatement = dbConnection.prepareStatement(
                "UPDATE \"organisationalUnitAssets\" SET quantity = ? WHERE" +
                        "\"organisationalUnitId\"::text = ? AND" +
                        "\"assetTypeId\"::text = ?;"
        );
        updateStatement.setInt(1, quantity);
        updateStatement.setString(2, organisationalUnitId.toString());
        updateStatement.setString(3, assetTypeId.toString());

        return updateStatement.toString();
    }

    public String getCreateNewQuery(Asset newObject, UUID organisationalUnitId) throws SQLException {
        PreparedStatement createStatement = dbConnection.prepareStatement(
                "INSERT INTO \"organisationUnitAssets\" VALUES (uuid(?), uuid(?), ?);"
        );

        createStatement.setString(1, organisationalUnitId.toString());
        createStatement.setString(2, newObject.getAssetTypeId().toString());
        createStatement.setInt(3, newObject.getQuantity());

        return createStatement.toString();
    }

}
