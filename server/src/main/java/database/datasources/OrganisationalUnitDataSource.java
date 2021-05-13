package database.datasources;

import database.DBConnection;
import models.OrganisationalUnit;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class OrganisationalUnitDataSource extends AbstractDataSource<OrganisationalUnit> {

    private final Connection dbConnection = DBConnection.getInstance();

    protected OrganisationalUnit resultSetToObject(ResultSet results) throws SQLException {
        AssetDataSource assetDataSource = new AssetDataSource();
        return new OrganisationalUnit(
                UUID.fromString(results.getString("organisationalUnitId")),
                results.getString("organisationalUnitName"),
                results.getFloat("creditBalance"),
                assetDataSource.getByOrgUnitId(UUID.fromString(results.getString("organisationalUnitId")))
        );
    }

    public OrganisationalUnit getById(UUID id) throws SQLException {
        PreparedStatement getById = dbConnection.prepareStatement(
                "SELECT * FROM \"organisationalUnits\" WHERE \"organisationalUnitId\"::text = ?;"
        );

        getById.setString(1, id.toString());

        ResultSet results = getById.executeQuery();
        results.next();

        return resultSetToObject(results);
    }

    public ArrayList<OrganisationalUnit> getAll() {
        return null;
    }

    public void createNew(OrganisationalUnit newObject) {

    }

    public void updateByAttribute(UUID id, String attribute, OrganisationalUnit value) throws SQLException, InvalidParameterException {
        Object attrValue;

        switch (attribute) {
            case "creditBalance":
                attrValue = value.getCreditBalance();
                break;
            default:
                throw new InvalidParameterException();
        }

        PreparedStatement updateStatement = dbConnection.prepareStatement(
                "UPDATE \"organisationalUnits\" SET \"" + attribute + "\" = ? WHERE \"organisationalUnitId\"::text = ?;"
        );
        updateStatement.setObject(1, attrValue);
        updateStatement.setString(2, id.toString());

        updateStatement.execute();
    }

    public boolean checkExistById(UUID id) throws SQLException {
        return false;
    }

    public void deleteById(UUID id) throws SQLException {

    }

    public String getUpdateByAttributeQuery(UUID id, String attribute, OrganisationalUnit value) throws SQLException, InvalidParameterException {
        Object attrValue;

        switch (attribute) {
            case "creditBalance":
                attrValue = value.getCreditBalance();
                break;
            default:
                throw new InvalidParameterException();
        }

        PreparedStatement updateStatement = dbConnection.prepareStatement(
                "UPDATE \"organisationalUnits\" SET \"" + attribute + "\" = ? WHERE \"organisationalUnitId\"::text = ?;"
        );
        updateStatement.setObject(1, attrValue);
        updateStatement.setString(2, id.toString());

        return updateStatement.toString();
    }

}
