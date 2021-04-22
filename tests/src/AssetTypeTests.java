import static org.junit.jupiter.api.Assertions.*;

import models.Asset;
import models.AssetType;
import org.junit.jupiter.api.*;
import java.util.UUID;

public class AssetTypeTests {




    AssetType assetType;
    String assetTypeId = UUID.randomUUID().toString();
    String assetName = "test_asset";


    /*
     * Test 0: Declaring Asset objects
     */
    @BeforeEach
    @Test
    public void setUpAssetType() {
        assetType = new AssetType(
                assetTypeId.toString(),
                assetName);
    }

    /*
     * Test 1: Get Asset's name
     */
    @Test
    public void getAssetName(){
        assertEquals(assetType.getAssetName(), assetName);
    }

    /*
     * Test 2: Get AssetType Id
     */
    @Test
    public void getAssetTypeId(){
        assertEquals(assetType.getAssetTypeId(), assetTypeId);
    }
}
