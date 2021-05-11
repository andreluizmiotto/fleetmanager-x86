package br.com.fleetmanager.controller;

import br.com.fleetmanager.connection.implementation.ConnectionFactory;
import br.com.fleetmanager.dao.VehicleDAO;
import br.com.fleetmanager.model.Vehicle;
import br.com.fleetmanager.utils.fxmlFunctions.DeleteButtonOnTableColumn;
import br.com.fleetmanager.utils.Functions;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static br.com.fleetmanager.utils.fxmlFunctions.FXMLStaticFunctions.clearErrorClass;
import static br.com.fleetmanager.utils.fxmlFunctions.FXMLStaticFunctions.isRequiredFieldMissing;

public class VehicleController implements Initializable {

    private VehicleDAO vehicleDAO;

    @FXML
    private TableView<Vehicle> tableViewVehicle;

    @FXML
    private TableColumn<Vehicle, Integer> colId;

    @FXML
    private TableColumn<Vehicle, String> colPlate;

    @FXML
    private TableColumn<Vehicle, String> colDescription;

    @FXML
    private TableColumn<Vehicle, Integer> colYearFabr;

    @FXML
    private TableColumn<Vehicle, Void> colBtnRemove;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfPlate;

    @FXML
    private TextField tfVehicle;

    @FXML
    private TextField tfYearFabr;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnClear;

    final EventHandler<ActionEvent> btnSaveHandler = (ActionEvent event) -> {
        if (missingRequiredFields())
            return;
        Vehicle vehicle = new Vehicle(
                tfPlate.getText(),
                tfVehicle.getText(),
                tfYearFabr.getText());
        if (!tfId.getText().isEmpty())
            vehicle.setId(Integer.parseInt(tfId.getText()));
        vehicleDAO.Save(vehicle);
        clearFields();
        listVehicles();
    };

    final EventHandler<ActionEvent> btnClearHandler = (ActionEvent event) -> clearFields();

    final EventHandler<MouseEvent> lvMouseClicked = (MouseEvent event) -> {
        Vehicle vehicle = tableViewVehicle.getSelectionModel().getSelectedItem();
        if (vehicle == null)
            return;
        tfId.setText(String.valueOf(vehicle.getId()));
        tfVehicle.setText(vehicle.getDescription());
        tfPlate.setText(vehicle.getPlate());
        tfYearFabr.setText(vehicle.getYearFabricated());
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.vehicleDAO = new VehicleDAO(new ConnectionFactory().getNewConnection());

        btnSave.setOnAction(btnSaveHandler);
        btnClear.setOnAction(btnClearHandler);
        tableViewVehicle.setOnMouseClicked(lvMouseClicked);

        tfPlate.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Functions.isNotNull(newValue) && (newValue.length() > 8))
                tfPlate.setText(oldValue);
            clearErrorClass(tfPlate);
        });

        tfVehicle.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Functions.isNotNull(newValue) && ((newValue.length() > 99)))
                tfVehicle.setText(oldValue);
            clearErrorClass(tfVehicle);
        });

        tfYearFabr.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Functions.isNotNull(newValue) && (((!newValue.matches("\\d+")) || (newValue.length() > 4))))
                tfYearFabr.setText(oldValue);
        });

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPlate.setCellValueFactory(new PropertyValueFactory<>("plate"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.getStyleClass().add("longtext");
        colYearFabr.setCellValueFactory(new PropertyValueFactory<>("yearFabricated"));
        addButtonToTable();

        listVehicles();
    }

    private void listVehicles() {
        tableViewVehicle.setItems(FXCollections.observableArrayList(vehicleDAO.ListAll()));
    }

    private void clearFields() {
        tfId.clear();
        tfVehicle.clear();
        tfPlate.clear();
        tfYearFabr.clear();
        clearErrorClass(tfPlate);
        clearErrorClass(tfVehicle);
    }

    private void addButtonToTable() {
        DeleteButtonOnTableColumn<Vehicle> deleteButtonOnTableColumn = new DeleteButtonOnTableColumn<>();
        colBtnRemove.setCellFactory(deleteButtonOnTableColumn.getDeleteButton(new VehicleDAO()));
    }

    private boolean missingRequiredFields() {
        boolean isMissing = isRequiredFieldMissing(tfPlate);
        isMissing = isRequiredFieldMissing(tfVehicle) || isMissing;
        return isMissing;
    }

}