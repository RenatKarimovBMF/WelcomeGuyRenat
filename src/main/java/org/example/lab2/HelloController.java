package org.example.lab2;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

        if (userFound) {
            welcomeText.setText("Welcome, " + username + "!");

            // create popup window
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Welcome");
            alert.setHeaderText(null); // no big header text
            alert.setContentText("Welcome " + username + "!");

            alert.showAndWait(); // show dialog and wait until user closes it
        } else {
            welcomeText.setText("Wrong username or password");
        }

    }
}