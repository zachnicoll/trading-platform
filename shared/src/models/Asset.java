package models;

/**
 * Pairs an AssetType with a quantity. Used for storing how
 * much of a given AssetType an OrganisationUnit has. This de-couples
 * directly using the AssetType class inside the OrganisationalUnit class.
 * Is the equivalent of the asset-orgunit table in the database.
 */
public class Asset {
    private AssetType assetType;
    private Integer quantity;

    public Asset (AssetType assetType, Integer quantity) {
        this.assetType = assetType;
        this.quantity = quantity;
    }

    /**
     * Get the UUID of the stored AssetType.
     * @return UUID string of associated AssetType
     */
    public String getAssetTypeId() {
        return assetType.getAssetTypeId();
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
     */
    public void subtractQuantity (Integer quantity) {
        this.quantity -= quantity;
    }

    /**
     * Directly set the quantity of the AssetType to a given amount. Used in conjunction
     * with OrganisationalUnit.updateAssetQuantity() Admin operation.
     * @see OrganisationalUnit
     * @param quantity value to set quantity to
     */
    public void setQuantity(Integer quantity) {
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
