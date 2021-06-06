package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import errors.JsonError;
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
import models.AssetType;
import models.partial.PartialAssetType;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.UUID;

public class AdminAssetMgmtController {

    ObservableList<AssetType> tableData;
    @FXML
    private TextField txtAssMAssetName;
    @FXML
    private JFXButton btnAssMNewAsset;
    @FXML
    private TableColumn<?, ?> tblcolAssMAsset;
    @FXML
    private TableColumn<?, ?> tblcolAssMUuid;
    @FXML
    private TableView<AssetType> assetTypeTable;

    private final Gson gson = new Gson();

    @FXML
    public void initialize() throws IOException, InterruptedException {
        tblcolAssMUuid.setCellValueFactory(new PropertyValueFactory<>("assetTypeId"));
        tblcolAssMAsset.setCellValueFactory(new PropertyValueFactory<>("assetName"));

        refreshTable();
        addDeleteButtonsToTable();

    }

    private AssetType[] getAllAssetTypes() throws IOException, InterruptedException {
        HttpResponse<String> assetTypesResponse = Client.clientGet(Route.getRoute(Route.assettype));
        return gson.fromJson(assetTypesResponse.body(), AssetType[].class);
    }

    private void refreshTable() throws IOException, InterruptedException {
        tableData = FXCollections.observableArrayList();
        tableData.setAll(getAllAssetTypes());
        assetTypeTable.setItems(tableData);
    }

    private void handleDelete(UUID assetTypeId) throws IOException, InterruptedException {
        HttpResponse<String> deleteResponse = Client.clientDelete(Route.getRoute(Route.assettype) + assetTypeId);

        if (deleteResponse.statusCode() == 200) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully deleted AssetType.");
            alert.showAndWait();

            // Re-fetch AssetTypes and set table data
            refreshTable();
        } else {
            JsonError jsonError = gson.fromJson(deleteResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete AssetType.\n" + jsonError.getError());
            alert.showAndWait();
        }
    }

    private void addDeleteButtonsToTable() {
        TableColumn<AssetType, Void> colBtn = new TableColumn("");

        colBtn.setPrefWidth(88);
        Callback<TableColumn<AssetType, Void>, TableCell<AssetType, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<AssetType, Void> call(final TableColumn<AssetType, Void> param) {
                return new TableCell<>() {

                    private final JFXButton btn = new JFXButton("Delete");

                    {//  Handles when delete button is pressed
                        btn.setOnAction((ActionEvent event) -> {
                            AssetType selectedAssetType = getTableView().getItems().get(getIndex());
                            try {
                                handleDelete(selectedAssetType.getAssetTypeId());
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    // Adds delete buttons to table
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

        colBtn.setCellFactory(cellFactory);

        assetTypeTable.getColumns().add(colBtn);
    }

    @FXML
    public void handleCreateNew() throws IOException, InterruptedException {
        // Return early if input is empty
        if (txtAssMAssetName.getText().strip().length() == 0) {
            return;
        }

        PartialAssetType newAssetType = new PartialAssetType(txtAssMAssetName.getText().strip());
        HttpResponse<String> newAssetTypeResponse = Client.clientPost(Route.getRoute(Route.assettype), newAssetType);

        if (newAssetTypeResponse.statusCode() == 200) {
            txtAssMAssetName.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully created AssetType.");
            alert.showAndWait();

            // Re-fetch AssetTypes and set table data
            refreshTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create AssetType.");
            alert.showAndWait();
        }
    }

}
