package br.com.fleetmanager.utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class FXMLStaticFunctions {

    private static final String cCssErrorFlag = "error";

    public static void clearErrorClass(TextField pTField) {
        if (!pTField.getText().isBlank())
            pTField.getStyleClass().remove(cCssErrorFlag);
    }

    public static void clearErrorClass(ComboBox pComboBox) {
        if ((pComboBox.getValue() != null))
            pComboBox.getStyleClass().remove(cCssErrorFlag);
    }

    public static boolean validateField(TextField pTField) {
        if (pTField.getText().isBlank()) {
            pTField.getStyleClass().add("error");
            return false;
        }
        return true;
    }

    public static boolean validateField(ComboBox pComboBox) {
        if (pComboBox.getValue() != null)
            return true;
        pComboBox.getStyleClass().add("error");
        return false;
    }

}
