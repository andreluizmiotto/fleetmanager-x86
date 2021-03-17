package br.com.fleetmanager;

import br.com.fleetmanager.controller.WindowController;
import br.com.fleetmanager.utils.FXMLEnum;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage pStage) throws Exception{
        setPrimaryStage(pStage);
        WindowController.openWindow(FXMLEnum.Enum.MAINWINDOW, pStage);
    }

    public static void main(String[] args) {

        launch(args);
    }

    public static Stage getPrimaryStage() {
        return stage;
    }

    private void setPrimaryStage(Stage pStage) {
        Main.stage = pStage;
    }

}