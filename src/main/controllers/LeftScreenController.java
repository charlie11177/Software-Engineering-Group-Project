package main.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import main.Airport;
import main.Model;
import main.PhysicalRunWay;

import java.util.ArrayList;
import java.util.List;

public class LeftScreenController {

    @FXML private AirportConfigController airportConfigController;
    @FXML private RunwayConfigController runwayConfigController;
    @FXML private ObstacleConfigController obstacleConfigController;

    @FXML
    private void initialize() {
        runwayChoiceBoxChanger();
        airportConfigController.aiportChoiceBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener( (observable, oldValue, newValue) -> runwayChoiceBoxChanger());
        runwayConfigController.runwayConfig.expandedProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue == true && Model.airports.isEmpty()) {
                runwayConfigController.disableElements(runwayConfigController.runwayMainMenu);
            }
            else if (newValue == true){
                runwayConfigController.enableElements(runwayConfigController.runwayMainMenu);
            }
            System.out.println("Airports:" + Model.airports.size());
        });
        runwayConfigController.runwayChoiceBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener( (observable, oldValue, newValue) -> runwayDetailsChanger(newValue));
    }

    private void runwayChoiceBoxChanger() {
        String name = airportConfigController.getSelectedAirport();
        Airport airport = Model.getAirportByName(name);
        if(airport != null && airport.getRunways() != null)
            runwayConfigController.populateRunwayNames(airport.getRunways());
        else
            runwayConfigController.runwayChoiceBox.getItems().clear();
    }

    private void runwayDetailsChanger(String value) {
        String name = airportConfigController.getSelectedAirport();
        //System.out.println(name);
        for(Airport a : Model.airports)
            System.out.println(a.getName());
        Airport airport = Model.getAirportByName(name);
        if(airport.getRunways() != null){
            for(PhysicalRunWay r : airport.getRunways()){
                if(r.toString().equals(value)){
                    runwayConfigController.populateRunwayDetails(r);
                }
            }
        }
    }

    @FXML
    private void calculateButtonClick() {
        System.out.println(airportConfigController.getItem());
    }
}
