package database.datasources;

import database.DBConnection;
import models.OrganisationalUnit;
import models.ResolvedTrade;
import models.partial.PartialOrganisationalUnit;

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

        if (!results.next()) {
            throw new SQLException("OrganisationalUnit does not exist");
        }

        return resultSetToObject(results);
    }

    public ArrayList<OrganisationalUnit> getAll() throws SQLException {
        PreparedStatement getAllOrgUnits = dbConnection.prepareStatement(
                "SELECT * FROM \"organisationalUnits\" ORDER BY \"organisationalUnitName\" ASC;"
        );
        ResultSet results = getAllOrgUnits.executeQuery();

        ArrayList<OrganisationalUnit> allOrganisationalUnits = new ArrayList<>();
        while (results.next()) {
            OrganisationalUnit unit = resultSetToObject(results);
            allOrganisationalUnits.add(unit);
        }
        return allOrganisationalUnits;
    }

    public void createNew(OrganisationalUnit newObject) throws SQLException {
        PreparedStatement createNew = dbConnection.prepareStatement(
                "INSERT INTO \"organisationalUnits\" VALUES (uuid(?),?,?);"
        );

        createNew.setString(1, newObject.getUnitId().toString());
        createNew.setString(2, newObject.getUnitName());
        createNew.setFloat(3, newObject.getCreditBalance());

        createNew.execute();
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
        PreparedStatement checkIfExist = dbConnection.prepareStatement(
                "SELECT EXISTS(SELECT 1 FROM \"organisationalUnits\" WHERE \"organisationalUnitId\"::text = ?);"
        );

        checkIfExist.setString(1, id.toString());

        //checks if first element is either 't' or 'f' indicating if the row exists in the database
        ResultSet check = checkIfExist.executeQuery();
        check.next(); // moves cursor to the next row
        String confirm = check.getString("exists");

        return confirm.equals("t") ? true : false;
    }

    public void deleteById(UUID id) throws SQLException {
        PreparedStatement deleteStatement = dbConnection.prepareStatement(
                "DELETE FROM \"organisationalUnits\" WHERE \"organisationalUnitId\"::text = ?;"
        );
        deleteStatement.setString(1, id.toString());
        deleteStatement.execute();
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
