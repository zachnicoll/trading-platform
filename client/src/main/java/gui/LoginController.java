package gui;

import com.google.gson.Gson;
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
import models.AuthenticationToken;
import models.Credentials;
import models.User;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

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
    private void submitCredentials(ActionEvent event) throws IOException, InterruptedException {

        //TODO implement submit checks
        String loginUsername;
        String loginPassword;

        loginUsername = txtUsername.getText();
        loginPassword = txtPassword.getText();

        Credentials loginInfo = new Credentials(loginUsername, loginPassword);

        String loginRequestURL = "http://localhost:8000/login/";

        HttpClient loginClient = HttpClient.newBuilder().build();
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(loginRequestURL))
                .POST(HttpRequest.BodyPublishers.ofString(loginInfo.loginFormat()))
                .build();

        HttpResponse<String> loginResponse = loginClient.send(loginRequest, HttpResponse.BodyHandlers.ofString());

        Gson loginGson = new Gson();

        AuthenticationToken authToken = loginGson.fromJson(loginResponse.body(), AuthenticationToken.class);

        int loginApiResponse = loginResponse.statusCode();
        System.out.println(authToken.toString());



        /*String userRequestURL = "http://localhost:8000/user/";

        HttpClient userClient = HttpClient.newBuilder().build();
        HttpRequest userRequest = HttpRequest.newBuilder()
                .uri(URI.create(userRequestURL))
                .GET().setHeader(authToken)
                .build();

        HttpResponse<String> userResponse = userClient.send(userRequest, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();

        User theUser = gson.fromJson(userResponse.body(), User.class);

        int temp = userResponse.statusCode();
        System.out.println(theUser.getUsername());*/

        if (loginApiResponse == 200)
        {

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password combination invalid, please try again.", ButtonType.OK);
            alert.showAndWait();
            txtUsername.clear();
            txtPassword.clear();
        }

        //login is from a user
        if ((loginUsername.equals("user")) && (loginPassword.equals("password"))) {

            //Close login stage
            Stage loginStage = (Stage) loginBorderId.getScene().getWindow();
            loginStage.close();

            //Create new User Menu stage
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