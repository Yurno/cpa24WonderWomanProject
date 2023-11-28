package com.example.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TaskManager extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // will load fxml file
        Parent root = new FXMLLoader().load(getClass().getResource("login_screen.fxml"));

        // makes a scene
        Scene scene = new Scene(root);

        stage.setScene(scene);

        int width = 1000, height = 800;
        stage.setTitle("Task Manager");
        stage.setWidth(width);
        stage.setHeight(height);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
