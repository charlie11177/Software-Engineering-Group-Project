package main;

public class Calculator {
    private static PhysicalRunWay physicalRunway;
    private static Obstacle obstacle;

    public static void recalculate(){
        //obstacle = Model.currentObstacle;
        physicalRunway = Model.currentRunway;
        //TEST CODE until Model.current pulls current
        obstacle = new Obstacle("scenario1", 12, 50, new Position(-50, 3646,0,null));
        try {
            physicalRunway = new PhysicalRunWay(9, new LogicalRunWay(9, Direction.L, 3902, 3902, 3902, 3595, 306), new LogicalRunWay(27, Direction.R, 3884, 3962, 3884, 3884, 0), obstacle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(obstacle != null){
            if (obstacle.getPosition().getDistanceToLeft() > obstacle.getPosition().getDistanceToRight()){
                recalculateAWAY(physicalRunway.getRightRunway(), obstacle.getPosition().getDistanceToRight());
                recalculateTOWARDS(physicalRunway.getLeftRunway(), obstacle.getPosition().getDistanceToLeft());
            }
            else {
                recalculateAWAY(physicalRunway.getLeftRunway(), obstacle.getPosition().getDistanceToLeft());
                recalculateTOWARDS(physicalRunway.getRightRunway(), obstacle.getPosition().getDistanceToRight());
            }
        }
    }



    private static void recalculateAWAY(LogicalRunWay runway, int distanceFromThreshold) { //(Take off Away, Landing Over)
        int tora, toda, asda, lda;
        int blastAllowance = 300; //Blast protection is between 300-500 not sure whether this is defined by the user

        tora = runway.getTORA() - blastAllowance - distanceFromThreshold - runway.getThreshold();// TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold
        toda = tora + runway.getClearway();//(R) TORA + CLEARWAY
        lda = runway.getLDA() - distanceFromThreshold - 60 - (obstacle.getHeight() * 50);//LDA = Original LDA - Distance from Threshold – Strip End - Slope Calculation
        asda = tora + runway.getStopway();//ASDA = (R) TORA + STOPWAY
        Model.console.addLog("Runway: " + runway + ", TORA:" + tora + ", TODA:" + toda + ", LDA:" + lda + ", ASDA:" + asda);
    }
    private static void recalculateTOWARDS(LogicalRunWay runway, int distanceFromThreshold) { //(Take off Towards, Landing Towards)
        int tora, toda, asda, lda;

        tora = distanceFromThreshold - (obstacle.getHeight() * 50) - 60;//TORA = Distance from Threshold - Slope Calculation - Strip End
        asda = tora;//ASDA = (R) TORA
        toda = tora;//TODA = (R) TORA
        lda = distanceFromThreshold - 240 - 60;//LDA = Distance from Threshold - RESA - Strip End
        Model.console.addLog("Runway: " + runway + ", TORA:" + tora + ", TODA:" + toda + ", LDA:" + lda + ", ASDA:" + asda);

    }
}

