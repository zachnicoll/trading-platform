package data;

import database.datasources.AssetDataSource;
import database.datasources.AssetTypeDataSource;
import database.datasources.OrganisationalUnitDataSource;
import models.Asset;
import models.AssetType;
import models.OrganisationalUnit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class TradeResolverDataGenerator extends AbstractDataGenerator {

    /*
     * Publicly accessible fields to reference in tests
     */
    public final UUID orgUnit1Id = UUID.randomUUID();
    public final UUID orgUnit2Id = UUID.randomUUID();

    public final UUID assetType1Id = UUID.randomUUID();
    public final UUID assetType2Id = UUID.randomUUID();

    public final UUID buyTrade1Id = UUID.randomUUID();
    public final UUID buyTrade2Id = UUID.randomUUID();
    public final UUID buyTrade3Id = UUID.randomUUID();
    public final UUID buyTrade4Id = UUID.randomUUID();

    public final UUID sellTrade1Id = UUID.randomUUID();
    public final UUID sellTrade2Id = UUID.randomUUID();
    public final UUID sellTrade3Id = UUID.randomUUID();
    public final UUID sellTrade4Id = UUID.randomUUID();

    public TradeResolverDataGenerator() throws SQLException {
        generateData();
    }

    protected void generateData() throws SQLException {
        createOrgUnits();
        createAssetTypes();
    }

    private void createOrgUnits() throws SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        organisationalUnitDataSource.createNew(new OrganisationalUnit(orgUnit1Id, "Test Org Unit 1" + orgUnit1Id, 1000.0f, new ArrayList<>()));
        organisationalUnitDataSource.createNew(new OrganisationalUnit(orgUnit2Id, "Test Org Unit 2" + orgUnit2Id, 1000.0f, new ArrayList<>()));
    }

    private void createAssetTypes() throws SQLException {
        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();
        assetTypeDataSource.createNew(new AssetType(assetType1Id, "Test AssetType 1" + assetType1Id));
        assetTypeDataSource.createNew(new AssetType(assetType2Id, "Test AssetType 2" + assetType2Id));
    }

    public void createAssetsOrg1() throws SQLException {
        AssetDataSource assetDataSource = new AssetDataSource();
        assetDataSource.createNew(new Asset(assetType1Id, 1000), orgUnit1Id);
        assetDataSource.createNew(new Asset(assetType2Id, 1000), orgUnit1Id);
    }

    public void createAssetsOrg2() throws SQLException {
        AssetDataSource assetDataSource = new AssetDataSource();
        assetDataSource.createNew(new Asset(assetType1Id, 1000), orgUnit2Id);
        assetDataSource.createNew(new Asset(assetType2Id, 1000), orgUnit2Id);
    }

    public void destroyTestData() throws SQLException {
        OrganisationalUnitDataSource organisationalUnitDataSource = new OrganisationalUnitDataSource();
        AssetTypeDataSource assetTypeDataSource = new AssetTypeDataSource();

        organisationalUnitDataSource.deleteById(orgUnit1Id);
        organisationalUnitDataSource.deleteById(orgUnit2Id);

        assetTypeDataSource.deleteById(assetType1Id);
        assetTypeDataSource.deleteById(assetType2Id);
    }
}
