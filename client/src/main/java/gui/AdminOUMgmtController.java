package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import errors.JsonError;
import helpers.Client;
import helpers.ClientInfo;
import helpers.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.util.Callback;
import models.Asset;
import models.AssetType;
import models.OrganisationalUnit;
import models.partial.PartialOrganisationalUnit;

import java.io.IOException;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminOUMgmtController {

    ObservableList<Asset> tableData;

    @FXML
    private TextField txtNewOUName;

    @FXML
    private TextField txtNewOUBalance;

    @FXML
    private JFXButton btnAddOU;

    @FXML
    private JFXButton btnOMAssetAdd;

    @FXML
    private JFXComboBox<OrganisationalUnit> comboOUSelect;

    @FXML
    private TextField txtOUExistingBalance;

    @FXML
    private JFXButton btnOMDeleteOU;

    @FXML
    private JFXComboBox<AssetType> comboOMAssetAdd;

    @FXML
    private TextField txtOUNewAssetQuantity;

    @FXML
    private TableView<Asset> tblOM;

    @FXML
    private TableColumn<?, ?> tblcolOMAssets;

    @FXML
    private TableColumn<?, ?> tblcolOMQuantity;


    private ClientInfo clientInfo;
    private UUID currentOrg;
    private Gson gson = new Gson();
    JsonError errorResponse;
    ObservableList<OrganisationalUnit> orgNames;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        tblcolOMAssets.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblcolOMQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        clientInfo = ClientInfo.getInstance();

        getAllOrgsRefresh();

        AssetType[] allAssetTypes = getAllAssetTypes();

        //Initialize asset combobox
        ObservableList<AssetType> assetNames = FXCollections.observableArrayList();


        for(AssetType anAsset:allAssetTypes)
        {
            assetNames.add(anAsset);
        }

        comboOMAssetAdd.setItems(assetNames);
    }

    @FXML
    void selectedOU(ActionEvent event) throws IOException, InterruptedException {
        currentOrg = comboOUSelect.getValue().getUnitId();
        refreshTable(currentOrg);
    }

    private Asset[] getAllAsset(UUID orgUnit) throws IOException, InterruptedException {
        HttpResponse<String> assetResponse = Client.clientGet("/assets/"+ orgUnit);
        return gson.fromJson(assetResponse.body(), Asset[].class);
    }

    private void getAllOrgsRefresh() throws IOException, InterruptedException {
        HttpResponse<String> orgResponse = Client.clientGet(Route.getRoute(Route.orgunit));

        if (orgResponse.statusCode() == 200) {
            OrganisationalUnit[] tempOrgs = gson.fromJson(orgResponse.body(), OrganisationalUnit[].class);

            orgNames = FXCollections.observableArrayList();

            for(OrganisationalUnit anOrg:tempOrgs)
            {
                orgNames.add(anOrg);
            }

            comboOUSelect.setItems(orgNames);

        } else {
            errorResponse = gson.fromJson(orgResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not all Organisations." + errorResponse.getError());
            alert.showAndWait();
        }
    }

    private AssetType[] getAllAssetTypes() throws IOException, InterruptedException {
        HttpResponse<String> assetTypesResponse = Client.clientGet(Route.getRoute(Route.assettype));
        return gson.fromJson(assetTypesResponse.body(), AssetType[].class);
    }

    private void refreshTable(UUID orgUnit) throws IOException, InterruptedException {
        tblOM.getItems().clear();
        tableData = FXCollections.observableArrayList();
        tableData.setAll(getAllAsset(orgUnit));
        tblOM.setItems(tableData);
    }


    @FXML
    private void handleDeleteAsset() throws IOException, InterruptedException {

        Asset toDelete = tblOM.getSelectionModel().getSelectedItem();

        HttpResponse<String> deleteResponse = Client.clientDelete("/assets/"+ currentOrg + "/" + toDelete.getAssetTypeId());


        if (deleteResponse.statusCode() == 200) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully deleted Asset from the selected Organisation.");
            alert.showAndWait();

            // Re-fetch AssetTypes and set table data
            refreshTable(currentOrg);

        } else {
            errorResponse = gson.fromJson(deleteResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete Asset from the Organisation." + errorResponse.getError());
            alert.showAndWait();
            // Re-fetch AssetTypes and set table data
            refreshTable(currentOrg);
        }
    }

    @FXML
    private void addAsset() throws IOException, InterruptedException {


        int quantity = Integer.valueOf(txtOUNewAssetQuantity.getText());

        Asset asset = new Asset(comboOMAssetAdd.getValue().getAssetTypeId(), quantity);

        HttpResponse<String> putResponse = Client.clientPut("/assets/"+ currentOrg, asset);

        if (putResponse.statusCode() == 200) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully added asset to the selected Organisation.");
            alert.showAndWait();

            // Re-fetch AssetTypes and set table data
            refreshTable(currentOrg);

        } else {
            errorResponse = gson.fromJson(putResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not add Asset to the selected Organisation." + errorResponse.getError());
            alert.showAndWait();
        }

    }


    @FXML
    private void addOU() throws IOException, InterruptedException {


        String newOrgName = txtNewOUName.getText();
        Float newOrgBalance = Float.valueOf(txtNewOUBalance.getText());

        PartialOrganisationalUnit newOrg = new PartialOrganisationalUnit(newOrgName, newOrgBalance);


        HttpResponse<String> postResponse = Client.clientPost(Route.getRoute(Route.orgunit), newOrg);


        if (postResponse.statusCode() == 200) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully added new Organisation.");
            alert.showAndWait();

            // Re-fetch AssetTypes and set table data
            refreshTable(currentOrg);

        } else {
            errorResponse = gson.fromJson(postResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not add new Organisation." + errorResponse.getError());
            alert.showAndWait();
        }

    }
}