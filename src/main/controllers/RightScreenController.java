package main.controllers;

import javafx.fxml.FXML;
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
        allCalculationsTowardsTA.setFont(Font.font("Consolas", size));
        allCalculationsAwayTA.setFont(Font.font("Consolas",size));
        toraTopTA.setFont(Font.font("Consolas", size));
        toraBottomTA.setFont(Font.font("Consolas",size));
        todaTopTA.setFont(Font.font("Consolas",size));
        todaBottomTA.setFont(Font.font("Consolas",size));
        ldaTopTA.setFont(Font.font("Consolas",size));
        ldaBottomTA.setFont(Font.font("Consolas",size));
        asdaTopTA.setFont(Font.font("Consolas",size));
        asdaBottomTA.setFont(Font.font("Consolas",size));
    }
}
