package database.datasources;

import models.User;

import java.util.List;

public class UserDataSource implements TradingPlatformDataSource<User> {


    @Override
    public User getById(String id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public boolean createNew(User newObject) {
        return false;
    }

    @Override
    public boolean updateByAttribute(String id, String attribute, User value) {
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
