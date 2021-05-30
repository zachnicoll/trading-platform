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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class AdminServerMgmtController {

    @FXML
    private TextField txtSMIp;

    @FXML
    private TextField txtSMPort;

    @FXML
    private JFXButton btnSMExport;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void exportConfig(ActionEvent event) {
        try {
            //define variables
            String mgmtIp;
            String mgmtPort;

            mgmtIp = txtSMIp.getText();
            mgmtPort = txtSMPort.getText();

            DirectoryChooser directoryChooser = new DirectoryChooser();
            Stage currentStage = (Stage) anchorPane.getScene().getWindow();
            File configFile = directoryChooser.showDialog(currentStage);

            configFile = new File(configFile.getAbsolutePath() + "/config.properties");

            Properties props = new Properties();
            props.setProperty("ip", mgmtIp);
            props.setProperty("port", mgmtPort);
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "Trading Platform Connection Configuration File\n" +
                    "THIS FILE HAS BEEN AUTOMATICALLY GENERATED, CHANGE VALUES AT YOUR OWN RISK.");
            writer.close();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Successfully exported server config file to " + configFile.getAbsolutePath());
            alert.showAndWait();

            txtSMIp.clear();
            txtSMPort.clear();
        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save config file.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
