package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.*;

import java.util.List;

public class RunwayConfigController {

    private Boolean edit;
    private String valueEdited;

    public TitledPane runwayConfig;
    public VBox runwayMainMenu;
    public ChoiceBox<String> runwayChoiceBox;

    @FXML private HBox editButtons;
    @FXML private Button saveRunway;
    @FXML private Button cancelRunway;
    @FXML private Button newRuway;
    @FXML private Button editRunway;
    @FXML private VBox runwayOptions;
    @FXML private ChoiceBox<Character> leftPosition;
    @FXML private ChoiceBox<Character> rightPosition;
    @FXML private TextField leftDegree;
    @FXML private TextField rightDegree;
    @FXML private TextField leftTora;
    @FXML private TextField rightTora;
    @FXML private TextField leftToda;
    @FXML private TextField rightToda;
    @FXML private TextField leftAsda;
    @FXML private TextField rightAsda;
    @FXML private TextField leftLda;
    @FXML private TextField rightLda;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label editLabel;

    public RunwayConfigController(){
        edit = false;
    }

    @FXML
    public void initialize(){
        //showOnlyMode(runwayOptions);
        hideOptions(editButtons);
        showOnlyMode(runwayOptions);
    }

    public void populateRunwayNames(List<PhysicalRunWay> runWays){
        runwayChoiceBox.getItems().clear();
        for(PhysicalRunWay r : runWays){
           runwayChoiceBox.getItems().add(r.toString());
        }
        runwayChoiceBox.setValue(runWays.get(0).toString());
        //TODO: TEAM2 code for loading airport data from XML here
    }

    public void populateRunwayDetails(PhysicalRunWay runWay){
        LogicalRunWay left = runWay.getLeftRunway();
        LogicalRunWay right = runWay.getRightRunway();

        leftDegree.setText(String.valueOf(left.getDegree()));
        leftToda.setText(String.valueOf(left.getTODA()));
        leftTora.setText(String.valueOf(left.getTORA()));
        leftAsda.setText(String.valueOf(left.getASDA()));
        leftLda.setText(String.valueOf(left.getLDA()));

        rightDegree.setText(String.valueOf(right.getDegree()));
        rightToda.setText(String.valueOf(right.getTODA()));
        rightTora.setText(String.valueOf(right.getTORA()));
        rightAsda.setText(String.valueOf(right.getASDA()));
        rightLda.setText(String.valueOf(right.getLDA()));
    }

    @FXML
    public void newRunwayClick(ActionEvent actionEvent) {
        edit = false;
        disableElements(runwayMainMenu);
        editLabel.setVisible(false);
        editableMode(runwayOptions);
        showOptions(editButtons);
    }

    public String getItem(){
        return runwayChoiceBox.getValue();
    }

    @FXML
    public void editRunwayClick(ActionEvent actionEvent) {
        edit = true;
        disableElements(runwayMainMenu);
        editLabel.setVisible(false);
        editableMode(runwayOptions);
        showOptions(editButtons);
    }

    @FXML
    private void saveRunwayClick(ActionEvent actionEvent) {
        showOnlyMode(runwayOptions);
        enableElements(runwayMainMenu);
        hideOptions(editButtons);
        editLabel.setVisible(true);
    }

    @FXML
    public void cancelRunwayClick(ActionEvent actionEvent) {
        showOnlyMode(runwayOptions);
        enableElements(runwayMainMenu);
        hideOptions(editButtons);
        editLabel.setVisible(true);
    }

    public void disableElements(Pane elements){
        elements.setDisable(true);
    }

    public void showOnlyMode(Pane pane){
        pane.setMouseTransparent(true);
    }

    public void editableMode(Pane pane){
        pane.setMouseTransparent(false);
    }

    public void updateDetails(){

    }

    public void enableElements(Pane elements){
        elements.setDisable(false);
    }

    private void showOptions(Pane options){
        options.setVisible(true);
    }

    private void hideOptions(Pane options){
        options.setVisible(false);
    }

}
