package database.datasources;

import models.AssetType;

import java.util.List;

public class AssetTypeDataSource implements TradingPlatformDataSource<AssetType> {


    @Override
    public AssetType getById(String id) {
        return null;
    }

    @Override
    public List<AssetType> getAll() {
        return null;
    }

    @Override
    public boolean createNew(AssetType newObject) {
        return false;
    }

    @Override
    public boolean updateByAttribute(String id, String attribute, AssetType value) {
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
