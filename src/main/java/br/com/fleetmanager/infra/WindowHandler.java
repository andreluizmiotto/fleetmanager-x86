package br.com.fleetmanager.infra;

import br.com.fleetmanager.Launcher;
import br.com.fleetmanager.interfaces.controller.IControllerBase;
import br.com.fleetmanager.utils.Constants;
import br.com.fleetmanager.utils.FXMLEnum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WindowHandler {

    public static void openWindow(final FXMLEnum.Enum pFXML) throws IOException {
        Stage stage = new Stage();
        stage.initOwner(Launcher.getPrimaryStage());
        openWindow(pFXML, stage);
    }

    public static void openWindow(final FXMLEnum.Enum pFXML, final Stage pStage) throws IOException {
        pStage.setTitle(FXMLEnum.getFXMLTittle(pFXML));
        pStage.getIcons().add(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream(Constants.sIconsFolder + FXMLEnum.getFXMLIcon(pFXML)))));

        FXMLLoader loader = getLoader(FXMLEnum.getFXMLFile(pFXML));
        pStage.setScene(getScene(loader.load()));
        pStage.setOnHidden(e -> ((IControllerBase) loader.getController()).storePreferences());
        pStage.show();
    }

    private static Scene getScene(Parent pParent) {
        Scene scene = new Scene(pParent, 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(
                Launcher.class.getResource(Constants.sStylesFolder + "FXMLStyles.css")).toExternalForm());
        return scene;
    }

    private static FXMLLoader getLoader(String pFXMLName) throws IOException {
        FXMLLoader vFXMLLoader = new FXMLLoader();
        vFXMLLoader.setLocation(Launcher.class.getClassLoader().getResource(pFXMLName + ".fxml"));
        return vFXMLLoader;
    }

}
