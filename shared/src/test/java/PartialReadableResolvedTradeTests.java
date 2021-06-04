import models.partial.PartialReadableResolvedTrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartialReadableResolvedTradeTests {

    /*
     * Test 0: Declaring PartialReadableResolvedTradeTests object
     */
    UUID buyTradeId = UUID.randomUUID();
    UUID sellTradeId = UUID.randomUUID();
    String boughtFrom = "unit seller";
    String soldTo = "unit buyer";
    String assetTypeName = "test asset";
    Integer quantity = 105;
    Float price = 11.2f;
    Timestamp dateResolved = Timestamp.valueOf("2021-05-27 13:49:43");

    PartialReadableResolvedTrade partialReadableResolvedTrade;

    /* Test 1: Constructing an PartialReadableResolvedTradeTests object
     */
    @BeforeEach
    @Test
    public void setUpPartialReadableResolvedTrade() {
        partialReadableResolvedTrade = new PartialReadableResolvedTrade(buyTradeId, sellTradeId, assetTypeName, quantity, price, dateResolved, boughtFrom, soldTo);
    }

    /* Test 2: Get PartialReadableResolvedTradeTests BuyTradeId
     */
    @Test
    public void getPartialReadableResolvedTradeBuyTradeId() {
        assertEquals(buyTradeId, partialReadableResolvedTrade.getBuyTradeId());
    }

    /* Test 3: Get PartialReadableResolvedTradeTests SellTradeId
     */
    @Test
    public void getPartialReadableResolvedTradeSellTradeId() {
        assertEquals(sellTradeId, partialReadableResolvedTrade.getSellTradeId());
    }

    /* Test 4: Get PartialReadableResolvedTradeTests AssetTypeName
     */
    @Test
    public void getPartialReadableResolvedTradeAssetTypeName() {
        assertEquals("test asset", partialReadableResolvedTrade.getAssetTypeName());
    }

    /* Test 5: Get PartialReadableResolvedTradeTests Quantity
     */
    @Test
    public void getPartialReadableResolvedTradeQuantity() {
        assertEquals(105, partialReadableResolvedTrade.getQuantity());
    }

    /* Test 6: Get PartialReadableResolvedTradeTests Price
     */
    @Test
    public void getPartialReadableResolvedTradePrice() {
        assertEquals(11.2f, partialReadableResolvedTrade.getPrice());
    }

    /* Test 7: Get PartialReadableResolvedTradeTests DateResolved
     */
    @Test
    public void getPartialReadableResolvedTradeDateResolved() {
        assertEquals(Timestamp.valueOf("2021-05-27 13:49:43"), partialReadableResolvedTrade.getDateResolved());
    }

    /* Test 8: Get PartialReadableResolvedTradeTests DateResolved
     */
    @Test
    public void getPartialReadableResolvedTradeBoughtFrom() {
        assertEquals("unit seller", partialReadableResolvedTrade.getBoughtFrom());
    }

    /* Test 9: Get PartialReadableResolvedTradeTests DateResolved
     */
    @Test
    public void getPartialReadableResolvedTradeSoldTo() {
        assertEquals("unit buyer", partialReadableResolvedTrade.getSoldTo());
    }

}
