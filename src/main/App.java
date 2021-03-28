package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.model.Model;

public class App extends Application {

    public static Parent root = null;
    public static Stage stage = null;

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage s) throws Exception {
        App.stage = s;
        root = FXMLLoader.load(getClass().getResource("view/MainWindow.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(768);
        stage.setWidth(1366);
        stage.setMinHeight(768);
        stage.setMinWidth(850);
        stage.setTitle("Runway Re-declaration Tool");
        stage.show();
        Model.centerScreenController.console.setFont(Font.font("Consolas",12.5));
        Model.rightScreenController.changeFontSize(12.5);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }
}
