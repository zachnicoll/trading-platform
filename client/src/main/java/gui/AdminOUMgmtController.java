package gui;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;

import java.awt.*;

public class AdminOUMgmtController {

    @FXML
    private TextField txtNewOUName;

    @FXML
    private TextField txtNewOUBalance;

    @FXML
    private JFXButton btnAddOU;

    @FXML
    void addOU(ActionEvent event) {
        //TODO implement add OU
        String newOU;
        Float newOUBalance;

        newOU = txtNewOUName.getText();
        newOUBalance = Float.parseFloat(txtNewOUBalance.getText());

        System.out.println("New OU Name:" + newOU + "\tNew OU Balance: $" + newOUBalance);
    }

}