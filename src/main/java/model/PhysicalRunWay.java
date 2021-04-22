package model;

public class PhysicalRunWay {

    private LogicalRunWay leftRunway;
    private LogicalRunWay rightRunway;
    private int runwayID;
    private Obstacle obstacle;

    public PhysicalRunWay (int id, LogicalRunWay left, LogicalRunWay right, Obstacle obstacle) throws Exception {
        if(!checkRunwayDirections(left.getDirection(), right.getDirection())) throw new Exception("Wrong directions of logical runways.");
        this.runwayID = id;
        this.leftRunway = left;
        this.rightRunway= right;
        this.obstacle = obstacle;
    }

    /*
    Added a check for correct runway directions
     */
    private boolean checkRunwayDirections(Direction left, Direction right)  {
        switch (left) {
            case C:
                if (right != Direction.C) return false;
                break;
            case L:
                if (right != Direction.R) return false;
                break;
            case R: {
                if (right != Direction.L) return false;
                break;
            }
        }
        return true;
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
