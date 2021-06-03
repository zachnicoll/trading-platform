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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Asset;
import models.AssetType;
import models.NewPassword;
import models.TradeType;
import models.partial.PartialOpenTrade;

import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static helpers.PasswordHasher.hashPassword;

public class AdminProfileController {


    @FXML
    private Text lblAPName;

    @FXML
    private PasswordField txtAPPassword;

    @FXML
    private JFXButton btnAPSubmit;

    @FXML
    private PasswordField txtAPPasswordConfirm;

    @FXML
    public void initialize() {
        ClientInfo clientInfo = ClientInfo.getInstance();
        lblAPName.setText(clientInfo.getCurrentUser().getUsername());

    }

    @FXML
    void submitAPChange(ActionEvent event) throws IOException, InterruptedException {

        NewPassword newPassword = new NewPassword(hashPassword(txtAPPassword.getText()), hashPassword(txtAPPasswordConfirm.getText()));

        if (newPassword.checkMatchingPasswords())
        {
            changePassword(newPassword);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "New passwords did not match.");
            alert.showAndWait();
        }
        txtAPPassword.clear();
        txtAPPasswordConfirm.clear();
    }

    @FXML
    public void changePassword(NewPassword newPassword) throws IOException, InterruptedException {

        HttpResponse<String> changePassResponse = Client.clientPost(Route.getRoute(Route.resetpassword), newPassword);

        if (changePassResponse.statusCode() == 200) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully updated password.");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not update password.");
            alert.showAndWait();
        }
    }
}
