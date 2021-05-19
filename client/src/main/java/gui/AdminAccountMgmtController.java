package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import helpers.PasswordHasher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.AccountType;
import models.partial.PartialUser;

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
    private JFXComboBox<?> comboAMOU;

    @FXML
    private TableView<?> tblAM;

    @FXML
    private TableColumn<?, ?> tblcolAMName;

    @FXML
    private TableColumn<?, ?> tblcolAMOU;

    @FXML
    private TableColumn<?, ?> tblcolAMDelete;

    @FXML
    private void onSubmitNewMember(ActionEvent event) {
        String username = txtAMUserName.getText();
        String password = PasswordHasher.hashPassword(txtAMPassword.getText());
        UUID orgUnitId = (UUID) comboAMOU.getValue();
        AccountType accountType = isAdminCheckbox.isSelected() ? AccountType.ADMIN : AccountType.USER;

        PartialUser newUser = new PartialUser(username, accountType, orgUnitId, password);

        /*
         * TODO: Post new User to server.
         */
    }
}
