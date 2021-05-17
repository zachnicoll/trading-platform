package gui;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
public class UserMarketplaceController {


    @FXML
    private JFXComboBox<?> comboboxSelectAsset;

    @FXML
    private JFXButton btnMPBuyAsset;

    @FXML
    private JFXButton btnMPSellAsset;

    @FXML
    private AnchorPane anchorpaneMPBuySell;

    private FxmlLoader pageLoader = new FxmlLoader();

    public void sceneMarketplaceBuy(ActionEvent event) throws IOException {
        Pane view = pageLoader.getPage("MarketplaceBuy");
        anchorpaneMPBuySell.getChildren().setAll(view);
    }

    public void sceneMarketplaceSell(ActionEvent event) throws IOException {
        Pane view = pageLoader.getPage("MarketplaceSell");
        anchorpaneMPBuySell.getChildren().setAll(view);
    }

}