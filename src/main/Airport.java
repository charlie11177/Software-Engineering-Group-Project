package main;

import java.util.ArrayList;
import java.util.List;

public class Airport {
    private String name;
    private List<PhysicalRunWay> runways;
    private String runwayStrip;

    public Airport (String name, List<PhysicalRunWay> runways){
        this.name = name;
        this.runways = runways;
    }

    public void addNewRunway(PhysicalRunWay runway){
        if (runways.contains(runway))
            System.out.println("This Runway Already Exist");
        else{
            runways.add(runway);
            System.out.println("Runway has been successfully added");
        }
    }

    public static void main(String args){

        LogicalRunWay left = new LogicalRunWay("09", LogicalRunWay.Direction.Left,3902,3902,3202,3595);
        LogicalRunWay right = new LogicalRunWay("27", LogicalRunWay.Direction.Right,3884,3962,3884,0);
        PhysicalRunWay runWay = new PhysicalRunWay(1, left,right, null);

        //based on figure 3 in spec
        Obstacle airplane = new Obstacle("airplane1", 25 , 25*50, new Position(3655,0));

        Airport southampton = new Airport("SOU", new ArrayList<>());
        southampton.addNewRunway(runWay);
        southampton.addNewRunway(runWay);

    }

}
