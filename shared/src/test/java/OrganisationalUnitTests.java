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
    UUID unitId = UUID.randomUUID();
    String unitName ="test_Unit";
    Float creditBalance = 1000.50f;
    List<Asset> assets;

    //AssetType
    UUID assetTypeId = UUID.randomUUID();
    //Asset
    int quantity = ASSET_QUANTITY;
    Asset asset = new Asset(assetTypeId, quantity);

    /*
     * Test 1: Constructing an Organisational Unit Object
     */
    @BeforeEach
    @Test
    public void setUpOrgUnit()
    {
        assets = new ArrayList<>();
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
        assertEquals(asset, organisationalUnit.findExistingAsset(assetTypeId));
    }

    /*
     * Test 7: Determine if this OU already owns an AssetType that matches the given AssetType ID : False
     */
    @Test
    public void findExistingAssetFalse()
    {
        UUID localAssetTypeId = UUID.randomUUID();
        String localAssetName = "another_test_asset";
        AssetType localAssetType = new AssetType(localAssetTypeId, localAssetName);

        assertNull(organisationalUnit.findExistingAsset(localAssetType.getAssetTypeId()));
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
    public void updateCreditBalanceFail()
    {
        // TODO: Make this fail correctly when API is hooked-up

        assertThrows(ApiException.class, () -> organisationalUnit.updateCreditBalance(2000f));
    }

    /*
     * Test 10: Update the Organisational Unit's quantity of a given AssetType : Owned
     */
    @Test
    public void updateAssetQuantityOwned() throws ApiException
    {
        organisationalUnit.updateAssetQuantity(assetTypeId, 15);

        assertEquals(15, organisationalUnit.findExistingAsset(assetTypeId).getQuantity());

    }

    /*
     * Test 11: Update the Organisational Unit's quantity of a given AssetType : Not Owned
     */
    @Test
    public void updateAssetQuantityNotOwned() throws ApiException
    {
        UUID localAssetTypeId = UUID.randomUUID();
        String localAssetName = "another_test_asset";
        AssetType localAssetType = new AssetType(localAssetTypeId, localAssetName);

        organisationalUnit.updateAssetQuantity(localAssetType.getAssetTypeId(), 25);
        assertEquals(25, organisationalUnit.findExistingAsset(localAssetType.getAssetTypeId()).getQuantity());
    }

    /*
     * Test 12: Update the Organisational Unit's quantity of a given AssetType : Failed
     */
    @Test
    public void updateAssetQuantityFailed()
    {
        // TODO: Make this fail correctly when API is hooked-up

        assertThrows(ApiException.class, () -> organisationalUnit.updateCreditBalance(2000f));
    }

    /*
     * Test 13: Purchase an OU's Asset : Succeed Existing
     */
    @Test
    public void purchaseAssetSucceedExisting() throws ApiException, InvalidTransactionException
    {
        Float PRICE_PER = 2.50f;
        int quantity = 100;

        organisationalUnit.purchaseAsset(PRICE_PER, assetTypeId, quantity);
        assertEquals(750.5f, organisationalUnit.getCreditBalance());
        assertEquals(110, organisationalUnit.findExistingAsset(assetTypeId).getQuantity());
    }

    /*
     * Test 14: Purchase an OU's Asset : Succeed New
     */
    @Test
    public void purchaseAssetSucceedNew() throws ApiException, InvalidTransactionException
    {
        Float PRICE_PER = 2f;
        int quantity = 200;

        UUID localAssetTypeId = UUID.randomUUID();

        organisationalUnit.purchaseAsset(PRICE_PER, localAssetTypeId, quantity);
        assertEquals(600.5f, organisationalUnit.getCreditBalance());
        assertEquals(200, organisationalUnit.findExistingAsset(localAssetTypeId).getQuantity());
    }

    /*
     * Test 15: Purchase an OU's Asset : Fail
     */
    @Test
    public void purchaseAssetFail()
    {
        Float PRICE_PER = 100f;
        int quantity = 12;

        // TODO: Make this fail correctly when InvalidTransactionException is complete
        assertThrows(InvalidTransactionException.class, () -> organisationalUnit.purchaseAsset(PRICE_PER, assetTypeId, quantity));


    }

    /*
     * Test 16: Purchase an OU's Asset : Invalid
     */
    @Test
    public void purchaseAssetInvalid()
    {
        Float PRICE_PER = 2.50f;
        int quantity = 100;

        // TODO: Make this fail correctly when API is hooked-up

        assertThrows(ApiException.class, () -> organisationalUnit.purchaseAsset(PRICE_PER, assetTypeId, quantity));
    }

    /*
     * Test 17: Sell an OU's Asset : Succeed
     */
    @Test
    public void sellAssetSucceed() throws ApiException, InvalidTransactionException
    {
        Float PRICE_PER = 5f;
        int quantity = 5;

        organisationalUnit.sellAsset(PRICE_PER, assetTypeId, quantity);

        assertEquals(1025.5f, organisationalUnit.getCreditBalance());
        assertEquals(5, organisationalUnit.findExistingAsset(assetTypeId).getQuantity());
    }

    /*
     * Test 18: Sell an OU's Asset : Fail Missing Asset
     */
    @Test
    public void sellAssetFailMissingAsset()
    {
        Float PRICE_PER = 5f;
        int quantity = 5;

        UUID localAssetTypeId = UUID.randomUUID();

        // TODO: Make this fail correctly when InvalidTransactionException is complete
        assertThrows(InvalidTransactionException.class, () -> organisationalUnit.sellAsset(PRICE_PER, localAssetTypeId, quantity));
    }

    /*
     * Test 19: Sell an OU's Asset : Fail Quantity
     */
    @Test
    public void sellAssetFailQuantity()
    {
        Float PRICE_PER = 5f;
        int quantity = 11;
        // TODO: Make this fail correctly when InvalidTransactionException is complete
        assertThrows(InvalidTransactionException.class, () -> organisationalUnit.sellAsset(PRICE_PER, assetTypeId, quantity));
    }

    /*
     * Test 20: Sell an OU's Asset : Invalid
     */
    @Test
    public void sellAssetInvalid()
    {
        Float PRICE_PER = 5f;
        int quantity = 5;

        // TODO: Make this fail correctly when API is hooked-up
        assertThrows(ApiException.class, () -> organisationalUnit.sellAsset(PRICE_PER, assetTypeId, quantity));
    }

}