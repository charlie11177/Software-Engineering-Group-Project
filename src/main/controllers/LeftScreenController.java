package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import main.model.Calculator;
import main.model.CalculatorOutput;
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
        CalculatorOutput calculatorOutput = null;
        if (!calculateAllowed){
            calculateAllowedMode();
            Model.console.addLog("Editing: Calculated values removed!");
            return;
        }
        if(Model.currentAirport == null){
            AlertController.showWarningAlert("No airport specified !","Make sure that you've selected airport, runway and obstacle from the left menu to perform calculations");
        } else if (Model.currentRunway == null) {
            AlertController.showWarningAlert("No runway specified !","Make sure that you've selected airport, runway and obstacle from the left menu to perform calculations");
        } else if (Model.currentObstacle == null) {
            AlertController.showWarningAlert("No obstacle specified !","Make sure that you've selected airport, runway and obstacle from the left menu to perform calculations");
        } else if (!Model.obstaclePlaced) {
            AlertController.showWarningAlert("No obstacle has been placed on the runway!","Make sure that you've placed an obstacle on the runway");
        } else {
            Model.console.addLog("Calculate button clicked!");
            Model.currentRunway.setObstacle(Model.currentObstacle);
            Model.console.addLogWithoutTime("-------------------------- Selected config --------------------------");
            Model.console.addLogWithoutTime("Airport: " + Model.currentAirport.toString());
            PhysicalRunWay temp = Model.currentRunway;
            Model.console.addLogWithoutTime("Physical Runway: " + temp.toString() + "  ID: " + temp.getRunwayID() + "  Placed Obstacle: " + temp.getObstacle().getName());
            Model.console.addLogWithoutTime("Left Runway: " +  temp.getLeftRunway().getData());
            Model.console.addLogWithoutTime("Right Runway: " + temp.getRightRunway().getData());
            Model.console.addLogWithoutTime("Obstacle " + Model.currentObstacle.toString());
            Model.console.addLogWithoutTime("---------------------------------------------------------------------");

            calculatorOutput = Calculator.recalculate(Model.currentRunway);
            Model.originalRunwayLeft = Model.currentRunway.getLeftRunway();
            Model.recalculatedRunwayLeft = calculatorOutput.getRunwayLeft().getRecalculatedRunway();
            Model.originalRunwayRight = Model.currentRunway.getRightRunway();
            Model.recalculatedRunwayRight = calculatorOutput.getRunwayRight().getRecalculatedRunway();

            Model.rightScreenController.populateTables();
            Model.rightScreenController.populateBreakDowns(calculatorOutput);

            calculateNotAllowedMode();
        }
    }

}
