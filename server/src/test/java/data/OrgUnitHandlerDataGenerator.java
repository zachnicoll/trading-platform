package data;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.datasources.OrganisationalUnitDataSource;
import database.datasources.UserDataSource;
import models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Class for generating fake data for OrgUnitHandlerTests. Contains publicly
 * accessible fields that can be referenced in Tests (like IDs) to make valid API calls.
 */
public class OrgUnitHandlerDataGenerator extends AbstractDataGenerator {
    /*
     * Publicly accessible fields to reference in tests
     */
    public final UUID orgUnit1Id = UUID.randomUUID();
    public final UUID user1Id = UUID.randomUUID();
    public OrganisationalUnit fullOrgUnit;

    public OrgUnitHandlerDataGenerator() throws SQLException, IOException, InterruptedException {
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
        if(Objects.nonNull(fullOrgUnit)) organisationalUnitDataSource.deleteById(fullOrgUnit.getUnitId());
        userDataSource.deleteById(user1Id);
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
