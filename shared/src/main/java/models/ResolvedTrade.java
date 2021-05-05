package models;

import java.util.Date;
import java.util.UUID;

public class ResolvedTrade {
    private final UUID buyTradeId;
    private final UUID buyOrgUnitId;
    private final UUID sellTradeId;
    private final UUID sellOrgUnitId;
    private final Integer quantity;
    private final Float price;
    private final Date dateResolved;

    public ResolvedTrade (UUID buyTradeId, UUID buyOrgUnitId, UUID sellTradeId, UUID sellOrgUnitId, Integer quantity, Float price, Date dateResolved){
        this.buyTradeId = buyTradeId;
        this.buyOrgUnitId = buyOrgUnitId;
        this.sellTradeId = sellTradeId;
        this.sellOrgUnitId = sellOrgUnitId;
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

    public Date getDateResolved() {
        return dateResolved;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
