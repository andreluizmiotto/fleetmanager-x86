package br.com.fleetmanager.controller;

import br.com.fleetmanager.connection.implementation.ConnectionFactory;
import br.com.fleetmanager.dao.FinancialCategoryDAO;
import br.com.fleetmanager.dao.FinancialTransactionDAO;
import br.com.fleetmanager.dao.VehicleDAO;
import br.com.fleetmanager.model.FinancialCategory;
import br.com.fleetmanager.model.FinancialTransaction;
import br.com.fleetmanager.model.Vehicle;
import br.com.fleetmanager.utils.fxmlFunctions.AutoCompleteCombobox;
import br.com.fleetmanager.utils.Constants;
import br.com.fleetmanager.utils.fxmlFunctions.DeleteButtonOnTableColumn;
import br.com.fleetmanager.utils.Functions;
import br.com.fleetmanager.utils.fxmlFields.CurrencyField;
import br.com.fleetmanager.utils.fxmlFunctions.AutoCompleteCombobox;
import br.com.fleetmanager.utils.fxmlFunctions.DeleteButtonOnTableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static br.com.fleetmanager.utils.fxmlFunctions.FXMLStaticFunctions.clearErrorClass;
import static br.com.fleetmanager.utils.fxmlFunctions.FXMLStaticFunctions.isRequiredFieldMissing;

public class TransactionsController implements Initializable {

    private FinancialTransactionDAO transactionDAO;

    @FXML
    private TableView<FinancialTransaction> tableView;

    @FXML
    private TableColumn<FinancialTransaction, Integer> colId;

    @FXML
    private TableColumn<FinancialTransaction, Date> colDate;

    @FXML
    private TableColumn<FinancialTransaction, String> colPlate;

    @FXML
    private TableColumn<FinancialTransaction, String> colVehicle;

    @FXML
    private TableColumn<FinancialTransaction, String> colCategory;

    @FXML
    private TableColumn<FinancialTransaction, String> colValue;

    @FXML
    private TableColumn<FinancialTransaction, String> colDescription;

    @FXML
    private TableColumn<FinancialTransaction, Void> colBtnRemove;

    @FXML
    private TextField tfId;

    @FXML
    private DatePicker dtTransaction;

    @FXML
    private ComboBox<Vehicle> cbVehicle;

    @FXML
    private ComboBox<FinancialCategory> cbCategory;

    @FXML
    private CurrencyField tfValue;

    @FXML
    private TextField tfDescription;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnClear;

    @FXML
    private RadioButton rbListBetween;

    @FXML
    private DatePicker dtInitialDate;

    @FXML
    private DatePicker dtFinalDate;

    @FXML
    private RadioButton rbListAll;

    @FXML
    private TextField tfSumIncome;

    @FXML
    private TextField tfSumExpenses;

    @FXML
    private TextField tfBalance;

    final EventHandler<ActionEvent> btnSaveHandler = (ActionEvent event) -> {

        if (missingRequiredFields())
            return;
        FinancialTransaction financialTransaction = new FinancialTransaction(
                Date.valueOf(dtTransaction.getValue()),
                cbVehicle.getValue(),
                cbCategory.getValue(),
                tfValue.getAmount(),
                tfDescription.getText());
        if (!tfId.getText().isEmpty())
            financialTransaction.setId(Integer.parseInt(tfId.getText()));
        transactionDAO.Save(financialTransaction);
        clearFields(false);
        listTransactions();
    };

    final EventHandler<ActionEvent> btnClearHandler = (ActionEvent event) -> clearFields(true);

