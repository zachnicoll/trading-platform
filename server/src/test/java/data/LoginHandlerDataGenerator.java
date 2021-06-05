package data;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.datasources.OrganisationalUnitDataSource;
import database.datasources.UserDataSource;
import helpers.PasswordHasher;
import models.AccountType;
import models.OrganisationalUnit;
import models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class LoginHandlerDataGenerator extends AbstractDataGenerator {

    public final UUID userId = UUID.randomUUID();
    public final UUID orgUnitId = UUID.randomUUID();
    public final String username = "Test User " + userId;
    public final String password = PasswordHasher.hashPassword("password");

    public LoginHandlerDataGenerator() throws SQLException {
        generateData();
    }

    protected void generateData() throws SQLException {
        createOrgUnit();
        createUser();
    }

    private void createOrgUnit() throws SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        organisationalUnitDataSource.createNew(
                new OrganisationalUnit(orgUnitId, "Test Org Unit " + orgUnitId, 1000.0f, new ArrayList<>())
        );
    }

    private void createUser() throws SQLException {
        UserDataSource userDataSource = new UserDataSource();
        userDataSource.createNew(
                new User(userId, username, AccountType.USER, orgUnitId),
                BCrypt.withDefaults().hashToString(12, password.toCharArray())
        );
    }

    public void destroyTestData() throws SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        organisationalUnitDataSource.deleteById(orgUnitId);
    }
}
