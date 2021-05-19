package gui;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import com.jfoenix.controls.JFXComboBox;
import helpers.ClientInfo;
import models.Asset;
import models.AssetType;
import models.OrganisationalUnit;
import models.User;

import static helpers.Client.clientGet;

public class UserMarketplaceController {


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

    private List<Asset> orgAssets;

    private Gson gson = new Gson();

    private ClientInfo clientInfo;

    @FXML
    public void initialize() throws IOException, InterruptedException {


        clientInfo = ClientInfo.getInstance();

    }

    public void buySelected(ActionEvent event) throws IOException, InterruptedException {
        String aStyle = String.format("-fx-border-color: #5DC273; -fx-background-color: #4f5d75; -fx-border-width: 5;");
        anchorboxMP.setStyle(aStyle);


    }

    public void sellSelected(ActionEvent event) throws IOException, InterruptedException {
        String aStyle = String.format("-fx-border-color: #e95d5d; -fx-background-color: #4f5d75; -fx-border-width: 5;");
        anchorboxMP.setStyle(aStyle);

        ObservableList<Asset> assetNames = FXCollections.observableArrayList();

        HttpResponse<String> assetResponse = clientGet("/assets/"+clientInfo.currentUser.getOrganisationalUnitId(), clientInfo.authToken);

        orgAssets = Arrays.asList(gson.fromJson(assetResponse.body(), Asset[].class).clone());


        for(Asset anAsset:orgAssets)
        {
            assetNames.add(anAsset);
        }

        comboboxSelectAsset.setItems(assetNames);
    }

}