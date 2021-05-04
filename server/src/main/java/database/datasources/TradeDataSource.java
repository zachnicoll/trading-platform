package database.datasources;

import models.Trade;

import java.util.List;

public class TradeDataSource implements TradingPlatformDataSource<Trade> {


    @Override
    public Trade getById(String id) {
        return null;
    }

    @Override
    public List<Trade> getAll() {
        return null;
    }

    @Override
    public boolean createNew(Trade newObject) {
        return false;
    }

    @Override
    public boolean updateByAttribute(String id, String attribute, Trade value) {
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
