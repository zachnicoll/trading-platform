package database.datasources;

import models.Asset;

import java.util.List;

public class AssetDataSource implements TradingPlatformDataSource<Asset> {

    @Override
    public Asset getById(String id) {
        return null;
    }

    @Override
    public List<Asset> getAll() {
        return null;
    }

    @Override
    public boolean createNew(Asset newObject) {
        return false;
    }

    @Override
    public boolean updateByAttribute(String id, String attribute, Asset value) {
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
