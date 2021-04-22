package test;

import model.*;
import org.junit.*;

import java.util.ArrayList;

public class CalculatorTest {

    protected static int[] runwayLeft;
    protected static int[] runwayRight;

    protected static ArrayList<Airport> airports = new ArrayList<>();

    protected static LogicalRunWay left1;
    protected static LogicalRunWay right1;
    protected static LogicalRunWay left2;
    protected static LogicalRunWay right2;
    protected static LogicalRunWay left3;
    protected static LogicalRunWay right3;
    protected static LogicalRunWay left4;
    protected static LogicalRunWay right4;

    protected static PhysicalRunWay r1;
    protected static PhysicalRunWay r2;
    protected static PhysicalRunWay r3;
    protected static PhysicalRunWay r4;

    protected static Obstacle airplaneObstacle;
    protected static Obstacle airplaneObstacle1;
    protected static Obstacle airplaneObstacle2;
    protected static Obstacle airplaneObstacle3;
    protected static Obstacle airplaneObstacle4;

    // assigning the values
    @BeforeClass
    public static void initialSetUp(){
//        left1 = new LogicalRunWay(9, Direction.L,3902,3902,3202,3595, 0);
//        right1 = new LogicalRunWay(27, Direction.R,3884,3962,3884,0, 0);
//
//        left2 = new LogicalRunWay(10, Direction.L,3802,3802,3102,3495, 0);
//        right2 = new LogicalRunWay(28, Direction.R,1,1,1,0, 0);

        left3 = new LogicalRunWay(9, Direction.R,3660,3660,3660,3353, 307);
        right3 = new LogicalRunWay(27, Direction.L,3660,3660,3660,3660, 0);

        left4 = new LogicalRunWay(9, Direction.L, 3902, 3902, 3902, 3595, 306);
        right4 = new LogicalRunWay(27, Direction.R, 3884, 3962, 3884, 3884, 0);

//        r1 = null;
//        r2 = null;
        r3 = null;
        r4 = null;

        try {
//            r1 = new PhysicalRunWay(1,left1, right1, null);
//            r2 = new PhysicalRunWay(2,left2, right2, null);
            r3 = new PhysicalRunWay(3,left3, right3, null);
            r4 = new PhysicalRunWay(4, right4,left4, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        airplaneObstacle = new Obstacle("airplane", 25 , 25*50, new Position(3655,0, 10, "L"));

        airplaneObstacle1 = new Obstacle("airplane", 20 , 25*50, new Position(50,3546, 20, "R"));
        airplaneObstacle2 = new Obstacle("airplane", 15 , 15*50, new Position(150,3203, 60, "R"));
        airplaneObstacle3 = new Obstacle("airplane", 25 , 25*50, new Position(2853,500, 20, "L"));
        airplaneObstacle4 = new Obstacle("airplane", 12 , 25*50, new Position(3646,-50, 0, "R"));
    }

    @Test
    public void scenario1(){
        r4.setObstacle(airplaneObstacle4);
        Calculator.recalculate(r4);

        runwayLeft = Calculator.getAhead();
        runwayRight = Calculator.getTowards();

//                      {tora, toda, lda, asda};
        int[] answers = {3346, 3346, 2985, 3346};
        Assert.assertArrayEquals(answers, runwayLeft);
        answers = new int[]{2986,2986,3346,2986};
        Assert.assertArrayEquals(answers, runwayRight);
    }

    @Test
    public void scenario2(){
        r3.setObstacle(airplaneObstacle3);
        Calculator.recalculate(r3);

        runwayLeft = Calculator.getTowards();
        runwayRight = Calculator.getAhead();

        int[] answers = {1850,1850,2553,1850};
        Assert.assertArrayEquals(answers, runwayLeft);
        answers = new int[]{2860,2860,1850,2860};
        Assert.assertArrayEquals(answers, runwayRight);
    }

    @Test
    public void scenario3(){
        r3.setObstacle(airplaneObstacle2);
        Calculator.recalculate(r3);

        runwayLeft = Calculator.getAhead();
        runwayRight = Calculator.getTowards();

        int[] answers = {2903,2903,2393,2903};
        Assert.assertArrayEquals(answers, runwayLeft);
        answers = new int[]{2393,2393,2903,2393};
        Assert.assertArrayEquals(answers, runwayRight);
    }

    @Test
    public void scenario4(){
        r4.setObstacle(airplaneObstacle1);
        Calculator.recalculate(r4);

        runwayLeft = Calculator.getTowards();
        runwayRight = Calculator.getAhead();

        int[] answers = {2792,2792,3246,2792};
        Assert.assertArrayEquals(answers, runwayLeft);
        answers = new int[]{3534,3612,2774,3534};
        Assert.assertArrayEquals(answers, runwayRight);
    }


//    @Test
//    // test method with no consequences of obstacle
//    public void noChange(){
//
//        Assert.assertEquals(0);
//    }

//    @Test
//    test method for almost covered runway of obstacle
//    public void noChange(){
//
//        Assert.assertEquals(0);
//    }


//    @Test
//    test method for overlapping distance arrows for lengths (from both ends)
//    public void noChange(){
//
//        Assert.assertEquals(0);
//    }



//    @Test
//    test method for outside tall obstacle
//    public void noChange(){
//
//        Assert.assertEquals(0);
//    }


//    @Before
//    public void setUp(){
//        output = null;
//    }

    @After
    public void tearDown(){
        runwayRight = null;
        runwayLeft = null;
    }

    @AfterClass
    public static void clearUp(){
        left1 = right1 = left2 = right2 = left3 = right3 = left4 = right4 = null;

    }




}