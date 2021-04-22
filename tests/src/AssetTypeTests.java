import static org.junit.jupiter.api.Assertions.*;

import models.AssetType;
import org.junit.jupiter.api.*;
import java.util.UUID;

public class AssetTypeTests {



    /*
     * Test 0: Declaring Asset objects
     */
    AssetType assetType;
    String assetTypeId = UUID.randomUUID().toString();
    String assetName = "test_asset";


    /*
     * Test 1: Declaring Asset objects
     */
    @BeforeEach
    @Test
    public void setUpAssetType() {
        assetType = new AssetType(
                assetTypeId.toString(),
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
}
