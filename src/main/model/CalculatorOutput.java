package main.model;

public class CalculatorOutput {
    private LogicalRunWay recalculatedRunway;
    private String toraBD, todaBD, ldaBD, asdaBD, allBD; //Breakdowns must always be stored in the same order

    public CalculatorOutput(LogicalRunWay recalculatedRunway, String toraBD, String todaBD, String ldaBD, String asdaBD) {
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
