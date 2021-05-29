package gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import helpers.Client;
import helpers.ClientInfo;
import helpers.PasswordHasher;
import helpers.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.AccountType;
import models.AssetType;
import models.OrganisationalUnit;
import models.User;
import models.partial.PartialAssetType;
import models.partial.PartialUser;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;

public class AdminAccountMgmtController {

    @FXML
    private TextField txtAMUserName;

    @FXML
    private TextField txtAMPassword;

    @FXML
    private CheckBox isAdminCheckbox;

    @FXML
    private JFXButton btnAMNewMember;

    @FXML
    private JFXComboBox<OrganisationalUnit> comboAMOU;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<?, ?> tblcolAMName;

    @FXML
    private TableColumn<?, ?> tblcolAMOU;

    private ClientInfo clientInfo;
    private ObservableList<User> tableData;
    private ObservableList<OrganisationalUnit> organisationalUnits;
    private OrganisationalUnit selectedOrgUnit;
    private Gson gson = new Gson();

    @FXML
    public void initialize() throws IOException, InterruptedException {
        tblcolAMName.setCellValueFactory(new PropertyValueFactory<>("username"));
        tblcolAMOU.setCellValueFactory(new PropertyValueFactory<>("organisationalUnitId"));

        clientInfo = ClientInfo.getInstance();
        refreshComboBox();
        refreshTable();
        addDeleteButtonsToTable();

    }

    private void refreshComboBox() throws IOException, InterruptedException {
        organisationalUnits = FXCollections.observableArrayList();
        organisationalUnits.setAll(getAllOrgUnits());
        comboAMOU.setItems(organisationalUnits);
    }

    @FXML
    private void selectedOrgUnit(ActionEvent event){
        selectedOrgUnit = comboAMOU.getValue();
    }

    private OrganisationalUnit[] getAllOrgUnits() throws IOException, InterruptedException {
        HttpResponse<String> usersResponse = Client.clientGet(Route.getRoute(Route.orgunit));
        return gson.fromJson(usersResponse.body(), OrganisationalUnit[].class);
    }

    private User[] getAllUsers() throws IOException, InterruptedException {
        HttpResponse<String> usersResponse = Client.clientGet(Route.getRoute(Route.user) + "all");
        return gson.fromJson(usersResponse.body(), User[].class);
    }

    private void refreshTable() throws IOException, InterruptedException {
        tableData = FXCollections.observableArrayList();
        tableData.setAll(getAllUsers());
        userTable.setItems(tableData);
    }


    private void handleDelete(UUID userId) throws IOException, InterruptedException {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?!");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if(result.get() == ButtonType.OK){
            HttpResponse<String> deleteResponse = Client.clientDelete(Route.getRoute(Route.user) + userId);

            if (deleteResponse.statusCode() == 200) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Successfully deleted User.");
                successAlert.showAndWait();
                refreshTable();

            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Could not delete User.");
                errorAlert.showAndWait();
            }
        }
    }

    private void addDeleteButtonsToTable() {
        TableColumn<User, Void> colBtn = new TableColumn("");

        colBtn.setPrefWidth(88);
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<>() {

                    private final JFXButton btn = new JFXButton("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            User selectedUser = getTableView().getItems().get(getIndex());
                            try {
                                handleDelete(selectedUser.getUserId());
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colBtn.setCellFactory(cellFactory);

        userTable.getColumns().add(colBtn);
    }



    @FXML
    private void onSubmitNewMember(ActionEvent event) {
        String username = txtAMUserName.getText();
        String password = PasswordHasher.hashPassword(txtAMPassword.getText());
        UUID orgUnitId = (UUID) comboAMOU.getValue().getUnitId();
        AccountType accountType = isAdminCheckbox.isSelected() ? AccountType.ADMIN : AccountType.USER;

        PartialUser newUser = new PartialUser(username, accountType, orgUnitId, password);

        /*
         * TODO: Post new User to server.
         */
    }
}
