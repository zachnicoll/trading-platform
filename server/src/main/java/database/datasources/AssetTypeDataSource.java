package database.datasources;

import models.AssetType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AssetTypeDataSource extends AbstractDataSource<AssetType> {

    protected AssetType resultSetToObject(ResultSet results) throws SQLException {
        //return new AssetType();
        return null;
    }

    public AssetType getById(String id) {
        return null;
    }

    public ArrayList<AssetType> getAll() {
        return null;
    }

    public boolean createNew(AssetType newObject) {
        return false;
    }

    public boolean updateByAttribute(String id, String attribute, AssetType value) {
        return false;
    }

    public boolean checkExistById(String id) {
        return false;
    }

    public void deleteById(String id) {

    }
}
