package model;

public class Position {

    private Integer distanceToLeft;
    private Integer distanceToRight;
    private Integer distanceFromCL;

    private String directionFromCL;

    public Position(Integer distanceToLeft, Integer distanceToRight, Integer distanceFromCL, String directionFromCL){
        this.distanceToLeft = distanceToLeft;
        this.distanceToRight = distanceToRight;
        this.distanceFromCL = distanceFromCL;
        this.directionFromCL = directionFromCL;
    }

    public Integer getDistanceToLeft() {
        return distanceToLeft;
    }

    public Integer getDistanceToRight() {
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

    public Integer getDistanceFromCL() {
        return distanceFromCL;
    }

    public void setDistanceFromCL(int distanceFromCL) {
        this.distanceFromCL = distanceFromCL;
    }

}
