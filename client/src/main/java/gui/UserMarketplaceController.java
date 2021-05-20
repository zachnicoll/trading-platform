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

import static helpers.Client.clientGet;

public class UserMarketplaceController {


    private final Gson gson = new Gson();
    @FXML
    private AnchorPane anchorboxMP;
    @FXML
    private JFXButton btnMPBuyAsset;
    @FXML
    private JFXButton btnMPSellAsset;
    @FXML
    private JFXComboBox<AssetType> comboboxSelectAsset;
    @FXML
    private TextField txtMPPrice;
    @FXML
    private TextField txtMPQuantity;
    @FXML
    private JFXButton btnMPBuyConfirmOrder;
    @FXML
    private Text lblUnitsAvailable;
    private ClientInfo clientInfo;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        clientInfo = ClientInfo.getInstance();
    }

    public void buySelected(ActionEvent event) throws IOException, InterruptedException {
        // Change border to green to indicate BUY selected
        String aStyle = "-fx-border-color: #5DC273; -fx-background-color: #4f5d75; -fx-border-width: 5;";
        anchorboxMP.setStyle(aStyle);

        ObservableList<AssetType> assetNames = FXCollections.observableArrayList();

        // Get all AssetTypes to display in dropdown
        HttpResponse<String> assetTypeResponse = clientGet(Route.getRoute(Route.assettype));

        // Extract AssetType array from response
        AssetType[] allAssetTypes = gson.fromJson(assetTypeResponse.body(), AssetType[].class).clone();
        assetNames.addAll(allAssetTypes);

        // Set combo-box items to response data
        comboboxSelectAsset.setItems(assetNames);
    }

    public void sellSelected(ActionEvent event) throws IOException, InterruptedException {
        // Change border to red to indicate SELL selected
        String aStyle = "-fx-border-color: #e95d5d; -fx-background-color: #4f5d75; -fx-border-width: 5;";
        anchorboxMP.setStyle(aStyle);

        ObservableList<AssetType> assetNames = FXCollections.observableArrayList();

        // Get all Assets that an Org Unit owns to display in dropdown
        HttpResponse<String> assetResponse = clientGet(
                Route.getRoute(Route.assets) +
                        clientInfo.currentUser.getOrganisationalUnitId()
        );

        // Extract Asset array from response
        Asset[] orgAssets = gson.fromJson(assetResponse.body(), Asset[].class).clone();
        for (Asset asset : orgAssets) {
            // Convert to AssetType
            AssetType assetType = new AssetType(asset.getAssetTypeId(), asset.getName());
            assetNames.add(assetType);
        }

        // Set combo-box items to response data
        comboboxSelectAsset.setItems(assetNames);
    }

}