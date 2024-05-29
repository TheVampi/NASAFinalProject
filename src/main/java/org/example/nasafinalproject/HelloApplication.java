package org.example.nasafinalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/Login/login-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/Register/register-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/AdminDashboard/admindashboard-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/NASAMedia/nasamedia-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/APOD/APOD_MainView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/APOD/APOD_MainView.fxml"));


        Scene scene = new Scene(fxmlLoader.load(), 363, 525);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}