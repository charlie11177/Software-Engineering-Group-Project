package main;

public class LogicalRunWay {
    private int degree;
    private Direction direction;
    private int TORA;
    private int TODA;
    private int ASDA;
    private int LDA;

    public LogicalRunWay(int degree, Direction direction, int TORA, int TODA, int ASDA, int LDA){
        this.degree = degree;
        this.direction = direction;
        this.TORA = TORA;
        this.TODA = TODA;
        this.ASDA = ASDA;
        this.LDA = LDA;
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

    @Override
    public String toString(){
        return String.valueOf(degree) + direction.toString();
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
