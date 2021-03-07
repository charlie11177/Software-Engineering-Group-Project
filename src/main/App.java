package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(768);
        stage.setWidth(1366);
        stage.setMinHeight(768);
        stage.setMinWidth(850);
        stage.setTitle("Runway Re-declaration Tool");
        stage.show();
    }

    @Override
    public void init() throws Exception {
        System.out.println("init");
        super.init();
    }
}
