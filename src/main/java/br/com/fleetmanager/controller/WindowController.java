package br.com.fleetmanager.controller;

import br.com.fleetmanager.Main;
import br.com.fleetmanager.utils.Constants;
import br.com.fleetmanager.utils.FXMLEnum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowController {

    public static void openWindow(final FXMLEnum.Enum pFXML) throws IOException {
        Stage stage = new Stage();
        stage.initOwner(Main.getPrimaryStage());
        openWindow(pFXML, stage);
    }

    public static void openWindow(final FXMLEnum.Enum pFXML, final Stage pStage) throws IOException {
        Parent root = loadFXML(FXMLEnum.getFXMLFile(pFXML));
        pStage.setTitle(FXMLEnum.getFXMLTittle(pFXML));
        pStage.getIcons().add(new Image(Main.class.getResourceAsStream(Constants.sIconsFolder + FXMLEnum.getFXMLIcon(pFXML))));
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(Main.class.getResource(Constants.sStylesFolder + "FXMLStyles.css").toExternalForm());
        pStage.setScene(scene);
        pStage.show();
    }

    private static Parent loadFXML(String pFXMLName) throws IOException {
        FXMLLoader vFXMLLoader = new FXMLLoader();
        vFXMLLoader.setLocation(Main.class.getClassLoader().getResource(pFXMLName + ".fxml"));
        return vFXMLLoader.load();
    }

}
