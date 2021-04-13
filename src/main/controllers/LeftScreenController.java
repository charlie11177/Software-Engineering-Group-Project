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
            AlertController.showWarningAlert("No airport specified !");
        } else if (Model.currentRunway == null) {
            AlertController.showWarningAlert("No runway specified !");
        } else if (Model.currentObstacle == null) {
            AlertController.showWarningAlert("No obstacle specified !");
        } else if (!Model.obstaclePlaced) {
            AlertController.showWarningAlert("No obstacle has been placed on the runway!");
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

            calculatorOutput = Calculator.recalculate();
            Model.calculationsBreakdownLeft = calculatorOutput.getRunwayLeft().getTotalBD();
            Model.originalRunwayLeft = Model.currentRunway.getLeftRunway();
            Model.recalculatedRunwayLeft = calculatorOutput.getRunwayLeft().getRecalculatedRunway();
            Model.calculationsBreakDownRight = calculatorOutput.getRunwayRight().getTotalBD();
            Model.originalRunwayRight = Model.currentRunway.getRightRunway();
            Model.recalculatedRunwayRight = calculatorOutput.getRunwayRight().getRecalculatedRunway();
            
            Model.rightScreenController.allCalculationsLeftTA.setText(Model.calculationsBreakdownLeft);
            Model.rightScreenController.allCalculationsRightTA.setText(Model.calculationsBreakDownRight);
            Model.rightScreenController.populateTables();
            Model.rightScreenController.toraTopTA.setText(calculatorOutput.getRunwayLeft().getBD("tora"));
            Model.rightScreenController.toraBottomTA.setText(calculatorOutput.getRunwayRight().getBD("tora"));
            Model.rightScreenController.todaTopTA.setText(calculatorOutput.getRunwayLeft().getBD("toda"));
            Model.rightScreenController.todaBottomTA.setText(calculatorOutput.getRunwayRight().getBD("toda"));
            Model.rightScreenController.ldaTopTA.setText(calculatorOutput.getRunwayLeft().getBD("lda"));
            Model.rightScreenController.ldaBottomTA.setText(calculatorOutput.getRunwayRight().getBD("lda"));
            Model.rightScreenController.asdaTopTA.setText(calculatorOutput.getRunwayLeft().getBD("asda"));
            Model.rightScreenController.asdaBottomTA.setText(calculatorOutput.getRunwayRight().getBD("asda"));
            calculateNotAllowedMode();
        }
    }

}
