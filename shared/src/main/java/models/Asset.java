package models;

import java.util.UUID;

/**
 * Pairs an AssetType with a given quantity. Used for storing how
 * much of a given AssetType an OrganisationUnit has. This de-couples
 * directly using the AssetType class inside the OrganisationalUnit class.
 * Is the equivalent of the asset-orgunit table in the database.
 */
public class Asset {
    private final UUID assetTypeId;
    private Integer quantity;

    public Asset (UUID assetTypeId, Integer quantity) {
        this.assetTypeId = assetTypeId;
        this.quantity = quantity;
    }

    /**
     * Get the UUID of the stored AssetType.
     * @return UUID string of associated AssetType
     */
    public UUID getAssetTypeId() {
        return assetTypeId;
    }

    /**
     * Increase the quantity of the AssetType by a given amount. Used in conjunction
     * with BUY orders.
     * @param quantity value to increase quantity by
     */
    public void addQuantity (Integer quantity) {
        this.quantity += quantity;
    }

    /**
     * Decrease the quantity of the AssetType by a given amount. Used in conjunction
     * with SELL orders.
     * @param quantity value to decrease quantity by
     * @throws ArithmeticException if resulting quantity would be less than 0
     */
    public void subtractQuantity (Integer quantity) throws ArithmeticException {
        if (this.quantity - quantity < 0) {
            throw new ArithmeticException();
        }
        this.quantity -= quantity;
    }

    /**
     * Directly set the quantity of the AssetType to a given amount. Used in conjunction
     * with OrganisationalUnit.updateAssetQuantity() Admin operation.
     * @see OrganisationalUnit
     * @param quantity value to set quantity to
     * @throws ArithmeticException if quantity is less than 0
     */
    public void setQuantity(Integer quantity) throws ArithmeticException {
        if (quantity < 0) {
            throw new ArithmeticException();
        }
        this.quantity = quantity;
    }

    /**
     * Get the current quantity of the AssetType.
     * @return quantity of AssetType
     */
    public Integer getQuantity() {
        return quantity;
    }
}
