package database.datasources;

import models.AssetType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

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

    public void createNew(AssetType newObject) {

    }

    public void updateByAttribute(UUID id, String attribute, AssetType value) throws SQLException {

    }

    public boolean checkExistById(UUID id) throws SQLException {
        return false;
    }

    public void deleteById(UUID id) throws SQLException {

    }
}
