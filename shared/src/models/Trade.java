package models;

import java.util.Date;
import java.util.UUID;

/**
 * Class for storing and retrieving a Trade's information.
 */
public class Trade {

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
    private AssetType assetType;

    /**
     * Quantity of AssetType to buy or sell
     */
    private Integer quantity;

    /**
     * Price for a single asset of type AssetType
     */
    private Float pricePerAsset;

    /**
     * Date that the trade order was opened
     */
    private Date date;

    /**
     * Construct a new User with given information, most likely provided by the API.
     * @param tradeId Unique UUID identifier for each trade
     * @param tradeType whether the trade is a BUY or SELL order
     * @param organisationalUnitId the OrganisationalUnit that placed the Trade order
     * @param assetType the AssetType that is being bought/sold
     * @param quantity quantity of AssetType to buy or sell
     * @param pricePerAsset price for a single asset of type AssetType
     * @param date date that the trade order was opened
     */
    public Trade (UUID tradeId, TradeType tradeType, UUID organisationalUnitId, AssetType assetType, Integer quantity, Float pricePerAsset, Date date) {
        this.tradeId = tradeId;
        this.tradeType = tradeType;
        this.organisationalUnitId = organisationalUnitId;
        this.assetType = assetType;
        this.quantity = quantity;
        this.pricePerAsset = pricePerAsset;
        this.date = date;
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
    public AssetType getAssetType() {
        return assetType;
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
    public Date getDate() {
        return date;
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
}
