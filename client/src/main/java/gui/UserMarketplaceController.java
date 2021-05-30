package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import errors.JsonError;
import helpers.Client;
import helpers.ClientInfo;
import helpers.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Asset;
import models.AssetType;
import models.ResolvedTrade;
import models.TradeType;
import models.partial.PartialOpenTrade;
import models.partial.PartialReadableOpenTrade;
import models.partial.PartialReadableResolvedTrade;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;

import static helpers.Client.clientGet;
import static helpers.Client.clientPost;

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
    private Text txtCurrentPrice;
    @FXML
    private TextField txtMPQuantity;
    @FXML
    private JFXButton btnMPBuyConfirmOrder;
    @FXML
    private LineChart<String, Float> lnchrtPriceGraph;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Text lblUnitsAvailable;
    @FXML
    private Text unitsAvailable;

    private ClientInfo clientInfo;
    private TradeType tradeType = null;
    private final Gson gson = new Gson();
    private final ObservableList<Asset> assetNames = FXCollections.observableArrayList();
    private XYChart.Series priceHistorySeries;


    @FXML
    public void initialize() throws IOException, InterruptedException {
        clientInfo = ClientInfo.getInstance();
        xAxis.setLabel("Date");
        yAxis.setLabel("Price");
        lnchrtPriceGraph.setTitle("Asset Trade History");
        lnchrtPriceGraph.setAnimated(false);
        priceHistorySeries = new XYChart.Series();
        txtCurrentPrice.setText("");
    }

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
                        clientInfo.getCurrentUser().getOrganisationalUnitId()
        );

        // Extract Asset array from response
        List<Asset> sellAssetTypes = Arrays.asList(gson.fromJson(assetResponse.body(), Asset[].class).clone());

        assetNames.setAll(sellAssetTypes);

        // Set combo-box items to response data
        comboboxSelectAsset.setItems(assetNames);
    }

    public void comboBoxSelected(ActionEvent event) throws IOException, InterruptedException {
        Asset selectedAsset = comboboxSelectAsset.getValue();

        // Quantity of -1 means it's from the BUY assets, which should not display a quantity
        if (selectedAsset != null && selectedAsset.getQuantity() > -1) {
            unitsAvailable.setText(selectedAsset.getQuantity().toString());
        }
        refreshGraph(selectedAsset);
    }

    private void refreshGraph(Asset selectedAsset) throws IOException, InterruptedException {
        if (Objects.nonNull(selectedAsset)) {
            clearChartData();
            lnchrtPriceGraph.setTitle(String.format("%s Price History", selectedAsset.getName()));
            priceHistorySeries.setName(selectedAsset.getName());
            PartialReadableResolvedTrade[] resolvedTrades = getAllTrades(selectedAsset.getAssetTypeId());

            if(Objects.nonNull(resolvedTrades)){
                for (int i = 0; i < resolvedTrades.length; i++) {
                    priceHistorySeries.getData().add(new XYChart.Data(new SimpleDateFormat("dd/MM/yy HH:mm").format(resolvedTrades[i].getDateResolved()), resolvedTrades[i].getPrice()));
                }
                if(Objects.nonNull(resolvedTrades[0].getPrice()))
                {
                    txtCurrentPrice.setText(String.format("Latest %s trade price: $%s", selectedAsset.getName(), resolvedTrades[0].getPrice().toString()));
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Error displaying %s latest price", selectedAsset.getName()), ButtonType.OK);
                    alert.showAndWait();
                }
                lnchrtPriceGraph.getData().add(priceHistorySeries);
            }
        }
    }


    private PartialReadableResolvedTrade[] getAllTrades(UUID assetTypeId) throws IOException, InterruptedException {
        HttpResponse<String> resolvedTradesResponse = Client.clientGet(Route.getRoute(Route.trades) + assetTypeId + "/history");

        if (resolvedTradesResponse.statusCode() == 200) {
            return gson.fromJson(resolvedTradesResponse.body(), PartialReadableResolvedTrade[].class);
        } else if (resolvedTradesResponse.statusCode() == 400) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Selected Asset type does not have any price history to show", ButtonType.OK);
            alert.showAndWait();
            txtCurrentPrice.setText("Latest Asset Trade Price:");
            return null;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error occurred while fetching price history data", ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }
    private void clearChartData(){
        lnchrtPriceGraph.getData().clear();
        priceHistorySeries.getData().clear();
    }

    private void clearUserInput(){
        txtMPPrice.clear();
        txtMPQuantity.clear();
    }

    public void confirmOrder(ActionEvent event) throws IOException, InterruptedException {
        Asset selectedAsset = comboboxSelectAsset.getValue();
        Integer quantity;
        Float price;
        try{
            quantity = Integer.valueOf(txtMPQuantity.getText());
            price = Float.valueOf(txtMPPrice.getText());
        }catch(NumberFormatException error){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Quantity and Price must be numerical inputs", ButtonType.OK);
            alert.showAndWait();
            clearUserInput();
            return;
        }


        PartialOpenTrade newOpenTrade = new PartialOpenTrade(
                tradeType,
                clientInfo.getCurrentUser().getOrganisationalUnitId(),
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