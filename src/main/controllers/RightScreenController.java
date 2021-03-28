package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import main.model.LogicalRunWay;
import main.model.Model;

public class RightScreenController {

    public TextArea allCalculationsTowardsTA;
    public TextArea allCalculationsAwayTA;
    @FXML private TableView<LogicalRunWay> topTableView;
    @FXML private TableView<LogicalRunWay> bottomTableView;
    public TextArea toraTopTA;
    public TextArea toraBottomTA;
    public TextArea todaTopTA;
    public TextArea todaBottomTA;
    public TextArea ldaTopTA;
    public TextArea ldaBottomTA;
    public TextArea asdaTopTA;
    public TextArea asdaBottomTA;

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
