package model;

public class CalculatorOutput {
    private RecalculatedRunwayOutput runwayLeft;
    private RecalculatedRunwayOutput runwayRight;

    public CalculatorOutput(RecalculatedRunwayOutput runwayLeft, RecalculatedRunwayOutput runwayRight) {
        this.runwayLeft = runwayLeft;
        this.runwayRight = runwayRight;
    }

    public CalculatorOutput(){}

    public void setRunwayLeft(RecalculatedRunwayOutput runwayLeft) {
        this.runwayLeft = runwayLeft;
    }

    public void setRunwayRight(RecalculatedRunwayOutput runwayRight) {
        this.runwayRight = runwayRight;
    }
    public RecalculatedRunwayOutput getRunwayLeft() {
        return runwayLeft;
    }

    public RecalculatedRunwayOutput getRunwayRight() {
        return runwayRight;
    }

    public static class RecalculatedRunwayOutput {
        private LogicalRunWay recalculatedRunway;
        private String toraBD, todaBD, ldaBD, asdaBD, allBD; //Breakdowns must always be stored in the same order

        public RecalculatedRunwayOutput(LogicalRunWay recalculatedRunway, String toraBD, String todaBD, String ldaBD, String asdaBD) {
            this.recalculatedRunway = recalculatedRunway;
            this.toraBD = toraBD;
            this.todaBD = todaBD;
            this.ldaBD = ldaBD;
            this.asdaBD = asdaBD;
            allBD = "--" + recalculatedRunway + "--" + "\n" +
                    "TORA: " + toraBD + "\n" +
                    "TODA: " + todaBD + "\n" +
                    "LDA: " + ldaBD + "\n" +
                    "ASDA: " + asdaBD;
        }

        public String getTotalBD(){
            return allBD;
        }

        public String getBD(String property){
            switch(property) {
                case "tora":
                    return toraBD;
                case "toda":
                    return todaBD;
                case "lda":
                    return ldaBD;
                case "asda":
                    return asdaBD;

            }
            System.out.println("Invalid BD type");
            return "";
        }

        public LogicalRunWay getRecalculatedRunway(){
            return recalculatedRunway;
        }

    }
}
