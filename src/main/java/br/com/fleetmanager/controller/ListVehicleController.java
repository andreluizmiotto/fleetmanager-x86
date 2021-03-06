package br.com.fleetmanager.controller;

import br.com.fleetmanager.connection.ConnectionFactory;
import br.com.fleetmanager.dao.VehicleDAO;
import br.com.fleetmanager.model.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPlate.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colYearFabr.setCellValueFactory(new PropertyValueFactory<>("anoFabricacao"));

        try {
            tableViewVehicle.setItems(vehicles());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private ObservableList<Vehicle> vehicles() throws SQLException {

        try(Connection connection = new ConnectionFactory().getNewConnection()) {
            return FXCollections.observableArrayList(new VehicleDAO(connection).ListAll());
        }

    }
}