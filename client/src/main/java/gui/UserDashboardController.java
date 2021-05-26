package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class UserDashboardController {

    @FXML
    private TableView<?> openTradesTable;

    @FXML
    private TableColumn<?, ?> tblcolAsset;

    @FXML
    private TableColumn<?, ?> tblcolPrice;

    @FXML
    private TableColumn<?, ?> tblcolQuantity;

    @FXML
    private TableColumn<?, ?> tblcolOr;

    @FXML
    private TextField txtFieldSearch;

    @FXML
    private Text txtUnitBalance;

}
