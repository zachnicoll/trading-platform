package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import helpers.Client;
import helpers.ClientInfo;
import helpers.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.Asset;
import models.AssetType;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.UUID;

public class AdminAssetMgmtController {

    @FXML
    private TextField txtAssMAssetName;

    @FXML
    private JFXButton btnAssMNewAsset;

    @FXML
    private TableColumn<?, ?> tblcolAssMAsset;

    @FXML
    private TableColumn<?, ?> tblcolAssMUuid;

    @FXML
    private TableColumn<?, ?> tblcolAssMDelete;

    @FXML
    private TableView<AssetType> assetTypeTable;

    ObservableList<AssetType> tableData;

    private ClientInfo clientInfo;
    private Gson gson = new Gson();

    private AssetType[] getAllAssetTypes() throws IOException, InterruptedException {
        HttpResponse<String> assetTypesResponse = Client.clientGet(Route.getRoute(Route.assettype));
        return gson.fromJson(assetTypesResponse.body(), AssetType[].class);
    }

    private void handleDelete(UUID assetTypeId) throws IOException, InterruptedException {
        HttpResponse<String> deleteResponse = Client.clientDelete(Route.getRoute(Route.assettype) + assetTypeId);

        if (deleteResponse.statusCode() == 200) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Successfully deleted AssetType.");
            alert.showAndWait();

            // Re-fetch AssetTypes and set table data
            tableData = FXCollections.observableArrayList();
            tableData.setAll(getAllAssetTypes());
            assetTypeTable.setItems(tableData);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete AssetType.");
            alert.showAndWait();
        }
    }

    private void addButtonToTable() {
        TableColumn<AssetType, Void> colBtn = new TableColumn("");

        Callback<TableColumn<AssetType, Void>, TableCell<AssetType, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<AssetType, Void> call(final TableColumn<AssetType, Void> param) {
                final TableCell<AssetType, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            AssetType selectedAssetType = getTableView().getItems().get(getIndex());
                            try {
                                handleDelete(selectedAssetType.getAssetTypeId());
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
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        assetTypeTable.getColumns().add(colBtn);
    }

    @FXML
    public void initialize() throws IOException, InterruptedException {
        tblcolAssMUuid.setCellValueFactory(new PropertyValueFactory<>("assetTypeId"));
        tblcolAssMAsset.setCellValueFactory(new PropertyValueFactory<>("assetName"));

        clientInfo = ClientInfo.getInstance();

        tableData = FXCollections.observableArrayList();
        tableData.setAll(getAllAssetTypes());

        assetTypeTable.setItems(tableData);

        addButtonToTable();
    }
}
