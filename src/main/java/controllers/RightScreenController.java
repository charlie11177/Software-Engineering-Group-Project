package main.java.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.java.model.LogicalRunWay;
import main.java.model.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class RightScreenController {



    public TableView<LogicalRunWay> topTableView;
    public TableView<LogicalRunWay> bottomTableView;
    public TextArea allCalculationsTowardsTA;
    public TextArea allCalculationsAwayTA;
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
        textAreas.addAll(Arrays.asList(allCalculationsTowardsTA, allCalculationsAwayTA, toraTopTA, toraBottomTA,
                todaTopTA, todaBottomTA, ldaTopTA, ldaBottomTA, asdaTopTA, asdaBottomTA));
    }

    public void populateTables() {
        topTableView.getItems().clear();
        topTableView.getItems().addAll(Model.originalRunwayTowards, Model.originalRunwayAway);
        bottomTableView.getItems().clear();
        bottomTableView.getItems().addAll(Model.recalculatedRunwayTowards, Model.recalculatedRunwayAway);
    }

    public void changeFontSize(double size){
        allCalculationsTowardsTA.setFont(Font.font("Courier New", size));
        allCalculationsAwayTA.setFont(Font.font("Courier New",size));
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
