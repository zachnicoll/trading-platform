package tests;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.ApiException;

import exceptions.InvalidTransactionException;
import models.OrganisationalUnit;
import models.Asset;
import models.AssetType;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrganisationalUnitTests {

    /*
     * Test 0: Declaring OrganisationalUnit objects and parameters
     */

    final int ASSET_QUANTITY = 10;

    //OU
    OrganisationalUnit organisationalUnit;
    String unitId = UUID.randomUUID().toString();
    String unitName ="test_Unit";
    Float creditBalance = 1000.50f;
    List<Asset> assets;

    //AssetType
    String assetTypeId = UUID.randomUUID().toString();
    String assetName = "test_asset";
    AssetType assetType = new AssetType(assetTypeId.toString(), assetName);

    //Asset
    int quantity = ASSET_QUANTITY;
    Asset asset = new Asset(assetType, quantity);

    /*
     * Test 1: Constructing an Organisational Unit Object
     */
    @BeforeEach
    @Test
    public void setUpOrgUnit()
    {
        assets = new ArrayList<Asset>();
        this.assets.add(asset);

        organisationalUnit = new OrganisationalUnit(unitId, unitName, creditBalance, assets);

    }

    /* Test 2: Get the UnitID of the OU
     */
    @Test
    public void getUnitId() {
        assertEquals(organisationalUnit.getUnitId(), unitId);
    }

    /* Test 3: Get the UnitName of the OU
     */
    @Test
    public void getUnitName() {
        assertEquals(organisationalUnit.getUnitName(), unitName);
    }

    /* Test 4: Get the CreditBalance of the OU
     */
    @Test
    public void getCreditBalance() {
        assertEquals(organisationalUnit.getCreditBalance(), creditBalance);
    }

    /* Test 5: Get the Assets of the OU
     */
    @Test
    public void getAssets() {
        assertEquals(organisationalUnit.getAssets(), assets);
    }

    /*
     * Test 6: Determine if this OU already owns an AssetType that matches the given AssetType ID : True
     */
    @Test
    public void findExistingAssetTrue()
    {
        assertEquals(asset, organisationalUnit.findExistingAsset(assetType));
    }

    /*
     * Test 7: Determine if this OU already owns an AssetType that matches the given AssetType ID : False
     */
    @Test
    public void findExistingAssetFalse()
    {
        String localAssetTypeId = UUID.randomUUID().toString();
        String localAssetName = "another_test_asset";
        AssetType localAssetType = new AssetType(localAssetTypeId.toString(), localAssetName);

        assertEquals(null, organisationalUnit.findExistingAsset(localAssetType));
    }


    /*
     * Test 8: Update OU Credit Balance : Succeed
     */
    @Test
    public void updateCreditBalanceSucceed() throws ApiException
    {
        organisationalUnit.updateCreditBalance(2000f);
        assertEquals(2000f, organisationalUnit.getCreditBalance());
    }

    /*
     * Test 9: Update OU Credit Balance : Fail
     */
    @Test
    public void updateCreditBalanceFail() throws ApiException
    {
        // TODO: Make this fail correctly when API is hooked-up
    }

    /*
     * Test 10: Update the Organisational Unit's quantity of a given AssetType : Owned
     */
    @Test
    public void updateAssetQuantityOwned() throws ApiException
    {
        assets.add(asset);


    }

    /*
     * Test 11: Update the Organisational Unit's quantity of a given AssetType : Not Owned
     */
    @Test
    public void updateAssetQuantityNotOwned() throws ApiException
    {

    }

    /*
     * Test 12: Update the Organisational Unit's quantity of a given AssetType : Failed
     */
    @Test
    public void updateAssetQuantityFailed() throws ApiException
    {
        // TODO: Make this fail correctly when API is hooked-up
    }

    /*
     * Test 13: Purchase an OU's Asset : Succeed
     */
    @Test
    public void purchaseAssetSucceed() throws ApiException, InvalidTransactionException
    {

    }

    /*
     * Test 14: Purchase an OU's Asset : Fail
     */
    @Test
    public void purchaseAssetFail() throws ApiException, InvalidTransactionException
    {

    }

    /*
     * Test 15: Purchase an OU's Asset : Invalid
     */
    @Test
    public void purchaseAssetInvalid() throws ApiException, InvalidTransactionException
    {
        // TODO: Make this fail correctly when API is hooked-up
    }

    /*
     * Test 16: Sell an OU's Asset : Succeed
     */
    @Test
    public void sellAssetSucceed() throws ApiException, InvalidTransactionException
    {

    }

    /*
     * Test 17: Sell an OU's Asset : Fail
     */
    @Test
    public void sellAssetFail() throws ApiException, InvalidTransactionException
    {

    }

    /*
     * Test 18: Sell an OU's Asset : Invalid
     */
    @Test
    public void sellAssetInvalid() throws ApiException, InvalidTransactionException
    {
        // TODO: Make this fail correctly when API is hooked-up
    }

}