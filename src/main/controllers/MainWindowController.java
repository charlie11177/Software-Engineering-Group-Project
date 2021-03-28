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

    @FXML private LeftScreenController leftScreenController;
    @FXML private CenterScreenController centerScreenController;
    @FXML private RightScreenController rightScreenController;
    @FXML private AirportConfigController airportConfigController;
    @FXML private MenuBar menuBar;

    private XMLParser xmlParser;

    public MainWindowController() {
        xmlParser = new XMLParser();
    }

    @FXML private void initialize() { }


    // TODO: This method should be called when user only wants to import whole configuration
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

    // TODO: When user clicks to import airports but nothing else
    public void importAirports(ActionEvent actionEvent) {
    }

    // TODO: When user clicks to import obstacles but nothing else
    public void importObstacles(ActionEvent actionEvent) {
    }

    /**
     * Resets the menu when new values are succcessfuly imported call this method whenever something gets imported
     * and data in model is changed
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

    // TODO: When user clicks to export whole configuration
    public void exportConfig(ActionEvent actionEvent) {
    }

    // TODO: This method should be called when user only wants to export airports but nothing else
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

    // TODO: When user clicks to export obstacles but nothing else
    public void exportObstacles(ActionEvent actionEvent) {
    }


    @FXML
    private void defaultFontClick() {
        App.root.setStyle("-fx-font-size: 12 px");
        System.out.println(Model.centerScreenController.console.getFont().getSize());
        Model.centerScreenController.console.setFont(Font.font("Consolas",12.5));
        Model.rightScreenController.changeFontSize(12.5);
        Model.rightScreenController.topTableView.setPrefHeight(78);
        Model.rightScreenController.bottomTableView.setPrefHeight(78);
        Model.rightScreenController.rightScreen.setPrefWidth(296);
        if(App.stage.getHeight() < 768) App.stage.setHeight(768);
        App.stage.setMinHeight(768);
    }

    @FXML
    private void mediumFontClick() {
        App.root.setStyle("-fx-font-size: 13 px");
        Model.centerScreenController.console.setFont(Font.font("Consolas",13));
        Model.rightScreenController.changeFontSize(13);
        Model.rightScreenController.topTableView.setPrefHeight(87);
        Model.rightScreenController.bottomTableView.setPrefHeight(87);
        Model.rightScreenController.rightScreen.setPrefWidth(296);
        if(App.stage.getHeight() < 832)  App.stage.setHeight(832);
        App.stage.setMinHeight(832);
    }

    @FXML
    private void largeFontClick() {
        App.root.setStyle("-fx-font-size: 14.5 px");
        Model.centerScreenController.console.setFont(Font.font("Consolas",14.5));
        Model.rightScreenController.changeFontSize(14);
        Model.rightScreenController.topTableView.setPrefHeight(95);
        Model.rightScreenController.bottomTableView.setPrefHeight(95);
        Model.rightScreenController.rightScreen.setPrefWidth(310);
        if(App.stage.getHeight() < 882) App.stage.setHeight(882);
        App.stage.setMinHeight(882);
    }
}
