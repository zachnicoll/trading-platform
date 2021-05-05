package database.datasources;

import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDataSource extends AbstractDataSource<User> {

    protected User resultSetToObject(ResultSet results) throws SQLException {
        //return new User();
        return null;
    }

    public User getById(String id) {
        return null;
    }

    public ArrayList<User> getAll() {
        return null;
    }

    public boolean createNew(User newObject) {
        return false;
    }

    public boolean updateByAttribute(String id, String attribute, User value) {
        return false;
    }

    public boolean checkExistById(String id) {
        return false;
    }

    public void deleteById(String id) {

    }
}
