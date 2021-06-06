package gui;

import helpers.AuthRefresh;
import helpers.ClientInfo;
import helpers.NotificationChecker;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Timer;

/**
 * Wrapper class for JavaFX GUI application. This is contains and displays all
 * GUI elements & windows.
 */
public class GraphicalUserInterface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientInfo clientInfo = ClientInfo.getInstance();
        clientInfo.resetClientInfo();

        final int authResolvePeriod = 1000 * 43200; //refresh every 12 hours
        final int notificationsPeriod = 1000 * 10; //check notifications every 30 seconds

        // Start Trade Resolver Task
        Timer timer1 = new Timer();
        AuthRefresh authResolver = new AuthRefresh();
        timer1.schedule(authResolver, 0, authResolvePeriod);

//        Timer timer2 = new Timer();
//        NotificationChecker notificationChecker = NotificationChecker.getInstance();
//        timer2.schedule(notificationChecker, 0, notificationsPeriod);

        Timeline checkNotifications = new Timeline(
                new KeyFrame(
                        Duration.seconds(10),
                        event -> {
                            NotificationChecker notificationChecker = NotificationChecker.getInstance();
                            notificationChecker.run();
                        })
        );
        checkNotifications.setCycleCount(Timeline.INDEFINITE);
        checkNotifications.play();

        Parent root = FXMLLoader.load(getClass().getResource("../fxml/Login.fxml"));
        primaryStage.setTitle("Login");
        Scene login = new Scene(root, 1280, 720);
        primaryStage.setScene(login);
        primaryStage.show();
        primaryStage.setResizable(false);

    }
}
