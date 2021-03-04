module br.com.fleetmanager {
    requires javafx.controls;
    requires javafx.fxml;

    opens br.com.fleetmanager.controller to javafx.fxml;
    exports br.com.fleetmanager;
}