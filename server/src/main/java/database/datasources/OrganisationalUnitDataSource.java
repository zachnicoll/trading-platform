package database.datasources;

import models.OrganisationalUnit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public boolean createNew(OrganisationalUnit newObject) {
        return false;
    }

    public boolean updateByAttribute(String id, String attribute, OrganisationalUnit value) {
        return false;
    }

    public boolean checkExistById(String id) {
        return false;
    }

    public void deleteById(String id) {

    }
}
