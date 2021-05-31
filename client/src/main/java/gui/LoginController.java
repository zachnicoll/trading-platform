package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import errors.JsonError;
import helpers.ClientInfo;
import helpers.PasswordHasher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.AccountType;
import models.AuthenticationToken;
import models.Credentials;
import models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Properties;

import static helpers.Client.clientGet;
import static helpers.Client.clientPost;

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
    public static void showLogin() throws IOException {
        Stage LoginStage = new Stage();
        Parent root = FXMLLoader.load(LoginController.class.getResource("../fxml/Login.fxml"));
        LoginStage.setTitle("Login");
        Scene UserMainMenuScene = new Scene(root, 1280, 720);
        LoginStage.setScene(UserMainMenuScene);
        LoginStage.show();
        LoginStage.setResizable(false);
    }

    @FXML
    private void browseFile(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        Stage currentStage = (Stage) loginBorderId.getScene().getWindow();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".properties", "*.properties"));
        File file = fileChooser.showOpenDialog(currentStage);

        try (InputStream serverConfigFile = new FileInputStream(file.getAbsolutePath())) {

            Properties serverConfig = new Properties();

            // load a properties file
            serverConfig.load(serverConfigFile);

            // get the property value and print it out
            System.out.println(serverConfig.getProperty("port"));
            System.out.println(serverConfig.getProperty("ip"));

        } catch (IOException ex) {
           Alert alert = new Alert(AlertType.ERROR, "Error occurred while importing server config file");
           alert.showAndWait();
        }

        if (file != null) {
            System.out.println(("Path: " + file.getAbsolutePath()));
            txtFile.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void submitCredentials(ActionEvent event) throws IOException, InterruptedException {
        Gson gson = new Gson();
        JsonError errorResponse;

        String loginUsername;
        String loginPassword;

        loginUsername = txtUsername.getText();

        // Hash password before sending to server
        loginPassword = PasswordHasher.hashPassword(txtPassword.getText());

        Credentials loginInfo = new Credentials(loginUsername, loginPassword);

        HttpResponse<String> loginResponse = clientPost("/login/", loginInfo);

        if (loginResponse.statusCode() == 200) {
            ClientInfo clientInfo = ClientInfo.getInstance();

            AuthenticationToken authToken = gson.fromJson(loginResponse.body(), AuthenticationToken.class);
            clientInfo.saveClientInfo(authToken, null); // Save auth token for use in following request

            // For development
            System.out.println(authToken.toString());

            HttpResponse<String> userResponse = clientGet("/user/");

            if (userResponse.statusCode() == 200) {
                User theUser = gson.fromJson(userResponse.body(), User.class);

                // As user response successful, save the user info as well
                clientInfo.saveClientInfo(authToken, theUser);

                //Close login stage
                Stage loginStage = (Stage) loginBorderId.getScene().getWindow();
                loginStage.close();

                if (theUser.getAccountType() == AccountType.USER) {
                    // Logging-in as a USER

                    //Create new User Menu stage
                    Stage UserMainMenuStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/UserMainMenu.fxml"));
                    UserMainMenuStage.setTitle("Main Menu");
                    Scene UserMainMenuScene = new Scene(root, 1280, 720);
                    UserMainMenuStage.setScene(UserMainMenuScene);
                    UserMainMenuStage.show();
                    UserMainMenuStage.setResizable(false);
                } else if (theUser.getAccountType() == AccountType.ADMIN) {
                    // Logging-in as an ADMIN

                    //Create new Admin Menu stage
                    Stage AdminMainMenuStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/AdminMainMenu.fxml"));
                    AdminMainMenuStage.setTitle("Admin Main Menu");
                    Scene AdminMainMenuScene = new Scene(root, 1280, 720);
                    AdminMainMenuStage.setScene(AdminMainMenuScene);
                    AdminMainMenuStage.show();
                    AdminMainMenuStage.setResizable(false);
                }
            } else {
                errorResponse = gson.fromJson(userResponse.body(), JsonError.class);
                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        "Could not fetch User information. " + errorResponse.getError(),
                        ButtonType.OK
                );
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password combination invalid, please try again.", ButtonType.OK);
            alert.showAndWait();
            txtUsername.clear();
            txtPassword.clear();
        }


    }


}