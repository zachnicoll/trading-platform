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
import models.partial.PartialAssetType;
import models.partial.PartialOpenTrade;

import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static helpers.Client.clientGet;
import static helpers.Client.clientPost;
import static helpers.PasswordHasher.hashPassword;

public class UserProfileController {


    @FXML
    private Text lblUPName;

    @FXML
    private PasswordField txtUPPassword;

    @FXML
    private JFXButton btnUPSumbit;

    @FXML
    private PasswordField txtUPPasswordConfirm;

    @FXML
    public void initialize() {
        ClientInfo clientInfo = ClientInfo.getInstance();
        lblUPName.setText(clientInfo.getCurrentUser().getUsername());

    }

    @FXML
    void submitPChange(ActionEvent event) throws IOException, InterruptedException {

        //TODO IDK IF WE NEED TO REFRESH THE USER'S JWT?

        NewPassword newPassword = new NewPassword(hashPassword(txtUPPassword.getText()), hashPassword(txtUPPasswordConfirm.getText()));

        if (newPassword.checkMatchingPasswords())
        {
            changePassword(newPassword);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "New passwords did not match.");
            alert.showAndWait();
        }
        txtUPPassword.clear();
        txtUPPasswordConfirm.clear();
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
