package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.*;

import java.util.Arrays;
import java.util.List;

public class RunwayConfigController {

    private Boolean edit;
    private List<TextField> textFields;

    public TitledPane runwayConfig;
    public VBox runwayMainMenu;
    public ChoiceBox<String> runwayChoiceBox;
    public PhysicalRunWay currentRunway;

    @FXML private HBox editButtons;
    @FXML private Button editRunway;
    @FXML private VBox runwayOptions;
    @FXML private ChoiceBox<String> leftPosition;
    @FXML private ChoiceBox<String> rightPosition;
    @FXML private TextField leftDegreeTF;
    @FXML private TextField rightDegreeTF;
    @FXML private TextField leftToraTF;
    @FXML private TextField rightToraTF;
    @FXML private TextField leftTodaTF;
    @FXML private TextField rightTodaTF;
    @FXML private TextField leftAsdaTF;
    @FXML private TextField rightAsdaTF;
    @FXML private TextField leftLdaTF;
    @FXML private TextField rightLdaTF;
    @FXML private TextField leftThresholdTF;
    @FXML private TextField rightThresholdTF;

    public RunwayConfigController(){
        edit = false;
    }

    @FXML
    private void initialize(){
        Model.runwayConfigController = this;
        setupTextFields();
        runwayMenuChanger();
        runwayDetailsChanger(runwayChoiceBox.getValue());
        addRunwayChoiceBoxListener();
        addTilePaneListener();
        setupDirectionChoiceBox();
        hideOptions(editButtons);
        showOnlyMode();
    }

    public void setupTextFields(){
        textFields = Arrays.asList(leftDegreeTF, leftTodaTF, leftToraTF, leftAsdaTF, leftLdaTF, leftThresholdTF,
                rightDegreeTF, rightTodaTF, rightToraTF, rightAsdaTF, rightLdaTF, rightThresholdTF);

        for(TextField t : textFields)
            t.setTextFormatter(new TextFormatter<>(change -> {
                if (!change.getText().matches("[0-9]*")) change.setText("");
                return change;
            }));
    }

    public void setupDirectionChoiceBox(){
        directionChoiceBoxHandler(leftPosition, rightPosition);
        directionChoiceBoxHandler(rightPosition, leftPosition);
        leftPosition.getItems().add("L");
        leftPosition.getItems().add("R");
        leftPosition.getItems().add("C");
        rightPosition.getItems().add("L");
        rightPosition.getItems().add("R");
        rightPosition.getItems().add("C");
    }

