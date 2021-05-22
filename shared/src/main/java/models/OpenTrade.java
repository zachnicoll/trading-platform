package models;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

/**
 * Represents trade information stored in the openTrades table in the database.
 * It contains the type of trade (BUY/SELL), which Organisational Unit placed the trade,
 * and for how many of a given AssetType (via assetTypeId).
 */
public class OpenTrade {

    /**
     * Unique UUID identifier for each trade
     */
    private final UUID tradeId;

    /**
     * Whether the Trade is a BUY or SELL order
     */
    private TradeType tradeType;

    /**
     * The OrganisationalUnit that placed the Trade order
     */
    private final UUID organisationalUnitId;

    /**
     * The AssetType that is being bought/sold
     */
    private final UUID assetTypeId;

    /**
     * Quantity of AssetType to buy or sell
     */
    private Integer quantity;

    /**
     * Price for a single asset of type AssetType
     */
    private final Float pricePerAsset;

    /**
     * Date that the trade order was openedstoring and retrieving
     */
    private final Timestamp dateOpened;

    /**
     * Construct a new OpenTrade with given information, most likely provided by the API.
     * @param tradeId Unique UUID identifier for each trade
     * @param tradeType whether the trade is a BUY or SELL order
     * @param organisationalUnitId the OrganisationalUnit that placed the Trade order
     * @param assetTypeId the AssetType that is being bought/sold
     * @param quantity quantity of AssetType to buy or sell
     * @param pricePerAsset price for a single asset of type AssetType
     * @param dateOpened date that the trade order was opened
     */
    public OpenTrade (UUID tradeId, TradeType tradeType, UUID organisationalUnitId, UUID assetTypeId, Integer quantity, Float pricePerAsset, Timestamp dateOpened) {
        this.tradeId = tradeId;
        this.tradeType = tradeType;
        this.organisationalUnitId = organisationalUnitId;
        this.assetTypeId = assetTypeId;
        this.quantity = quantity;
        this.pricePerAsset = pricePerAsset;
        this.dateOpened = dateOpened;
    }


    /**
     * Get the UUID unique identifier of the Trade
     * @return the Trade's tradeId
     */
    public UUID getTradeId() {
        return tradeId;
    }


    /**
     * Get the AssetType of the Trade
     * @return the Trade's AssetType
     */
    public UUID getAssetType() {
        return assetTypeId;
    }

    /**
     * Get the TradeType of the Trade
     * @return the Trade's TradeType
     */
    public TradeType getTradeType() {
        return tradeType;
    }

    /**
     * Sets the trade type (buy or sell) of the Trade
     */
    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * Get the Date this Trade was opened
     * @return the Trade's creation date
     */
    public Timestamp getDate() {
        return dateOpened;
    }

    /**
     * Get the price of each asset set for this Trade
     * @return the Trade's BUY/SELL price for a single asset
     */
    public Float getPricePerAsset() {
        return pricePerAsset;
    }

    /**
     * Get the quantity of assets being traded through this Trade
     * @return the Trade's quantity of assets being bought/sold
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Get the OrganisationalUnit that opened this Trade
     * @return the Trade's OrganisationUnit
     */
    public UUID getOrganisationalUnit() {
        return organisationalUnitId;
    }

    /**
     * Set an OpenTrades quantity. This should be used as a result of two OpenTrades being resolved, with one of
     * the OpenTrades not being completely fulfilled.
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Comparator for sorting Trades by date, earliest first.
     */
    public static Comparator<OpenTrade> tradeDateComparator = (t1, t2) -> {
        Date date1 = t1.getDate();
        Date date2 = t2.getDate();

        return date1.compareTo(date2);
    };

    @Override
    public String toString() {
        return "TradeID: " + tradeId + "\n" +
                "TradeType: " + tradeType + "\n" +
                "OrgUnitId: " + organisationalUnitId + "\n" +
                "AssetTypeId: " + assetTypeId + "\n" +
                "Quantity: " + quantity + "\n" +
                "PricePerAsset: " + pricePerAsset + "\n" +
                "DateOpened: " + dateOpened + "\n";
    }
}
