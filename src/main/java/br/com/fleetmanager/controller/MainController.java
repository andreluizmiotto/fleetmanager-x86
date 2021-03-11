package br.com.fleetmanager.controller;

import br.com.fleetmanager.utils.FXMLEnum;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class MainController {

    @FXML
    private MenuItem menuItemCadVeiculos;

    @FXML
    void actionOpenCadastroVeiculos(ActionEvent event) throws IOException {
        WindowController.openWindow(FXMLEnum.LISTVEHICLE);
    }

}
