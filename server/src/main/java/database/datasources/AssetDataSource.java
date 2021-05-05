package database.datasources;

import models.Asset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class AssetDataSource extends AbstractDataSource<Asset> {

    protected Asset resultSetToObject(ResultSet results) throws SQLException {
        return null;
        // return new Asset();
    }

    public Asset getById(String id) {
        return null;
    }

    public ArrayList<Asset> getAll() {
        return null;
    }

    public void createNew(Asset newObject) {

    }

    public void updateByAttribute(UUID id, String attribute, Asset value) throws SQLException {

    }

    public boolean checkExistById(UUID id) throws SQLException {
        return false;
    }

    public void deleteById(UUID id) throws SQLException {

    }
}
