package models.partial;

import models.TradeType;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Class similar to the OpenTrade class, but all attributes
 * are in human readable form, ie String assetTypeName rather
 * than UUID assetTypeId. This class is used when creating a new OpenTrade to be
 * sent to the client and displayed on a table and read by a user.
 */
public class PartialReadableOpenTrade {
    private final UUID tradeId;
    private final TradeType tradeType;
    private final String organisationalUnitName;
    private final String assetTypeName;
    private final Integer quantity;
    private final Float pricePerAsset;
    private final Timestamp dateOpened;

    public PartialReadableOpenTrade (UUID tradeId, TradeType tradeType, String organisationalUnitName, String assetTypeName, Integer quantity, Float pricePerAsset, Timestamp dateOpened) {
        this.tradeId = tradeId;
        this.tradeType = tradeType;
        this.organisationalUnitName = organisationalUnitName;
        this.assetTypeName = assetTypeName;
        this.quantity = quantity;
        this.pricePerAsset = pricePerAsset;
        this.dateOpened = dateOpened;
    }


    public Float getPricePerAsset() {
        return pricePerAsset;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getAssetTypeName() {
        return assetTypeName;
    }

    public String getOrganisationalUnitName() {
        return organisationalUnitName;
    }

    public Timestamp getDateOpened() {
        return dateOpened;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public UUID getTradeId() {
        return tradeId;
    }


}