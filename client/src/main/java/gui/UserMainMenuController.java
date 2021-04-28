package gui;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserMainMenuController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private AnchorPane mainPane;

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

    public void sceneDashboard(javafx.event.ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("UserDashboard");
        mainPane.getChildren().setAll(view);
    }

    public void sceneMarketplace(javafx.event.ActionEvent event) throws IOException {
        System.out.println("btn pressed");
        fxmlLoader object = new fxmlLoader();
        Pane view = object.getPage("UserMarketplace");
        mainPane.getChildren().setAll(view);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }

}
