package model;

public class Position {

    private int distanceToLeft;
    private int distanceToRight;
    private int distanceFromCL;

    private String directionFromCL;

    public Position(int distanceToLeft, int distanceToRight, int distanceFromCL, String directionFromCL){
        this.distanceToLeft = distanceToLeft;
        this.distanceToRight = distanceToRight;
        this.distanceFromCL = distanceFromCL;
        this.directionFromCL = directionFromCL;
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

    public String getDirectionFromCL() {
        return directionFromCL;
    }

    public void setDirectionFromCL(String directionFromCL) {
        this.directionFromCL = directionFromCL;
    }

    public int getDistanceFromCL() {
        return distanceFromCL;
    }

    public void setDistanceFromCL(int distanceFromCL) {
        this.distanceFromCL = distanceFromCL;
    }

}
