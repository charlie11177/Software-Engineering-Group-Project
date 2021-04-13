package main.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.model.CalculatorOutput;
import main.model.LogicalRunWay;
import main.model.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class RightScreenController {



    public TableView<LogicalRunWay> topTableView;
    public TableView<LogicalRunWay> bottomTableView;
    public TextArea allCalculationsLeftTA;
    public TextArea allCalculationsRightTA;
    public TextArea toraTopTA;
    public TextArea toraBottomTA;
    public TextArea todaTopTA;
    public TextArea todaBottomTA;
    public TextArea ldaTopTA;
    public TextArea ldaBottomTA;
    public TextArea asdaTopTA;
    public TextArea asdaBottomTA;
    public VBox rightScreen;
    public Label recalculatedValuesLabel;
    public Label originalValuesLabel;
    public Label breakdownLabel;
    public TableColumn bottomNameColumn;
    public ArrayList<TextArea> textAreas;


    public RightScreenController() {
        textAreas = new ArrayList<>();
    }

    public void initialize(){
        Model.rightScreenController = this;
        textAreas.addAll(Arrays.asList(allCalculationsLeftTA, allCalculationsRightTA, toraTopTA, toraBottomTA,
                todaTopTA, todaBottomTA, ldaTopTA, ldaBottomTA, asdaTopTA, asdaBottomTA));
    }

    public void populateTables() {
        topTableView.getItems().clear();
        topTableView.getItems().addAll(Model.originalRunwayLeft, Model.originalRunwayRight);
        bottomTableView.getItems().clear();
        bottomTableView.getItems().addAll(Model.recalculatedRunwayLeft, Model.recalculatedRunwayRight);
    }

    public void populateBreakDowns(CalculatorOutput calculatorOutput) {
        allCalculationsLeftTA.setText(calculatorOutput.getRunwayLeft().getTotalBD());
        allCalculationsRightTA.setText(calculatorOutput.getRunwayRight().getTotalBD());

        toraTopTA.setText(calculatorOutput.getRunwayLeft().getBD("tora"));
        toraBottomTA.setText(calculatorOutput.getRunwayRight().getBD("tora"));
        todaTopTA.setText(calculatorOutput.getRunwayLeft().getBD("toda"));
        todaBottomTA.setText(calculatorOutput.getRunwayRight().getBD("toda"));
        ldaTopTA.setText(calculatorOutput.getRunwayLeft().getBD("lda"));
        ldaBottomTA.setText(calculatorOutput.getRunwayRight().getBD("lda"));
        asdaTopTA.setText(calculatorOutput.getRunwayLeft().getBD("asda"));
        asdaBottomTA.setText(calculatorOutput.getRunwayRight().getBD("asda"));
    }

    public void changeFontSize(double size){
        allCalculationsLeftTA.setFont(Font.font("Courier New", size));
        allCalculationsRightTA.setFont(Font.font("Courier New",size));
        toraTopTA.setFont(Font.font("Courier New", size));
        toraBottomTA.setFont(Font.font("Courier New",size));
        todaTopTA.setFont(Font.font("Courier New",size));
        todaBottomTA.setFont(Font.font("Courier New",size));
        ldaTopTA.setFont(Font.font("Courier New",size));
        ldaBottomTA.setFont(Font.font("Courier New",size));
        asdaTopTA.setFont(Font.font("Courier New",size));
        asdaBottomTA.setFont(Font.font("Courier New",size));
    }

    public void clear() {
        for (TextArea ta : textAreas) {
            ta.clear();
        }
        topTableView.getItems().clear();
        bottomTableView.getItems().clear();
    }
}
