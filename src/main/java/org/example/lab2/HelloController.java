package org.example.lab2;
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

public class HelloController
{
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label welcomeText;
    private ArrayList<User> users= new ArrayList<>();
    public void setUsers(ArrayList<User> users)
    {
        this.users=users;
    }

    @FXML
    protected void onLoginButtonClick()
    {
        boolean userFound=false;
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            welcomeText.setText("Please enter username and password");
            return;
        }
        for(User u:users)
        {
            if(u.getName().equals(username)&&u.getPassword().equals(password))
            {
                userFound=true;
                break;
            }
        }

        if (userFound)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("welcome-w.fxml"));
                Parent root = loader.load();
                Stage welcomeStage = new Stage();
                welcomeStage.setTitle("Welcome!");
                welcomeStage.setScene(new Scene(root));
                welcomeStage.setResizable(false);
                welcomeStage.setOnCloseRequest(event ->
                {
                    Platform.exit();
                    System.exit(0);
                });
                welcomeStage.show();
                Stage loginStage = (Stage) usernameField.getScene().getWindow();
                loginStage.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        } else {
            welcomeText.setText("Wrong username or password");
        }

    }
}