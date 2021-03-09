package main.controllers;

import javafx.event.ActionEvent;
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
    private String valueEdited;
    private List<TextField> textFields;

    public TitledPane runwayConfig;
    public VBox runwayMainMenu;
    public ChoiceBox<String> runwayChoiceBox;
    public PhysicalRunWay currentRunway;

    @FXML private HBox editButtons;
    @FXML private Button saveRunway;
    @FXML private Button cancelRunway;
    @FXML private Button newRuway;
    @FXML private Button editRunway;
    @FXML private VBox runwayOptions;
    @FXML private ChoiceBox<String> leftPosition;
    @FXML private ChoiceBox<String> rightPosition;
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
        setupTextFields();
        runwayChoiceBoxChanger();
        runwayDetailsChanger(runwayChoiceBox.getValue());

        addAirportChoiceBoxListener();
        addRunwayChoiceBoxListener();
        addTilePaneListener();

        Model.runwayConfigController = this;
        setupDirectionChoiceBox();
        hideOptions(editButtons);
        showOnlyMode();
    }
    public void setupTextFields(){
        textFields = Arrays.asList(leftDegree,leftToda,leftTora,leftAsda,leftLda,
                rightDegree,rightToda,rightTora,rightAsda,rightLda);
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
        choiceBox.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    switch (newValue) {
                        case "L":
                            oppositeChoiceBox.setValue("R");
                            break;
                        case "R":
                            oppositeChoiceBox.setValue("L");
                            break;
                        case "C":
                            oppositeChoiceBox.setValue("C");
                            break;
                    }
                }));
    }

    public void addAirportChoiceBoxListener(){
        Model.airportConfigController.aiportChoiceBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener( (observable, oldValue, newValue) -> runwayChoiceBoxChanger());
    }

    private void runwayChoiceBoxChanger() {
        String name = Model.airportConfigController.getSelectedAirport();
        Airport airport = Model.getAirportByName(name);
        if(airport != null && !airport.getRunways().isEmpty()){
            this.populateRunwayNames(airport.getRunways());
            this.showOptions(runwayOptions);
            editLabel.setVisible(true);
            editButtons.setVisible(false);
            editRunway.setDisable(false);
            runwayChoiceBox.setDisable(false);
        }
        else{
            this.runwayChoiceBox.getItems().clear();
            this.clearRunwayDetails();
            this.hideOptions(runwayOptions);
            editLabel.setVisible(false);
            editButtons.setVisible(false);
            editRunway.setDisable(true);
            runwayChoiceBox.setDisable(true);
        }
    }

    private void addRunwayChoiceBoxListener(){
        runwayChoiceBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener( (observable, oldValue, newValue) -> runwayDetailsChanger(newValue));
    }

    private void runwayDetailsChanger(String value) {
        String name = Model.airportConfigController.getSelectedAirport();
        //System.out.println(name);
        Airport airport = Model.getAirportByName(name);
        if(airport.getRunways() != null){
            for(PhysicalRunWay r : airport.getRunways()){
                if(r.toString().equals(value)){
                    populateRunwayDetails(r);
                    currentRunway = r;
                }
            }
        }
    }

    public void addTilePaneListener(){
        runwayConfig.expandedProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue == true && Model.airports.isEmpty()) {
                disableElements(runwayMainMenu);
            }
            else if (newValue == true){
                enableElements(runwayMainMenu);
            }
            System.out.println("Airports:" + Model.airports.size());
        });
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
        leftDegree.setText(String.valueOf(left.getDegree()));
        leftToda.setText(String.valueOf(left.getTODA()));
        leftTora.setText(String.valueOf(left.getTORA()));
        leftAsda.setText(String.valueOf(left.getASDA()));
        leftLda.setText(String.valueOf(left.getLDA()));

        rightPosition.setValue(String.valueOf(right.getDirection()));
        rightDegree.setText(String.valueOf(right.getDegree()));
        rightToda.setText(String.valueOf(right.getTODA()));
        rightTora.setText(String.valueOf(right.getTORA()));
        rightAsda.setText(String.valueOf(right.getASDA()));
        rightLda.setText(String.valueOf(right.getLDA()));
    }

    public void clearRunwayDetails(){
        leftPosition.setValue("L");
        leftDegree.setText("");
        leftToda.setText("");
        leftTora.setText("");
        leftAsda.setText("");
        leftLda.setText("");

        rightPosition.setValue("L");
        rightDegree.setText("");
        rightToda.setText("");
        rightTora.setText("");
        rightAsda.setText("");
        rightLda.setText("");
    }

    @FXML
    public void newRunwayClick() {
        edit = false;
        clearRunwayDetails();
        disableElements(runwayMainMenu);
        editLabel.setVisible(false);
        showOptions(runwayOptions);
        editableMode();
        showOptions(editButtons);
    }


    @FXML
    public void editRunwayClick() {
        edit = true;
        disableElements(runwayMainMenu);
        editLabel.setVisible(false);
        editableMode();
        showOptions(editButtons);
    }

    @FXML
    private void saveRunwayClick() {
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
        editLabel.setVisible(true);
        String name = Model.airportConfigController.getSelectedAirport();
        Airport airport = Model.getAirportByName(name);

        //TODO: change variable names
        Direction leftDirection = Direction.valueOf(leftPosition.getValue().toString());
        int leftDeg = Integer.parseInt(leftDegree.getText());
        int leftASDA = Integer.parseInt(leftAsda.getText());
        int leftLDA = Integer.parseInt(leftLda.getText());
        int leftTODA = Integer.parseInt(leftToda.getText());
        int leftTORA = Integer.parseInt(leftTora.getText());

        Direction rightDirection = Direction.valueOf(rightPosition.getValue().toString());
        int rightDeg = Integer.parseInt(rightDegree.getText());
        int rightASDA = Integer.parseInt(rightAsda.getText());
        int rightLDA = Integer.parseInt(rightLda.getText());
        int rightTODA = Integer.parseInt(rightToda.getText());
        int rightTORA = Integer.parseInt(rightTora.getText());

        if(edit){
            edit = false;
            if(airport.getRunways() != null){
                PhysicalRunWay runWay = airport.findRunwayByID(airport.getRunways(), currentRunway.getRunwayID());
                LogicalRunWay left = runWay.getLeftRunway();
                LogicalRunWay right = runWay.getRightRunway();

                left.setDirection(leftDirection);
                left.setDegree(leftDeg);
                left.setASDA(leftASDA);
                left.setLDA(leftLDA);
                left.setTODA(leftTODA);
                left.setTORA(leftTORA);

                right.setDirection(rightDirection);
                right.setDegree(rightDeg);
                right.setASDA(rightASDA);
                right.setLDA(rightLDA);
                right.setTODA(rightTODA);
                right.setTORA(rightTORA);

                runwayChoiceBox.getItems().remove(runwayChoiceBox.getSelectionModel().getSelectedItem());
                runwayChoiceBox.getItems().add(currentRunway.toString());
                runwayChoiceBox.setValue(currentRunway.toString());
            }
            else{
                System.err.println("Error RunwayController:257");
            }
        } else{
            LogicalRunWay left = new LogicalRunWay(leftDeg,leftDirection,leftTORA,leftTODA,leftASDA,leftLDA);
            LogicalRunWay right = new LogicalRunWay(rightDeg,rightDirection,rightTORA,rightTODA,rightASDA,rightLDA);
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
        }
        showOnlyMode();
        enableElements(runwayMainMenu);
        editLabel.setVisible(true);
        //clearRunwayDetails();
    }

    @FXML
    public void cancelRunwayClick(ActionEvent actionEvent) {
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
        editLabel.setVisible(true);
        populateRunwayDetails(currentRunway);
    }

    public void disableElements(Pane elements){
        elements.setDisable(true);
    }

    public void showOnlyMode(){
        leftPosition.setMouseTransparent(true);
        rightPosition.setMouseTransparent(true);
        for(TextField t : textFields)
            t.setEditable(false);
    }

    public void editableMode(){
        leftPosition.setMouseTransparent(false);
        rightPosition.setMouseTransparent(false);
        for(TextField t : textFields)
            t.setEditable(true);
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
