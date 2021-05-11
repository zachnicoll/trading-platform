package database.datasources;

import database.DBConnection;
import models.Asset;
import models.OpenTrade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class AssetDataSource extends AbstractDataSource<Asset> {

    private final Connection dbConnection = DBConnection.getInstance();

    protected Asset resultSetToObject(ResultSet results) throws SQLException {
        return new Asset(
                UUID.fromString(results.getString("assetTypeId")),
                results.getInt("quantity")
        );
    }

    public Asset getById(UUID id) { return null; }

    public ArrayList<Asset> getByOrgUnitId(UUID organisationalUnitId) throws SQLException {
        PreparedStatement getAllByOrgUnitId = dbConnection.prepareStatement(
                "SELECT * FROM \"organisationalUnitAssets\" WHERE \"organisationalUnitId\"::text = ?;"
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

    public ArrayList<Asset> getAll() {
        return null;
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
}
