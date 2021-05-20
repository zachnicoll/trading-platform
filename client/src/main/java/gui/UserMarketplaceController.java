package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import errors.JsonError;
import helpers.ClientInfo;
import helpers.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Asset;
import models.AssetType;
import models.TradeType;
import models.partial.PartialOpenTrade;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static helpers.Client.clientGet;
import static helpers.Client.clientPost;

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
    private TradeType tradeType = null;

    private void handleOrderTypeChange(TradeType orderType) {
        // Clear combo box
        assetNames.setAll();

        // Clear displayed units available
        unitsAvailable.setText("");

        tradeType = orderType;

        if (orderType == TradeType.BUY) {
            // Change border to green to indicate BUY selected
            String aStyle = "-fx-border-color: #5DC273; -fx-background-color: #4f5d75; -fx-border-width: 5;";
            anchorboxMP.setStyle(aStyle);

            lblUnitsAvailable.setVisible(false);

        } else if (orderType == TradeType.SELL) {
            // Change border to red to indicate SELL selected
            String aStyle = "-fx-border-color: #e95d5d; -fx-background-color: #4f5d75; -fx-border-width: 5;";
            anchorboxMP.setStyle(aStyle);

            lblUnitsAvailable.setVisible(true);
        }
    }

    @FXML
    public void initialize() throws IOException, InterruptedException {
        clientInfo = ClientInfo.getInstance();
    }

    public void buySelected(ActionEvent event) throws IOException, InterruptedException {
        handleOrderTypeChange(TradeType.BUY);


        // Get all AssetTypes to display in dropdown
        HttpResponse<String> assetTypeResponse = clientGet(Route.getRoute(Route.assettype));

        // Extract AssetType array from response
        List<Asset> buyAssetTypes = new ArrayList<>();
        AssetType[] assetTypes = gson.fromJson(assetTypeResponse.body(), AssetType[].class).clone();

        // Convert AssetTypes to Assets
        for (AssetType assetType : assetTypes) {
            Asset asset = new Asset(assetType.getAssetTypeId(), -1, assetType.getAssetName());
            buyAssetTypes.add(asset);
        }


        assetNames.setAll(buyAssetTypes);

        // Set combo-box items to response data
        comboboxSelectAsset.setItems(assetNames);
    }

    public void sellSelected(ActionEvent event) throws IOException, InterruptedException {
        handleOrderTypeChange(TradeType.SELL);


        // Get all Assets that an Org Unit owns to display in dropdown
        HttpResponse<String> assetResponse = clientGet(
                Route.getRoute(Route.assets) +
                        clientInfo.currentUser.getOrganisationalUnitId()
        );

        // Extract Asset array from response
        List<Asset> sellAssetTypes = Arrays.asList(gson.fromJson(assetResponse.body(), Asset[].class).clone());

        assetNames.setAll(sellAssetTypes);

        // Set combo-box items to response data
        comboboxSelectAsset.setItems(assetNames);
    }

    public void comboBoxSelected(ActionEvent event) {
        Asset selectedAsset = comboboxSelectAsset.getValue();

        // Quantity of -1 means it's from the BUY assets, which should not display a quantity
        if (selectedAsset != null && selectedAsset.getQuantity() > -1) {
            unitsAvailable.setText(selectedAsset.getQuantity().toString());
        }
    }

    public void confirmOrder(ActionEvent event) throws IOException, InterruptedException {
        Asset selectedAsset = comboboxSelectAsset.getValue();
        Integer quantity = Integer.valueOf(txtMPQuantity.getText());
        Float price = Float.valueOf(txtMPPrice.getText());

        PartialOpenTrade newOpenTrade = new PartialOpenTrade(
                tradeType,
                clientInfo.currentUser.getOrganisationalUnitId(),
                selectedAsset.getAssetTypeId(),
                quantity,
                price
        );

        HttpResponse<String> tradeResponse = clientPost(Route.getRoute(Route.trades), newOpenTrade);

        if (tradeResponse.statusCode() == 200) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully created new Trade!", ButtonType.OK);
            alert.showAndWait();
        } else {
            JsonError responseError = gson.fromJson(tradeResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create new Trade. Error: " + responseError.getError(), ButtonType.OK);
            alert.showAndWait();
        }
    }
}