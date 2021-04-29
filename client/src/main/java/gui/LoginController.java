package gui;

import com.jfoenix.controls.JFXButton;
import com.sun.glass.ui.CommonDialogs;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
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

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private TextField txtFile;

    @FXML
    private JFXButton btnBrowse;

    @FXML
    private BorderPane loginBorderId;



    @FXML
    private void browseFile(ActionEvent event){
        final FileChooser fileChooser = new FileChooser();
        Stage currentStage = (Stage) loginBorderId.getScene().getWindow();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        File file = fileChooser.showOpenDialog(currentStage);

        //TODO implement file read

        if(file != null)
        {
            System.out.println(("Path: " + file.getAbsolutePath()));
            txtFile.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void submitCredentials(ActionEvent event)  throws IOException {

        //TODO implement submit checks
        String loginUsername;
        String loginPassword;

        loginUsername = txtUsername.getText();
        loginPassword = txtPassword.getText();

        //login is from a user
        if ((loginUsername.equals("user")) && (loginPassword.equals("password"))) {

            //Close login stage
            Stage loginStage = (Stage) loginBorderId.getScene().getWindow();
            loginStage.close();

            //Create new User Menu stage
            System.out.println("submit button pressed");
            Stage UserMainMenuStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/UserMainMenu.fxml"));
            UserMainMenuStage.setTitle("Main Menu");
            Scene UserMainMenuScene = new Scene(root, 1280, 720);
            UserMainMenuStage.setScene(UserMainMenuScene);
            UserMainMenuStage.show();
            UserMainMenuStage.setResizable(false);

        }
        //login is from an admin
        else if((loginUsername.equals("admin")) && (loginPassword.equals("password")))
        {
            //Close login stage
            Stage loginStage = (Stage) loginBorderId.getScene().getWindow();
            loginStage.close();

            //Create new User Menu stage
            System.out.println("submit button pressed");
            Stage AdminMainMenuStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/AdminMainMenu.fxml"));
            AdminMainMenuStage.setTitle("Admin Main Menu");
            Scene AdminMainMenuScene = new Scene(root, 1280, 720);
            AdminMainMenuStage.setScene(AdminMainMenuScene);
            AdminMainMenuStage.show();
            AdminMainMenuStage.setResizable(false);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password combination invalid, please try again.", ButtonType.OK);
            alert.showAndWait();
            txtUsername.clear();
            txtPassword.clear();
        }
    }


}