    final EventHandler<MouseEvent> lvMouseClicked = (MouseEvent event) -> {
        FinancialTransaction transaction = tableView.getSelectionModel().getSelectedItem();
        if (transaction == null)
            return;
        tfId.setText(String.valueOf(transaction.getId()));
        dtTransaction.setValue(transaction.getDate().toLocalDate());
        cbVehicle.setValue(transaction.getVehicle());
        cbCategory.setValue(transaction.getCategory());
        tfDescription.setText(transaction.getDescription());
        tfValue.setAmount(transaction.getValue());
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        transactionDAO = new FinancialTransactionDAO(new ConnectionFactory().getNewConnection());

        initializeComboBox();

        tfDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Functions.isNotNull(newValue) && ((newValue.length() > 99)))
                tfDescription.setText(oldValue);
        });

        btnSave.setOnAction(btnSaveHandler);
        btnClear.setOnAction(btnClearHandler);
        tableView.setOnMouseClicked(lvMouseClicked);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDate.setCellFactory(column -> new TableCell<FinancialTransaction, Date>() {
            private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(format.format(item));
                }
            }
        });
        colPlate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehicle().getPlate()));
        colVehicle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVehicle().getDescription()));
        colVehicle.getStyleClass().add("longtext");
        colCategory.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory().getDescription()));
        colCategory.getStyleClass().add("longtext");
        colValue.setCellValueFactory(column -> {
            SimpleStringProperty property = new SimpleStringProperty();
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            property.setValue(numberFormat.format(column.getValue().getValue()));
            return property;
        });
        colValue.getStyleClass().add("currencyfield");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.getStyleClass().add("longtext");
        addButtonToTable();

        final ToggleGroup toggleGroup = new ToggleGroup();
        rbListAll.setToggleGroup(toggleGroup);
        rbListBetween.setToggleGroup(toggleGroup);
        rbListBetween.setSelected(true);

        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                listTransactions();
            }
        });

        dtInitialDate.setValue(LocalDate.from(LocalDateTime.now().withDayOfMonth(1)));
        dtFinalDate.setValue(LocalDate.from(LocalDateTime.now()));

        dtInitialDate.valueProperty().addListener((observable, oldDate, newDate)-> listTransactions());

        dtFinalDate.valueProperty().addListener((observable, oldDate, newDate)-> listTransactions());

        tfSumIncome.setStyle("-fx-text-inner-color: darkgreen;");
        tfSumExpenses.setStyle("-fx-text-inner-color: firebrick;");

        clearFields(true);
        listTransactions();
    }

    private void listTransactions() {
        List<FinancialTransaction> transactions;
        if (rbListAll.isSelected())
            transactions = transactionDAO.ListAll();
        else
            transactions = transactionDAO.ListByPeriod(
                    Date.valueOf(dtInitialDate.getValue()), Date.valueOf(dtFinalDate.getValue()));
        tableView.setItems(FXCollections.observableArrayList(transactions));
        double sumIncomes = sumTransactions(transactions.stream().filter(
                                            obj -> obj.getCategory().getType() == Constants.cIncome)
                                            .collect(Collectors.toList()));
        double sumExpenses = sumTransactions(transactions.stream().filter(
                                            obj -> obj.getCategory().getType() == Constants.cExpense)
                                            .collect(Collectors.toList()));
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
        tfSumIncome.setText(format.format(sumIncomes));
        tfSumExpenses.setText(format.format(sumExpenses));
        tfBalance.setText(format.format(sumIncomes - sumExpenses));
        if ((sumIncomes - sumExpenses) < 0f)
            tfBalance.setStyle("-fx-text-inner-color: red;");
        else
            tfBalance.setStyle("-fx-text-inner-color: green;");
    }

    private double sumTransactions(List<FinancialTransaction> pTransactions) {
        double sum = 0;
        for (FinancialTransaction transaction : pTransactions)
            sum += transaction.getValue();
        return sum;
    }

    private void initializeComboBox() {
        try(Connection connection = new ConnectionFactory().getNewConnection()) {
            new AutoCompleteCombobox<>(cbVehicle, FXCollections.observableArrayList(
                    new VehicleDAO(connection).ListAll()));
            new AutoCompleteCombobox<>(cbCategory, FXCollections.observableArrayList(
                    new FinancialCategoryDAO(connection).ListAll()));
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    private void clearFields(boolean pAllFields) {
        tfId.clear();
        if (pAllFields) {
            dtTransaction.setValue(LocalDate.from(LocalDateTime.now()));
            cbVehicle.setValue(null);
        }
        cbCategory.setValue(null);
        tfValue.clear();
        tfDescription.clear();
        clearErrorClass(cbVehicle);
        clearErrorClass(cbCategory);
        clearErrorClass(tfValue);
    }

    private void addButtonToTable() {
        DeleteButtonOnTableColumn<FinancialTransaction> deleteButtonOnTableColumn = new DeleteButtonOnTableColumn<>();
        colBtnRemove.setCellFactory(deleteButtonOnTableColumn.getDeleteButton(new FinancialTransactionDAO()));
    }

    private boolean missingRequiredFields() {
        boolean isMissing = isRequiredFieldMissing(cbVehicle);
        isMissing = isRequiredFieldMissing(cbCategory) || isMissing;
        isMissing = isRequiredFieldMissing(tfValue) || isMissing;
        return isMissing;
    }
}
