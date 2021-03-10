package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import main.*;

import java.util.ArrayList;

public class LeftScreenController {

    public Button calculateButton;
    public Accordion accordion;

    @FXML
    private void initialize() {
        Model.leftScreenController = this;
    }

    @FXML
    private void calculateButtonClick() {
        try{
            Model.currentAirport = Model.airportConfigController.currentAirport;
            Model.currentRunway = Model.runwayConfigController.currentRunway;
            boolean placeObstacle = Model.obstacleConfigController.isPlaceObstacleSelected();
            if(placeObstacle && obstaclePlaced()){
                Model.currentRunway.setObstacle(Model.currentObstacle);
//                Model.console.addLog("Obstacle: " + Model.obstacleConfigController.currentObstacle.getName() + " Position: " + Model.obstacleConfigController.currentObstacle.getPosition().getDirectionFromCL());
//                Model.console.addLog("Airport: " + Model.airportConfigController.currentAirport.toString());
//                Model.console.addLog("Runway: " + Model.runwayConfigController.currentRunway.toString());
//                System.out.println(Model.currentRunway.getLeftRunway().getTODA() + "  " +  Model.currentRunway.getRightRunway().getTODA());
                Calculator.recalculate();
                Model.rightScreenController.allCalculationsButtonTA.setText(Model.awayCalculationBreakdown);
                Model.rightScreenController.allCalculationsTopTA.setText(Model.towardsCalculationBreakdown);
            } else if (!placeObstacle){
                Alert noObject = new Alert(Alert.AlertType.WARNING, "No object has been placed on the runway!");
                noObject.setHeaderText(null);
                noObject.showAndWait();
            }
        } catch (NullPointerException e){
            System.out.println("Error");
        }
    }

    private boolean obstaclePlaced() {
        ObstacleConfigController oC = Model.obstacleConfigController;
        if (oC.distanceFromLTF.getText().equals("") || oC.distanceFromRTF.getText().equals("") || (oC.distanceFromCLTF.getText().equals(""))) {
            Alert noObject = new Alert(Alert.AlertType.WARNING, "Obstacle placement is not specified!");
            noObject.setHeaderText(null);
            noObject.showAndWait();
            return false;
        } else {
            int distancefromL = Integer.parseInt(oC.distanceFromLTF.getText());
            int distanceFromR = Integer.parseInt(oC.distanceFromRTF.getText());
            int distanceFromCL = Integer.parseInt(oC.distanceFromCLTF.getText());
            String dirFromCL = oC.dirFromCLChoiceBox.getValue();
            Position position = new Position(distancefromL,distanceFromR,distanceFromCL,dirFromCL);
            oC.currentObstacle.setPosition(position);
            Model.currentObstacle = oC.currentObstacle;
            return true;
        }
    }

}
