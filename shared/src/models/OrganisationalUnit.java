package models;

import exceptions.ApiException;
import exceptions.InvalidTransactionException;

import java.util.List;
import java.util.UUID;

/**
 * Class for storing and manipulating Organisational Unit information locally and on the server.
 */
public class OrganisationalUnit {
    /**
     * UUID String identifying the OrganisationalUnit.
     * Unique.
     */
    private final UUID unitId;

    /**
     * Name displayed publicly to identify OrganisationUnit.
     * Unique.
     */
    private String unitName;

    /**
     * Current credit balance of the Organisational Unit.
     */
    private Float creditBalance;

    /**
     * List of Assets the Organisational Unit owns.
     * @see Asset
     */
    private List<Asset> assets;

    /**
     * Construct a new OrganisationalUnit with given information, most likely provided by the API.
     * @param unitId Unique identifier for Organisational Unit
     * @param unitName Displayed name of the Organisational Unit
     * @param creditBalance Current balance of the Organisational Unit
     * @param assets List of Assets the Organisational Unit owns
     */
    public OrganisationalUnit(UUID unitId, String unitName, Float creditBalance, List<Asset> assets) {
        this.unitId = unitId;
        this.unitName = unitName;
        this.creditBalance = creditBalance;
        this.assets = assets;
    }

    /**
     * Determine if this OU already owns an AssetType that matches the given AssetType ID.
     * @param assetType AssetType to match in the list of Assets
     * @return The matching Asset if it already exists, null if not
     */
    private Asset findExistingAsset(AssetType assetType) {
        for (Asset asset : assets) {
            if (asset.getAssetTypeId() == assetType.getAssetTypeId()) {
                return asset;
            }
        }
        return null;
    }

    /**
     * Update the Organisational Unit's current balance. Operation can only be
     * performed by an Admin. Calls the /org-unit/[ord-id]/balance PUT endpoint.
     * @param newBalance New credit balance to set
     * @throws ApiException if the API request fails while updating
     */
    public void updateCreditBalance(Float newBalance) throws ApiException {
        /**
         * Update OU credit balance on API, throw APIException if fails
         */

        creditBalance = newBalance;
    }

    /**
     * Update the Organisational Unit's quantity of a given AssetType. Operation can
     * only be performed by an Admin. If the AssetType specified is not currently owned
     * by the OU, the AssetType is added to the OU's list of assets.
     * Calls the /org-unit/[orgId]/quantity/ PUT endpoint.
     * @param assetType AssetType to be updated
     * @param newQuantity New quantity of the given asset to be set
     * @throws ApiException
     */
    public void updateAssetQuantity(AssetType assetType, Integer newQuantity) throws ApiException {
        /**
         * Make API request to update this OU's quantity of the given assetType
         */

        // Potentially use assets = responseObject.assets to easily update this

        Asset existingAsset = findExistingAsset(assetType);
        if (existingAsset != null) {
            existingAsset.setQuantity(newQuantity);
        } else {
            assets.add(new Asset(assetType.getAssetTypeId(), newQuantity));
        }
    }

    /**
     * Method to be called after a BUY order has been resolved for this OU. The total
     * price of the trade (pricePerAsset * quantity) is deducted from the OU's credit balance,
     * and that quantity of asset is added to OU.
     * @param pricePerAsset Credit price for a single unit of the given AssetType
     * @param assetType AssetType to be purchased
     * @param quantity Quantity of asset to be purchased
     * @throws ApiException thrown if API request fails
     * @throws InvalidTransactionException thrown if the total price of the purchase exceeds the OU's credit balance
     */
    public void purchaseAsset(Float pricePerAsset, AssetType assetType, Integer quantity) throws ApiException, InvalidTransactionException {
        final Float totalPrice = pricePerAsset * quantity;
        if (totalPrice > creditBalance) {
            throw new InvalidTransactionException();
        }

        /**
         * Make API request to update OU asset quantity, and reduce creditBalance.
         * Throw ApiException if request fails.
         */

        creditBalance -= totalPrice;
        Asset existingAsset = findExistingAsset(assetType);
        if (existingAsset != null) {
            existingAsset.addQuantity(quantity);
        } else {
            assets.add(new Asset(assetType.getAssetTypeId(), quantity));
        }
    }

    /**
     * Method to be called after a SELL order has been resolved for this OU. The total
     * price of the trade (pricePerAsset * quantity) is added from the OU's credit balance,
     * and that quantity of asset is subtracted from the OU.
     * @param pricePerAsset Credit price for a single unit of the given AssetType
     * @param assetType AssetType to be sold
     * @param quantity Quantity of asset to be sold
     * @throws ApiException thrown if API request fails
     * @throws InvalidTransactionException thrown if the quantity of asset to be sold exceeds the OU's quantity of the asset
     */
    public void sellAsset(Float pricePerAsset, AssetType assetType, Integer quantity) throws ApiException, InvalidTransactionException {
        final Float totalPrice = pricePerAsset * quantity;
        Asset existingAsset = findExistingAsset(assetType);

        if (existingAsset == null || existingAsset.getQuantity() < quantity) {
            throw new InvalidTransactionException();
        }

        /**
         * Make API request to update OU asset quantity, and reduce creditBalance.
         * Throw ApiException if request fails.
         */

        creditBalance += totalPrice;
        existingAsset.subtractQuantity(quantity);
        if (existingAsset.getQuantity() == 0) {
            assets.remove(existingAsset);
        }
    }
}
