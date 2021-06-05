package data;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.datasources.AssetTypeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import database.datasources.ResolvedTradeDataSource;
import database.datasources.UserDataSource;
import models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Class for generating fake data for TradeHandlerTests. Contains publicly
 * accessible fields that can be referenced in Tests (like IDs) to make valid API calls.
 */
public class TradesHandlerDataGenerator extends AbstractDataGenerator {

    /*
     * Publicly accessible fields to reference in tests
     */
    public final UUID orgUnit1Id = UUID.randomUUID();
    public final UUID orgUnit2Id = UUID.randomUUID();
    public final UUID assetType1Id = UUID.randomUUID();
    public final UUID user1Id = UUID.randomUUID();

    public final UUID buyTrade1Id = UUID.randomUUID();
    public final UUID buyTrade2Id = UUID.randomUUID();
    public final UUID sellTrade1Id = UUID.randomUUID();
    public final UUID sellTrade2Id = UUID.randomUUID();

    public TradesHandlerDataGenerator() throws IOException, InterruptedException, SQLException {
        // Create data in DB
        generateData();

        // Get and set auth token that can be used externally
        login("Test User " + user1Id);
    }

    protected void generateData() throws SQLException {
        createTestOrgUnits();
        createTestAssetTypes();
        createTestUser();
        createTestResolvedTrades();
    }

    private void createTestOrgUnits() throws SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        OrganisationalUnit organisationalUnit1 = new OrganisationalUnit(
                orgUnit1Id,
                "Test Org Unit " + orgUnit1Id,
                1000.0f,
                new ArrayList<>()
        );
        OrganisationalUnit organisationalUnit2 = new OrganisationalUnit(
                orgUnit2Id,
                "Test Org Unit " + orgUnit2Id,
                1000.0f,
                new ArrayList<>()
        );
        organisationalUnitDataSource.createNew(organisationalUnit1);
        organisationalUnitDataSource.createNew(organisationalUnit2);
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

    private void createTestResolvedTrades() throws SQLException {
        ResolvedTradeDataSource resolvedTradeDataSource = new ResolvedTradeDataSource();
        ResolvedTrade resolvedTrade1 = new ResolvedTrade(
                buyTrade1Id,
                sellTrade1Id,
                orgUnit1Id,
                orgUnit2Id,
                assetType1Id,
                10,
                10.0f,
                Timestamp.from(Instant.now())
        );
        ResolvedTrade resolvedTrade2 = new ResolvedTrade(
                buyTrade2Id,
                sellTrade2Id,
                orgUnit1Id,
                orgUnit2Id,
                assetType1Id,
                10,
                10.0f,
                Timestamp.from(Instant.now())
        );

        resolvedTradeDataSource.createNew(resolvedTrade1);
        resolvedTradeDataSource.createNew(resolvedTrade2);
    }

    public void destroyTestData() throws SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();

        organisationalUnitDataSource.deleteById(orgUnit1Id);
        organisationalUnitDataSource.deleteById(orgUnit2Id);
        assetTypeDataSource.deleteById(assetType1Id);
    }
}
