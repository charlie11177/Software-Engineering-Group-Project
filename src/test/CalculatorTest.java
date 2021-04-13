package test;

import junit.framework.TestCase;
import main.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class CalculatorTest {

    protected ArrayList<Airport> airports = new ArrayList<>();

    protected LogicalRunWay left1;
    protected LogicalRunWay right1;
    protected LogicalRunWay left2;
    protected LogicalRunWay right2;
    protected LogicalRunWay left3;
    protected LogicalRunWay right3;
    protected LogicalRunWay left4;
    protected LogicalRunWay right4;

    PhysicalRunWay r1;
    PhysicalRunWay r2;
    PhysicalRunWay r3;
    PhysicalRunWay r4;

    Obstacle airplaneObstacle;

    protected CalculatorOutput.RecalculatedRunwayOutput runwayLeft;
    protected CalculatorOutput.RecalculatedRunwayOutput runwayRight;

    // assigning the values
    @Before
    public void setUp(){
        left1 = new LogicalRunWay(9, Direction.L,3902,3902,3202,3595, 0);
        right1 = new LogicalRunWay(27, Direction.R,3884,3962,3884,0, 0);

        left2 = new LogicalRunWay(10, Direction.L,3802,3802,3102,3495, 0);
        right2 = new LogicalRunWay(28, Direction.R,1,1,1,0, 0);

        left3 = new LogicalRunWay(9, Direction.R,3660,3660,3660,3353, 307);
        right3 = new LogicalRunWay(27, Direction.L,3660,3660,3660,3660, 0);

        left4 = new LogicalRunWay(9, Direction.L, 3902, 3902, 3902, 3595, 306);
        right4 = new LogicalRunWay(27, Direction.R, 3884, 3962, 3884, 3884, 0);

        r1 = null;
        r2 = null;
        r3 = null;
        r4 = null;

        try {
            r1 = new PhysicalRunWay(1,left1, right1, null);
            r2 = new PhysicalRunWay(2,left2, right2, null);
            r3 = new PhysicalRunWay(3,left3, right3, null);
            r4 = new PhysicalRunWay(4,left4, right4, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        airplaneObstacle = new Obstacle("airplane1", 25 , 25*50, new Position(3655,0, 10, "L"));
    }


    @Test
    // test method from the docs
    public void testExample(){
        r4.setObstacle(airplaneObstacle);


        Assert.assertEquals(6, 5, 0.0);
    }

    @Test
    public void scenario1(){

    }

    @Test
    // test method with no consequences of obstacle
    public void noChange(){

        Assert.assertEquals(6, 5, 0.0);
    }






}