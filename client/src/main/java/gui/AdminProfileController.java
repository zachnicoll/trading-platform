package gui;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import errors.JsonError;
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
import models.TradeType;
import models.partial.PartialOpenTrade;

import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    void submitAPPChange(ActionEvent event) {

    }
}
