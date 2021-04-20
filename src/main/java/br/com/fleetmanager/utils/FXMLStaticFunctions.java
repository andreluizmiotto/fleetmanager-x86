package br.com.fleetmanager.utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class FXMLStaticFunctions {

    private static final String cCssErrorFlag = "error";

    public static void clearErrorClass(TextField pTField) {
        if (!pTField.getText().isBlank())
            pTField.getStyleClass().removeAll(cCssErrorFlag);
    }

    public static void clearErrorClass(ComboBox pComboBox) {
        if ((pComboBox.getValue() != null))
            pComboBox.getStyleClass().removeAll(cCssErrorFlag);
    }

    public static boolean isRequiredFieldMissing(TextField pTField) {
        if (pTField.getText().isBlank()) {
            pTField.getStyleClass().add("error");
            return true;
        }
        return false;
    }

    public static boolean isRequiredFieldMissing(ComboBox pComboBox) {
        if (pComboBox.getValue() != null)
            return false;
        pComboBox.getStyleClass().add("error");
        return true;
    }

}
