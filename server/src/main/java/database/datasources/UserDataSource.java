package database.datasources;

import database.DBConnection;
import models.Credentials;
import models.AccountType;
import models.ResolvedTrade;
import models.User;
import models.partial.PartialReadableOpenTrade;
import models.partial.PartialReadableUser;

import java.security.InvalidParameterException;
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

    protected PartialReadableUser resultSetToReadableObject(ResultSet results) throws SQLException {
        return new PartialReadableUser(
                UUID.fromString(results.getString("userId")),
                results.getString("username"),
                AccountType.valueOf(results.getString("userType")),
                results.getString("organisationalUnitName")
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

    public ArrayList<User> getAll() throws SQLException {

        PreparedStatement getAllUsers = dbConnection.prepareStatement(
                "SELECT * FROM \"users\";"
        );
        ResultSet results = getAllUsers.executeQuery();

        ArrayList<User> allUsers = new ArrayList<>();
        while (results.next()) {
            User aUser = resultSetToObject(results);
            allUsers.add(aUser);
        }

        return allUsers;
    }
    public ArrayList<PartialReadableUser> getAllReadable() throws SQLException {

        PreparedStatement getAllUsersReadable = dbConnection.prepareStatement(
                """
                        SELECT
                               u."userId",
                               u.username,
                               u."userType",\s
                               ou."organisationalUnitName"\s
                               FROM
                               users u\s
                               JOIN "organisationalUnits" ou ON ou."organisationalUnitId" = u."organisationalUnitId";""");

        ResultSet results = getAllUsersReadable.executeQuery();

        ArrayList<PartialReadableUser> allUsersReadable = new ArrayList<>();
        while (results.next()) {
            PartialReadableUser user = resultSetToReadableObject(results);
            allUsersReadable.add(user);
        }

        return allUsersReadable;
    }



    public void createNew(User newObject) throws SQLException {
        //TODO FIGURE OUT WHAT THIS IS FOR
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

    public void updateByAttribute(UUID id, String attribute, User value) throws SQLException, InvalidParameterException {

        Object attrValue;
        switch (attribute) {
            case "organisationalUnitId":
                attrValue = value.getOrganisationalUnitId();
                break;

            default:
                throw new InvalidParameterException();
        }

        PreparedStatement updateByAttribute = dbConnection.prepareStatement(
                "UPDATE \"users\" SET \"?\" = uuid(?) WHERE \"userId\" = uuid(?);"
        );


        updateByAttribute.setString(1, attribute);
        updateByAttribute.setString(2, attrValue.toString());
        updateByAttribute.setString(3, id.toString());

        updateByAttribute.execute();
    }

    public boolean checkExistById(UUID id) throws SQLException {
        PreparedStatement checkExistById = dbConnection.prepareStatement(
                "SELECT EXISTS(SELECT 1 FROM \"users\" WHERE \"userId\" = uuid(?));"
        );
        checkExistById.setString(1, id.toString());

        //checks if first element is either 't' or 'f' indicating if the row exists in the database
        ResultSet check = checkExistById.executeQuery();
        check.next(); // moves cursor to the next row
        String confirm = check.getString("exists");

        return confirm.equals("t") ? true : false;
    }


    public void deleteById(UUID id) throws SQLException {
        PreparedStatement deleteUser = dbConnection.prepareStatement(
                "DELETE FROM \"users\" WHERE \"userId\" = uuid(?);"
        );
        deleteUser.setString(1, id.toString());

        deleteUser.execute();
    }

    public void changePassword(UUID id, String password) throws SQLException, InvalidParameterException {

        PreparedStatement updatePassword = dbConnection.prepareStatement(
                "UPDATE \"users\" SET \"password\" = ? WHERE \"userId\" = uuid(?);"
        );


        updatePassword.setString(1, password);
        updatePassword.setString(2, id.toString());

        updatePassword.execute();
    }
}
