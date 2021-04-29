package app;

import controllers.AlertController;
import controllers.FontSize;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Model;

import java.io.*;
import java.util.Scanner;

public class App extends Application {

    public static Parent root = null;
    public static Stage stage = null;

    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage s) throws Exception {
        stage = s;
        //Icons made by Freepik "https://www.freepik.com" from Flaticon "https://www.flaticon.com/"
        stage.getIcons().add(new Image("icon.png"));
        //Compass edited from : "https://pngtree.com/so/compass" , pngtree.com
        FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource("view/MainWindow.fxml"));
        //URL url = new File("src/main/resources/view/MainWindow.fxml").toURI().toURL();
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(768);
        stage.setWidth(1366);
        stage.setMinHeight(768);
        stage.setMinWidth(850);
        stage.setTitle("Runway Re-declaration Tool");
        stage.show();
        Model.mainWindowController.defaultFontClick();

        try {
            Model.mainWindowController.importDefaultConfig(new File("defaultConfig.xml"));
        } catch (Exception e) {
            System.out.println("Default config not found");
        }

        stage.setOnCloseRequest(event -> {
            Boolean save = AlertController.saveProgressAlert();
            if(save == null) {
                event.consume();
            } else if (save) {
                save();
            }
        });
    }

    public void save(){
        Model.mainWindowController.exportConfigOnExit();
    }

    @Override
    public void stop() {
        /*
        TODO:
         Save:
         fontsize : default, medium, large
         colorblind: true/false
        File file = new File("config.log");
        File consoleData = new File ("console.log");
        FileWriter writer = null; // true to append, false to overwrite.
        try {
            writer = new FileWriter(file, false);
            String saveData = "Fontsize:"+ Model.getCurrentFontSize()+";";
            writer.write(saveData);
            writer.close();
            System.out.println("Saved config data: " + saveData);

            writer = new FileWriter(consoleData, false);
            String consoleContent = Model.centerScreenController.console.getText();
            writer.write(consoleContent);
            writer.close();
//            System.out.println("Saved console data:  " + consoleContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
