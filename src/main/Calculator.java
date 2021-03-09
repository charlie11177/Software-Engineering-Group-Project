package main;

public class Calculator {
    private static PhysicalRunWay physicalRunWay;
    private static Obstacle obstacle;

    public static void recalculate(){
        if (obstacle.getPosition().getDistanceToLeft() > obstacle.getPosition().getDistanceToRight()){
           recalculateAWAY(physicalRunWay.getRightRunway());
           recalculateTOWARDS(physicalRunWay.getLeftRunway());
        }
        else {
            recalculateAWAY(physicalRunWay.getLeftRunway());
            recalculateTOWARDS(physicalRunWay.getRightRunway());
        }
    }



    private static void recalculateAWAY(LogicalRunWay runway) { //(Take off Away, Landing Over)
        int tora, toda, asda, lda;
        int blastAllowance = 300; //Blast protection is between 300-500 not sure whether this is defined by the user
        tora = runway.getTORA() - blastAllowance;// TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold
        asda = 0;//ASDA = (R) TORA + STOPWAY
        toda = 0;//(R) TORA + CLEARWAY
        lda = 0;//LDA = Original LDA - Distance from Threshold – Strip End - Slope Calculation
    }
    private static void recalculateTOWARDS(LogicalRunWay runway) { //(Take off Towards, Landing Towards)
        int tora, toda, asda, lda;

        tora = 0;//TORA = Distance from Threshold - Slope Calculation - Strip End
        asda = 0;//ASDA = (R) TORA
        toda = 0;//TODA = (R) TORA
        lda = 0;//LDA = Distance from Threshold - RESA - Strip End

    }
}

