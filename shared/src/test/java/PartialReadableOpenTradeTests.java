import models.TradeType;
import models.partial.PartialReadableOpenTrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartialReadableOpenTradeTests {

    /*
     * Test 0: Declaring PartialReadableOpenTrade object
     */
    UUID tradeId = UUID.randomUUID();
    TradeType tradeType = TradeType.SELL;
    String organisationalUnitName = "test unit";
    String assetTypeName = "test asset";
    Integer quantity = 100;
    Float pricePerAsset = 7.5f;
    Timestamp dateOpened = Timestamp.valueOf("2021-05-27 13:49:43");
    UUID organisationalUnitId = UUID.randomUUID();

    PartialReadableOpenTrade partialReadableOpenTrade;

    /* Test 1: Constructing an PartialReadableOpenTrade object
     */
    @BeforeEach
    @Test
    public void setUpPartialReadableOpenTrade() {
        partialReadableOpenTrade = new PartialReadableOpenTrade(tradeId,tradeType,organisationalUnitName, assetTypeName, quantity, pricePerAsset, dateOpened, organisationalUnitId);
    }

    /* Test 2: Get PartialReadableOpenTrade's TradeId
     */
    @Test
    public void getPartialReadableOpenTradeTradeId() {
        assertEquals(tradeId, partialReadableOpenTrade.getTradeId());
    }

    /* Test 3: Get PartialReadableOpenTrade's TradeType
     */
    @Test
    public void getPartialReadableOpenTradeTradeType() {
        assertEquals("SELL", partialReadableOpenTrade.getTradeType());
    }

    /* Test 4: Get PartialReadableOpenTrade's OrganisationalUnitName,
     */
    @Test
    public void getPartialReadableOpenTradeOrganisationalUnitName() {
        assertEquals("test unit", partialReadableOpenTrade.getOrganisationalUnitName());
    }

    /* Test 5: Get PartialReadableOpenTrade's AssetTypeName,
     */
    @Test
    public void getPartialReadableOpenTradeAssetTypeName() {
        assertEquals("test asset", partialReadableOpenTrade.getAssetTypeName());
    }

    /* Test 6: Get PartialReadableOpenTrade's Quantity,
     */
    @Test
    public void getPartialReadableOpenTradeQuantity() {
        assertEquals(100, partialReadableOpenTrade.getQuantity());
    }

    /* Test 7: Get PartialReadableOpenTrade's PricePerAsset,
     */
    @Test
    public void getPartialReadableOpenTradePricePerAsset() {
        assertEquals("$7.50", partialReadableOpenTrade.getPricePerAsset());
    }

    /* Test 8: Get PartialReadableOpenTrade's DateOpened,
     */
    @Test
    public void getPartialReadableOpenTradeDateOpened() {
        assertEquals("27/05/2021 13:49:43", partialReadableOpenTrade.getDateOpened());
    }

    /* Test 9: Get PartialReadableOpenTrade's OrganisationalUnitId,
     */
    @Test
    public void getPartialReadableOpenTradeOrganisationalUnitId() {
        assertEquals(organisationalUnitId, partialReadableOpenTrade.getOrganisationalUnitId());
    }

}
