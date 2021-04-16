package main.model;

public class Calculator {
    private static PhysicalRunWay physicalRunway;
    private static Obstacle obstacle;

    private static int[] ahead;
    private static int[] towards;


    public static CalculatorOutput recalculate(PhysicalRunWay currentRunway){
        physicalRunway = currentRunway;
        CalculatorOutput output = new CalculatorOutput();
        if(physicalRunway != null){
            obstacle = physicalRunway.getObstacle();
            if(obstacle != null && obstacle.getPosition() != null){
                if (obstacle.getPosition().getDistanceToLeft() > obstacle.getPosition().getDistanceToRight()){
                    output.setRunwayRight(recalculateAWAY(physicalRunway.getRightRunway(), obstacle.getPosition().getDistanceToRight()));
                    output.setRunwayLeft(recalculateTOWARDS(physicalRunway.getLeftRunway(), obstacle.getPosition().getDistanceToLeft()));
                }
                else {
                    output.setRunwayLeft(recalculateAWAY(physicalRunway.getLeftRunway(), obstacle.getPosition().getDistanceToLeft()));
                    output.setRunwayRight(recalculateTOWARDS(physicalRunway.getRightRunway(), obstacle.getPosition().getDistanceToRight()));
                }
            }else {
                System.err.println("No object has been placed on the runway");
                //AlertController.showWarningAlert("No object has been placed on the runway!","Please choose an obstacle in the left menu and place it on the runway");
            }
        }else{
            System.err.println("No runway has been selected");

            //AlertController.showWarningAlert("No runway has been selected!","");
        }
        return output;
    }

    private static CalculatorOutput.RecalculatedRunwayOutput recalculateAWAY(LogicalRunWay runway, int distanceFromThreshold) {             //(Take off Away, Landing Over)
        int blastAllowance = 300;                                                                                                           //Blast protection is between 300-500 not sure whether this is defined by the user
        int tora, toda, lda, asda;
        String toraBD, todaBD, ldaBD, asdaBD, allBD;
        LogicalRunWay recalculatedRunway;

        tora = runway.getTORA() - blastAllowance - distanceFromThreshold - runway.getThreshold();                                           // TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold
        toda = tora + runway.getClearway();                                                                                                 //(R) TORA + CLEARWAY
        lda = runway.getLDA() - distanceFromThreshold - 60 - (obstacle.getHeight() * 50);                                                   //LDA = Original LDA - Distance from Threshold – Strip End - Slope Calculation
        asda = tora + runway.getStopway();                                                                                                  //ASDA = (R) TORA + STOPWAY
        recalculatedRunway = new LogicalRunWay(runway.getDegree(), runway.getDirection(), tora, toda, asda, lda, runway.getThreshold());

        toraBD = runway.getTORA() + " - " + blastAllowance + " - " + distanceFromThreshold + " - " + runway.getThreshold() + " = " + tora;
        todaBD = tora + " + " + runway.getClearway() + " = " + toda;
        ldaBD = runway.getLDA() + " - " + distanceFromThreshold + " - 60 - (" + obstacle.getHeight() + " * 50) = " + lda;
        asdaBD = tora + " + " + runway.getStopway() + " = " + asda;

        setRecalculatedAWAY(tora, toda, lda, asda);
        return new CalculatorOutput.RecalculatedRunwayOutput(recalculatedRunway, toraBD, todaBD, ldaBD, asdaBD);
    }

    private static CalculatorOutput.RecalculatedRunwayOutput recalculateTOWARDS(LogicalRunWay runway, int distanceFromThreshold) {          //(Take off Towards, Landing Towards)
        int tora, toda, asda, lda;
        String toraBD, todaBD, ldaBD, asdaBD, allBD;
        LogicalRunWay recalculatedRunway;

        tora = distanceFromThreshold + runway.getThreshold() - (obstacle.getHeight() * 50) - 60;                                            //TORA = Distance from Threshold + runwaythreshold - Slope Calculation - Strip End
        toda = tora;                                                                                                                        //TODA = (R) TORA
        lda = distanceFromThreshold - 240 - 60;                                                                                             //LDA = Distance from Threshold - RESA - Strip End
        asda = tora;                                                                                                                        //ASDA = (R) TORA
        recalculatedRunway = new LogicalRunWay(runway.getDegree(), runway.getDirection(), tora, toda, asda, lda, runway.getThreshold());

        if(runway.getThreshold() == 0) toraBD = distanceFromThreshold + " - (" + obstacle.getHeight() + " * 50) - 60 = " + tora;
        else toraBD = distanceFromThreshold + " + " + runway.getThreshold()  + "- (" + obstacle.getHeight() + " * 50) - 60 = " + tora;
        todaBD = "" + tora;
        ldaBD = distanceFromThreshold + " - 240 - 60 = " + lda;
        asdaBD = "" + tora;

        setRecalculatedTOWARDS(tora, toda, lda, asda);
        return new CalculatorOutput.RecalculatedRunwayOutput(recalculatedRunway, toraBD, todaBD, ldaBD, asdaBD);
    }

    private static void setRecalculatedAWAY(int tora, int toda, int lda, int asda){
        ahead = new int[]{tora, toda, lda, asda};
    }

    private static void setRecalculatedTOWARDS(int tora, int toda, int lda, int asda){
        towards = new int[]{tora, toda, lda, asda};
    }

    public static int[] getAhead() {
        return ahead;
    }
    public static int[] getTowards() {
        return towards;
    }
}

