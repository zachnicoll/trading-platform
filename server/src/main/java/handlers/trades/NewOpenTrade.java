package handlers.trades;

import models.TradeType;

import java.sql.Timestamp;
import java.util.UUID;

public class NewOpenTrade {
        public final TradeType tradeType;
        public final UUID organisationalUnitId;
        public final UUID assetTypeId;
        public final Integer quantity;
        public final Float pricePerAsset;

        public NewOpenTrade (TradeType tradeType, UUID organisationalUnitId, UUID assetTypeId, Integer quantity, Float pricePerAsset) {
            this.tradeType = tradeType;
            this.organisationalUnitId = organisationalUnitId;
            this.assetTypeId = assetTypeId;
            this.quantity = quantity;
            this.pricePerAsset = pricePerAsset;
        }
}
