package main;

public class LogicalRunWay {
    private String name;
    private int degree;
    private Direction direction;
    private int TORA;
    private int TODA;
    private int ASDA;
    private int LDA;

    enum Direction {
        Left,
        Right
    }

    public LogicalRunWay(String name, Direction direction, int TORA, int TODA, int ASDA, int LDA){
        this.name = name;
        this.direction = direction;
        setDegree(Integer.parseInt(name) * 10);
        this.TORA = TORA;
        this.TODA = TODA;
        this.ASDA = ASDA;
        this.LDA = LDA;
    }

    public String getName() {
        return name;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getASDA() {
        return ASDA;
    }

    public int getLDA() {
        return LDA;
    }

    public int getTODA() {
        return TODA;
    }

    public int getTORA() {
        return TORA;
    }

}
