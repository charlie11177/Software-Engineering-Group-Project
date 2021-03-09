package main.controllers;

import javafx.fxml.FXML;
import main.Airport;
import main.Calculator;
import main.Model;
import main.PhysicalRunWay;

import java.util.ArrayList;

public class LeftScreenController {

    @FXML private AirportConfigController airportConfigController;
    @FXML private RunwayConfigController runwayConfigController;
    @FXML private ObstacleConfigController obstacleConfigController;

    @FXML
    private void initialize() {
        Model.airportConfigController = airportConfigController;
        Model.runwayConfigController = runwayConfigController;
    }

    @FXML
    private void calculateButtonClick() {
        Airport airport = new Airport("Heathrow","HTR",new ArrayList<PhysicalRunWay>());
        Model.airports.add(airport);
        airportConfigController.addAirports(airport);
        Calculator.recalculate();
    }
}
