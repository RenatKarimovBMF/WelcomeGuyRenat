package org.example.lab3;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Platform;

public class HelloController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label welcomeText;

    private ArrayList<User> users = new ArrayList<>();

    // new: manager that handles attempts + blocking
    private LoginManager loginManager;

    private int maxAttempts;
    private int waitingSeconds;

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setData(int n, int t) {
        this.maxAttempts = n;
        this.waitingSeconds = t;

        // 🔹 Load users from file
        UsersApp loader = new UsersApp();
        ArrayList<User> loadedUsers = loader.loadUsers();
        this.users = loadedUsers;

        // 🔹 Create LoginManager with:
        //    - valid users
        //    - maxAttempts
        //    - waitingSeconds (block duration)
        loginManager = new LoginManager(loadedUsers, maxAttempts, waitingSeconds);
    }

    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            welcomeText.setText("Please enter username and password");
            return;
        }

        if (loginManager == null) {
            welcomeText.setText("Internal error: login manager not initialized");
            return;
        }

        // 🔹 Ask LoginManager if this login is allowed
        LoginResult result = loginManager.tryLogin(username, password);

        switch (result.getType()) {
            case SUCCESS -> {
                // open welcome window (FXML version)
                try {
                    FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("welcome-w.fxml"));
                    Parent root = loader.load();
                    Stage welcomeStage = new Stage();
                    welcomeStage.setTitle("Welcome!");
                    welcomeStage.setScene(new Scene(root));
                    welcomeStage.setResizable(false);
                    welcomeStage.setOnCloseRequest(event -> {
                        Platform.exit();
                        System.exit(0);
                    });
                    welcomeStage.show();

                    Stage loginStage = (Stage) usernameField.getScene().getWindow();
                    loginStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case BLOCKED -> {
                long seconds = (result.getRemainingBlockMillis() + 999) / 1000;
                welcomeText.setText("You are blocked. Try again in " + seconds + " seconds");
            }
            case WRONG_CREDENTIALS, USER_NOT_FOUND -> {
                welcomeText.setText("Wrong username or password");
            }
        }
    }
}
