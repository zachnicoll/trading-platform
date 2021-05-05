package database.datasources;

import models.Asset;
import models.OpenTrade;
import models.TradeType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public boolean createNew(Asset newObject) {
        return false;
    }

    public boolean updateByAttribute(String id, String attribute, Asset value) {
        return false;
    }

    public boolean checkExistById(String id) {
        return false;
    }

    public void deleteById(String id) {

    }


}
