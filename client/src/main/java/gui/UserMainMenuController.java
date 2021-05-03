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

    public void sceneDashboard(ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("UserDashboard");
        UserMainPane.getChildren().setAll(view);
    }

    public void sceneMarketplace(ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("UserMarketplace");
        UserMainPane.getChildren().setAll(view);
    }

    public void sceneTradeHistory(ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("UserTradeHistory");
        UserMainPane.getChildren().setAll(view);
    }

    public void sceneProfile(ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("UserProfile");
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

            //Create new login stage
            Stage LoginStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/Login.fxml"));
            LoginStage.setTitle("Login");
            Scene UserMainMenuScene = new Scene(root, 1280, 720);
            LoginStage.setScene(UserMainMenuScene);
            LoginStage.show();
            LoginStage.setResizable(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }



}
