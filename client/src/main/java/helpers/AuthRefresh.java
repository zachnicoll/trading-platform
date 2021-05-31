package helpers;

import com.google.gson.Gson;
import errors.JsonError;
import javafx.scene.control.Alert;
import models.AssetType;
import models.AuthenticationToken;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.TimerTask;

public class AuthRefresh extends TimerTask {

    private ClientInfo clientInfo;
    private Gson gson = new Gson();
    private String errorResponse = "An error occurred while attempting to automatically refresh the Auth Token. Please log in again.\n";

    @Override
    public void run() {
        clientInfo = clientInfo.getInstance();

        if(clientInfo.getAuthToken() != null) {
            try {
                HttpResponse<String> refreshResponse = Client.clientGet(Route.getRoute(Route.refresh));

                if (refreshResponse.statusCode() == 200) {

                    clientInfo.saveAuthToken(gson.fromJson(refreshResponse.body(), AuthenticationToken.class));
                    System.out.println("refreshing now " + clientInfo.getAuthToken());

                } else {
                    JsonError jsonError = gson.fromJson(refreshResponse.body(), JsonError.class);
                    Alert alert = new Alert(Alert.AlertType.ERROR, errorResponse + jsonError.getError());
                    alert.showAndWait();
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, errorResponse + e.getMessage());
                alert.showAndWait();
            } catch (InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, errorResponse + e.getMessage());
                alert.showAndWait();
            }
        }

    }
}
