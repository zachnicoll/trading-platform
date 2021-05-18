package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AdminAccountMgmtController {

    @FXML
    private TextField txtAMFirstName;

    @FXML
    private TextField txtAMLastName;

    @FXML
    private JFXButton btnAMNewMember;

    @FXML
    private JFXComboBox<?> comboAMOU;

    @FXML
    private TableView<?> tblAM;

    @FXML
    private TableColumn<?, ?> tblcolAMName;

    @FXML
    private TableColumn<?, ?> tblcolAMPassword;

    @FXML
    private TableColumn<?, ?> tblcolAMOU;

    @FXML
    private TableColumn<?, ?> tblcolAMDelete;

}
