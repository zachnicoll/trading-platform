package src.main.java.database.datasources;

import java.util.List;


public class OrganisationalUnitDataSource implements TradingPlatformDataSource {

    @Override
    public Object getById(String id) {
        return null;
    }

    @Override
    public List<Object> getAll() {
        return null;
    }

    @Override
    public boolean createNew(Object newObject) {
        return false;
    }

    @Override
    public boolean updateByAttribute(String id, String attribute, Object value) {
        return false;
    }


    @Override
    public boolean checkExistById(String organisationalUnitId) {
        return false;
    }

}
