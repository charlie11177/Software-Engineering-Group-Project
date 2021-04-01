package model;

public class Obstacle {
    private String name;
    private int height;
    private int width;
    private Position position;

    public Obstacle(String name, int height, int width, Position position){
        this.name= name;
        this.height = height;
        this.width = width;
        this.position = position;
    }

    public Boolean placeObstacle(PhysicalRunWay runway){
        return runway.getObstacle() != null ;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Position getPosition() {
        return position;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String toString() {
        return "Name: " + name + "  Height: " + height + "  Width: " + width + "\nDistance from L: " + position.getDistanceToLeft() +
                "  Distance from R: " + position.getDistanceToRight() + "  Distance from C/L: " + position.getDistanceFromCL() +
                "  Direction from CL: " + position.getDirectionFromCL();
    }

}
