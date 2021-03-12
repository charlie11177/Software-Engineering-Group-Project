package main.controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import main.*;

import java.util.Arrays;
import java.util.List;

public class RunwayConfigController implements Observer{

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
    @FXML private Label rightPosition;
    @FXML private TextField leftDegreeTF;
    @FXML private Label rightDegreeLabel;
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
    @FXML private ChangeListener<String> choiceBoxListener;


    public RunwayConfigController(){
        edit = false;
    }

    @FXML
    private void initialize(){
        Model.runwayConfigController = this;
        runwayOptions.setOpacity(1);
        Model.attachAirportObserver(this);
        setupTextFields();
        noCurrentAirportView();
        choiceBoxListener = (observable, oldValue, newValue) -> runwayChoiceBoxUpdater(newValue);
        setChoiceBoxListenerEnabled(true);
        setupDirectionChoiceBox();
    }

    public void setupTextFields(){
        textFields = Arrays.asList(leftDegreeTF, leftTodaTF, leftToraTF, leftAsdaTF, leftLdaTF, leftThresholdTF,
                rightTodaTF, rightToraTF, rightAsdaTF, rightLdaTF, rightThresholdTF);
        for(TextField t : textFields)
            t.setTextFormatter(new TextFormatter<>(change -> {
                if (!change.getText().matches("[0-9]*")) change.setText("");
                return change;
            }));
        leftDegreeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals("")){
                int number = Integer.parseInt(newValue);
                if(number < 19)
                    rightDegreeLabel.setText(String.valueOf(number+18));
                else {
                    rightDegreeLabel.setText("0");
                }
            } else {
                rightDegreeLabel.setText("0");
            }
        });
    }

    public void setupDirectionChoiceBox(){
        leftPosition.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "L": {rightPosition.setText("R"); break;}
                case "R": {rightPosition.setText("L"); break;}
                case "C": {rightPosition.setText("C"); break;}
            }
        }));
        leftPosition.getItems().add("L");
        leftPosition.getItems().add("R");
        leftPosition.getItems().add("C");
    }

    private void runwayConfigMenu(){
        editRunway.setVisible(false);
        editButtons.setVisible(true);
        runwayOptions.setVisible(true);
        runwayOptions.setDisable(false);
        if(!edit) clearRunwayDetails();
    }

    private void noCurrentAirportView(){
        runwayMainMenu.setDisable(true);
        runwayOptions.setVisible(false);
        editRunway.setVisible(false);
    }

    private void selectedCurrentAirportView() {
        if(Model.currentAirport.getRunways().isEmpty()){
            clearRunwayDetails();
            runwayChoiceBox.setDisable(true);
            runwayOptions.setVisible(false);
            runwayMainMenu.setDisable(false);
            editRunway.setVisible(false);
        } else if (Model.currentRunway != null){
            populateRunwayNames();
            runwayChoiceBox.setDisable(false);
            runwayMainMenu.setDisable(false);
            runwayOptions.setVisible(true);
            if(Model.currentRunway != null)
                editRunway.setVisible(true);
            showOnlyMode();
        }
    }

    private void setChoiceBoxListenerEnabled(Boolean enable) {
        if (enable)
            runwayChoiceBox.getSelectionModel().selectedItemProperty().addListener(choiceBoxListener);
        else
            runwayChoiceBox.getSelectionModel().selectedItemProperty().removeListener(choiceBoxListener);
    }

    @Override
    public void update() {
        edit = false;
        setChoiceBoxListenerEnabled(false);
        runwayChoiceBox.getItems().clear();
        setChoiceBoxListenerEnabled(true);
        if(Model.currentAirport == null)
            noCurrentAirportView();
        else
            selectedCurrentAirportView();
    }

    public void populateRunwayNames(){
        setChoiceBoxListenerEnabled(false);
        runwayChoiceBox.getItems().clear();
        for(PhysicalRunWay r : Model.currentAirport.getRunways()){
            runwayChoiceBox.getItems().add(r.toString());
        }
        setChoiceBoxListenerEnabled(true);
    }


    private void runwayChoiceBoxUpdater(String value) {
        if(Model.currentAirport != null && Model.currentAirport.getRunways() != null){
            for(PhysicalRunWay r : Model.currentAirport.getRunways()){
                if(r.toString().equals(value)){
                    populateRunwayDetails(r);
                    editRunway.setVisible(true);
                }
            }
        }
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

        rightPosition.setText(String.valueOf(right.getDirection()));
        rightDegreeLabel.setText(String.valueOf(right.getDegree()));
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

        rightPosition.setText("R");
        rightDegreeLabel.setText("");
        rightTodaTF.setText("");
        rightToraTF.setText("");
        rightAsdaTF.setText("");
        rightLdaTF.setText("");
        rightThresholdTF.setText("");
    }

    @FXML
    public void newRunwayClick() {
        runwayConfigMenu();
        editableMode();
    }

    @FXML
    public void editRunwayClick() {
        edit = true;
        runwayConfigMenu();
        editableMode();
    }

    public void editableMode(){
        leftPosition.setMouseTransparent(false);
        rightPosition.setMouseTransparent(false);
        editButtons.setVisible(true);
        runwayChoiceBox.setDisable(true);
        for(TextField t : textFields)
            t.setEditable(true);
    }

    private Pair<LogicalRunWay,LogicalRunWay> parseRunwayDetails() {
        Direction leftDirection = Direction.valueOf(leftPosition.getValue());
        int leftDegree = Integer.parseInt(leftDegreeTF.getText());
        int leftAsda = Integer.parseInt(leftAsdaTF.getText());
        int leftLda = Integer.parseInt(leftLdaTF.getText());
        int leftToda = Integer.parseInt(leftTodaTF.getText());
        int leftTora = Integer.parseInt(leftToraTF.getText());
        int leftThreshold = Integer.parseInt(leftThresholdTF.getText());

        Direction rightDirection = Direction.valueOf(rightPosition.getText());
        int rightDegree = Integer.parseInt(rightDegreeLabel.getText());
        int rightAsda = Integer.parseInt(rightAsdaTF.getText());
        int rightLda = Integer.parseInt(rightLdaTF.getText());
        int rightToda = Integer.parseInt(rightTodaTF.getText());
        int rightTora = Integer.parseInt(rightToraTF.getText());
        int rightThreshold = Integer.parseInt(rightThresholdTF.getText());

        if(leftDegree > 36 || rightDegree > 36 || Math.abs(leftDegree-rightDegree) != 18){
            AlertController.showWarningAlert("Wrong value of the runway degrees.");
            return null;
        }
        LogicalRunWay left = new LogicalRunWay(leftDegree, leftDirection,leftTora, leftToda, leftAsda, leftLda, leftThreshold);
        LogicalRunWay right = new LogicalRunWay(rightDegree, rightDirection,rightTora, rightToda, rightAsda, rightLda, rightThreshold);
        return new Pair<>(left,right);
    }

    @FXML
    private void saveRunwayClick(){
        for(TextField t : textFields){
            if (t.getText().isEmpty()) {
                System.out.println("Some fields are empty");
                return;
            }
        }
        if(rightDegreeLabel.getText().isEmpty()){
            System.err.println("error");
            return;
        }
        Pair<LogicalRunWay,LogicalRunWay> p = parseRunwayDetails();
        if(p == null) return;
        LogicalRunWay left = p.getKey();
        LogicalRunWay right = p.getValue();

        if(left.getDegree() > 36 || right.getDegree() > 36 || Math.abs(left.getDegree()-right.getDegree()) != 18){
            AlertController.showWarningAlert("Wrong value of the runway degrees.");
            return;
        }
        if (edit){
            String previousRunway = runwayChoiceBox.getValue();
            saveEditedRunway(left,right);
            Model.console.addLog("Runway " + previousRunway + " edited to: " + Model.currentRunway.toString());
        } else {
            System.out.println(Model.currentAirport);
            System.out.println(Model.currentRunway);
            System.out.println(Model.currentAirport.getRunways().size());
            System.err.println(runwayChoiceBox.getItems());
            int id = 1;
            if(Model.currentAirport.getRunways() != null)
                for (PhysicalRunWay r : Model.currentAirport.getRunways())
                    if(r.getRunwayID() > id) id = r.getRunwayID();

            PhysicalRunWay runWay = null;
            try { runWay = new PhysicalRunWay(id,left,right,null); }
            catch (Exception e) { e.printStackTrace(); }
            Model.currentAirport.addNewRunway(runWay);
            Model.currentRunway = runWay;
            setChoiceBoxListenerEnabled(false);
            runwayChoiceBox.getItems().add(runWay.toString());
            runwayChoiceBox.setValue(runWay.toString());
            setChoiceBoxListenerEnabled(true);
            System.out.println(Model.currentAirport);
            System.out.println(Model.currentRunway);
            System.out.println(Model.currentAirport.getRunways().size());
            System.err.println(runwayChoiceBox.getItems());
            Model.console.addLog("Runway " + runwayChoiceBox.getValue() + " added");
        }
        runwayChoiceBox.setDisable(false);
        expandedRunwayMenuView();
    }

    private void expandedRunwayMenuView(){
        runwayChoiceBox.setDisable(false);
        runwayMainMenu.setDisable(false);
        runwayOptions.setDisable(true);
        editButtons.setVisible(false);
        editRunway.setDisable(false);
        editRunway.setVisible(true);
        showOnlyMode();
    }

    private void saveEditedRunway(LogicalRunWay left, LogicalRunWay right) {
        if(Model.currentAirport.getRunways() != null){
            Model.currentRunway.setLeftRunway(left);
            Model.currentRunway.setRightRunway(right);
            setChoiceBoxListenerEnabled(false);
            runwayChoiceBox.getItems().remove(runwayChoiceBox.getValue());
            runwayChoiceBox.getItems().add(Model.currentRunway.toString());
            setChoiceBoxListenerEnabled(true);
            runwayChoiceBox.setValue(Model.currentRunway.toString());
            edit = false;
        }
        else System.err.println("Error RunwayController:257");
    }

    @FXML
    public void cancelRunwayClick() {
        edit = false;
        Model.leftScreenController.calculateButton.setDisable(false);
        selectedCurrentAirportView();
        editRunway.setVisible(true);
        if(Model.currentRunway != null) {
            populateRunwayDetails(Model.currentRunway);
            runwayChoiceBox.setValue(Model.currentRunway.toString());
        } else {
            selectedCurrentAirportView();
        }
        showOnlyMode();
    }

    public void showOnlyMode(){
        leftPosition.setMouseTransparent(true);
        for(TextField t : textFields)
            t.setEditable(false);
        editButtons.setVisible(false);
        runwayOptions.setDisable(true);
    }


}
