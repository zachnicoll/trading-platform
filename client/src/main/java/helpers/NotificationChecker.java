package helpers;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Window;
import models.partial.PartialReadableResolvedTrade;

import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class PopupWrapper {
    private final Popup popup;
    private final int index;
    Window currentWindow;

    public PopupWrapper(Boolean isBuy, Window currentWindow, String heading, String body, int index) {
        popup = new Popup();
        this.currentWindow = currentWindow;
        this.index = index;

        // Create a container for the notification
        AnchorPane container = new AnchorPane();
        container.setMinWidth(220);
        container.setMinHeight(100);
        if (isBuy) {
            container.setStyle("-fx-background-color: #5dc273; -fx-border-color: black;");
        } else {
            container.setStyle("-fx-background-color: #e95d5d; -fx-border-color: black;");
        }

        // Create heading label
        Label headingLabel = new Label(heading);
        headingLabel.setStyle("-fx-font-weight: 700;");
        headingLabel.setAlignment(Pos.CENTER);
        headingLabel.contentDisplayProperty().set(ContentDisplay.CENTER);
        headingLabel.setMinWidth(200);
        headingLabel.setMinHeight(16);
        headingLabel.setLayoutX(10);
        headingLabel.setLayoutY(6);

        // Create body label
        Label bodyLabel = new Label(body);
        bodyLabel.setAlignment(Pos.TOP_LEFT);
        bodyLabel.contentDisplayProperty().set(ContentDisplay.CENTER);
        bodyLabel.setMinWidth(200);
        bodyLabel.setMinHeight(43);
        bodyLabel.setLayoutX(10);
        bodyLabel.setLayoutY(29);

        // Create close button
        JFXButton closeButton = new JFXButton();
        closeButton.setText("Close");
        closeButton.setLayoutX(156);
        closeButton.setLayoutY(74);
        closeButton.setStyle("-fx-background-color: grey");
        closeButton.setOnAction(e -> {
            popup.hide();
        });

        // Add heading, body, and close button to the AnchorPane container
        container.getChildren().addAll(headingLabel, bodyLabel, closeButton);

        // Add AnchorPane to the Popup
        popup.getContent().add(container);
    }

    public void show() {
        popup.show(currentWindow.getScene().getWindow(), 10, 10 + index * 110);
    }

    public void hide() {
        popup.hide();
    }
}

public class NotificationChecker {
    private static NotificationChecker single_instance = null;
    private final Gson gson = new Gson();
    private List<PartialReadableResolvedTrade> resolvedTrades;
    private boolean firstFetchComplete;
    private AnchorPane currentWindow;
    private List<PopupWrapper> popupWrappers;

    private NotificationChecker() {
        resolvedTrades = new ArrayList<>();
        firstFetchComplete = false;
        popupWrappers = new ArrayList<>();
    }

    public static NotificationChecker getInstance() {
        if (single_instance == null)
            single_instance = new NotificationChecker();

        return single_instance;
    }

    public void setCurrentWindow(AnchorPane currentWindow) {
        this.currentWindow = currentWindow;
    }

    public void run() {
        try {
            HttpResponse<String> tradeHistoryResponse = Client.clientGet(Route.getRoute(Route.trades) + "history/");

            if (tradeHistoryResponse.statusCode() != 200) {
                throw new Exception("User is not authenticated");
            }

            List<PartialReadableResolvedTrade> newResolvedTrades = Arrays.asList(gson.fromJson(tradeHistoryResponse.body(), PartialReadableResolvedTrade[].class));

            // Sort array so latest is first
            newResolvedTrades.sort((t1, t2) -> {
                Timestamp date1 = t1.getDateResolved();
                Timestamp date2 = t2.getDateResolved();

                return date2.compareTo(date1);
            });

            if (firstFetchComplete) {
                // Compare new and old trades

                // Get the latest timestamp of the previous fetch - any resolved trades with a timestamp after this
                // will be new, and the User should be notified
                Timestamp lastOldTimestamp = resolvedTrades.get(0).getDateResolved();
                Object[] filtered = newResolvedTrades.stream().filter(x -> x.getDateResolved().compareTo(lastOldTimestamp) > 0).toArray();
                if (filtered.length > 0) {
                    for (PopupWrapper popupWrapper : popupWrappers) {
                        popupWrapper.hide();
                    }

                    popupWrappers = new ArrayList<>();

                    PartialReadableResolvedTrade[] filteredResolvedTrades = Arrays.copyOf(filtered, filtered.length, PartialReadableResolvedTrade[].class);
                    // There are new resolved trades, create notifications for them
                    for (int i = 0; i < filteredResolvedTrades.length; i++) {
                        PartialReadableResolvedTrade newResolvedTrade = filteredResolvedTrades[i];

                        if (newResolvedTrade.getBoughtFromId().equals(ClientInfo.getInstance().getCurrentUser().getOrganisationalUnitId())) {
                            // Notify that SELL trade went through
                            PopupWrapper popupWrapper = new PopupWrapper(
                                    false, currentWindow.getScene().getWindow(), "SELL Trade Resolved!", String.format("Sold %d of %s to %s.", newResolvedTrade.getQuantity(), newResolvedTrade.getAssetTypeName(), newResolvedTrade.getSoldTo()), i
                            );

                            popupWrappers.add(popupWrapper);
                        } else if (newResolvedTrade.getSoldToId().equals(ClientInfo.getInstance().getCurrentUser().getOrganisationalUnitId())) {
                            // Notify that BUY trade went through
                            // Notify that SELL trade went through
                            PopupWrapper popupWrapper = new PopupWrapper(
                                    true, currentWindow.getScene().getWindow(), "Buy Trade Resolved!", String.format("Bought %d of %s from %s.", newResolvedTrade.getQuantity(), newResolvedTrade.getAssetTypeName(), newResolvedTrade.getBoughtFrom()), i
                            );

                            popupWrappers.add(popupWrapper);
                        }
                    }

                    for (PopupWrapper popupWrapper : popupWrappers) {
                        popupWrapper.show();
                    }
                }
            } else {
                // Can't compare new and old trades as old trades is an empty array. This is the 'initial'
                // setup for notifications to function.
                firstFetchComplete = true;
            }
            resolvedTrades = newResolvedTrades;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
