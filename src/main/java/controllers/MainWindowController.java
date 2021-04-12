package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import app.App;
import model.Airport;
import model.Model;
import model.Obstacle;
import model.XMLParser;

import java.io.File;
import java.util.ArrayList;


public class MainWindowController {

    @FXML private MenuBar menuBar;

    private XMLParser xmlParser;

    public MainWindowController() {
        xmlParser = new XMLParser();
    }

    @FXML private void initialize() {
        Model.mainWindowController = this;
    }

    @FXML private void importXML (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;
        Model.console.addLog("--- Importing Configuration from: " + xmlFile.getName() + " ---" );
        try {
            ArrayList<Airport> importedAirports = xmlParser.importAirports(xmlFile);
            ArrayList<Obstacle> importedObstacles = xmlParser.importObstacle(xmlFile);

            if (!importedAirports.isEmpty())
            {
                Model.airports = importedAirports;
                Model.console.addLogWithoutTime("--- Imported Airports and Runways ---");
                for (Airport a : Model.airports) {
                    Model.console.addLogWithoutTime(a.toString());
                    Model.console.addLogWithoutTime(a.getRunways().toString());
                }
            } else {
                Model.console.addLogWithoutTime("--- No Airports were found to import ---");
                Model.airports.clear();
            }
            Model.console.addLogWithoutTime("--- Finished Importing Airports ---");

            Model.console.addLogWithoutTime("--- Imported Obstacles ---");
            if (!importedObstacles.isEmpty())
            {
                Model.obstacles = importedObstacles;
                for (Obstacle o : Model.obstacles) {
                    Model.console.addLogWithoutTime(o.getName());
                }
            } else {
                Model.console.addLogWithoutTime("--- No Obstacles were found to import ---");
                Model.obstacles.clear();
            }
            if(importedObstacles.isEmpty() && importedAirports.isEmpty()) {
                throw (new Exception("Failed import"));
            }
            Model.console.addLogWithoutTime("--- Finished Importing Obstacles ---");
//            Model.console.addLogWithoutTime("Imported " + importedAirports.size() + " Airports");
//            Model.console.addLogWithoutTime("Imported " + importedObstacles.size() + " Obstacles");
            Model.console.addLog("--- Finished importing Configuration from: " + xmlFile.getName() + " ---" );
            resetMenus();   // whole UI has to be reset and user actions removed, since the data is rewritten
        }
        catch (Exception e) {
            Model.console.addLog("Failed an import - Invalid Configuration XML selected");
            controllers.AlertController.showErrorAlert("Import Failed");
        }
    }

    public void importAirports(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;
        Model.console.addLog("--- Importing Airports from: " + xmlFile.getName() + " ---" );
        try {
            ArrayList<Airport> importedAirports = xmlParser.importAirports(xmlFile);
            if (!importedAirports.isEmpty()) {
                for(Airport a1 : Model.airports) {
                    importedAirports.removeIf(a2 -> a1.getName().equals(a2.getName()) || a1.getCode().equals(a2.getCode()));
                }
                Model.airports.addAll(importedAirports);
                if(importedAirports.isEmpty())
                    Model.console.addLogWithoutTime("--- No unique airports were found, no data was imported ---");
                else {
                    Model.console.addLogWithoutTime("--- Imported Airports and Runways ---");
                    for (Airport a : importedAirports) {
                        Model.console.addLogWithoutTime(a.toString());
                        Model.console.addLogWithoutTime(a.getRunways().toString());
                    }
                }
                Model.console.addLogWithoutTime("--- Finished importing airports from: " + xmlFile.getName() + " ---" );
//                resetMenus(); // doesnt need to call this since it only appends data
                updateUI();
            } else {
                throw (new Exception("Failed import"));
            }
        }  catch (Exception e) {
            Model.console.addLog("Failed an import - Invalid Airports XML selected");
            controllers.AlertController.showErrorAlert("Import Failed");
        }
    }

