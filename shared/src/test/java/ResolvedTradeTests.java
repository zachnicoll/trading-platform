
import models.ResolvedTrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ResolvedTradeTests {


    /*
     * Test 0: Declaring ResolvedTrade objects
     */
    UUID buyTradeId = UUID.randomUUID();
    UUID sellTradeId = UUID.randomUUID();
    UUID buyOrgUnitId = UUID.randomUUID();
    UUID sellOrgUnitId = UUID.randomUUID();
    UUID assetTypeId = UUID.randomUUID();
    Integer quantity = 10;
    Float price = 150.74f;
    Timestamp dateResolved = Timestamp.valueOf("2021-05-27 13:49:43");

    ResolvedTrade resolvedTrade;

    /* Test 1: Constructing an ResolvedTrade object
     */
    @BeforeEach
    @Test
    public void setUpResolvedTrade() {
        resolvedTrade = new ResolvedTrade(buyTradeId, sellTradeId, buyOrgUnitId, sellOrgUnitId, assetTypeId, quantity, price, dateResolved);
    }

    /* Test 2: Get ResolvedTrade BuyTradeId
     */
    @Test
    public void getResolvedTradeBuyTradeId() {
        assertEquals(buyTradeId, resolvedTrade.getBuyTradeId());
    }

    /* Test 3: Get ResolvedTrade SellTradeId
     */
    @Test
    public void getResolvedTradeSellTradeId() {
        assertEquals(sellTradeId, resolvedTrade.getSellTradeId());
    }

    /* Test 4: Get ResolvedTrade BuyOrgUnitId
     */
    @Test
    public void getResolvedTradeBuyOrgUnitId() {
        assertEquals(buyOrgUnitId, resolvedTrade.getBuyOrgUnitId());
    }

    /* Test 5: Get ResolvedTrade SellOrgUnitId
     */
    @Test
    public void getResolvedTradeSellOrgUnitId() {
        assertEquals(sellOrgUnitId, resolvedTrade.getSellOrgUnitId());
    }

    /* Test 6: Get ResolvedTrade AssetTypeId
     */
    @Test
    public void getResolvedTradeAssetTypeId() {
        assertEquals(assetTypeId, resolvedTrade.getAssetTypeId());
    }

    /* Test 7: Get ResolvedTrade Quantity
     */
    @Test
    public void getResolvedTradeQuantity() {
        assertEquals(10, resolvedTrade.getQuantity());
    }

    /* Test 8: Get ResolvedTrade Price
     */
    @Test
    public void getResolvedTradePrice() {
        assertEquals(150.74f, resolvedTrade.getPrice());
    }

    /* Test 9: Get ResolvedTrade DateResolved
     */
    @Test
    public void getResolvedTradeDateResolved() {
        assertEquals(dateResolved, resolvedTrade.getDateResolved());
    }

}