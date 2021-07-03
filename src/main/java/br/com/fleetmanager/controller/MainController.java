package br.com.fleetmanager.controller;

import br.com.fleetmanager.connection.implementation.ConnectionFactory;
import br.com.fleetmanager.dao.FinancialCategoryDAO;
import br.com.fleetmanager.dao.VehicleDAO;
import br.com.fleetmanager.infra.JasperReport;
import br.com.fleetmanager.infra.WindowHandler;
import br.com.fleetmanager.interfaces.controller.IControllerBase;
import br.com.fleetmanager.model.FinancialCategory;
import br.com.fleetmanager.model.Vehicle;
import br.com.fleetmanager.utils.FXMLEnum;
import br.com.fleetmanager.utils.Functions;
import br.com.fleetmanager.utils.fxmlFunctions.AutoCompleteCombobox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static br.com.fleetmanager.utils.fxmlFunctions.FXMLStaticFunctions.isRequiredFieldMissing;

public class MainController implements Initializable, IControllerBase {

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
    private CheckBox ckbSinceTheBeginning;

    @FXML
    private CheckBox ckbSelectVehicle;

    @FXML
    private ComboBox<Vehicle> cbVehicleHist;

    @FXML
    private CheckBox ckbSelectCategory;

    @FXML
    private ComboBox<FinancialCategory> cbCategory;

    @FXML
    private RadioButton rbSimplReport;

    @FXML
    private RadioButton rbAnalyticReport;

    @FXML
    private Button btnGenerateReport;

    @FXML
    private CheckBox ckbGroupByCategory;

    final EventHandler<ActionEvent> actionOpenVehicles = (ActionEvent event) -> {
        try {
            WindowHandler.openWindow(FXMLEnum.Enum.VEHICLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    final EventHandler<ActionEvent> actionOpenCategories = (ActionEvent event) -> {
        try {
            WindowHandler.openWindow(FXMLEnum.Enum.FINANCIALCATEGORY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    final EventHandler<ActionEvent> actionOpenFinancialTransactions = (ActionEvent event) -> {
        try {
            WindowHandler.openWindow(FXMLEnum.Enum.FINANCIALTRANSACTION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    final EventHandler<ActionEvent> actionEnableCbVehicle = (ActionEvent event) -> {
        cbVehicleHist.setDisable(!ckbSelectVehicle.isSelected());
    };

    final EventHandler<ActionEvent> actionEnableCbCategory = (ActionEvent event) -> {
        cbCategory.setDisable(!ckbSelectCategory.isSelected());
        rbSimplReport.setDisable(false);
        ckbGroupByCategory.setDisable(!rbAnalyticReport.isSelected());
        if (ckbSelectCategory.isSelected()) {
            rbAnalyticReport.setSelected(true);
            rbSimplReport.setDisable(true);
            ckbGroupByCategory.setDisable(true);
            ckbGroupByCategory.setSelected(false);
        }
    };

    final EventHandler<ActionEvent> actionEnablePeriod = (ActionEvent event) -> {
        dtInitialDate.setDisable(ckbSinceTheBeginning.isSelected());
        dtFinalDate.setDisable(ckbSinceTheBeginning.isSelected());
    };

    final EventHandler<ActionEvent> actionGenerateReport = (ActionEvent event) -> {

        btnGenerateReport.setDisable(true);
        try {
            if (ckbSelectVehicle.isSelected() && (isRequiredFieldMissing(cbVehicleHist)))
                return;

            if (ckbSelectCategory.isSelected() && (isRequiredFieldMissing(cbCategory)))
                return;

            Map<String, Object> parameters = new HashMap<>();
            if (ckbSinceTheBeginning.isSelected()) {
                parameters.put("dataInicial", Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                parameters.put("dataFinal", Date.from(LocalDate.of(2100, 12, 30).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                parameters.put("semPeriodo", true);
            } else {
                parameters.put("dataInicial", Date.from(dtInitialDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                parameters.put("dataFinal", Date.from(dtFinalDate.getValue().atTime(LocalTime.MAX).toInstant(ZoneOffset.MAX)));
                parameters.put("semPeriodo", false);
            }

            if (ckbSelectVehicle.isSelected()) {
                parameters.put("sqlFieldExtra", "idVeiculo");
                parameters.put("sqlValorExtra", cbVehicleHist.getValue().getId());
            }

            if (ckbSelectCategory.isSelected()) {
                parameters.put("sqlFieldExtra2", "idCategoria");
                parameters.put("sqlValorExtra2", cbCategory.getValue().getId());
            }

            String reportName = "syntheticReport";
            if (rbAnalyticReport.isSelected()) {
                reportName = "analyticHistoric";
                if (ckbGroupByCategory.isSelected() || ckbSelectCategory.isSelected())
                    reportName = "analyticReport";
            }

            JasperReport report = new JasperReport(parameters, reportName);
            report.GeneratePDF();
        } finally {
            btnGenerateReport.setDisable(false);
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UpdateDatabase();

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
        ckbSinceTheBeginning.setOnAction(actionEnablePeriod);
        ckbSinceTheBeginning.setSelected(false);

        initializeComboBox();

        ckbSelectVehicle.setOnAction(actionEnableCbVehicle);
        ckbSelectVehicle.setSelected(false);
        ckbSelectCategory.setOnAction(actionEnableCbCategory);
        ckbSelectCategory.setSelected(false);

        final ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener(observable -> {
            ckbGroupByCategory.setDisable(rbSimplReport.isSelected());
            if (ckbGroupByCategory.isDisabled())
                ckbGroupByCategory.setSelected(false);
        });
        rbSimplReport.setToggleGroup(toggleGroup);
        rbAnalyticReport.setToggleGroup(toggleGroup);
        rbSimplReport.setSelected(true);

        btnGenerateReport.setOnAction(actionGenerateReport);
    }

    private void UpdateDatabase() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.UpdateDatabase();
    }

    private void initializeComboBox() {
        try(Connection connection = new ConnectionFactory().getNewConnection()) {
            new AutoCompleteCombobox<>(cbVehicleHist, FXCollections.observableArrayList(
                    new VehicleDAO(connection).ListAll()));
            cbVehicleHist.setDisable(true);

            new AutoCompleteCombobox<>(cbCategory, FXCollections.observableArrayList(
                    new FinancialCategoryDAO(connection).ListAll()));
            cbCategory.setDisable(true);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    @Override
    public void StorePreferences() {}

    @Override
    public void LoadPreferences() {}
}
