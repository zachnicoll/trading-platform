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
    public final TradeType tradeType = TradeType.BUY;
    public final UUID organisationalUnitId = UUID.randomUUID();
    public final UUID assetTypeId = UUID.randomUUID();
    public final Integer quantity = 10;
    public final Float pricePerAsset = 5f;

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
        assertEquals(partialOpenTrade.tradeType, TradeType.BUY);
    }

    /* Test 3: Get PartialOpenTrade's TradeType
     */
    @Test
    public void getPartialOpenTradeOrgId() { assertEquals(partialOpenTrade.organisationalUnitId, organisationalUnitId); }

    /* Test 4: Get PartialOpenTrade's TradeType
     */
    @Test
    public void getPartialOpenTradeAssetId() {
        assertEquals(partialOpenTrade.assetTypeId, assetTypeId);
    }

    /* Test 5: Get PartialOpenTrade's TradeType
     */
    @Test
    public void getPartialOpenTradeQuantity() {
        assertEquals(partialOpenTrade.quantity, quantity);
    }

    /* Test 6: Get PartialOpenTrade's TradeType
     */
    @Test
    public void getPartialOpenTradePrice() {
        assertEquals(partialOpenTrade.pricePerAsset, pricePerAsset);
    }

}
