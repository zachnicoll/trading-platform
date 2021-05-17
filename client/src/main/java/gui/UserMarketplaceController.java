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
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
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
    private AnchorPane anchorboxMP;

    @FXML
    private JFXButton btnMPBuyAsset;

    @FXML
    private JFXButton btnMPSellAsset;

    @FXML
    private JFXComboBox<?> comboboxSelectAsset;

    @FXML
    private TextField txtMPPrice;

    @FXML
    private TextField txtMPQuantity;

    @FXML
    private JFXButton btnMPBuyConfirmOrder;

    @FXML
    private Text lblUnitsAvailable;

    public void buySelected(ActionEvent event) throws IOException {
        String aStyle = String.format("-fx-border-color: #5DC273; -fx-background-color: #4f5d75; -fx-border-width: 5;");
        anchorboxMP.setStyle(aStyle);
    }

    public void sellSelected(ActionEvent event) throws IOException {
        String aStyle = String.format("-fx-border-color: #e95d5d; -fx-background-color: #4f5d75; -fx-border-width: 5;");
        anchorboxMP.setStyle(aStyle);
    }

}