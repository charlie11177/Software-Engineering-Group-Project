package main.controllers;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import main.LogicalRunWay;
import main.Model;

public class RightScreenController {

    public TextArea allCalculationsTowardsTA;
    public TextArea allCalculationsAwayTA;
    public TableView<LogicalRunWay> topTableView;
    public TableView<LogicalRunWay> bottomTableView;

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
}
