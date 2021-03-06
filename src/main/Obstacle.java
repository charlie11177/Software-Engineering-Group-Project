package main;

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
}
