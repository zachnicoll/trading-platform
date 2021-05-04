package database.datasources;

import database.DBConnection;
import models.Trade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradeDataSource implements TradingPlatformDataSource<Trade> {

    @Override
    public Trade getById(String id) {
        return null;
    }

    public List<Trade> getAllUnresolved() throws SQLException {
        Connection dbConnection = DBConnection.getInstance();
        PreparedStatement getAllUnresolved = dbConnection.prepareStatement("SELECT * FROM trades t WHERE t.status = 'UNRESOLVED'");
        ResultSet results = getAllUnresolved.executeQuery();
        System.out.println(results);
        dbConnection.close();

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
