package database.datasources;

import models.OrganisationalUnit;

import java.util.List;


public class OrganisationalUnitDataSource implements TradingPlatformDataSource<OrganisationalUnit> {


    @Override
    public OrganisationalUnit getById(String id) {
        return null;
    }

    @Override
    public List<OrganisationalUnit> getAll() {
        return null;
    }

    @Override
    public boolean createNew(OrganisationalUnit newObject) {
        return false;
    }

    @Override
    public boolean updateByAttribute(String id, String attribute, OrganisationalUnit value) {
        return false;
    }

    @Override
    public boolean checkExistById(String id) {
        return false;
    }

    @Override
    public void deleteById(String id) {

    }
}
