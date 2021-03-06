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

    public void setDistanceToRight(int distanceToRight) {
        this.distanceToRight = distanceToRight;
    }
}
