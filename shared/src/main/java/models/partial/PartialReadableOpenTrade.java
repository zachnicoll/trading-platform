package models.partial;

import models.TradeType;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
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
    private final UUID organisationalUnitId;

    public PartialReadableOpenTrade (UUID tradeId, TradeType tradeType, String organisationalUnitName, String assetTypeName, Integer quantity, Float pricePerAsset, Timestamp dateOpened, UUID organisationalUnitId) {
        this.tradeId = tradeId;
        this.tradeType = tradeType;
        this.organisationalUnitName = organisationalUnitName;
        this.assetTypeName = assetTypeName;
        this.quantity = quantity;
        this.pricePerAsset = pricePerAsset;
        this.dateOpened = dateOpened;
        this.organisationalUnitId = organisationalUnitId;
    }


    public String getPricePerAsset() {
        return NumberFormat.getCurrencyInstance(Locale.US).format(pricePerAsset);
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

    public String getDateOpened() { return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateOpened); }

    public String getTradeType() {
        return tradeType.name();
    }

    public UUID getTradeId() {
        return tradeId;
    }

    public UUID getOrganisationalUnitId() {
        return organisationalUnitId;
    }
}