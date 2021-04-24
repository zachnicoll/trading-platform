package models;

/**
 * Class for storing the UUID and Name of an AssetType. Quantity
 * of the AssetType is NOT stored here.
 * @see Asset for pairing with a quantity
 */
public class AssetType {
    /**
     * UUID String identifying the AssetType.
     * Unique.
     */
    private String assetTypeId;

    /**
     * Name displayed publicly to identify AssetType.
     * Unique.
     */
    private String assetName;

    /**
     * Construct a new AssetType with given unique identifier and display name.
     * @param assetTypeId Unique identifier for AssetType
     * @param assetName Displayed name of the AssetType
     */
    public AssetType(String assetTypeId, String assetName) {
        this.assetTypeId = assetTypeId;
        this.assetName = assetName;
    }

    /**
     * Get the AssetType's name for displaying.
     * @return the AssetType's name
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     *  Get the AssetType's unique identifier.
     * @return the AssetType's UUID
     */
    public String getAssetTypeId() {
        return assetTypeId;
    }
}
