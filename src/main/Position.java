package main;

public class Position {

    private int distanceToLeft;
    private int distanceToRight;

    public Position(int distanceToLeft, int distanceToRight){
        this.distanceToLeft = distanceToLeft;
        this.distanceToRight = distanceToRight;
    }

    public int getDistanceToLeft() {
        return distanceToLeft;
    }

    public int getDistanceToRight() {
        return distanceToRight;
    }

    public void setDistanceToRight(int distanceToRight) {
        this.distanceToRight = distanceToRight;
    }

    public void setDistanceToLeft(int distanceToLeft) {
        this.distanceToLeft = distanceToLeft;
    }
}