    public void directionChoiceBoxHandler(ChoiceBox<String> choiceBox, ChoiceBox<String> oppositeChoiceBox){
        choiceBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
                    switch (newValue) {
                        case "L": {oppositeChoiceBox.setValue("R"); break;}
                        case "R": {oppositeChoiceBox.setValue("L"); break;}
                        case "C": {oppositeChoiceBox.setValue("C"); break;}
                    }
        }));
    }

    public void runwayMenuChanger() {
        Airport airport = Model.airportConfigController.currentAirport;
        if(airport != null && !airport.getRunways().isEmpty()){
            this.populateRunwayNames(airport.getRunways());
            this.showOptions(runwayOptions);
            //editLabel.setVisible(true);
            editRunway.setVisible(true);
            runwayChoiceBox.setDisable(false);
        }
        else{
            this.runwayChoiceBox.getItems().clear();
            this.clearRunwayDetails();
            this.hideOptions(runwayOptions);
            //editLabel.setVisible(false);
            editRunway.setVisible(false);
            runwayChoiceBox.setDisable(true);
        }
        editButtons.setVisible(false);
    }

    public void addTilePaneListener(){
        runwayConfig.expandedProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue && Model.airports.isEmpty()) {
                disableElements(runwayMainMenu);
            }
            else if (newValue){
                runwayMenuChanger();
                enableElements(runwayMainMenu);
            }
            System.out.println("Number of airports:" + Model.airports.size());
        });
    }

    private void addRunwayChoiceBoxListener(){
        runwayChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> runwayDetailsChanger(newValue)
        );
    }

    private void runwayDetailsChanger(String value) {
        Airport airport = Model.airportConfigController.currentAirport;
        if(airport != null && airport.getRunways() != null){
            for(PhysicalRunWay r : airport.getRunways()){
                if(r.toString().equals(value)){
                    populateRunwayDetails(r);
                    currentRunway = r;
                }
            }
        }
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
        leftPosition.setValue(String.valueOf(left.getDirection()));
        leftDegreeTF.setText(String.valueOf(left.getDegree()));
        leftTodaTF.setText(String.valueOf(left.getTODA()));
        leftToraTF.setText(String.valueOf(left.getTORA()));
        leftAsdaTF.setText(String.valueOf(left.getASDA()));
        leftLdaTF.setText(String.valueOf(left.getLDA()));
        leftThresholdTF.setText((String.valueOf(left.getThreshold())));

        rightPosition.setValue(String.valueOf(right.getDirection()));
        rightDegreeTF.setText(String.valueOf(right.getDegree()));
        rightTodaTF.setText(String.valueOf(right.getTODA()));
        rightToraTF.setText(String.valueOf(right.getTORA()));
        rightAsdaTF.setText(String.valueOf(right.getASDA()));
        rightLdaTF.setText(String.valueOf(right.getLDA()));
        rightThresholdTF.setText(String.valueOf(left.getThreshold()));
    }

    public void clearRunwayDetails(){
        leftPosition.setValue("L");
        leftDegreeTF.setText("");
        leftTodaTF.setText("");
        leftToraTF.setText("");
        leftAsdaTF.setText("");
        leftLdaTF.setText("");
        leftThresholdTF.setText("");

        rightPosition.setValue("R");
        rightDegreeTF.setText("");
        rightTodaTF.setText("");
        rightToraTF.setText("");
        rightAsdaTF.setText("");
        rightLdaTF.setText("");
        rightThresholdTF.setText("");
    }

    @FXML
    public void newRunwayClick() {
        edit = false;
        clearRunwayDetails();
        disableElements(runwayMainMenu);
        //editLabel.setVisible(false);
        editRunway.setVisible(false);
        showOptions(runwayOptions);
        editableMode();
        showOptions(editButtons);
    }

    @FXML
    public void editRunwayClick() {
        edit = true;
        disableElements(runwayMainMenu);
        //editLabel.setVisible(false);
        editRunway.setVisible(false);
        editableMode();
        showOptions(editButtons);
    }

    @FXML
    private void saveRunwayClick(){
        Airport airport = Model.airportConfigController.currentAirport;
        String previousRunway = runwayChoiceBox.getValue();

        Direction leftDirection = Direction.valueOf(leftPosition.getValue());
        int leftDegree = Integer.parseInt(leftDegreeTF.getText());
        int leftAsda = Integer.parseInt(leftAsdaTF.getText());
        int leftLda = Integer.parseInt(leftLdaTF.getText());
        int leftToda = Integer.parseInt(leftTodaTF.getText());
        int leftTora = Integer.parseInt(leftToraTF.getText());
        int leftThreshold = Integer.parseInt(leftThresholdTF.getText());

        Direction rightDirection = Direction.valueOf(rightPosition.getValue());
        int rightDegree = Integer.parseInt(rightDegreeTF.getText());
        int rightAsda = Integer.parseInt(rightAsdaTF.getText());
        int rightLda = Integer.parseInt(rightLdaTF.getText());
        int rightToda = Integer.parseInt(rightTodaTF.getText());
        int rightTora = Integer.parseInt(rightToraTF.getText());
        int rightThreshold = Integer.parseInt(rightThresholdTF.getText());

        for(TextField t : textFields){
            if (t.getText().isEmpty()) {
                //TODO: Error notficiation for empty text fields
                System.out.println("Some fields are empty");
                return;
            }
        }
        showOnlyMode();
        enableElements(runwayMainMenu);
        hideOptions(editButtons);
        //editLabel.setVisible(true);
        editRunway.setVisible(true);

        if(edit){
            if(airport.getRunways() != null){
                PhysicalRunWay runWay = airport.findRunwayByID(airport.getRunways(), currentRunway.getRunwayID());
                LogicalRunWay left = runWay.getLeftRunway();
                LogicalRunWay right = runWay.getRightRunway();
                left.setDirection(leftDirection);
                left.setDegree(leftDegree);
                left.setASDA(leftAsda);
                left.setLDA(leftLda);
                left.setTODA(leftToda);
                left.setTORA(leftTora);
                left.setThreshold(leftThreshold);
                right.setDirection(rightDirection);
                right.setDegree(rightDegree);
                right.setASDA(rightAsda);
                right.setLDA(rightLda);
                right.setThreshold(rightThreshold);
                right.setTODA(rightToda);
                right.setTORA(rightTora);
                runwayChoiceBox.getItems().remove(runwayChoiceBox.getSelectionModel().getSelectedItem());
                runwayChoiceBox.getItems().add(currentRunway.toString());
                runwayChoiceBox.setValue(currentRunway.toString());
                edit = false;
                Model.console.addLog("Runway " + previousRunway + " edited to: " + currentRunway.toString());
            }
            else System.err.println("Error RunwayController:257");
        } else {
            LogicalRunWay left = new LogicalRunWay(leftDegree,leftDirection,leftTora,leftToda,leftAsda,leftLda, leftThreshold);
            LogicalRunWay right = new LogicalRunWay(rightDegree,rightDirection,rightTora,rightToda,rightAsda,rightLda, rightThreshold);
            int id = 1;
            if(airport.getRunways() != null){
                for (PhysicalRunWay r : airport.getRunways()){
                    if(r.getRunwayID() > id) id = r.getRunwayID();
                }
                runwayChoiceBox.setDisable(false);
                editRunway.setDisable(false);
            }
            PhysicalRunWay runWay = null;
            try { runWay = new PhysicalRunWay(id,left,right,null); }
            catch (Exception e) { e.printStackTrace(); }
            airport.addNewRunway(runWay);
            runwayChoiceBox.getItems().add(runWay.toString());
            runwayChoiceBox.setValue(runWay.toString());
            Model.console.addLog("Runway " + runwayChoiceBox.getValue() + " added");
        }
        showOnlyMode();
        enableElements(runwayMainMenu);
        //editLabel.setVisible(true);
        editRunway.setVisible(true);
    }

    @FXML
    public void cancelRunwayClick() {
        if(runwayChoiceBox.getItems().isEmpty()){
            hideOptions(editButtons);
            enableElements(runwayMainMenu);
            hideOptions(runwayOptions);
            clearRunwayDetails();
            return;
        }
        showOnlyMode();
        enableElements(runwayMainMenu);
        hideOptions(editButtons);
        //editLabel.setVisible(true);
        editRunway.setVisible(true);
        populateRunwayDetails(currentRunway);
    }

    public void showOnlyMode(){
        leftPosition.setMouseTransparent(true);
        rightPosition.setMouseTransparent(true);
        for(TextField t : textFields)
            t.setEditable(false);
        runwayOptions.setOpacity(0.75);
    }

    public void editableMode(){
        leftPosition.setMouseTransparent(false);
        rightPosition.setMouseTransparent(false);
        for(TextField t : textFields)
            t.setEditable(true);
        runwayOptions.setOpacity(1);
    }

    public void enableElements(Pane elements){
        elements.setDisable(false);
    }

    public void disableElements(Pane elements){
        elements.setDisable(true);
    }

    private void showOptions(Pane options){
        options.setVisible(true);
    }

    private void hideOptions(Pane options){
        options.setVisible(false);
    }

}
