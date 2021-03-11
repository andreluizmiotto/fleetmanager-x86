package br.com.fleetmanager.controller;

import br.com.fleetmanager.connection.ConnectionFactory;
import br.com.fleetmanager.dao.VehicleDAO;
import br.com.fleetmanager.model.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ListVehicleController implements Initializable {

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

    EventHandler<ActionEvent> btnSaveHandler = (ActionEvent event) -> {

        try(Connection connection = new ConnectionFactory().getNewConnection()) {
            VehicleDAO vehicleDAO = new VehicleDAO(connection);
            Vehicle vehicle = new Vehicle(
                    tfPlate.getText(),
                    tfVehicle.getText(),
                    tfYearFabr.getText());
            if (!tfId.getText().isEmpty())
                vehicle.setId(Integer.parseInt(tfId.getText()));
            vehicleDAO.Save(vehicle);
            clearFields();
            listVehicles();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    };

    EventHandler<ActionEvent> btnClearHandler = (ActionEvent event) -> {
        clearFields();
    };

    EventHandler<MouseEvent> lvMouseClicked = (MouseEvent event) -> {
        Vehicle vehicle = tableViewVehicle.getSelectionModel().getSelectedItem();
        tfId.setText(String.valueOf(vehicle.getId()));
        tfVehicle.setText(vehicle.getDescription());
        tfPlate.setText(vehicle.getPlate());
        tfYearFabr.setText(vehicle.getYearFabricated());
    };

    private boolean isNull(String value) {
        return ((value == null) || (value.isBlank()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnSave.setOnAction(btnSaveHandler);
        btnClear.setOnAction(btnClearHandler);
        tableViewVehicle.setOnMouseClicked(lvMouseClicked);

        tfPlate.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isNull(newValue) && (newValue.length() > 8))
                tfPlate.setText(oldValue);
        });

        tfVehicle.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isNull(newValue) && ((newValue.length() > 99)))
                tfVehicle.setText(oldValue);
        });

        tfYearFabr.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isNull(newValue) && (((!newValue.matches("\\d+")) || (newValue.length() > 4))))
                tfYearFabr.setText(oldValue);
        });

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPlate.setCellValueFactory(new PropertyValueFactory<>("plate"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colYearFabr.setCellValueFactory(new PropertyValueFactory<>("yearFabricated"));

        listVehicles();
    }

    private void listVehicles() {
        try {
            tableViewVehicle.setItems(vehicles());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private ObservableList vehicles() throws SQLException {
        try(Connection connection = new ConnectionFactory().getNewConnection()) {
            return FXCollections.observableArrayList(new VehicleDAO(connection).ListAll());
        }
    }

    private void clearFields() {
        tfId.clear();
        tfVehicle.clear();
        tfPlate.clear();
        tfYearFabr.clear();
    }

}