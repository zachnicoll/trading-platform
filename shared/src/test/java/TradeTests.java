import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TradeTests {

    /*
     * Test 0: Declaring Trade objects
     */

    UUID assetTypeId;

    Trade trade;
    UUID tradeId = UUID.randomUUID();
    UUID organisationalUnitId = UUID.randomUUID();
    TradeType tradeType = TradeType.BUY;
    Integer quantity = 10;
    Float pricePerAsset = 10f;
    Date date = new Date();

    /* Test 1: Constructing BUY and SELL Trade objects
     */
    @BeforeEach
    @Test
    public void setUpTrade() {
        trade = new Trade(
                tradeId,
                tradeType,
                organisationalUnitId,
                assetTypeId,
                quantity,
                pricePerAsset,
                date
        );
    }

    /* Test 2: Get trade's tradeId
     */
    @Test
    public void getTradeId() {
        assertEquals(trade.getTradeId(), tradeId);
    }

    /* Test 3: Get trade's assetType
     */
    @Test
    public void getTradeAssetType() {
        assertEquals(trade.getAssetType(), assetTypeId);
    }

    /* Test 4: Get trade's tradeType "BUY"
     */
    @Test
    public void getTradeTypeBuy() {
        tradeType = TradeType.BUY;
        trade.setTradeType(TradeType.BUY);
        assertEquals(trade.getTradeType(), tradeType);
    }

    /* Test 5: Get trade's tradeType "SELL"
     */
    @Test
    public void getTradeTypeSell() {
        tradeType = TradeType.SELL;
        trade.setTradeType(TradeType.SELL);
        assertEquals(trade.getTradeType(), tradeType);
    }

    /* Test 6: Get trade date
     */
    @Test
    public void getTradeDate() {
        assertEquals(trade.getDate(), date);
    }

    /* Test 6: Get trade's asset price
     */
    @Test
    public void getTradeAssetPrice() {
        assertEquals(trade.getPricePerAsset(), pricePerAsset);
    }

    /* Test 7: Get trade's traded asset quantity
     */
    @Test
    public void getTradedAssetQuantity() {
        assertEquals(trade.getQuantity(), quantity);
    }

    /* Test 8: Get the organisational unit that created the trade
     */
    @Test
    public void getTradesOrganisationalUnit() {
        assertEquals(trade.getOrganisationalUnit(), organisationalUnitId);
    }

}
