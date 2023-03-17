package com.devpinda.autoclicker.minecraftautoclickerjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AutoclickerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AutoclickerApplication.class.getResource("autoclicker-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 980, 540);
        stage.setTitle("Autoclicker");
        stage.setScene(scene);
        try {
            AutoclickerController autoclickerController = fxmlLoader.getController();
            stage.setOnCloseRequest(event -> {
                autoclickerController.stopClicker();
                System.exit(0);
            });
        } catch(NullPointerException ex){
            ex.printStackTrace();
        }
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}