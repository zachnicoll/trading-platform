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

public class UserMainMenuController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private AnchorPane UserMainPane;

    @FXML
    private JFXButton btnDashboard;

    @FXML
    private JFXButton btnMarketplace;

    @FXML
    private JFXButton btnTradeHistory;

    @FXML
    private JFXButton btnProfile;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private AnchorPane userMenuAnchorId;

    private FxmlLoader pageLoader = new FxmlLoader();

    public void sceneDashboard(ActionEvent event) throws IOException {
        Pane view = pageLoader.getPage("UserDashboard");
        UserMainPane.getChildren().setAll(view);
    }

    public void sceneMarketplace(ActionEvent event) throws IOException {
        Pane view = pageLoader.getPage("UserMarketplace");
        UserMainPane.getChildren().setAll(view);
    }

    public void sceneTradeHistory(ActionEvent event) throws IOException {
        Pane view = pageLoader.getPage("UserTradeHistory");
        UserMainPane.getChildren().setAll(view);
    }

    public void sceneProfile(ActionEvent event) throws IOException {
        Pane view = pageLoader.getPage("UserProfile");
        UserMainPane.getChildren().setAll(view);
    }

    public void sceneMarketplaceBuy(ActionEvent event) throws IOException {
        Pane view = pageLoader.getPage("MarketplaceBuy");
        UserMainPane.getChildren().setAll(view);
    }

    public void sceneMarketplaceSell(ActionEvent event) throws IOException {
        Pane view = pageLoader.getPage("MarketplaceSell");
        UserMainPane.getChildren().setAll(view);
    }

    public void logout(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            //do stuff

            //Close menu stage
            Stage loginStage = (Stage) userMenuAnchorId.getScene().getWindow();
            loginStage.close();

            LoginController.showLogin();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }



}
