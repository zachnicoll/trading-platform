package gui;

import com.jfoenix.controls.JFXButton;
import com.sun.glass.ui.CommonDialogs;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import java.awt.*;

public class AdminOUMgmtController {

    @FXML
    private TextField txtNewOUName;

    @FXML
    private TextField txtNewOUBalance;

    @FXML
    private JFXButton btnAddOU;

    @FXML
    private TreeTableView<?> tblOU;

    @FXML
    private TreeTableColumn<?, ?> tblcolOUName;

    @FXML
    private TreeTableColumn<?, ?> tblcolOUBalance;

    @FXML
    private TreeTableColumn<?, ?> tblcolOUEdit;

    @FXML
    private TreeTableColumn<?, ?> tblcolOUDelete;

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