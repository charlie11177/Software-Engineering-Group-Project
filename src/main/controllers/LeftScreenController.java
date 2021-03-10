package main.controllers;

import javafx.fxml.FXML;
import main.Airport;
import main.Calculator;
import main.Model;
import main.PhysicalRunWay;

import java.util.ArrayList;

public class LeftScreenController {

    @FXML
    private void initialize() {}

    @FXML
    private void calculateButtonClick() {
        //System.out.println(Model.airportConfigController.currentAirport.toString());
          //Model.console.addLog("Airport: " + Model.airportConfigController.currentAirport.toString());
          //Model.console.addLog("Runway: " + Model.runwayConfigController.currentRunway.toString());
        Calculator.recalculate();
          //Model.console.addLog("Obstacle: " + Model.obstacleConfigController.currentObstacle.getName() + " " + Model.currentObstacle.getPosition().getDirectionFromCL());
    }
}
