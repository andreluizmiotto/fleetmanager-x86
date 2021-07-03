package br.com.fleetmanager.controller;

import br.com.fleetmanager.connection.implementation.ConnectionFactory;
import br.com.fleetmanager.dao.FinancialCategoryDAO;
import br.com.fleetmanager.interfaces.controller.IControllerInputFields;
import br.com.fleetmanager.model.FinancialCategory;
import br.com.fleetmanager.utils.Functions;
import br.com.fleetmanager.utils.fxmlFunctions.DeleteButtonOnTableColumn;
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

import static br.com.fleetmanager.utils.fxmlFunctions.FXMLStaticFunctions.clearErrorClass;
import static br.com.fleetmanager.utils.fxmlFunctions.FXMLStaticFunctions.isRequiredFieldMissing;

public class FinancialCategoryController implements Initializable, IControllerInputFields {

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

    private void addButtonToTable() {
        DeleteButtonOnTableColumn<FinancialCategory> deleteButtonOnTableColumn = new DeleteButtonOnTableColumn<>();
        colBtnRemove.setCellFactory(deleteButtonOnTableColumn.getDeleteButton(financialCategoryDAO));
    }

    @Override
    public boolean missingRequiredFields() {
        boolean isMissing = isRequiredFieldMissing(tfCategory);
        isMissing = isRequiredFieldMissing(cbType) || isMissing;
        return isMissing;
    }

    @Override
    public void clearFields() {
        tfId.clear();
        tfCategory.clear();
        cbType.getSelectionModel().select(-1);
    }

    @Override
    public void StorePreferences() {}

    @Override
    public void LoadPreferences() {}

}
