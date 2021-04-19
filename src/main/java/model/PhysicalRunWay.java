package model;

public class PhysicalRunWay {

    private model.LogicalRunWay leftRunway;
    private model.LogicalRunWay rightRunway;
    private int runwayID;
    private model.Obstacle obstacle;

    public PhysicalRunWay (int id, model.LogicalRunWay left, model.LogicalRunWay right, model.Obstacle obstacle) throws Exception {
        if(!checkRunwayDirections(left.getDirection(), right.getDirection())) throw new Exception("Wrong directions of logical runways.");
        this.runwayID = id;
        this.leftRunway = left;
        this.rightRunway= right;
        this.obstacle = obstacle;
    }

    /*
    Added a check for correct runway directions
     */
    private boolean checkRunwayDirections(model.Direction left, model.Direction right)  {
        switch (left) {
            case C:
                if (right != model.Direction.C) return false;
                break;
            case L:
                if (right != model.Direction.R) return false;
                break;
            case R: {
                if (right != model.Direction.L) return false;
                break;
            }
        }
        return true;
    }

    @Override
    public String toString(){
        return leftRunway.toString() + "/" + rightRunway.toString();
    }

    public model.LogicalRunWay getLeftRunway() {
        return leftRunway;
    }

    public model.LogicalRunWay getRightRunway() {
        return rightRunway;
    }

    public int getRunwayID() {
        return runwayID;
    }

    public model.Obstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(model.Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public void setLeftRunway(model.LogicalRunWay leftRunway) {
        this.leftRunway = leftRunway;
    }

    public void setRightRunway(model.LogicalRunWay rightRunway) {
        this.rightRunway = rightRunway;
    }
}
