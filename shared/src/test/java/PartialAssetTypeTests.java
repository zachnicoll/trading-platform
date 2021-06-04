import models.partial.PartialAssetType;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

public class PartialAssetTypeTests {

    /*
     * Test 0: Declaring PartialAssetType object
     */
    String assetName = "test asset";
    PartialAssetType partialAssetType;

    /* Test 1: Constructing an Asset object
     */
    @BeforeEach
    @Test
    public void setUpPartialAssetType() {
        partialAssetType = new PartialAssetType(assetName);
    }

    /* Test 2: Get PartialAssetTypes's Name
     */
    @Test
    public void getPartialAssetTypeName() {
        assertEquals("test asset",partialAssetType.assetName);
    }

}
