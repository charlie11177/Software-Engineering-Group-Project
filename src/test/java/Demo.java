import model.Direction;
import model.Model;
import model.PhysicalRunWay;

import java.util.ArrayList;

public class Demo {

    public static void setup() {
        model.LogicalRunWay left1 = new model.LogicalRunWay(9, Direction.L, 3902, 3902, 3202, 3595, 0);
        model.LogicalRunWay right1 = new model.LogicalRunWay(27, Direction.R, 3884, 3962, 3884, 0, 0);

        model.LogicalRunWay left2 = new model.LogicalRunWay(10, Direction.L, 3802, 3802, 3102, 3495, 0);
        model.LogicalRunWay right2 = new model.LogicalRunWay(28, Direction.R, 1, 1, 1, 0, 0);

        model.LogicalRunWay left3 = new model.LogicalRunWay(5, Direction.R, 3660, 3660, 3660, 3353, 307);
        model.LogicalRunWay right3 = new model.LogicalRunWay(23, Direction.L, 3660, 3660, 3660, 3660, 0);

        model.LogicalRunWay left4 = new model.LogicalRunWay(1, Direction.L, 3902, 3902, 3902, 3595, 306);
        model.LogicalRunWay right4 = new model.LogicalRunWay(19, Direction.R, 3884, 3962, 3884, 3884, 0);

        PhysicalRunWay r1 = null;
        PhysicalRunWay r2 = null;
        PhysicalRunWay r3 = null;
        PhysicalRunWay r4 = null;

        try {
            r1 = new PhysicalRunWay(1, left1, right1, null);
            r2 = new PhysicalRunWay(2, left2, right2, null);
            r3 = new PhysicalRunWay(1, left3, right3, null);
            r4 = new PhysicalRunWay(3, left4, right4, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<PhysicalRunWay> runWays = new ArrayList<>();
        runWays.add(r1);
        runWays.add(r2);

        ArrayList<PhysicalRunWay> runWays2 = new ArrayList<>();
        runWays2.add(r3);
        runWays2.add(r4);

        model.Obstacle airplane = new model.Obstacle("Boeing 737", 25, 25 * 50, new model.Position(3655, 0, 10, "L"));
        if(Model.getObstacles().size() != 1)
            Model.obstacles.add(airplane);

        model.Airport airport = new model.Airport("Manchester Airport", "MAN", runWays2);
        model.Airport airport2 = new model.Airport("Birmingham International Airport", "BHX", runWays);
        if(Model.airports.size()!= 2){
            Model.airports.add(airport);
            Model.airports.add(airport2);
        }
    }
}

