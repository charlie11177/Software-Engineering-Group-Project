package model;

import controllers.AlertController;

public class Calculator {
    private static PhysicalRunWay physicalRunway;
    private static model.Obstacle obstacle;

    public static void recalculate(){
        //obstacle = Model.currentObstacle;
        physicalRunway = model.Model.currentRunway;
        if(physicalRunway != null){
            obstacle = model.Model.currentObstacle;
            if(obstacle != null && obstacle.getPosition() != null){
                if (obstacle.getPosition().getDistanceToLeft() > obstacle.getPosition().getDistanceToRight()){
                    recalculateAWAY(physicalRunway.getRightRunway(), obstacle.getPosition().getDistanceToRight());
                    recalculateTOWARDS(physicalRunway.getLeftRunway(), obstacle.getPosition().getDistanceToLeft());
                }
                else {
                    recalculateAWAY(physicalRunway.getLeftRunway(), obstacle.getPosition().getDistanceToLeft());
                    recalculateTOWARDS(physicalRunway.getRightRunway(), obstacle.getPosition().getDistanceToRight());
                }
            }else {
                AlertController.showWarningAlert("No object has been placed on the runway!");
            }
        }else{
            AlertController.showWarningAlert("No runway has been selected!");
        }
    }

    private static void recalculateAWAY(model.LogicalRunWay runway, int distanceFromThreshold) { //(Take off Away, Landing Over)
        int tora, toda, lda, asda;
        String toraBD, todaBD, ldaBD, asdaBD, allBD;
        int blastAllowance = 300; //Blast protection is between 300-500 not sure whether this is defined by the user

        tora = runway.getTORA() - blastAllowance - distanceFromThreshold - runway.getThreshold();// TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold
        toda = tora + runway.getClearway();//(R) TORA + CLEARWAY
        lda = runway.getLDA() - distanceFromThreshold - 60 - (obstacle.getHeight() * 50);//LDA = Original LDA - Distance from Threshold – Strip End - Slope Calculation
        asda = tora + runway.getStopway();//ASDA = (R) TORA + STOPWAY

        toraBD = runway.getTORA() + " - " + blastAllowance + " - " + distanceFromThreshold + " - " + runway.getThreshold() + " = " + tora;
        todaBD = tora + " + " + runway.getClearway() + " = " + toda;
        ldaBD = runway.getLDA() + " - " + distanceFromThreshold + " - 60 - (" + obstacle.getHeight() + " * 50) = " + lda;
        asdaBD = tora + " + " + runway.getStopway() + " = " + asda;
        allBD = "--" + runway + "--" + "\n" +
                "TORA: " + toraBD + "\n" +
                "TODA: " + todaBD + "\n" +
                "LDA: " + ldaBD + "\n" +
                "ASDA: " + asdaBD;

        //Model.console.addLog("Runway: " + runway);
        //Model.console.addLog("TORA:" + tora + ", TODA:" + toda + ", LDA:" + lda + ", ASDA:" + asda);
        model.Model.calculationsBreakDownAway = allBD;
        model.Model.originalRunwayAway = runway;
        model.Model.recalculatedRunwayAway = new model.LogicalRunWay(runway.getDegree(), runway.getDirection(), tora, toda, asda, lda, runway.getThreshold());
    }

    private static void recalculateTOWARDS(model.LogicalRunWay runway, int distanceFromThreshold) { //(Take off Towards, Landing Towards)
        int tora, toda, asda, lda;
        String toraBD, todaBD, ldaBD, asdaBD, allBD;

        tora = distanceFromThreshold + runway.getThreshold() - (obstacle.getHeight() * 50) - 60;//TORA = Distance from Threshold + runwaythreshold - Slope Calculation - Strip End
        toda = tora;//TODA = (R) TORA
        lda = distanceFromThreshold - 240 - 60;//LDA = Distance from Threshold - RESA - Strip End
        asda = tora;//ASDA = (R) TORA

        if(runway.getThreshold() == 0) toraBD = distanceFromThreshold + " - (" + obstacle.getHeight() + " * 50) - 60 = " + tora;
        else toraBD = distanceFromThreshold + " + " + runway.getThreshold()  + "- (" + obstacle.getHeight() + " * 50) - 60 = " + tora;
        todaBD = "" + tora;
        ldaBD = distanceFromThreshold + " - 240 - 60 = " + lda;
        asdaBD = "" + tora;
        allBD = "--" + runway + "--" + "\n" +
                "TORA: " + toraBD + "\n" +
                "TODA: " + todaBD + "\n" +
                "LDA: " + ldaBD + "\n" +
                "ASDA: " + asdaBD;

        //Model.console.addLog("Runway: " + runway);
        //Model.console.addLog("TORA:" + tora + ", TODA:" + toda + ", LDA:" + lda + ", ASDA:" + asda);
        model.Model.calculationsBreakdownTowards = allBD;
        model.Model.originalRunwayTowards = runway;
        model.Model.recalculatedRunwayTowards = new model.LogicalRunWay(runway.getDegree(), runway.getDirection(), tora, toda, asda, lda, runway.getThreshold());
    }
}