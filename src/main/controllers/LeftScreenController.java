package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import main.model.Calculator;
import main.model.Model;
import main.model.PhysicalRunWay;

public class LeftScreenController {

    public Button calculateButton;
    public Accordion accordion;
    boolean calculateAllowed;

    @FXML
    private void initialize() {
        Model.leftScreenController = this;
        calculateAllowed = true;
    }

    public void calculateAllowedMode() {
        calculateButton.setText("Calculate");
        calculateAllowed = true;
        disableTilePanes(false, 1);
        Model.rightScreenController.clear();
    }

    public void calculateNotAllowedMode() {
//      if(accordion.getExpandedPane()!=null)
//          accordion.getExpandedPane().setExpanded(false);
        calculateAllowed = false;
        calculateButton.setText("Edit");
        disableTilePanes(true, 0.75);
    }

    private void disableTilePanes(boolean val, double opacity){
        Model.airportConfigController.airportRoot.setDisable(val);
        Model.runwayConfigController.runwayRoot.setDisable(val);
        Model.obstacleConfigController.obstacleRoot.setDisable(val);
        Model.airportConfigController.airportRoot.setOpacity(opacity);
        Model.runwayConfigController.runwayRoot.setOpacity(opacity);
        Model.obstacleConfigController.obstacleRoot.setOpacity(opacity);
    }


    @FXML
    private void calculateButtonClick() {
        if (!calculateAllowed){
            calculateAllowedMode();
            Model.console.addLog("Editing: Calculated values removed!");
            return;
        }
        if(Model.currentAirport == null){
            AlertController.showWarningAlert("No airport specified !");
        } else if (Model.currentRunway == null) {
            AlertController.showWarningAlert("No runway specified !");
        } else if (Model.currentObstacle == null) {
            AlertController.showWarningAlert("No obstacle specified !");
        } else if (!Model.obstaclePlaced) {
            AlertController.showWarningAlert("No obstacle has been placed on the runway!");
        } else {
            try{ Model.obstacleConfigController.saveObstacleDimensions(Model.currentObstacle); }
            catch (NumberFormatException e) {
                AlertController.showWarningAlert("No obstacle placement not properly specified !");
                return;
            }
            Model.console.addLog("Calculate button clicked!");
            Model.currentRunway.setObstacle(Model.currentObstacle);
            Model.console.addLogWithoutTime("------------------------------" +
                    "Selected config" +
                    "------------------------------");
            Model.console.addLogWithoutTime("Airport: " + Model.currentAirport.toString());
            PhysicalRunWay temp = Model.currentRunway;
            Model.console.addLogWithoutTime("Physical Runway: " + temp.toString() + "  ID: " + temp.getRunwayID() + "  Placed Obstacle: " + temp.getObstacle().getName());
            Model.console.addLogWithoutTime("Left Runway: " +  temp.getLeftRunway().getData());
            Model.console.addLogWithoutTime("Right Runway: " + temp.getRightRunway().getData());
            Model.console.addLogWithoutTime("Obstacle " + Model.currentObstacle.toString());
            Model.console.addLogWithoutTime("--------------------------------------" +
                    "-------------------------------------");
            Calculator.recalculate();
            Model.rightScreenController.allCalculationsTowardsTA.setText(Model.calculationsBreakdownTowards);
            Model.rightScreenController.allCalculationsAwayTA.setText(Model.calculationsBreakDownAway);
            Model.rightScreenController.populateTables();
            calculateNotAllowedMode();
        }
    }

}
