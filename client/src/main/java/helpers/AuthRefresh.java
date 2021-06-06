package helpers;

import com.google.gson.Gson;
import errors.JsonError;
import javafx.scene.control.Alert;
import models.AssetType;
import models.AuthenticationToken;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.TimerTask;

/**
 * AuthRefresh automatically renews the client's JWT token after a 12 hour period of being logged in.
 * This is to enforce security and authenticity.
 * Current refresh every 12 hours
 */
public class AuthRefresh extends TimerTask {

    private final Gson gson = new Gson();

    @Override
    public void run() {
        ClientInfo clientInfo = ClientInfo.getInstance();

        if(clientInfo.getAuthToken() != null) {
            String errorResponse = "An error occurred while attempting to automatically refresh the Auth Token. Please log in again.\n";
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
            } catch (IOException | InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, errorResponse + e.getMessage());
                alert.showAndWait();
            }
        }

    }
}
