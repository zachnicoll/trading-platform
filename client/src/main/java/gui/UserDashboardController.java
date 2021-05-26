package gui;

import com.google.gson.Gson;
import helpers.Client;
import helpers.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import models.OpenTrade;
import models.partial.PartialReadableOpenTrade;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Locale;

public class UserDashboardController {



    @FXML
    private TableView<PartialReadableOpenTrade> openTradesTable;

    @FXML
    private TableColumn<?, ?> tblcolAsset;

    @FXML
    private TableColumn<?, ?> tblcolPrice;

    @FXML
    private TableColumn<?, ?> tblcolQuantity;

    @FXML
    private TableColumn<?, ?> tblcolOrgUnit;

    @FXML
    private TableColumn<?, ?> tblcolTradeType;

    @FXML
    private TextField txtFieldSearch;

    @FXML
    private Text txtUnitBalance;

    private ObservableList<PartialReadableOpenTrade> tableData;

    private Gson gson = new Gson();

    @FXML
    public void initialize() throws IOException, InterruptedException {
        tblcolAsset.setCellValueFactory(new PropertyValueFactory<>("assetTypeName"));
        tblcolPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerAsset"));
        tblcolQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblcolOrgUnit.setCellValueFactory(new PropertyValueFactory<>("organisationalUnitName"));
        tblcolTradeType.setCellValueFactory(new PropertyValueFactory<>("tradeType"));

        refreshTable();

        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<PartialReadableOpenTrade> filteredData = new FilteredList<>(tableData, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txtFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(trade -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (trade.getAssetTypeName().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches first name.
                } else if (trade.getOrganisationalUnitName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (trade.getTradeType().name().toLowerCase().indexOf(lowerCaseFilter)!=-1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<PartialReadableOpenTrade> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(openTradesTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        openTradesTable.setItems(sortedData);
    }

    private PartialReadableOpenTrade[] getAllOpenTrades() throws IOException, InterruptedException {
        HttpResponse<String> openTradeResponse = Client.clientGet(Route.getRoute(Route.trades));
        return gson.fromJson(openTradeResponse.body(), PartialReadableOpenTrade[].class);
    }


    private void refreshTable() throws IOException, InterruptedException {
        tableData = FXCollections.observableArrayList();
        tableData.setAll(getAllOpenTrades());
        openTradesTable.setItems(tableData);
    }
}
