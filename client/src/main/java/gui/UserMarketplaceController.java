package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import helpers.ClientInfo;
import helpers.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Asset;
import models.AssetType;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static helpers.Client.clientGet;

public class UserMarketplaceController {


    private final Gson gson = new Gson();
    private final ObservableList<Asset> assetNames = FXCollections.observableArrayList();
    @FXML
    private AnchorPane anchorboxMP;
    @FXML
    private JFXButton btnMPBuyAsset;
    @FXML
    private JFXButton btnMPSellAsset;
    @FXML
    private JFXComboBox<Asset> comboboxSelectAsset;
    @FXML
    private TextField txtMPPrice;
    @FXML
    private TextField txtMPQuantity;
    @FXML
    private JFXButton btnMPBuyConfirmOrder;
    @FXML
    private Text lblUnitsAvailable;
    @FXML
    private Text unitsAvailable;

    private ClientInfo clientInfo;
    private List<Asset> buyAssetTypes = null;
    private List<Asset> sellAssetTypes = null;

    private void handleOrderTypeChange(String orderType) {
        // Clear combo box
        assetNames.setAll();

        // Clear displayed units available
        unitsAvailable.setText("");

        if (orderType.equals("BUY")) {
            // Change border to green to indicate BUY selected
            String aStyle = "-fx-border-color: #5DC273; -fx-background-color: #4f5d75; -fx-border-width: 5;";
            anchorboxMP.setStyle(aStyle);
        } else if (orderType.equals("SELL")) {
            // Change border to red to indicate SELL selected
            String aStyle = "-fx-border-color: #e95d5d; -fx-background-color: #4f5d75; -fx-border-width: 5;";
            anchorboxMP.setStyle(aStyle);
        }
    }

    @FXML
    public void initialize() throws IOException, InterruptedException {
        clientInfo = ClientInfo.getInstance();
    }

    public void buySelected(ActionEvent event) throws IOException, InterruptedException {
        handleOrderTypeChange("BUY");

        if (buyAssetTypes == null) {
            // Get all AssetTypes to display in dropdown
            HttpResponse<String> assetTypeResponse = clientGet(Route.getRoute(Route.assettype));

            // Extract AssetType array from response
            buyAssetTypes = new ArrayList<>();
            AssetType[] assetTypes = gson.fromJson(assetTypeResponse.body(), AssetType[].class).clone();

            // Convert AssetTypes to Assets
            for (AssetType assetType : assetTypes) {
                Asset asset = new Asset(assetType.getAssetTypeId(), -1, assetType.getAssetName());
                buyAssetTypes.add(asset);
            }
        }

        assetNames.setAll(buyAssetTypes);

        // Set combo-box items to response data
        comboboxSelectAsset.setItems(assetNames);
    }

    public void sellSelected(ActionEvent event) throws IOException, InterruptedException {
        handleOrderTypeChange("SELL");

        if (sellAssetTypes == null) {
            // Get all Assets that an Org Unit owns to display in dropdown
            HttpResponse<String> assetResponse = clientGet(
                    Route.getRoute(Route.assets) +
                            clientInfo.currentUser.getOrganisationalUnitId()
            );

            sellAssetTypes = new ArrayList<>();
            // Extract Asset array from response
            sellAssetTypes = Arrays.asList(gson.fromJson(assetResponse.body(), Asset[].class).clone());
        }

        assetNames.setAll(sellAssetTypes);

        // Set combo-box items to response data
        comboboxSelectAsset.setItems(assetNames);
    }

    public void comboBoxSelected(ActionEvent event) {
        Asset selectedAsset = comboboxSelectAsset.getValue();

        // Quantity of -1 means it's from the BUY assets, which should not display a quantity
        if (selectedAsset.getQuantity() > -1) {
            unitsAvailable.setText(selectedAsset.getQuantity().toString());
        }
    }
}