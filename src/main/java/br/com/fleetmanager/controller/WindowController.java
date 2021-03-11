package br.com.fleetmanager.controller;

import br.com.fleetmanager.Main;
import br.com.fleetmanager.utils.FXMLEnum;
import br.com.fleetmanager.utils.FXMLEnumClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowController {

    public static void openWindow(final FXMLEnum pFXML) throws IOException {
        Stage stage = new Stage();
        stage.initOwner(Main.getPrimaryStage());
        openWindow(pFXML, stage);
    }

    public static void openWindow(final FXMLEnum pFXML, final Stage pStage) throws IOException {
        Parent root = loadFXML(FXMLEnumClass.getFXMLFile(pFXML));
        pStage.setTitle(FXMLEnumClass.getFXMLTittle(pFXML));
        pStage.setScene(new Scene(root, 1280, 720));
        pStage.show();
    }

    private static Parent loadFXML(String pFXMLName) throws IOException {
        FXMLLoader vFXMLLoader = new FXMLLoader();
        vFXMLLoader.setLocation(Main.class.getClassLoader().getResource(pFXMLName + ".fxml"));
        return vFXMLLoader.load();
    }

}
