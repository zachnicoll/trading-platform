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

public class AdminMainMenuController implements Initializable {

    @FXML
    private AnchorPane adminMenuAnchorId;

    @FXML
    private AnchorPane AdminMainPane;

    @FXML
    private JFXButton btnOUMgmt;

    @FXML
    private JFXButton btnAssetMgmt;

    @FXML
    private JFXButton btnAccountMgmt;

    @FXML
    private JFXButton btnServerMgmt;

    @FXML
    private JFXButton btnProfile;

    @FXML
    private JFXButton btnLogout;

    public void sceneAdminOrganisations(ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("AdminOUMgmt");
        AdminMainPane.getChildren().setAll(view);
    }

    public void sceneAdminAssets(ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("AdminAssetMgmt");
        AdminMainPane.getChildren().setAll(view);
    }

    public void sceneAdminAccounts(ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("AdminAccountMgmt");
        AdminMainPane.getChildren().setAll(view);
    }

    public void sceneAdminServerSettings(ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("AdminServerMgmt");
        AdminMainPane.getChildren().setAll(view);
    }

    public void sceneAdminProfile(ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("AdminProfile");
        AdminMainPane.getChildren().setAll(view);
    }

    public void logoutAdmin(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            //do stuff

            //Close menu stage
            Stage loginStage = (Stage) adminMenuAnchorId.getScene().getWindow();
            loginStage.close();

            //Create new login stage
            System.out.println("logout button pressed");
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
