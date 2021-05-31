package gui;

import helpers.AuthRefresh;
import helpers.ClientInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Timer;

/**
 * Wrapper class for JavaFX GUI application. This is contains and displays all
 * GUI elements & windows.
 */
public class GraphicalUserInterface extends Application {

    private ClientInfo clientInfo;

    @Override
    public void start(Stage primaryStage) throws Exception {


        clientInfo = clientInfo.getInstance();
        clientInfo.resetClientInfo();

        final Integer authResolvePeriod = 1000 * 43200; //refresh every 12 hours

        // Start Trade Resolver Task
        Timer time = new Timer();
        AuthRefresh authResolver = new AuthRefresh();
        time.schedule(authResolver, 0, authResolvePeriod);

        Parent root = FXMLLoader.load(getClass().getResource("../fxml/Login.fxml"));
        primaryStage.setTitle("Login");
        Scene login = new Scene(root, 1280, 720);
        primaryStage.setScene(login);
        primaryStage.show();
        primaryStage.setResizable(false);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
