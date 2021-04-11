package models;

/**
 *
 */
public class AssetType {
    /**
     *
     */
    private String assetTypeId;

    /**
     *
     */
    private String assetName;

    /**
     *
     * @param assetTypeId
     * @param assetName
     */
    public AssetType(String assetTypeId, String assetName) {
        this.assetTypeId = assetTypeId;
        this.assetName = assetName;
    }

    /**
     *
     * @return
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     *
     * @return
     */
    public String getAssetTypeId() {
        return assetTypeId;
    }
}
