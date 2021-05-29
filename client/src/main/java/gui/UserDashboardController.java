package gui;

import com.google.gson.Gson;
import helpers.Client;
import helpers.ClientInfo;
import helpers.Route;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Callback;
import models.OrganisationalUnit;
import models.TradeType;
import models.partial.PartialReadableOpenTrade;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.util.function.Predicate;

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
    private TableColumn<PartialReadableOpenTrade, String> tblcolTradeType;

    @FXML
    private TableColumn<?, ?> tblcolDateOpened;

    @FXML
    private TextField txtFieldSearch;

    @FXML
    private Text txtUnitBalance;

    @FXML
    private RadioButton btnMyUnit;

    private ObservableList<PartialReadableOpenTrade> tableData;
    private ClientInfo clientInfo;
    private Gson gson = new Gson();
    private OrganisationalUnit userOrgUnit;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        tblcolAsset.setCellValueFactory(new PropertyValueFactory<>("assetTypeName"));
        tblcolPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerAsset"));
        tblcolQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblcolOrgUnit.setCellValueFactory(new PropertyValueFactory<>("organisationalUnitName"));
        tblcolTradeType.setCellValueFactory(new PropertyValueFactory<>("tradeType"));
        tblcolDateOpened.setCellValueFactory(new PropertyValueFactory<>("dateOpened"));

        clientInfo = ClientInfo.getInstance();
        userOrgUnit = getOrgUnit();
        txtUnitBalance.setText(NumberFormat.getCurrencyInstance().format(userOrgUnit.getCreditBalance()));


        refreshTable();
        filterTables();
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

    private OrganisationalUnit getOrgUnit() throws IOException, InterruptedException {
        HttpResponse<String> orgUnitResponse = Client.clientGet(Route.getRoute(Route.orgunit) + clientInfo.getCurrentUser().getOrganisationalUnitId());
        return gson.fromJson(orgUnitResponse.body(), OrganisationalUnit.class);
    }

    /**
     * Filters trades based on search query AND checkbox (show only my organisational unit trades)
     */
    private void filterTables() {
        ObjectProperty<Predicate<PartialReadableOpenTrade>> searchFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<PartialReadableOpenTrade>> unitFilter = new SimpleObjectProperty<>();

        searchFilter.bind(Bindings.createObjectBinding(() ->
                        trade -> txtFieldSearch.getText().isEmpty()
                                || txtFieldSearch.getText() == null
                                || trade.getAssetTypeName().toLowerCase().contains(txtFieldSearch.getText().toLowerCase())
                                || trade.getOrganisationalUnitName().toLowerCase().contains(txtFieldSearch.getText().toLowerCase())
                                || trade.getTradeType().toLowerCase().contains(txtFieldSearch.getText().toLowerCase())
                                || trade.getDateOpened().toLowerCase().contains(txtFieldSearch.getText().toLowerCase()),
                txtFieldSearch.textProperty()));


        unitFilter.bind(Bindings.createObjectBinding(() ->
                        trade -> !btnMyUnit.selectedProperty().get() || (btnMyUnit.selectedProperty().get() && trade.getOrganisationalUnitName().toLowerCase().equals(userOrgUnit.getUnitName())),
                btnMyUnit.selectedProperty()));

        FilteredList<PartialReadableOpenTrade> filteredItems = new FilteredList<>(tableData);
        openTradesTable.setItems(filteredItems);


        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(
                () -> searchFilter.get().and(unitFilter.get()),
                searchFilter, unitFilter));
    }



}

