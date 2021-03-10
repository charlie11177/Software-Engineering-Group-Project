package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import main.Airport;
import main.Model;
import main.Obstacle;
import main.XMLParser;

import java.io.File;
import java.util.ArrayList;


public class MainWindowController {
    @FXML private LeftScreenController leftScreenController;
    @FXML private CenterScreenController centerScreenController;
    @FXML private RightScreenController rightScreenController;
    @FXML private AirportConfigController airportConfigController;

    @FXML private MenuBar menuBar;

    XMLParser xmlParser = new XMLParser();

    @FXML private void initialize() {
    }

    @FXML private void importXML (ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());

        /*
         * Try/catch and if statements to check whether an airport or obstacle has been imported
         * and if the import was successful
         * TODO: Figure out how to update the choicebox to show the updated model list
         */
        try {
            ArrayList<Airport> importedAirports = xmlParser.importAirports(xmlFile);
            if (!importedAirports.isEmpty()) {
                Model.airports = importedAirports;
                for (Airport a : Model.airports)
                {
                    System.out.println(a.getName());
                }
            }
            else {
                ArrayList<Obstacle> importedObstacles = xmlParser.importObstacle(xmlFile);
                if (!importedObstacles.isEmpty()) {
                    Model.obstacles = importedObstacles;
                }
                else {
                    throw new Exception();
                }
            }
        }
        catch (Exception e) {
            Alert failedImport = new Alert(Alert.AlertType.WARNING, "Import Failed");
            failedImport.setHeaderText(null);
            failedImport.showAndWait();
        }
    }
}
