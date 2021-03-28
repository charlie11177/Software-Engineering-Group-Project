package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TitledPane;
import javafx.stage.FileChooser;
import main.model.Airport;
import main.model.Model;
import main.model.Obstacle;
import main.model.XMLParser;

import java.io.File;
import java.util.ArrayList;


public class MainWindowController {

    @FXML private LeftScreenController leftScreenController;
    @FXML private CenterScreenController centerScreenController;
    @FXML private RightScreenController rightScreenController;
    @FXML private AirportConfigController airportConfigController;
    @FXML private MenuBar menuBar;

    private XMLParser xmlParser;

    public MainWindowController() {
        xmlParser = new XMLParser();
    }

    @FXML private void initialize() {
    }

    @FXML private void importXML (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        /*
         * Try/catch and if statements to check whether an airport or obstacle has been imported
         * and if the import was successful
         */
        try {
            ArrayList<Airport> importedAirports = xmlParser.importAirports(xmlFile);
            if (!importedAirports.isEmpty()) {
                Model.resetConfig();
                Model.airports = importedAirports;
                Model.console.addLog("--- Imported Airports and Runways ---");
                for (Airport a : Model.airports) {
                    Model.console.addLog(a.toString());
                    Model.console.addLog(a.getRunways().toString());
                }
                Model.console.addLog("--- Finished Importing ---");
            }
            else {
                ArrayList<Obstacle> importedObstacles = xmlParser.importObstacle(xmlFile);
                if (!importedObstacles.isEmpty()) {
                    Model.obstacles = importedObstacles;
                    Model.console.addLog("--- Imported Objects ---");
                    for (Obstacle o : Model.obstacles)
                    {
                        Model.console.addLog(o.getName());
                    }
                    Model.console.addLog("--- Finished Importing ---");
                }
                else {
                    throw new Exception();
                }
            }
            resetMenus();
        }
        catch (Exception e) {
            Model.console.addLog("Failed an import");
            Alert failedImport = new Alert(Alert.AlertType.WARNING, "Import Failed");
            failedImport.setHeaderText(null);
            failedImport.showAndWait();
        }
    }

    /**
     * Resets the menu when new values are succcessfuly imported
     */
    private void resetMenus(){
        Model.resetConfig();
        TitledPane currentPane = Model.leftScreenController.accordion.getExpandedPane();
        for(TitledPane t : Model.leftScreenController.accordion.getPanes()){
            t.setExpanded(true);
        }
        if (currentPane != null)
            currentPane.setExpanded(true);
        else Model.leftScreenController.accordion.getExpandedPane().setExpanded(false);
    }

    public void exportAirports(ActionEvent event) {
        if(Model.leftScreenController.accordion.getExpandedPane()!= null)
            Model.leftScreenController.accordion.getExpandedPane().setExpanded(false);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());

        try {
            xmlParser.exportAirports(xmlFile);
        }
        catch (Exception e) {
            Alert failedExport = new Alert(Alert.AlertType.WARNING, "Failed to export the current airports");
            failedExport.setHeaderText(null);
            failedExport.showAndWait();
        }
    }
}
