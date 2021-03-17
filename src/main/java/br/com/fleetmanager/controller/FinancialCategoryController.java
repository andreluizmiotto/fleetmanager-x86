package br.com.fleetmanager.controller;

import br.com.fleetmanager.connection.ConnectionFactory;
import br.com.fleetmanager.dao.FinancialCategoryDAO;
import br.com.fleetmanager.model.FinancialCategory;
import br.com.fleetmanager.utils.FXMLFunctions;
import br.com.fleetmanager.utils.Functions;
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

public class FinancialCategoryController implements Initializable {

    @FXML
    private TableView<FinancialCategory> tableView;

    @FXML
    private TableColumn<FinancialCategory, Integer> colId;

    @FXML
    private TableColumn<FinancialCategory, String> colDescription;

    @FXML
    private TableColumn<FinancialCategory, String> colType;

    @FXML
    private TableColumn<FinancialCategory, Void> colBtnRemove;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfCategory;

    @FXML
    private ComboBox<String> cbType;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnClear;

    EventHandler<ActionEvent> btnSaveHandler = (ActionEvent event) -> {

        if (!validateFields())
            return;
        try(Connection connection = new ConnectionFactory().getNewConnection()) {
            FinancialCategoryDAO financialCategoryDAO = new FinancialCategoryDAO(connection);
            FinancialCategory financialCategory = new FinancialCategory(
                    tfCategory.getText(),
                    (short) cbType.getSelectionModel().getSelectedIndex());
            if (!tfId.getText().isEmpty())
                financialCategory.setId(Integer.parseInt(tfId.getText()));
            financialCategoryDAO.Save(financialCategory);
            clearFields();
            listCategories();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    };

    EventHandler<ActionEvent> btnClearHandler = (ActionEvent event) -> {
        clearFields();
    };

    EventHandler<MouseEvent> lvMouseClicked = (MouseEvent event) -> {
        FinancialCategory financialCategory = tableView.getSelectionModel().getSelectedItem();
        if (financialCategory == null)
            return;
        tfId.setText(String.valueOf(financialCategory.getId()));
        tfCategory.setText(financialCategory.getDescription());
        cbType.getSelectionModel().select(financialCategory.getType());
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnSave.setOnAction(btnSaveHandler);
        btnClear.setOnAction(btnClearHandler);
        tableView.setOnMouseClicked(lvMouseClicked);

        tfCategory.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Functions.isNull(newValue) && ((newValue.length() > 99)))
                tfCategory.setText(oldValue);
            clearErrorClass(tfCategory);
        });

        cbType.getItems().addAll(
                "Receita", "Despesa");
        cbType.valueProperty().addListener((options, oldValue, newValue) -> {
            if ((cbType.getSelectionModel().getSelectedIndex() >= 0) && cbType.getStyleClass().contains("error"))
                cbType.getStyleClass().remove("error");
        });

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeToString"));
        addButtonToTable();

        listCategories();
    }

    private void listCategories() {
        try {
            tableView.setItems(categories());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private ObservableList categories() throws SQLException {
        try(Connection connection = new ConnectionFactory().getNewConnection()) {
            return FXCollections.observableArrayList(new FinancialCategoryDAO(connection).ListAll());
        }
    }

    private void clearFields() {
        tfId.clear();
        tfCategory.clear();
        cbType.getSelectionModel().select(-1);
    }

    private void addButtonToTable() {
        FXMLFunctions<FinancialCategory> fxmlFunctions = new FXMLFunctions<>();
        colBtnRemove.setCellFactory(fxmlFunctions.getDeleteButton(new FinancialCategoryDAO()));
    }

    private void clearErrorClass(TextField pTField) {
        if (!pTField.getText().isBlank() && pTField.getStyleClass().contains("error"))
            pTField.getStyleClass().remove("error");
    }

    private boolean validateFields() {
        if (tfCategory.getText().isBlank())
            tfCategory.getStyleClass().add("error");
        if ((short) cbType.getSelectionModel().getSelectedIndex() < 0)
            cbType.getStyleClass().add("error");
        return !(tfCategory.getText().isBlank() ||
                ((short) cbType.getSelectionModel().getSelectedIndex() < 0));
    }
}
