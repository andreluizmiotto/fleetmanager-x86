package br.com.fleetmanager.controller;

import br.com.fleetmanager.connection.ConnectionFactory;
import br.com.fleetmanager.dao.VehicleDAO;
import br.com.fleetmanager.model.Vehicle;
import br.com.fleetmanager.utils.AutoCompleteCombobox;
import br.com.fleetmanager.utils.FXMLEnum;
import br.com.fleetmanager.utils.Functions;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static br.com.fleetmanager.utils.FXMLStaticFunctions.clearErrorClass;
import static br.com.fleetmanager.utils.FXMLStaticFunctions.isRequiredFieldMissing;

public class MainController implements Initializable {

    @FXML
    private MenuItem menuItemVehicles;

    @FXML
    private MenuItem menuItemCategories;

    @FXML
    private MenuItem menuItemTransactions;

    @FXML
    private Button btnTransaction;

    @FXML
    private Button btnCategory;

    @FXML
    private Button btnVehicle;

    @FXML
    private DatePicker dtInitialDate;

    @FXML
    private DatePicker dtFinalDate;

    @FXML
    private RadioButton rbSimplReport;

    @FXML
    private RadioButton rbAnalyticReport;

    @FXML
    private Button btnGenerateReport;

    @FXML
    private ComboBox<Vehicle> cbVehicleHist;

    @FXML
    private CheckBox ckbGroupByCategory;

    @FXML
    private Button btnGenerateHistoric;

    final EventHandler<ActionEvent> actionOpenVehicles = (ActionEvent event) -> {
        try {
            WindowController.openWindow(FXMLEnum.Enum.VEHICLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    final EventHandler<ActionEvent> actionOpenCategories = (ActionEvent event) -> {
        try {
            WindowController.openWindow(FXMLEnum.Enum.FINANCIALCATEGORY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    final EventHandler<ActionEvent> actionOpenFinancialTransactions = (ActionEvent event) -> {
        try {
            WindowController.openWindow(FXMLEnum.Enum.FINANCIALTRANSACTION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    final EventHandler<ActionEvent> actionGenerateReport = (ActionEvent event) -> {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dataInicial", Date.from(dtInitialDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        parameters.put("dataFinal", Date.from(dtFinalDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        String reportName = "analyticReport";
        if (rbSimplReport.isSelected())
            reportName = "syntheticReport";

        ReportController report = new ReportController(parameters, reportName);
        report.GeneratePDF();
    };

    final EventHandler<ActionEvent> actionGenerateHistoric = (ActionEvent event) -> {

        if (isRequiredFieldMissing(cbVehicleHist))
            return;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("sqlFieldExtra", "idVeiculo");
        parameters.put("sqlValorExtra", cbVehicleHist.getValue().getId());

        String reportName = "analyticReport";
        if (ckbGroupByCategory.isSelected())
            reportName = "analyticReport";

        ReportController report = new ReportController(parameters, reportName);
        report.GeneratePDF();
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuItemVehicles.setOnAction(actionOpenVehicles);
        menuItemCategories.setOnAction(actionOpenCategories);
        menuItemTransactions.setOnAction(actionOpenFinancialTransactions);

        btnVehicle.setGraphic(Functions.getImageView(getClass(),"truck_80px.png"));
        btnCategory.setGraphic(Functions.getImageView(getClass(),"cashbook_80px.png"));
        btnTransaction.setGraphic(Functions.getImageView(getClass(),"transaction_80px.png"));

        btnVehicle.setOnAction(actionOpenVehicles);
        btnCategory.setOnAction(actionOpenCategories);
        btnTransaction.setOnAction(actionOpenFinancialTransactions);

        btnVehicle.setTooltip(new Tooltip("Veículos"));
        btnCategory.setTooltip(new Tooltip("Categorias"));
        btnTransaction.setTooltip(new Tooltip("Lançamentos"));

        dtInitialDate.setValue(LocalDate.from(LocalDateTime.now().withDayOfMonth(1)));
        dtFinalDate.setValue(LocalDate.from(LocalDateTime.now()));

        final ToggleGroup toggleGroup = new ToggleGroup();
        rbSimplReport.setToggleGroup(toggleGroup);
        rbAnalyticReport.setToggleGroup(toggleGroup);
        rbSimplReport.setSelected(true);

        btnGenerateReport.setOnAction(actionGenerateReport);
        btnGenerateHistoric.setOnAction(actionGenerateHistoric);

        initializeCbVehicle();
    }

    private void initializeCbVehicle() {
        try(Connection connection = new ConnectionFactory().getNewConnection()) {
            cbVehicleHist.itemsProperty().setValue(FXCollections.observableArrayList(new VehicleDAO(connection).ListAll()));
            new AutoCompleteCombobox<>(cbVehicleHist);
            cbVehicleHist.setConverter(new StringConverter<>() {
                @Override
                public String toString(Vehicle obj) {
                    if (obj == null)
                        return "";
                    return obj.toString();
                }

                @Override
                public Vehicle fromString(final String string) {
                    return cbVehicleHist.getItems().stream().filter(obj -> obj.toString().equals(string)).findFirst().orElse(null);
                }
            });
            cbVehicleHist.valueProperty().addListener((composant, oldValue, newValue) -> clearErrorClass(cbVehicleHist));
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

}
