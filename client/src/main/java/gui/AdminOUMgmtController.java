package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import errors.JsonError;
import exceptions.InvalidTransactionException;
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
import javafx.scene.text.Text;
import models.Asset;
import models.AssetType;
import models.OrganisationalUnit;
import models.partial.PartialOrganisationalUnit;

import java.io.IOException;

import java.net.http.HttpResponse;
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
    private Text lblOMOUBalance;

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
    private OrganisationalUnit currentOrg;
    private Gson gson = new Gson();
    JsonError errorResponse;
    ObservableList<OrganisationalUnit> orgNames;
    ObservableList<AssetType> assetNames;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        tblcolOMAssets.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblcolOMQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        clientInfo = ClientInfo.getInstance();

        getAllOrgsRefresh();

        getAllAssetTypes();
    }

    @FXML
    void selectedOU(ActionEvent event) throws IOException, InterruptedException {
        currentOrg = comboOUSelect.getValue();
        lblOMOUBalance.setText("$ "+ comboOUSelect.getValue().getCreditBalance().toString());
        refreshTable(currentOrg.getUnitId());

    }

    private Asset[] getAllAsset(UUID orgUnit) throws IOException, InterruptedException {
        HttpResponse<String> assetResponse = Client.clientGet("/assets/"+ orgUnit);
        return gson.fromJson(assetResponse.body(), Asset[].class);
    }

    private void getAllOrgsRefresh() throws IOException, InterruptedException {
        HttpResponse<String> orgResponse = Client.clientGet(Route.getRoute(Route.orgunit));

        if (orgResponse.statusCode() == 200) {
            orgNames = FXCollections.observableArrayList();
            orgNames.addAll(gson.fromJson(orgResponse.body(), OrganisationalUnit[].class));
            comboOUSelect.setItems(orgNames);

        } else {
            errorResponse = gson.fromJson(orgResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load all Organisations." + errorResponse.getError());
            alert.showAndWait();
        }
    }

    private void getAllAssetTypes() throws IOException, InterruptedException {
        HttpResponse<String> assetTypesResponse = Client.clientGet(Route.getRoute(Route.assettype));
        gson.fromJson(assetTypesResponse.body(), AssetType[].class);

        if (assetTypesResponse.statusCode() == 200) {

            assetNames = FXCollections.observableArrayList();
            assetNames.addAll(gson.fromJson(assetTypesResponse.body(), AssetType[].class));
            comboOMAssetAdd.setItems(assetNames);

        } else {
            errorResponse = gson.fromJson(assetTypesResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load all Asset Types." + errorResponse.getError());
            alert.showAndWait();
        }
    }

    private void refreshTable(UUID orgUnit) throws IOException, InterruptedException {
        tblOM.getItems().clear();
        tableData = FXCollections.observableArrayList();
        tableData.setAll(getAllAsset(orgUnit));
        tblOM.setItems(tableData);

    }

    private void resetAll() throws IOException, InterruptedException {
        tblOM.getItems().clear();
        comboOUSelect.getSelectionModel().clearSelection();
        comboOMAssetAdd.getSelectionModel().clearSelection();
        txtNewOUName.clear();
        txtNewOUBalance.clear();
        txtOUExistingBalance.clear();
        txtOUNewAssetQuantity.clear();
        lblOMOUBalance.setText("");
        currentOrg = null;
        getAllOrgsRefresh();
        getAllAssetTypes();
    }

    private void softReset() throws IOException, InterruptedException {
        txtNewOUName.clear();
        txtNewOUBalance.clear();
        txtOUExistingBalance.clear();
        txtOUNewAssetQuantity.clear();
    }

    @FXML
    private void handleChangeBal() throws IOException, InterruptedException, InvalidTransactionException {

        Float newBal = Float.parseFloat(txtOUExistingBalance.getText());

        PartialOrganisationalUnit tempOrg = new PartialOrganisationalUnit(newBal);
        HttpResponse<String> changeBalResponse = Client.clientPut("/orgunit/"+ currentOrg.getUnitId(), tempOrg);


        if (changeBalResponse.statusCode() == 200) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully updated the balance of selected Organisation.");
            alert.showAndWait();

            // Re-fetch AssetTypes and set table data

        } else {
            errorResponse = gson.fromJson(changeBalResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not update the balance of selected Organisation." + errorResponse.getError());
            alert.showAndWait();
            // Re-fetch AssetTypes and set table data
        }
        resetAll();
    }

    @FXML
    private void handleDeleteAsset() throws IOException, InterruptedException {

        if (tblOM.getSelectionModel().getSelectedItem() != null) {
            Asset toDelete = tblOM.getSelectionModel().getSelectedItem();

            HttpResponse<String> deleteResponse = Client.clientDelete("/assets/" + currentOrg.getUnitId() + "/" + toDelete.getAssetTypeId());


            if (deleteResponse.statusCode() == 200) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully deleted Asset from the selected Organisation.");
                alert.showAndWait();

                // Re-fetch AssetTypes and set table data

            } else {
                errorResponse = gson.fromJson(deleteResponse.body(), JsonError.class);
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete Asset from the Organisation." + errorResponse.getError());
                alert.showAndWait();
                // Re-fetch AssetTypes and set table data
            }
            softReset();
            refreshTable(currentOrg.getUnitId());
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an asset before continuing.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteOrg() throws IOException, InterruptedException {

        if (currentOrg != null) {
            Alert check = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the Organisation and all of its dependencies");
            check.showAndWait();
            if (check.getResult() == ButtonType.OK) {
                HttpResponse<String> deleteResponse = Client.clientDelete("/orgunit/" + currentOrg.getUnitId());

                if (deleteResponse.statusCode() == 200) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully deleted the Organisation.");
                    alert.showAndWait();
                    // Re-fetch AssetTypes and set table data

                } else {
                    errorResponse = gson.fromJson(deleteResponse.body(), JsonError.class);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete the Organisation." + errorResponse.getError());
                    alert.showAndWait();
                    // Re-fetch AssetTypes and set table data
                }
                resetAll();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an organisation before continuing.");
            alert.showAndWait();

        }
    }

    @FXML
    private void addAsset() throws IOException, InterruptedException {


        int quantity = Integer.valueOf(txtOUNewAssetQuantity.getText());

        Asset asset = new Asset(comboOMAssetAdd.getValue().getAssetTypeId(), quantity);

        HttpResponse<String> putResponse = Client.clientPut("/assets/"+ currentOrg.getUnitId(), asset);

        if (putResponse.statusCode() == 200) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully added asset to the selected Organisation.");
            alert.showAndWait();

            // Re-fetch AssetTypes and set table data

        } else {
            errorResponse = gson.fromJson(putResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not add Asset to the selected Organisation." + errorResponse.getError());
        }
        softReset();
        refreshTable(currentOrg.getUnitId());


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


        } else {
            errorResponse = gson.fromJson(postResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not add new Organisation." + errorResponse.getError());
            alert.showAndWait();
        }
        resetAll();

    }
}