package models;

import java.util.Date;

/**
 * Class for storing and retrieving a Trade's information.
 */
public class Trade {
    /**
     * Whether the Trade is a BUY or SELL order
     */
    private TradeType tradeType;

    /**
     * The OrganisationalUnit that placed the Trade order
     */
    private OrganisationalUnit organisationalUnit;

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
    private Integer pricePerAsset;

    /**
     * Date that the trade order was opened
     */
    private Date date;

    /**
     * Construct a new User with given information, most likely provided by the API.
     * @param tradeType whether the trade is a BUY or SELL order
     * @param organisationalUnit the OrganisationalUnit that placed the Trade order
     * @param assetType the AssetType that is being bought/sold
     * @param quantity quantity of AssetType to buy or sell
     * @param pricePerAsset price for a single asset of type AssetType
     * @param date date that the trade order was opened
     */
    public Trade (TradeType tradeType, OrganisationalUnit organisationalUnit, AssetType assetType, Integer quantity, Integer pricePerAsset, Date date) {
        this.tradeType = tradeType;
        this.organisationalUnit = organisationalUnit;
        this.assetType = assetType;
        this.quantity = quantity;
        this.pricePerAsset = pricePerAsset;
        this.date = date;
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
    public Integer getPricePerAsset() {
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
    public OrganisationalUnit getOrganisationalUnit() {
        return organisationalUnit;
    }
}
