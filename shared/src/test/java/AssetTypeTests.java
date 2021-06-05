import models.AssetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssetTypeTests {



    /*
     * Test 0: Declaring Asset objects
     */
    AssetType assetType;
    UUID assetTypeId = UUID.randomUUID();
    String assetName = "test_asset";


    /*
     * Test 1: Declaring Asset objects
     */
    @BeforeEach
    @Test
    public void setUpAssetType() {
        assetType = new AssetType(
                assetTypeId,
                assetName);
    }

    /*
     * Test 2: Get Asset's name
     */
    @Test
    public void getAssetName(){
        assertEquals(assetType.getAssetName(), assetName);
    }

    /*
     * Test 3: Get AssetType Id
     */
    @Test
    public void getAssetTypeId(){
        assertEquals(assetType.getAssetTypeId(), assetTypeId);
    }

    /*
     * Test 4: toString method to fetch name
     */
    @Test
    public void toStringTest(){
        assertEquals("test_asset", assetType.toString());
    }
}
