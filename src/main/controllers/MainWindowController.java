package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import main.App;
import main.model.Airport;
import main.model.Model;
import main.model.Obstacle;
import main.model.XMLParser;

import java.io.File;
import java.util.ArrayList;


public class MainWindowController {

//    @FXML private LeftScreenController leftScreenController;
//    @FXML private CenterScreenController centerScreenController;
//    @FXML private RightScreenController rightScreenController;
//    @FXML private AirportConfigController airportConfigController;
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
        try {
            ArrayList<Airport> importedAirports = xmlParser.importAirports(xmlFile);
            ArrayList<Obstacle> importedObstacles = xmlParser.importObstacle(xmlFile);

            if (!importedAirports.isEmpty())
            {
                Model.airports = importedAirports;
                Model.console.addLog("--- Imported Airports and Runways ---");
                for (Airport a : Model.airports) {
                    Model.console.addLog(a.toString());
                    Model.console.addLog(a.getRunways().toString());
                }
                Model.console.addLog("--- Finished Importing Airports ---");
            }

            if (!importedObstacles.isEmpty())
            {
                Model.obstacles = importedObstacles;
                Model.console.addLog("--- Imported Obstacles ---");
                for (Obstacle o : Model.obstacles) {
                    Model.console.addLog(o.getName());
                }
                Model.console.addLog("--- Finished Importing Obstacles ---");
            }
            Model.console.addLogWithoutTime("Imported " + importedAirports.size() + " Airports");
            Model.console.addLogWithoutTime("Imported " + importedObstacles.size() + " Obstacles");
            resetMenus();   // whole UI has to be reset and user actions removed, since the data is rewritten
        }
        catch (Exception e) {
            Model.console.addLog("Failed an import");
            AlertController.showErrorAlert("Import Failed");
        }
    }

    public void importAirports(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;
        try {
            ArrayList<Airport> importedAirports = xmlParser.importAirports(xmlFile);
            if (!importedAirports.isEmpty()) {
                for(Airport a1 : Model.airports) {
                    importedAirports.removeIf(a2 -> a1.getName().equals(a2.getName()) || a1.getCode().equals(a2.getCode()));
                }
                Model.airports.addAll(importedAirports);
                if(importedAirports.isEmpty())
                    Model.console.addLog("--- No unique airports were found, no data was imported ---");
                else {
                    Model.console.addLog("--- Imported Airports and Runways ---");
                    for (Airport a : importedAirports) {
                        Model.console.addLog(a.toString());
                        Model.console.addLog(a.getRunways().toString());
                    }
                    Model.console.addLog("--- Finished Importing ---");
                }
//                resetMenus(); // doesnt need to call this since it only appends data
                updateUI();
            } else {
                throw (new Exception("Failed import"));
            }
        }  catch (Exception e) {
            Model.console.addLog("Failed an import - Invalid Airports XML selected");
            AlertController.showErrorAlert("Import Failed");
        }
    }

    public void importObstacles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;
        try {
            ArrayList<Obstacle> importedObstacles = xmlParser.importObstacle(xmlFile);
            if (!importedObstacles.isEmpty()) {
                //Model.resetConfig();
                for(Obstacle o1 : Model.obstacles) {
                    importedObstacles.removeIf(o2 -> o1.getName().equals(o2.getName()));
                }
                Model.obstacles.addAll(importedObstacles);
                if(importedObstacles.isEmpty())
                    Model.console.addLog("--- No unique obstacles were found, no data was imported ---");
                else {
                    Model.console.addLog("--- Imported Objects ---");
                    for (Obstacle o : importedObstacles) {
                        Model.console.addLog(o.getName());
                    }
                    Model.console.addLog("--- Finished Importing ---");
                }
//                resetMenus(); // doesnt need to call this since it only appends data
                updateUI();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Model.console.addLog("Failed an import - Invalid Obstacles XML selected");
            AlertController.showErrorAlert("Import Failed");
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
            t.setExpanded(true);
        }
        if (currentPane != null)
            currentPane.setExpanded(true);
        else Model.leftScreenController.accordion.getExpandedPane().setExpanded(false);
    }

    public void exportConfig(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;

        try {
            xmlParser.exportAll(xmlFile);
        }
        catch (Exception e) {
            AlertController.showErrorAlert("Failed to export the current airports");
        }
    }

    public void exportAirports(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;

        try {
            xmlParser.exportAirports(xmlFile);
        }
        catch (Exception e) {
            AlertController.showErrorAlert("Failed to export the current airports");
        }
    }

    public void exportObstacles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files" , "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;

        try {
            xmlParser.exportObstacles(xmlFile);
        }
        catch (Exception e) {
            AlertController.showErrorAlert("Failed to export the current obstacles");
        }
    }


    public void defaultFontClick() {
        Model.setCurrentFontSize(FontSize.DEFAULT);
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
        Model.setCurrentFontSize(FontSize.MEDIUM);
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
        Model.setCurrentFontSize(FontSize.LARGE);
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
