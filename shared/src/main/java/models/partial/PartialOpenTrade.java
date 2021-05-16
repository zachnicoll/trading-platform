package models.partial;

import models.TradeType;
import java.util.UUID;

/**
 * Class similar to the OpenTrade class, but contains no methods and all attributes
 * are publicly accessible. This class is used when creating a new OpenTrade,
 * as only certain attributes need to be sent to the Rest API. For example,
 * the TradeId is generated on the Server and therefore should not be sent
 * with a POST request to the /trades/ endpoint.
 */
public class PartialOpenTrade {
        public final TradeType tradeType;
        public final UUID organisationalUnitId;
        public final UUID assetTypeId;
        public final Integer quantity;
        public final Float pricePerAsset;

        public PartialOpenTrade (TradeType tradeType, UUID organisationalUnitId, UUID assetTypeId, Integer quantity, Float pricePerAsset) {
            this.tradeType = tradeType;
            this.organisationalUnitId = organisationalUnitId;
            this.assetTypeId = assetTypeId;
            this.quantity = quantity;
            this.pricePerAsset = pricePerAsset;
        }
}
