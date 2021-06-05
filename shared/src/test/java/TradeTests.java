import models.OpenTrade;
import models.TradeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TradeTests {

    /*
     * Test 0: Declaring Trade objects
     */

    UUID assetTypeId = UUID.randomUUID();

    OpenTrade trade;
    UUID tradeId = UUID.randomUUID();
    UUID organisationalUnitId = UUID.randomUUID();
    TradeType tradeType = TradeType.BUY;
    Integer quantity = 10;
    Float pricePerAsset = 10f;
    Timestamp date = Timestamp.from(Instant.now());

    /* Test 1: Constructing BUY and SELL Trade objects
     */
    @BeforeEach
    @Test
    public void setUpTrade() {
        trade = new OpenTrade(
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

    /* Test 9: Set the quantity of assets in the trade
     */
    @Test
    public void setTradeQuantity() {
        trade.setQuantity(50);
        assertEquals(50, trade.getQuantity());
    }

    /* Test 10: Trade Comparator to sort trade by date in ascending order
     */
    @Test
    public void tradeComparator() {

        UUID assetTypeId2 = UUID.randomUUID();
        OpenTrade trade2;
        UUID tradeId2 = UUID.randomUUID();
        UUID organisationalUnitId2 = UUID.randomUUID();
        TradeType tradeType2 = TradeType.BUY;
        Integer quantity2 = 20;
        Float pricePerAsset2 = 15f;
        Timestamp date2 = Timestamp.valueOf("2021-05-27 13:49:43");

        trade2 = new OpenTrade(tradeId2, tradeType2, organisationalUnitId2, assetTypeId2, quantity2, pricePerAsset2, date2);

        List<OpenTrade> tempList = new ArrayList<OpenTrade>();
        tempList.add(trade);
        tempList.add(trade2);

        tempList.sort(OpenTrade.tradeDateComparator);

        assertEquals(tradeId2, tempList.get(0).getTradeId());
    }

    /* Test 11: OpenTrade toString method
     */
    @Test
    public void toStringTest() {
        String temp = "TradeID: " + tradeId + "\n" +
                "TradeType: " + tradeType + "\n" +
                "OrgUnitId: " + organisationalUnitId + "\n" +
                "AssetTypeId: " + assetTypeId + "\n" +
                "Quantity: " + quantity + "\n" +
                "PricePerAsset: " + pricePerAsset + "\n" +
                "DateOpened: " + date + "\n";

        assertEquals(temp, trade.toString());
    }

}
