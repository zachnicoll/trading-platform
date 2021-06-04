import models.TradeType;
import models.partial.PartialOpenTrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartialOpenTradeTests {

    /*
     * Test 0: Declaring PartialOpenTrade object
     */
    TradeType tradeType = TradeType.BUY;
    UUID organisationalUnitId = UUID.randomUUID();
    UUID assetTypeId = UUID.randomUUID();
    Integer quantity = 10;
    Float pricePerAsset = 5f;

    PartialOpenTrade partialOpenTrade;


    /* Test 1: Constructing an PartialOpenTrade object
     */
    @BeforeEach
    @Test
    public void setUpPartialOpenTrade() {
        partialOpenTrade = new PartialOpenTrade(tradeType,organisationalUnitId,assetTypeId,quantity,pricePerAsset);
    }

    /* Test 2: Get PartialOpenTrade's TradeType
     */
    @Test
    public void getPartialOpenTradeTradeType() {
        assertEquals(TradeType.BUY, partialOpenTrade.tradeType);
    }

    /* Test 3: Get PartialOpenTrade's TradeType
     */
    @Test
    public void getPartialOpenTradeOrgId() { assertEquals(organisationalUnitId, partialOpenTrade.organisationalUnitId); }

    /* Test 4: Get PartialOpenTrade's TradeType
     */
    @Test
    public void getPartialOpenTradeAssetId() {
        assertEquals(assetTypeId, partialOpenTrade.assetTypeId);
    }

    /* Test 5: Get PartialOpenTrade's TradeType
     */
    @Test
    public void getPartialOpenTradeQuantity() {
        assertEquals(10, partialOpenTrade.quantity);
    }

    /* Test 6: Get PartialOpenTrade's TradeType
     */
    @Test
    public void getPartialOpenTradePrice() {
        assertEquals(5f, partialOpenTrade.pricePerAsset);
    }

}
