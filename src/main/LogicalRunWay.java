package main;

public class LogicalRunWay {
    private int degree;
    private Direction direction;

    private int TORA;
    private int TODA;
    private int ASDA;
    private int LDA;
    private int clearway;
    private int stopway;

    public LogicalRunWay(int degree, Direction direction, int TORA, int TODA, int ASDA, int LDA){
        this.degree = degree;
        this.direction = direction;
        this.TORA = TORA;
        this.TODA = TODA;
        this.ASDA = ASDA;
        this.LDA = LDA;
        this.clearway = TODA - TORA;
        this.stopway = ASDA- TORA;
    }

    @Override
    public String toString(){
        return String.valueOf(degree) + direction.toString();
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setTORA(int TORA) {
        this.TORA = TORA;
    }

    public void setTODA(int TODA) {
        this.TODA = TODA;
    }

    public void setASDA(int ASDA) {
        this.ASDA = ASDA;
    }

    public void setLDA(int LDA) {
        this.LDA = LDA;
    }

    public int getDegree() {
        return degree;
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

    public int getStopway() {
        return stopway;
    }

    public int getClearway() {
        return clearway;
    }
}
