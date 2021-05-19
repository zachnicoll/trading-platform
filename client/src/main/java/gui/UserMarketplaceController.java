package gui;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

import java.io.IOException;

import com.jfoenix.controls.JFXComboBox;
import helpers.ClientInfo;

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

    @FXML
    public void initialize()
    {
        ClientInfo clientInfo = ClientInfo.getInstance();

    }

    public void buySelected(ActionEvent event) throws IOException {
        String aStyle = String.format("-fx-border-color: #5DC273; -fx-background-color: #4f5d75; -fx-border-width: 5;");
        anchorboxMP.setStyle(aStyle);
    }

    public void sellSelected(ActionEvent event) throws IOException {
        String aStyle = String.format("-fx-border-color: #e95d5d; -fx-background-color: #4f5d75; -fx-border-width: 5;");
        anchorboxMP.setStyle(aStyle);
    }

}