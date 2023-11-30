package com.example.project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button login;
    @FXML
    private Button register;
    @FXML
    private Text status;

    @FXML
    private void handleRegisterButtonAction() {
        String user = username.getText();
        String pass = password.getText();

        try {
            File file = new File(user + ".txt"); // Create a file with username as its name
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                writer.write(pass); // Write the password to the file
                writer.close();

                status.setText("Registration Successful!");
            } else {
                status.setText("User already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            status.setText("Error: Unable to register account.");
        }
    }

    @FXML
    private void handleLoginButtonAction() {
        String user = username.getText();
        String pass = password.getText();

        if (checkCredentials(user, pass)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
                Parent root = loader.load();

                TaskManagerController controller = loader.getController();
                controller.setUsername(user);
                controller.loadTasksForUser(user); // Load tasks for the user

                Stage stage = (Stage) login.getScene().getWindow();
                stage.setScene(new Scene(root));

                // window size
                stage.setWidth(1000);
                stage.setHeight(800);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                status.setText("Error: Unable to load main menu.");
            }
        } else {
            status.setText("Login Failed: Invalid username or password.");
        }
    }

    private boolean checkCredentials(String username, String password) {
        File file = new File(username + ".txt");
        if (file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                String storedPassword = scanner.nextLine();
                scanner.close();

                return storedPassword.equals(password);
            } catch (IOException e) {
                e.printStackTrace();
                status.setText("Error: Unable to access file.");
            }
        }
        return false;
    }
}
