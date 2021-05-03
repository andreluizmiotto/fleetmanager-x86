package br.com.fleetmanager;

import br.com.fleetmanager.controller.WindowController;
import br.com.fleetmanager.utils.FXMLEnum;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    private static Launcher instance ;
    private static Stage stage;

    @Override
    public void start(Stage pStage) throws Exception{
        setPrimaryStage(pStage);
        WindowController.openWindow(FXMLEnum.Enum.MAINWINDOW, pStage);
        instance = this;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return stage;
    }

    public static Launcher getInstance(){
        return instance;
    }

    private void setPrimaryStage(Stage pStage) {
        Launcher.stage = pStage;
    }

}