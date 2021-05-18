package gui;
import com.jfoenix.controls.JFXButton;
import com.sun.glass.ui.CommonDialogs;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import models.Asset;
import models.AssetType;
import models.OrganisationalUnit;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminAssetMgmtController {

    @FXML
    private TreeTableView<?> tblOU;

    @FXML
    private TreeTableColumn<?, ?> tblcolAMName;

    @FXML
    private TreeTableColumn<?, ?> tblcolAMOU;

    @FXML
    private TreeTableColumn<?, ?> tblcolAMEdit;

    @FXML
    private TreeTableColumn<?, ?> tblcolAMDelete;

    @FXML
    private TextField txtAssMAssetName;

    @FXML
    private JFXButton btnAssMNewAsset;

    @FXML
    private JFXComboBox<OrganisationalUnit> comboAssMOU;

    @FXML
    public void initialize()
    {
        //Initialize table


        //Initialize combobox
        ObservableList<OrganisationalUnit> orgNames = FXCollections.observableArrayList();

        //TODO GET ORG LIST FROM DATABASE
        List<OrganisationalUnit> temp = new ArrayList<OrganisationalUnit>();
        temp.add(new OrganisationalUnit(UUID. randomUUID(), "testunit", 10000f, new ArrayList<Asset>()));
        temp.add(new OrganisationalUnit(UUID. randomUUID(), "testunit1234", 10000f, new ArrayList<Asset>()));
        temp.add(new OrganisationalUnit(UUID. randomUUID(), "testunit5678", 10000f, new ArrayList<Asset>()));

        for(OrganisationalUnit anOrg:temp)
        {
            orgNames.add(anOrg);
        }

        comboAssMOU.setItems(orgNames);
    }

    public void populateAssetMgmtCombo(ActionEvent event) throws IOException {
        ArrayList<AssetType> temp = new ArrayList<>();
        temp.add(new AssetType(UUID. randomUUID(), "a1"));
        temp.add(new AssetType(UUID. randomUUID(), "a2"));


    }
}
