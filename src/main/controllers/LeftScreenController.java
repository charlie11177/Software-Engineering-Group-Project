package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.*;

import java.util.ArrayList;

public class LeftScreenController {

    public Button calculateButton;

    @FXML
    private void initialize() {
        Model.leftScreenController = this;
    }

    @FXML
    private void calculateButtonClick() {
        try{
            Model.currentAirport = Model.airportConfigController.currentAirport;
            Model.currentRunway = Model.runwayConfigController.currentRunway;
            Model.currentObstacle = Model.obstacleConfigController.currentObstacle;
            boolean placeObstacle = Model.obstacleConfigController.isPlaceObstacleSelected();
            Model.currentRunway.setObstacle(Model.currentObstacle);
            Model.console.addLog("Airport: " + Model.airportConfigController.currentAirport.toString());
            Model.console.addLog("Runway: " + Model.runwayConfigController.currentRunway.toString());
            if(placeObstacle)
                Model.console.addLog("Obstacle: " + Model.obstacleConfigController.currentObstacle.getName() + " Position: " + Model.obstacleConfigController.currentObstacle.getPosition().getDirectionFromCL());
            else
                Model.console.addLog("Obstacle: " + Model.obstacleConfigController.currentObstacle.getName());
        } catch (NullPointerException e){
            System.out.println("Error");
        }
        Calculator.recalculate();
    }

}
