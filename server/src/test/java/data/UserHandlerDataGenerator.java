package data;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.datasources.AssetTypeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import database.datasources.UserDataSource;
import models.AccountType;
import models.OrganisationalUnit;
import models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Class for generating fake data for UserHandlerTests. Contains publicly
 * accessible fields that can be referenced in Tests (like IDs) to make valid API calls.
 */

public class UserHandlerDataGenerator extends AbstractDataGenerator{

    /*
     * Publicly accessible fields to reference in tests
     */
    public final UUID user1Id = UUID.randomUUID();
    public final UUID orgUnit1Id = UUID.randomUUID();
    public User user;

    public UserHandlerDataGenerator() throws SQLException, IOException, InterruptedException {
        // Create data in DB
        generateData();

        // Get and set auth token that can be used externally
        login("Test User " + user1Id);
    }

    @Override
    protected void generateData() throws SQLException {
        createTestOrgUnits();
        createTestUser();

    }

    @Override
    public void destroyTestData() throws SQLException {
        UserDataSource userDataSource = new UserDataSource();
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();

        organisationalUnitDataSource.deleteById(orgUnit1Id);
        userDataSource.deleteById(user1Id);
        if(Objects.nonNull(user)) userDataSource.deleteById(user.getUserId());
    }

    private void createTestOrgUnits() throws SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        OrganisationalUnit organisationalUnit = new OrganisationalUnit(
                orgUnit1Id,
                "Test Org Unit " + orgUnit1Id,
                1000.0f,
                new ArrayList<>()
        );
        organisationalUnitDataSource.createNew(organisationalUnit);
    }

    private void createTestUser() throws SQLException {
        UserDataSource userDataSource = new UserDataSource();
        User user = new User(
                user1Id,
                "Test User " + user1Id,
                AccountType.ADMIN,
                orgUnit1Id
        );
        userDataSource.createNew(user, BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
    }
}
