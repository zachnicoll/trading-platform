import models.Asset;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class AssetTests {


    /*
     * Test 0: Declaring Asset objects
     */
    final Integer ASSET_QUANTITY = 20;
    Asset asset;
    UUID assetTypeId = UUID.randomUUID();
    String assetName = "test_asset";

    /* Test 1: Constructing an Asset object
     */
    @BeforeEach
    @Test
    public void setUpAsset() {
        asset = new Asset(
                assetTypeId,
                ASSET_QUANTITY);

    }

    /* Test 2: Get Asset's Quantity
     */
    @Test
    public void getAssetQuantity() {
        assertEquals(asset.getQuantity(), ASSET_QUANTITY);
    }

    /* Test 3: Get Asset's AssetType Id
     */
    @Test
    public void getAssetsAssetTypeId(){
        assertEquals(asset.getAssetTypeId(), assetTypeId);
    }

    /* Test 4: Add one to Asset quantity: Buying one unit of an asset
     */
    @Test
    public void buyOneAsset(){
        asset.addQuantity(1);
        assertEquals(asset.getQuantity(), ASSET_QUANTITY + 1);
    }

    /* Test 5: Negative number when using the addQuantity() method
     */
    @Test
    public void negativeAddQuantity() throws ArithmeticException {
        assertThrows(ArithmeticException.class, () -> {
            asset.addQuantity(-1);
        });
    }

    /* Test 6: Decrease Asset quantity by one: Selling one asset unit
     */
    @Test
    public void sellOneAsset(){
        asset.subtractQuantity(1);
        assertEquals(asset.getQuantity(), ASSET_QUANTITY - 1);
    }

    /* Test 7: Negative number when using the subtractQuantity() method
     */
    @Test
    public void negativeSubtractQuantity() throws ArithmeticException {
        assertThrows(ArithmeticException.class, () -> {
            asset.subtractQuantity(-1);
        });
    }

    /* Test 8: Set quantity of an asset to zero
     */
    @Test
    public void setQuantityOfAssetToZero(){
        asset.setQuantity(0);
        assertEquals(asset.getQuantity(), 0);
    }

    /* Test 9: Set quantity of an asset to ten
     */
    @Test
    public void setQuantityOfAssetToTen(){
        asset.setQuantity(10);
        assertEquals(asset.getQuantity(), 10);
    }

    /* Test 10: Negative number when using the setQuantity() method
     */
    @Test
    public void negativeSetQuantity() throws ArithmeticException {
        assertThrows(ArithmeticException.class, () -> {
            asset.setQuantity(-1);
        });
    }






}