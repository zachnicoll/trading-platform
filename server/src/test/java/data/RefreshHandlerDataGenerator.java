package data;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.datasources.OrganisationalUnitDataSource;
import database.datasources.UserDataSource;
import helpers.PasswordHasher;
import models.AccountType;
import models.OrganisationalUnit;
import models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class RefreshHandlerDataGenerator extends AbstractDataGenerator {
    public final UUID userId = UUID.randomUUID();
    public final UUID orgUnitId = UUID.randomUUID();
    public final String username = "Test User " + userId;

    public RefreshHandlerDataGenerator() throws SQLException, IOException, InterruptedException {
        generateData();
        login(username);
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
        String password = "password";
        userDataSource.createNew(
                new User(userId, username, AccountType.USER, orgUnitId),
                BCrypt.withDefaults().hashToString(12, password.toCharArray())
        );
    }

    void destroyTestData() throws SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        organisationalUnitDataSource.deleteById(orgUnitId);
    }
}
