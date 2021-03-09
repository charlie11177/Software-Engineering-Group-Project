package main.controllers;

import javafx.fxml.FXML;
import main.Airport;
import main.Model;
import main.PhysicalRunWay;


public class LeftScreenController {

    @FXML private AirportConfigController airportConfigController;
    @FXML private RunwayConfigController runwayConfigController;
    @FXML private ObstacleConfigController obstacleConfigController;

    @FXML
    private void initialize() {
//        runwayChoiceBoxChanger();
//        runwayDetailsChanger(runwayConfigController.runwayChoiceBox.getValue());
//
//        airportConfigController.aiportChoiceBox
//                .getSelectionModel()
//                .selectedItemProperty()
//                .addListener( (observable, oldValue, newValue) -> runwayChoiceBoxChanger());
//
//        runwayConfigController.runwayChoiceBox
//                .getSelectionModel()
//                .selectedItemProperty()
//                .addListener( (observable, oldValue, newValue) -> runwayDetailsChanger(newValue));
    }

    private void runwayChoiceBoxChanger() {
        String name = airportConfigController.getSelectedAirport();
        Airport airport = Model.getAirportByName(name);
        if(airport != null && airport.getRunways() != null)
            runwayConfigController.populateRunwayNames(airport.getRunways());
        else{
            runwayConfigController.runwayChoiceBox.getItems().clear();
            runwayConfigController.clearRunwayDetails();
        }
    }

    private void runwayDetailsChanger(String value) {
        String name = airportConfigController.getSelectedAirport();
        //System.out.println(name);
        Airport airport = Model.getAirportByName(name);
        if(airport.getRunways() != null){
            for(PhysicalRunWay r : airport.getRunways()){
                if(r.toString().equals(value)){
                    runwayConfigController.populateRunwayDetails(r);
                    runwayConfigController.currentRunway = r;
                }
            }
        }
    }

    @FXML
    private void calculateButtonClick() {
        System.out.println(airportConfigController.getItem());
    }
}
