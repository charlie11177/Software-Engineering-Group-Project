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

    @Override
    public String toString(){
        return leftRunway.toString() + "/" + rightRunway.toString();
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

    public void setLeftRunway(LogicalRunWay leftRunway) {
        this.leftRunway = leftRunway;
    }

    public void setRightRunway(LogicalRunWay rightRunway) {
        this.rightRunway = rightRunway;
    }
}
