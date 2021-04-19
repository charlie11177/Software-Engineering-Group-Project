package model;

public class LogicalRunWay {
    private int degree;
    private Direction direction;

    private int TORA;
    private int TODA;
    private int ASDA;
    private int LDA;
    private int threshold;
    private int clearway;
    private int stopway;
    private String name;

    public LogicalRunWay(int degree, Direction direction, int TORA, int TODA, int ASDA, int LDA, int threshold){
        this.degree = degree;
        this.direction = direction;
        this.TORA = TORA;
        this.TODA = TODA;
        this.ASDA = ASDA;
        this.LDA = LDA;
        this.threshold = threshold;
        this.clearway = TODA - TORA;
        this.stopway = ASDA - TORA;
        this.name = degree + direction.toString();
    }

    @Override
    public String toString(){
        return degree + direction.toString();
    }

    public String getData(){
        return "Degree: " + degree + "  Direction: " + direction + "  TORA: " + TORA + "  TODA: " + TODA + "  ASDA: " + ASDA
                + "  LDA: " + LDA + "  Threshold: " + threshold;
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

    public void setThreshold(int threshold) { this.threshold = threshold;}


    public String getName(){
        return name;
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

    public int getThreshold() { return threshold; }

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
