package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.sun.glass.ui.CommonDialogs;
import helpers.Client;
import helpers.ClientInfo;
import helpers.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.util.Callback;
import models.Asset;
import models.AssetType;
import models.OrganisationalUnit;

import java.io.File;
import java.io.IOException;

import java.awt.*;
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

    @FXML
    private TableColumn<?, ?> tblcolOMDelete;

    private ClientInfo clientInfo;
    private Gson gson = new Gson();

    @FXML
    public void initialize()
    {
        tblcolOMAssets.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblcolOMQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        clientInfo = ClientInfo.getInstance();

        //refreshTable();
        //addDeleteButtonsToTable();
        UUID tempa = clientInfo.getCurrentUser().getOrganisationalUnitId();



        //Initialize org combobox
        ObservableList<OrganisationalUnit> orgNames = FXCollections.observableArrayList();

        //TODO GET ORG LIST FROM DATABASE
        List<OrganisationalUnit> temp = new ArrayList<OrganisationalUnit>();
        temp.add(new OrganisationalUnit(tempa, "testunit", 10000f, new ArrayList<Asset>()));
        //temp.add(new OrganisationalUnit(UUID. randomUUID(), "testunit1234", 10000f, new ArrayList<Asset>()));
        //temp.add(new OrganisationalUnit(UUID. randomUUID(), "testunit5678", 10000f, new ArrayList<Asset>()));

        for(OrganisationalUnit anOrg:temp)
        {
            orgNames.add(anOrg);
        }

        comboOUSelect.setItems(orgNames);

        /*
        //Initialize asset combobox
        ObservableList<AssetType> assetNames = FXCollections.observableArrayList();

        //TODO GET ASSETTYPE LIST FROM DATABASE
        List<AssetType> temp2 = new ArrayList<AssetType>();
        temp2.add(new AssetType(UUID. randomUUID(), "testasset"));
        temp2.add(new AssetType(UUID. randomUUID(), "testasset1234"));
        temp2.add(new AssetType(UUID. randomUUID(), "testasset5678"));

        for(AssetType anAsset:temp2)
        {
            assetNames.add(anAsset);
        }

        comboOMAssetAdd.setItems(assetNames);*/
    }

    @FXML
    void selectedOU(ActionEvent event) throws IOException, InterruptedException {
        refreshTable(comboOUSelect.getValue().getUnitId());
    }

    private Asset[] getAllAsset(UUID orgUnit) throws IOException, InterruptedException {
        HttpResponse<String> assetResponse = Client.clientGet("/assets/"+ orgUnit);
        return gson.fromJson(assetResponse.body(), Asset[].class);
    }

    private void refreshTable(UUID orgUnit) throws IOException, InterruptedException {
        tableData = FXCollections.observableArrayList();
        tableData.setAll(getAllAsset(orgUnit));
        tblOM.setItems(tableData);
    }

    private void addDeleteButtonsToTable() {
        TableColumn<Asset, Void> colDelBtn = new TableColumn("");

        colDelBtn.setPrefWidth(98);
        Callback<TableColumn<Asset, Void>, TableCell<Asset, Void>> cellFactory = new Callback<>() {


            @Override
            public TableCell<Asset, Void> call(final TableColumn<Asset, Void> param) {
                return new TableCell<>() {

                    private final JFXButton btn = new JFXButton("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Asset selectedAsset = getTableView().getItems().get(getIndex());
                            try {
                                handleDelete(selectedAsset.getAssetTypeId());
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colDelBtn.setCellFactory(cellFactory);

        tblOM.getColumns().add(colDelBtn);
    }

    private void handleDelete(UUID assetTypeId) throws IOException, InterruptedException {
        HttpResponse<String> deleteResponse = Client.clientDelete(Route.getRoute(Route.assettype) + assetTypeId);

        if (deleteResponse.statusCode() == 200) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully deleted Asset from the selected Organisation.");
            alert.showAndWait();

            // Re-fetch AssetTypes and set table data
            refreshTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete Asset from the Organisation.");
            alert.showAndWait();
        }
    }
}