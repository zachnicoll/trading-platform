package data;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.datasources.AssetTypeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import database.datasources.UserDataSource;
import models.AccountType;
import models.AssetType;
import models.OrganisationalUnit;
import models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Class for generating fake data for TradeHandlerTests. Contains publicly
 * accessible fields that can be referenced in Tests (like IDs) to make valid API calls.
 */
public class TradeHandlerDataGenerator extends AbstractDataGenerator {

    /*
     * Publicly accessible fields to reference in tests
     */
    public final UUID orgUnit1Id = UUID.randomUUID();
    public final UUID assetType1Id = UUID.randomUUID();
    public final UUID user1Id = UUID.randomUUID();

    public TradeHandlerDataGenerator() throws IOException, InterruptedException, SQLException {
        // Create data in DB
        generateData();

        // Get and set auth token that can be used externally
        login("Test User " + user1Id);
    }

    protected void generateData() throws SQLException {
        createTestOrgUnits();
        createTestAssetTypes();
        createTestUser();
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

    private void createTestAssetTypes() throws SQLException {
        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
        AssetType assetType = new AssetType(
                assetType1Id,
                "Test Asset Type " + assetType1Id
        );
        assetTypeDataSource.createNew(assetType);
    }

    private void createTestUser() throws SQLException {
        UserDataSource userDataSource = new UserDataSource();
        User user = new User(
                user1Id,
                "Test User " + user1Id,
                AccountType.USER,
                orgUnit1Id
        );
        userDataSource.createNew(user, BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
    }

    public void destroyTestData() throws SQLException {
        UserDataSource userDataSource = new UserDataSource();
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();

        organisationalUnitDataSource.deleteById(orgUnit1Id);
        assetTypeDataSource.deleteById(assetType1Id);
        userDataSource.deleteById(user1Id);
    }
}
