package main.controllers;

import javafx.fxml.FXML;

public class LeftScreenController {

    @FXML private AirportConfigController airportConfigController;
    @FXML private RunwayConfigController runwayConfigController;
    @FXML private ObstacleConfigController obstacleConfigController;

    @FXML
    private void initialize() {

    }

    @FXML
    private void calculateButtonClick() {
        System.out.println(airportConfigController.getItem());
    }
}
