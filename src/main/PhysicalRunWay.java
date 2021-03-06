package main;

public class PhysicalRunWay {

    private LogicalRunWay leftRunway;
    private LogicalRunWay rightRunway;
    private int runwayID;
    private Obstacle obstacle;

    public PhysicalRunWay (int id, LogicalRunWay left, LogicalRunWay right, Obstacle obstacle){
        this.runwayID = id;
        this.leftRunway = left;
        this.rightRunway= right;
        this.obstacle = obstacle;
    }

    public LogicalRunWay getLeftRunway() {
        return leftRunway;
    }

    public LogicalRunWay getRightRunway() {
        return rightRunway;
    }

    public int getRunwayID() {
        return runwayID;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }
}
