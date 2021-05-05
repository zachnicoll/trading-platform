package database.datasources;

import models.OrganisationalUnit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class OrganisationalUnitDataSource extends AbstractDataSource<OrganisationalUnit> {

    protected OrganisationalUnit resultSetToObject(ResultSet results) throws SQLException {
        //return new OrganisationalUnit();
        return null;
    }

    public OrganisationalUnit getById(String id) {
        return null;
    }

    public ArrayList<OrganisationalUnit> getAll() {
        return null;
    }

    public void createNew(OrganisationalUnit newObject) {

    }

    public void updateByAttribute(UUID id, String attribute, OrganisationalUnit value) throws SQLException {

    }

    public boolean checkExistById(UUID id) throws SQLException {
        return false;
    }

    public void deleteById(UUID id) throws SQLException {

    }
}
