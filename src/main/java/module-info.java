module br.com.fleetmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;

    opens br.com.fleetmanager.model to javafx.base;
    opens br.com.fleetmanager.abstracts to javafx.base;
    opens br.com.fleetmanager.controller to javafx.fxml;

    exports br.com.fleetmanager;
    exports br.com.fleetmanager.utils.fxmlFields;
}