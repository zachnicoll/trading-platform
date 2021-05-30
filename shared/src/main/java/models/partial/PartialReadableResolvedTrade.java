package models.partial;

import models.TradeType;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Class similar to the ResolvedTrade class, but all attributes
 * are in human readable form, ie String assetTypeName rather
 * than UUID assetTypeId. This class is used when creating a new ResolvedTrade to be
 * sent to the client and displayed on a table and read by a user.
 */
public class PartialReadableResolvedTrade {
    private final UUID buyTradeId;
    private final UUID sellTradeId;
    private final String assetTypeName;
    private final Integer quantity;
    private final Float price;
    private final Timestamp dateResolved;

    public PartialReadableResolvedTrade (UUID buyTradeId, UUID sellTradeId, String assetTypeName, Integer quantity, Float price, Timestamp dateResolved) {
        this.assetTypeName = assetTypeName;
        this.quantity = quantity;
        this.price = price;
        this.dateResolved = dateResolved;
        this.buyTradeId = buyTradeId;
        this.sellTradeId = sellTradeId;
    }


    public Float getPrice() { return price; }

    public Integer getQuantity() {
        return quantity;
    }

    public String getAssetTypeName() {
        return assetTypeName;
    }

    public Timestamp getDateResolved() { return dateResolved; }

    public UUID getBuyTradeId() {
        return buyTradeId;
    }

    public UUID getSellTradeId() { return sellTradeId; }

}