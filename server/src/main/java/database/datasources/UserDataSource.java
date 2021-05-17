package database.datasources;

import database.DBConnection;
import models.Credentials;
import models.AccountType;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class UserDataSource extends AbstractDataSource<User> {

    private Connection dbConnection = DBConnection.getInstance();

    protected User resultSetToObject(ResultSet results) throws SQLException {
        return new User(
                UUID.fromString(results.getString("userId")),
                results.getString("username"),
                AccountType.valueOf(results.getString("userType")),
                UUID.fromString(results.getString("organisationalUnitId"))
        );
    }

    public User getById(UUID id) throws SQLException {
        PreparedStatement getById = dbConnection.prepareStatement(
                "SELECT * FROM \"users\" WHERE \"userId\"::text = ?;"
        );

        getById.setString(1, id.toString());

        ResultSet results = getById.executeQuery();

        if (!results.next()) {
            throw new SQLException("User does not exist");
        }

        return resultSetToObject(results);
    }

    public User getByUsername(String username) throws SQLException {
        PreparedStatement getById = dbConnection.prepareStatement(
                "SELECT * FROM \"users\" WHERE \"username\" = ?;"
        );

        getById.setString(1, username);

        ResultSet results = getById.executeQuery();

        if (!results.next()) {
            throw new SQLException("User does not exist");
        }

        return resultSetToObject(results);
    }

    public Credentials getExistingCredentials(String username) throws SQLException {
        PreparedStatement getById = dbConnection.prepareStatement(
                "SELECT * FROM \"users\" WHERE \"username\" = ?;"
        );

        getById.setString(1, username);

        ResultSet results = getById.executeQuery();

        if (!results.next()) {
            throw new SQLException("User does not exist");
        }

        return new Credentials(
                results.getString("username"),
                results.getString("password")
        );
    }

    public ArrayList<User> getAll() {
        return null;
    }

    public void createNew(User newObject) {
    }

    public void createNew(User newObject, String password) throws SQLException {
        PreparedStatement createNew = dbConnection.prepareStatement(
                "INSERT INTO \"users\" VALUES (uuid(?), ?, \"accountType\"(?), ?, uuid(?));"
        );

        createNew.setString(1, newObject.getUserId().toString());
        createNew.setString(2, newObject.getUsername());
        createNew.setString(3, newObject.getAccountType().toString());
        createNew.setString(4, password);
        createNew.setString(5, newObject.getOrganisationalUnitId().toString());

        createNew.execute();
    }

    public void updateByAttribute(UUID id, String attribute, User value) throws SQLException {

    }

    public boolean checkExistById(UUID id) throws SQLException {
        PreparedStatement createQueryUser = dbConnection.prepareStatement(
                "SELECT * FROM \"users\" WHERE \"userId\" = uuid(?);"
        );
        createQueryUser.setString(1, id.toString());

        return createQueryUser.executeQuery().next();
    }

    public void deleteById(UUID id) throws SQLException {
        PreparedStatement deleteUser = dbConnection.prepareStatement(
                "DELETE FROM \"users\" WHERE \"userId\" = uuid(?);"
        );
        deleteUser.setString(1, id.toString());

        deleteUser.execute();
    }
}
