package br.com.fleetmanager.controller;

import br.com.fleetmanager.utils.FXMLEnum;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private MenuItem menuItemVehicles;

    @FXML
    private MenuItem menuItemCategories;

    EventHandler<ActionEvent> actionOpenVehicles = (ActionEvent event) -> {
        try {
            WindowController.openWindow(FXMLEnum.Enum.VEHICLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    EventHandler<ActionEvent> actionOpenCategories = (ActionEvent event) -> {
        try {
            WindowController.openWindow(FXMLEnum.Enum.FINANCIALCATEGORY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuItemVehicles.setOnAction(actionOpenVehicles);
        menuItemCategories.setOnAction(actionOpenCategories);
    }

}
