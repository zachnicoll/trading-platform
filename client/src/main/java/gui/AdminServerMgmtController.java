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
import javafx.stage.Stage;

import java.io.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminServerMgmtController {

    @FXML
    private TextField txtSMIp;

    @FXML
    private TextField txtSMPort;

    @FXML
    private JFXButton btnSMExport;

    @FXML
    void exportConfig(ActionEvent event) {
        try {
            //define variables
            String mgmtIp;
            String mgmtPort;

            mgmtIp = txtSMIp.getText();
            mgmtPort = txtSMPort.getText();

            //write values to user.home
            String userHomeFolder = System.getProperty("user.home");
            File configFile = new File(userHomeFolder, "config.txt");
            Writer out = new BufferedWriter(new FileWriter(configFile));

            out.write(mgmtIp + "," + mgmtPort);
            out.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
