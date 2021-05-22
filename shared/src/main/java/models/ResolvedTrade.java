package models;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Represents trade information stored in the resolvedTrades table in the database.
 * It details the Organisational Units involved in the resolution of an OpenTrade e.g. the one
 * that placed the BUY trade, and the one that placed the SELL trade. The resolved price, quantity,
 * and date are also stored here for viewing and analysing trade history data over time.
 */
public class ResolvedTrade {
    /**
     * UUID of the BUY OpenTrade in the pair of trades that created this resolution
     */
    private final UUID buyTradeId;

    /**
     * UUID of the SELL OpenTrade in the pair of trades that created this resolution
     */
    private final UUID sellTradeId;

    /**
     * UUID of the OrganisationalUnit that placed the BUY OpenTrade
     */
    private final UUID buyOrgUnitId;

    /**
     * UUID of the OrganisationalUnit that placed the SELL OpenTrade
     */
    private final UUID sellOrgUnitId;

    /**
     * UUID of the AssetType that was exchanged in this trade
     */
    private final UUID assetTypeId;

    /**
     * Quantity of AssetType that was transferred
     */
    private final Integer quantity;

    /**
     * Price the trades were resolved at
     */
    private final Float price;

    /**
     * Timestamp the trades were resolved at
     */
    private final Timestamp dateResolved;

    /**
     * Construct a new ResolvedTrade with given information, most likely provided by the API.
     */
    public ResolvedTrade (UUID buyTradeId, UUID sellTradeId, UUID buyOrgUnitId, UUID sellOrgUnitId, UUID assetTypeId, Integer quantity, Float price, Timestamp dateResolved){
        this.buyTradeId = buyTradeId;
        this.buyOrgUnitId = buyOrgUnitId;
        this.sellTradeId = sellTradeId;
        this.sellOrgUnitId = sellOrgUnitId;
        this.assetTypeId = assetTypeId;
        this.quantity = quantity;
        this.price = price;
        this.dateResolved = dateResolved;
    }

    /**
     * Retrieve UUID of BUY OpenTrade in the pair of trades that created this resolution
     */
    public UUID getBuyTradeId() {
        return buyTradeId;
    }

    /**
     * Retrieve UUID of the SELL OpenTrade in the pair of trades that created this resolution
     */
    public UUID getSellTradeId() {
        return sellTradeId;
    }

    /**
     * Retrieve UUID of OrganisationalUnit that placed the BUY OpenTrade
     */
    public UUID getBuyOrgUnitId() {
        return buyOrgUnitId;
    }

    /**
     * Retrieve UUID of OrganisationalUnit that placed the SELL OpenTrade
     */
    public UUID getSellOrgUnitId() {
        return sellOrgUnitId;
    }

    /**
     * Retrieve the price that the trade was resolved at
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Retrieve the Timestamp the trade was resolved at
     */
    public Timestamp getDateResolved() {
        return dateResolved;
    }

    /**
     * Retrieve the quantity of the AssetType that was exchanged
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Retrieve the UUID of the AssetType that was exchanged
     */
    public UUID getAssetTypeId() {
        return assetTypeId;
    }
}
