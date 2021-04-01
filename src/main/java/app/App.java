package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import controllers.AlertController;
import controllers.FontSize;
import model.Model;

import java.io.*;
import java.net.URL;
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
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        URL url = new File("src/main/resources/view/MainWindow.fxml").toURI().toURL();
        root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(768);
        stage.setWidth(1366);
        stage.setMinHeight(768);
        stage.setMinWidth(850);
        stage.setTitle("Runway Re-declaration Tool");
        stage.show();
        setupFonts();
        stage.setOnCloseRequest(event -> {
            Boolean save = AlertController.saveProgressAlert();
            if(save == null) {
                event.consume();
            } else if (save) {
                save();
            }
        });
    }

    private void setupFonts() {
        if (Model.getCurrentFontSize() == FontSize.DEFAULT){
            Model.mainWindowController.defaultFontClick();
            System.out.println("Font size restored: DEFAULT");
        } else if (Model.getCurrentFontSize() == FontSize.MEDIUM) {
            Model.mainWindowController.mediumFontClick();
            System.out.println("Font size restored: MEDIUM");
        } else if (Model.getCurrentFontSize() == FontSize.LARGE) {
            Model.mainWindowController.largeFontClick();
            System.out.println("Font size restored: LARGE");
        } else {
            System.err.println("No valid fontsize type: " + Model.getCurrentFontSize() + ", proceeding with default setup...");
            Model.mainWindowController.defaultFontClick();
        }
    }

    @Override
    public void init() throws Exception {
        /*
        TODO:
         Load:
         airports/runways/obstacles +
         fontsize : default, medium, large
         colorblind: true/false
         currentairport:
         currentrunway:
         currentobstacle:
         obstacle placed: true/false
         showCalculations: true/false
         */
        super.init();
        Scanner in;
        try {
            in = new Scanner(new FileReader("config.log"));
        } catch (FileNotFoundException e) {
            System.out.println("No configuration file found, proceeding with default setup...");
            Model.setCurrentFontSize(FontSize.DEFAULT);
            return;
        }
        StringBuilder sb = new StringBuilder();
        while(in.hasNext()) {
            sb.append(in.next());
        }
        in.close();
        String result = sb.toString();
        String[] lines  = result.split(";");
        for (String line : lines){
            if(line.matches("Fontsize:.*")){
                String size = line.split(":")[1];
                switch (size){
                    case "DEFAULT":
                        Model.setCurrentFontSize(FontSize.DEFAULT);
                        break;
                    case "MEDIUM":
                        Model.setCurrentFontSize(FontSize.MEDIUM);
                        break;
                    case "LARGE":
                        Model.setCurrentFontSize(FontSize.LARGE);
                        break;
                    default:
                        System.err.println("Error occurred when initializing: " + size + ", proceeding with default setup...");
                        Model.setCurrentFontSize(FontSize.DEFAULT);
                }
            } else {
                System.err.println("Error when reading configuration file: " + line + ", proceeding with default setup...");
                Model.setCurrentFontSize(FontSize.DEFAULT);
            }
        }
    }

    public void save(){
        /*
        TODO:
         Save:
         airports/runways/obstacles +
         currentairport:
         currentrunway:
         currentobstacle:
         obstacle placed: true/false
         showCalculations: true/false
         */
        System.out.println("Saving progress...");
    }

    @Override
    public void stop() {
        /*
        TODO:
         Save:
         fontsize : default, medium, large
         colorblind: true/false
         */
        File file = new File("config.log");
        FileWriter writer = null; // true to append, false to overwrite.
        try {
            writer = new FileWriter(file, false);
            String saveData = "Fontsize:"+ Model.getCurrentFontSize()+";";
            writer.write(saveData);
            writer.close();
            System.out.println("Saved data " + saveData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
