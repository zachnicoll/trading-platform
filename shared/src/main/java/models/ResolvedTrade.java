package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class ResolvedTrade {
    private final UUID buyTradeId;
    private final UUID buyOrgUnitId;
    private final UUID sellTradeId;
    private final UUID sellOrgUnitId;
    private final Integer quantity;
    private final Float price;
    private final Timestamp dateResolved;
    private final UUID assetTypeId;

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

    public UUID getBuyTradeId() {
        return buyTradeId;
    }

    public UUID getBuyOrgUnitId() {
        return buyOrgUnitId;
    }

    public UUID getSellTradeId() {
        return sellTradeId;
    }

    public UUID getSellOrgUnitId() {
        return sellOrgUnitId;
    }

    public Float getPrice() {
        return price;
    }

    public Timestamp getDateResolved() {
        return dateResolved;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public UUID getAssetTypeId() {
        return assetTypeId;
    }
}
