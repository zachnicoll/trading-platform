package database.datasources;

import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class UserDataSource extends AbstractDataSource<User> {

    protected User resultSetToObject(ResultSet results) throws SQLException {
        //return new User();
        return null;
    }

    public User getById(UUID id) {
        return null;
    }

    public ArrayList<User> getAll() {
        return null;
    }

    public void createNew(User newObject) {

    }

    public void updateByAttribute(UUID id, String attribute, User value) throws SQLException {

    }

    public boolean checkExistById(UUID id) throws SQLException {
        return false;
    }

    public void deleteById(UUID id) throws SQLException {

    }
}
