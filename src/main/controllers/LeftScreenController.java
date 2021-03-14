package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import main.*;

public class LeftScreenController {

    public Button calculateButton;
    public Accordion accordion;

    @FXML
    private void initialize() {
        Model.leftScreenController = this;
    }

    @FXML
    private void calculateButtonClick() {
        if(Model.currentAirport == null){
            AlertController.showWarningAlert("No airport specified !");
        } else if (Model.currentRunway == null) {
            AlertController.showWarningAlert("No runway specified !");
        } else if (Model.currentObstacle == null) {
            AlertController.showWarningAlert("No obstacle specified !");
        } else if (!Model.obstaclePlaced) {
            AlertController.showWarningAlert("No obstacle has been placed on the runway!");
        } else {
            Model.currentRunway.setObstacle(Model.currentObstacle);
            Model.console.addLog("------------Selected config------------");
            Model.console.addLog("Airport: " + Model.currentAirport.toString());
            Model.console.addLog("Runway: " + Model.currentRunway.toString());
            Model.console.addLog("Obstacle: " + Model.currentObstacle.getName() + " " + Model.currentObstacle.getPosition().getDistanceToLeft());
            Model.console.addLog("---------------------------------------");
            Calculator.recalculate();
            Model.rightScreenController.allCalculationsTowardsTA.setText(Model.calculationsBreakdownTowards);
            Model.rightScreenController.allCalculationsAwayTA.setText(Model.calculationsBreakDownAway);
            Model.rightScreenController.populateTables();
        }
    }

}
