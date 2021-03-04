package br.com.fleetmanager.controller;

import br.com.fleetmanager.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowController {

    public static void openWindow(final String pFXMLName) throws IOException {
        openWindow(pFXMLName, new Stage());
    }

    public static void openWindow(final String pFXMLName, Stage pStage) throws IOException {
        Parent root = loadFXML(pFXMLName);
        pStage.setTitle("Hello World");
        pStage.setScene(new Scene(root, 1280, 720));
        pStage.show();
    }

    private static Parent loadFXML(String pFXMLName) throws IOException {
        FXMLLoader vFXMLLoader = new FXMLLoader();
        vFXMLLoader.setLocation(Main.class.getClassLoader().getResource(pFXMLName + ".fxml"));
        return vFXMLLoader.load();
    }

}
