package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.model.LogicalRunWay;
import main.model.Model;

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


    public RightScreenController() {}

    public void initialize(){
        Model.rightScreenController = this;
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
}
