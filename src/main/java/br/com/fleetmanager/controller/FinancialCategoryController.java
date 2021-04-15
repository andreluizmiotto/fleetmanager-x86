package br.com.fleetmanager.controller;

import br.com.fleetmanager.connection.ConnectionFactory;
import br.com.fleetmanager.dao.FinancialCategoryDAO;
import br.com.fleetmanager.model.FinancialCategory;
import br.com.fleetmanager.utils.FXMLFunctions;
import br.com.fleetmanager.utils.Functions;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static br.com.fleetmanager.utils.FXMLStaticFunctions.clearErrorClass;
import static br.com.fleetmanager.utils.FXMLStaticFunctions.isRequiredFieldMissing;

public class FinancialCategoryController implements Initializable {

    private FinancialCategoryDAO financialCategoryDAO;

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

    final EventHandler<ActionEvent> btnSaveHandler = (ActionEvent event) -> {

        if (missingRequiredFields())
            return;
        FinancialCategory financialCategory = new FinancialCategory(
                tfCategory.getText(),
                (short) cbType.getSelectionModel().getSelectedIndex());
        if (!tfId.getText().isEmpty())
            financialCategory.setId(Integer.parseInt(tfId.getText()));
        financialCategoryDAO.Save(financialCategory);
        clearFields();
        listCategories();
    };

    final EventHandler<ActionEvent> btnClearHandler = (ActionEvent event) -> clearFields();

    final EventHandler<MouseEvent> lvMouseClicked = (MouseEvent event) -> {
        FinancialCategory financialCategory = tableView.getSelectionModel().getSelectedItem();
        if (financialCategory == null)
            return;
        tfId.setText(String.valueOf(financialCategory.getId()));
        tfCategory.setText(financialCategory.getDescription());
        cbType.getSelectionModel().select(financialCategory.getType());
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        financialCategoryDAO = new FinancialCategoryDAO(new ConnectionFactory().getNewConnection());

        btnSave.setOnAction(btnSaveHandler);
        btnClear.setOnAction(btnClearHandler);
        tableView.setOnMouseClicked(lvMouseClicked);

        tfCategory.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Functions.isNotNull(newValue) && ((newValue.length() > 99)))
                tfCategory.setText(oldValue);
            clearErrorClass(tfCategory);
        });

        cbType.getItems().addAll(
                "Receita", "Despesa");
        cbType.valueProperty().addListener((options, oldValue, newValue) -> clearErrorClass(cbType));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.getStyleClass().add("longtext");
        colType.setCellValueFactory(new PropertyValueFactory<>("typeToString"));
        addButtonToTable();

        listCategories();
    }

    private void listCategories() {
        tableView.setItems(FXCollections.observableArrayList(financialCategoryDAO.ListAll()));
    }

    private void clearFields() {
        tfId.clear();
        tfCategory.clear();
        cbType.getSelectionModel().select(-1);
    }

    private void addButtonToTable() {
        FXMLFunctions<FinancialCategory> fxmlFunctions = new FXMLFunctions<>();
        colBtnRemove.setCellFactory(fxmlFunctions.getDeleteButton(financialCategoryDAO));
    }

    private boolean missingRequiredFields() {
        boolean isMissing = isRequiredFieldMissing(tfCategory);
        isMissing = isRequiredFieldMissing(cbType) || isMissing;
        return isMissing;
    }
}
