package gui;

import com.jfoenix.controls.JFXButton;
import com.sun.glass.ui.CommonDialogs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import models.Asset;
import models.AssetType;
import models.OrganisationalUnit;

import java.io.File;
import java.io.IOException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminOUMgmtController {

    @FXML
    private TextField txtNewOUName;

    @FXML
    private TextField txtNewOUBalance;

    @FXML
    private JFXButton btnAddOU;

    @FXML
    private JFXButton btnOMAssetAdd;

    @FXML
    private JFXComboBox<OrganisationalUnit> comboOUSelect;

    @FXML
    private TextField txtOUExistingBalance;

    @FXML
    private JFXButton btnOMDeleteOU;

    @FXML
    private JFXComboBox<AssetType> comboOMAssetAdd;

    @FXML
    private TextField txtOUNewAssetQuantity;

    @FXML
    private TableView<?> tblOM;

    @FXML
    private TableColumn<?, ?> tblcolOMAssets;

    @FXML
    private TableColumn<?, ?> tblcolOMQuantity;

    @FXML
    private TableColumn<?, ?> tblcolOMDelete;

    @FXML
    public void initialize()
    {

        //Initialize org combobox
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

        comboOUSelect.setItems(orgNames);


        //Initialize asset combobox
        ObservableList<AssetType> assetNames = FXCollections.observableArrayList();

        //TODO GET ASSETTYPE LIST FROM DATABASE
        List<AssetType> temp2 = new ArrayList<AssetType>();
        temp2.add(new AssetType(UUID. randomUUID(), "testasset"));
        temp2.add(new AssetType(UUID. randomUUID(), "testasset1234"));
        temp2.add(new AssetType(UUID. randomUUID(), "testasset5678"));

        for(AssetType anAsset:temp2)
        {
            assetNames.add(anAsset);
        }

        comboOMAssetAdd.setItems(assetNames);
    }

}