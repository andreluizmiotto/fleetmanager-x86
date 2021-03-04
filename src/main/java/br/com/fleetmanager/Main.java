package br.com.fleetmanager;

import br.com.fleetmanager.controller.WindowController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage pStage) throws Exception{
        WindowController.openWindow("mainWindow", pStage);
    }

    public static void main(String[] args) {

        launch(args);
    }

}