package br.com.fleetmanager.utils.fxmlFunctions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import static br.com.fleetmanager.utils.fxmlFunctions.FXMLStaticFunctions.clearErrorClass;

/* By JulianG:
    https://stackoverflow.com/questions/19924852/autocomplete-combobox-in-javafx */
public class AutoCompleteCombobox<T> implements EventHandler<KeyEvent> {

    private final ComboBox<T> comboBox;
    private final ObservableList<T> data;
    private boolean moveCaretToPos = false;
    private int caretPos;

    public AutoCompleteCombobox(final ComboBox<T> comboBox, final ObservableList<T> dataList) {
        this.comboBox = comboBox;
        data = dataList;

        this.comboBox.itemsProperty().setValue(dataList);
        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(t -> comboBox.hide());
        this.comboBox.setOnKeyReleased(AutoCompleteCombobox.this);
        this.comboBox.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T obj) {
                if (obj == null)
                    return "";
                return obj.toString();
            }

            @Override
            public T fromString(final String string) {
                return data.stream().filter(obj -> obj.toString().equals(string)).findFirst().orElse(null);
            }
        });
        this.comboBox.valueProperty().addListener((composant, oldValue, newValue) -> clearErrorClass(comboBox));
    }

    @Override
    public void handle(KeyEvent event) {

        if(event.getCode() == KeyCode.UP) {
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.DOWN) {
            if(!comboBox.isShowing()) {
                comboBox.show();
            }
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.BACK_SPACE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        } else if(event.getCode() == KeyCode.DELETE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        }

        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.isControlDown() || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
            return;
        }

        ObservableList<T> list = FXCollections.observableArrayList();
        for (T obj : data) {
            if (obj.toString().toLowerCase().contains(
                    AutoCompleteCombobox.this.comboBox.getEditor().getText().toLowerCase())) {
                list.add(obj);
            }
        }
        String t = comboBox.getEditor().getText();

        comboBox.setItems(list);
        comboBox.getEditor().setText(t);
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if(!list.isEmpty()) {
            comboBox.show();
        }
    }

    private void moveCaret(int textLength) {
        if(caretPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }

}