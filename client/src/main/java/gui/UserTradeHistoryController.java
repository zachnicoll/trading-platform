package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import errors.JsonError;
import helpers.Client;
import helpers.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.partial.PartialReadableResolvedTrade;

import java.io.IOException;
import java.net.http.HttpResponse;

public class UserTradeHistoryController {

    private final Gson gson = new Gson();
    ObservableList<PartialReadableResolvedTrade> tableData;
    @FXML
    private TableView<PartialReadableResolvedTrade> tradeHistoryTable;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private TableColumn<?, ?> dateColumn;
    @FXML
    private TableColumn<?, ?> assetTypeColumn;
    @FXML
    private TableColumn<?, ?> priceColumn;
    @FXML
    private TableColumn<?, ?> quantityColumn;
    @FXML
    private TableColumn<?, ?> boughtFromColumn;
    @FXML
    private TableColumn<?, ?> soldToColumn;

    private void refreshTable() throws IOException, InterruptedException {
        HttpResponse<String> getTradesResponse = Client.clientGet(Route.getRoute(Route.trades) + "history");

        if (getTradesResponse.statusCode() == 200) {
            PartialReadableResolvedTrade[] resolvedTrades = gson.fromJson(getTradesResponse.body(), PartialReadableResolvedTrade[].class);
            tableData.setAll(resolvedTrades);
            tradeHistoryTable.setItems(tableData);
        } else {
            JsonError error = gson.fromJson(getTradesResponse.body(), JsonError.class);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load trade history!\n" + error.getError());
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize() throws IOException, InterruptedException {
        // Sets up table columns
        tableData = FXCollections.observableArrayList();
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateResolved"));
        assetTypeColumn.setCellValueFactory(new PropertyValueFactory<>("assetTypeName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        boughtFromColumn.setCellValueFactory(new PropertyValueFactory<>("boughtFrom"));
        soldToColumn.setCellValueFactory(new PropertyValueFactory<>("soldTo"));

        refreshTable();
    }

    @FXML
    public void onRefresh() throws IOException, InterruptedException {
        refreshTable();
    }
}