    public void importObstacles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;
        Model.console.addLog("--- Importing Obstacles from: " + xmlFile.getName() + " ---" );
        try {
            ArrayList<Obstacle> importedObstacles = xmlParser.importObstacle(xmlFile);
            if (!importedObstacles.isEmpty()) {
                //Model.resetConfig();
                for(Obstacle o1 : Model.obstacles) {
                    importedObstacles.removeIf(o2 -> o1.getName().equals(o2.getName()));
                }
                Model.obstacles.addAll(importedObstacles);
                if(importedObstacles.isEmpty())
                    Model.console.addLogWithoutTime("--- No unique obstacles were found, no data was imported ---");
                else {
                    Model.console.addLogWithoutTime("--- Imported Objects ---");
                    for (Obstacle o : importedObstacles) {
                        Model.console.addLogWithoutTime(o.getName());
                    }
                }
                Model.console.addLog("--- Finished importing Obstacles from: " + xmlFile.getName() + " ---" );
//                resetMenus(); // doesnt need to call this since it only appends data
                updateUI();
            } else {
                throw new Exception("Failed import");
            }
        } catch (Exception e) {
            Model.console.addLog("Failed an import - Invalid Obstacles XML selected");
            controllers.AlertController.showErrorAlert("Import Failed");
        }
    }

    /**
     * Removes all data selected by user, only used when importing configuration
     */
    private void resetMenus(){
        Model.resetConfig();
        Model.leftScreenController.calculateAllowedMode();
        updateUI();
    }

    /**
     * Updates the UI, so the imported data are visible
      */
    private void updateUI(){
        TitledPane currentPane = Model.leftScreenController.accordion.getExpandedPane();
        for(TitledPane t : Model.leftScreenController.accordion.getPanes()){
            // only doing this because each titledpane has a listener, which updates the data when a pane is expanded
            t.setExpanded(true);
        }
        // expands the pane which was chosen by user before expanding
        if (currentPane != null)
            currentPane.setExpanded(true);
        else Model.leftScreenController.accordion.getExpandedPane().setExpanded(false);
    }

    public void exportConfig() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;

        try {
            Model.console.addLog("--- Exporting Configuration ---");
            xmlParser.exportAll(xmlFile);
            Model.console.addLog("--- Finished exporting to: " + xmlFile.getName() + " ---" );
        }
        catch (Exception e) {
            controllers.AlertController.showErrorAlert("Failed to export the current Configuration");
            Model.console.addLog("--- Failed to export the Configuration ---");
        }
    }

    public void exportAirports() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;

        try {
            Model.console.addLog("--- Exporting Airports ---");
            xmlParser.exportAirports(xmlFile);
            Model.console.addLog("--- Finished exporting to: " + xmlFile.getName() + " ---" );
        }
        catch (Exception e) {
            controllers.AlertController.showErrorAlert("Failed to export the current Airports");
            Model.console.addLog("--- Failed to export the current Airports ---");
        }
    }

    public void exportObstacles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;

        try {
            Model.console.addLog("--- Exporting Obstacles ---");
            xmlParser.exportObstacles(xmlFile);
            Model.console.addLog("--- Finished exporting to: " + xmlFile.getName() + " ---" );
        }
        catch (Exception e) {
            controllers.AlertController.showErrorAlert("Failed to export the current Obstacles");
            Model.console.addLog("--- Failed to export the current Obstacles ---");
        }
    }


    public void defaultFontClick() {
        Model.setCurrentFontSize(controllers.FontSize.DEFAULT);
        App.root.setStyle("-fx-font-size: 12 px");
        Model.centerScreenController.console.setFont(Font.font("Courier New",12.5));
        Model.rightScreenController.changeFontSize(12.5);
        Model.rightScreenController.topTableView.setPrefHeight(78);
        Model.rightScreenController.bottomTableView.setPrefHeight(78);
        Model.rightScreenController.rightScreen.setPrefWidth(296);
        Model.rightScreenController.originalValuesLabel.setFont(Font.font(20));
        Model.rightScreenController.recalculatedValuesLabel.setFont(Font.font(20));
        Model.rightScreenController.breakdownLabel.setFont(Font.font(20));
        if(App.stage.getHeight() < 768) App.stage.setHeight(768);
        App.stage.setMinHeight(768);
        Model.console.addLog("Fontsize set to: Default");
    }

    public void mediumFontClick() {
        Model.setCurrentFontSize(controllers.FontSize.MEDIUM);
        App.root.setStyle("-fx-font-size: 13 px");
        Model.centerScreenController.console.setFont(Font.font("Courier New",13));
        Model.rightScreenController.changeFontSize(13);
        Model.rightScreenController.topTableView.setPrefHeight(87);
        Model.rightScreenController.bottomTableView.setPrefHeight(87);
        Model.rightScreenController.rightScreen.setPrefWidth(296);
        Model.rightScreenController.originalValuesLabel.setFont(Font.font(21));
        Model.rightScreenController.recalculatedValuesLabel.setFont(Font.font(21));
        Model.rightScreenController.breakdownLabel.setFont(Font.font(21));
        if(App.stage.getHeight() < 832)  App.stage.setHeight(832);
        App.stage.setMinHeight(820);
        Model.console.addLog("Fontsize set to: Medium");
    }

    public void largeFontClick() {
        Model.setCurrentFontSize(controllers.FontSize.LARGE);
        App.root.setStyle("-fx-font-size: 14.5 px");
        Model.centerScreenController.console.setFont(Font.font("Courier New",14.5));
        Model.rightScreenController.changeFontSize(14);
        Model.rightScreenController.topTableView.setPrefHeight(95);
        Model.rightScreenController.bottomTableView.setPrefHeight(95);
        Model.rightScreenController.rightScreen.setPrefWidth(310);
        Model.rightScreenController.originalValuesLabel.setFont(Font.font(22.5));
        Model.rightScreenController.recalculatedValuesLabel.setFont(Font.font(22.5));
        Model.rightScreenController.breakdownLabel.setFont(Font.font(22.5));
        if(App.stage.getHeight() < 882) App.stage.setHeight(882);
        App.stage.setMinHeight(820);
        Model.console.addLog("Fontsize set to: Large");
    }
}
