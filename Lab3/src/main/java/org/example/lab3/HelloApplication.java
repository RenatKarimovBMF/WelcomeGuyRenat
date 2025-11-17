package org.example.lab3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException
    {
        var givenData = getParameters().getRaw();
        int maxAttempts = 3;
        int waitingSeconds = 15;
        /*int maxAttempts = Integer.parseInt(givenData.get(0));
        int waitingSeconds = Integer.parseInt(givenData.get(1));*/
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 240);
        HelloController controller = fxmlLoader.getController();
        controller.setData(maxAttempts,waitingSeconds);
        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}