package br.com.fleetmanager.controller;

import br.com.fleetmanager.Launcher;
import br.com.fleetmanager.utils.Constants;
import br.com.fleetmanager.utils.FXMLEnum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WindowController {

    public static void openWindow(final FXMLEnum.Enum pFXML) throws IOException {
        Stage stage = new Stage();
        stage.initOwner(Launcher.getPrimaryStage());
        openWindow(pFXML, stage);
    }

    public static void openWindow(final FXMLEnum.Enum pFXML, final Stage pStage) throws IOException {
        Parent root = loadFXML(FXMLEnum.getFXMLFile(pFXML));
        pStage.setTitle(FXMLEnum.getFXMLTittle(pFXML));
        pStage.getIcons().add(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream(Constants.sIconsFolder + FXMLEnum.getFXMLIcon(pFXML)))));
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(Launcher.class.getResource(Constants.sStylesFolder + "FXMLStyles.css")).toExternalForm());
        pStage.setScene(scene);
        pStage.show();
    }

    private static Parent loadFXML(String pFXMLName) throws IOException {
        FXMLLoader vFXMLLoader = new FXMLLoader();
        vFXMLLoader.setLocation(Launcher.class.getClassLoader().getResource(pFXMLName + ".fxml"));
        return vFXMLLoader.load();
    }

}
