package main.model;

import main.controllers.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Model class used as the main class that holds all data from classes that represent Airport, Runway, Obstacles, etc.
 * Used for communicating between Controller and Model classes
 */
public class Model {

    // Center of the UI, main visualisation window and console textArea
    // Do not use these in classes other then controllers
    public static CenterScreenController centerScreenController;
    public static RunwayConfigController runwayConfigController;
    public static AirportConfigController airportConfigController;
    public static ObstacleConfigController obstacleConfigController;
    public static LeftScreenController leftScreenController;
    public static RightScreenController rightScreenController;
    public static MainWindowController mainWindowController;

    /**
     * These variables are the data that is being displayed on the UI
     */
    public static ArrayList<Airport> airports = new ArrayList<>();
    public static ArrayList<Obstacle> obstacles = new ArrayList<>();
    public static Airport currentAirport;   // selected airport, can be null if none is chosen
    public static PhysicalRunWay currentRunway; // selected runway, can be null if none is chosen
    public static Obstacle currentObstacle; // selected obstacle, can be null if none is chosen
    public static boolean obstaclePlaced = false;   // true, if user chose to place an obstacle on the selected runway

    public static String calculationsBreakdownLeft;
    public static String calculationsBreakDownRight;

    public static LogicalRunWay recalculatedRunwayLeft;
    public static LogicalRunWay recalculatedRunwayRight;

    public static LogicalRunWay originalRunwayLeft;
    public static LogicalRunWay originalRunwayRight;

    // console that holds all the log texts to display
    public static Console console = new Console();
    private static FontSize currentFontSize;

    public static void setCurrentFontSize(FontSize fontSize) {
        currentFontSize = fontSize;
    }

    public static ArrayList<Airport> getAirports() {
        return airports;
    }

    public static ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public static void addAirports(Airport...as) {
        Model.currentRunway = null;
        Model.obstaclePlaced = false;
        airports.addAll(Arrays.asList(as));
    }

    public static void setCurrentRunway(PhysicalRunWay runway) {
        Model.currentRunway = runway;
        Model.originalRunwayLeft = runway.getLeftRunway();
        Model.originalRunwayRight = runway.getRightRunway();
        Model.obstaclePlaced = false;
        updateVisualisation();
    }

    public static void setObstaclePlaced(boolean value) {
        obstaclePlaced = value;
        updateVisualisation();
    }

    public static boolean getObstaclePlaced() {
        return obstaclePlaced;
    }

    public static void setCurrentAirport(Airport airport) {
        currentAirport = airport;
        Model.obstaclePlaced = false;
        if(!Model.currentAirport.getRunways().contains(Model.currentRunway)){
            Model.currentRunway = null;
        }
        updateVisualisation();
    }

    // removes all user, chosen data, used when a full configuration is imported from an XML
    public static void resetConfig(){
        currentAirport = null;
        currentRunway = null;
        currentObstacle = null;
        obstaclePlaced = false;
        updateVisualisation();
    }

    public static void updateVisualisation(){
        if (!leftScreenController.calculateAllowed) centerScreenController.updateVisualisation(TopDownView.CALCULATIONS_RUNWAY);
        else if (obstaclePlaced) centerScreenController.updateVisualisation(TopDownView.REDECLAREDRUNWAY);
        else if (currentRunway != null) centerScreenController.updateVisualisation(TopDownView.RUNWAY);
        else centerScreenController.updateVisualisation(TopDownView.DEFAULT);
    }

    public static FontSize getCurrentFontSize() {
        return currentFontSize;
    }


    public static void demo(){
        LogicalRunWay left1 = new LogicalRunWay(9, Direction.L,3902,3902,3202,3595, 0);
        LogicalRunWay right1 = new LogicalRunWay(27, Direction.R,3884,3962,3884,0, 0);

        LogicalRunWay left2 = new LogicalRunWay(10, Direction.L,3802,3802,3102,3495, 0);
        LogicalRunWay right2 = new LogicalRunWay(28, Direction.R,1,1,1,0, 0);

        LogicalRunWay left3 = new LogicalRunWay(9, Direction.R,3660,3660,3660,3353, 307);
        LogicalRunWay right3 = new LogicalRunWay(27, Direction.L,3660,3660,3660,3660, 0);

        LogicalRunWay left4 = new LogicalRunWay(9, Direction.L, 3902, 3902, 3902, 3595, 306);
        LogicalRunWay right4 = new LogicalRunWay(27, Direction.R, 3884, 3962, 3884, 3884, 0);

        PhysicalRunWay r1 = null;
        PhysicalRunWay r2 = null;
        PhysicalRunWay r3 = null;
        PhysicalRunWay r4 = null;

        try {
            r1 = new PhysicalRunWay(1,left1, right1, null);
            r2 = new PhysicalRunWay(2,left2, right2, null);
            r3 = new PhysicalRunWay(1,left3, right3, null);
            r4 = new PhysicalRunWay(3,left4, right4, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<PhysicalRunWay> runWays = new ArrayList<>();
        runWays.add(r1);
        runWays.add(r2);

        ArrayList<PhysicalRunWay> runWays2 = new ArrayList<>();
        runWays2.add(r3);
        runWays2.add(r4);

        Obstacle airplane = new Obstacle("airplane1", 25 , 25*50, new Position(3655,0, 10, "L"));
        //obstacles.add(airplane);

        Airport airport = new Airport("London_Gatwick", "LGW",runWays2);
        Airport airport2 = new Airport("London_Heathrow", "LHR",runWays);
        airports.add(airport);
        airports.add(airport2);
    }
}
