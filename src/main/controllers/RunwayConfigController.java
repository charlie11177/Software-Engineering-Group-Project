package main.controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import main.model.Direction;
import main.model.LogicalRunWay;
import main.model.Model;
import main.model.PhysicalRunWay;

import java.util.ArrayList;
import java.util.Arrays;

public class RunwayConfigController {

    private Boolean edit;
    private final ArrayList<TextField> textFields;
    private ChangeListener<String> choiceBoxListener;

//    @FXML private VBox runwayMainMenu;
    @FXML private TitledPane runwayConfig;
    @FXML private ChoiceBox<String> runwayChoiceBox;
    @FXML private HBox editButtons;
    @FXML private Button editRunway;
    @FXML private Button newRunway;
    @FXML private VBox runwayOptions;

    @FXML private Label rightDegreeLabel;
    @FXML private Label rightPositionLabel;
    @FXML private ChoiceBox<String> leftPosition;
    @FXML private TextField leftDegreeTF;
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
    @FXML private Button removeRunway;
    public VBox runwayRoot;

    public RunwayConfigController(){
        edit = false;
        textFields = new ArrayList<>();
    }

    @FXML
    private void initialize(){
        Model.runwayConfigController = this;
        setupTextFields();
        setupDirectionChoiceBox();
        runwayConfig.expandedProperty().addListener((observable, oldValue, newValue) -> update(newValue));
        choiceBoxListener = (observable, oldValue, newValue) -> choiceBoxUpdater(newValue);
        setChoiceBoxListenerEnabled(true);
//        runwayChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> choiceBoxUpdater(newValue));
    }

    public void updateVisualisation() {
        System.out.println("Visualisation for runway " + Model.currentRunway);
        //TODO: calls for visualisation methods for displaying runways can be placed here
        // something like this: Model.CenterScreenController.draw();
    }

    private void setChoiceBoxListenerEnabled(Boolean enable) {
        if (enable)
            runwayChoiceBox.valueProperty().addListener(choiceBoxListener);
        else
            runwayChoiceBox.valueProperty().removeListener(choiceBoxListener);
    }

    private void setupTextFields(){
        textFields.addAll(Arrays.asList(leftTodaTF, leftToraTF, leftAsdaTF, leftLdaTF, leftThresholdTF,
                rightTodaTF, rightToraTF, rightAsdaTF, rightLdaTF, rightThresholdTF));
        for(TextField t : textFields)
            t.setTextFormatter(new TextFormatter<>(change -> {
                if (!change.getText().matches("[0-9]*")) change.setText("");
                return change;
            }));
        textFields.add(leftDegreeTF);
        leftDegreeTF.setTextFormatter(new TextFormatter<>(change -> {
            if (!change.getText().matches("[0-9]+")) change.setText("");
            return change;
        }));
        leftDegreeTF.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) rightDegreeLabel.setText("");
            else if(Integer.parseInt(newValue) > 18) rightDegreeLabel.setText("");
            else rightDegreeLabel.setText(String.valueOf(Integer.parseInt(newValue)+18));
        });
    }

    private void setupDirectionChoiceBox(){
        leftPosition.getItems().add("L");
        leftPosition.getItems().add("R");
        leftPosition.getItems().add("C");
        leftPosition.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "L": {rightPositionLabel.setText("R"); break;}
                case "R": {rightPositionLabel.setText("L"); break;}
                case "C": {rightPositionLabel.setText("C"); break;}
            }
        }));
    }

    private void update(Boolean expanded) {
        if(expanded){
            runwayConfig.setText("Runway");
            showOnlyMode();
//            System.out.println("RUNWAY UPDATE:" + Model.currentRunway);
            populateRunwayNames();
            if (Model.currentAirport == null) {
                noAirportView();
            } else if (Model.currentAirport.getRunways().isEmpty()){
                noRunwaysView();
            } else {
                setChoiceBoxListenerEnabled(false);
                if(Model.currentRunway != null) {
                    runwayChoiceBox.setValue(Model.currentRunway.toString());
                } else {
                    runwayChoiceBox.setValue(null);
                }
                setChoiceBoxListenerEnabled(true);
                hasRunwaysView();
            }
        } else {
            windowCloseProcedure();
        }
    }

    private void specifyView(boolean runwayOptionsVisible, double runwayOptionsOpacity,
                             boolean runwayChoiceBoxDisable, boolean newRunwayDisable, boolean deleteRunwayDisable, boolean editRunwayVisible, boolean editButtonsVisible) {
        runwayOptions.setVisible(runwayOptionsVisible);
        if(runwayOptionsOpacity == 1 || runwayOptionsOpacity == 0.5)
            runwayOptions.setOpacity(runwayOptionsOpacity);
        runwayChoiceBox.setDisable(runwayChoiceBoxDisable);
        newRunway.setDisable(newRunwayDisable);
        removeRunway.setDisable(deleteRunwayDisable);
        editRunway.setVisible(editRunwayVisible);
        editButtons.setVisible(editButtonsVisible);
    }

    private void noAirportView() {
        specifyView(false,
                0,
                true,
                true,
                true,
                false,
                false);
    }

    private void noRunwaysView() {
        specifyView(false,
                0,
                true,
                false,
                true,
                false,
                false);
    }

    private void hasRunwaysView() {
        boolean editRunwayVisible;
        boolean runwayOptionsVisible;
        boolean removeRunwayDisabled;

        if(Model.currentRunway != null) {
            populateRunwayDetails(Model.currentRunway);
            runwayOptionsVisible = true;
            removeRunwayDisabled = false;
            editRunwayVisible = true;
        } else {
            runwayOptionsVisible = false;
            removeRunwayDisabled = true;
            editRunwayVisible = false;
            clearRunwayDetails();
        }
        specifyView(runwayOptionsVisible,
                0,
                false,
                false,
                removeRunwayDisabled,
                editRunwayVisible,
                false);
    }

    private void editableMode(){
        Model.leftScreenController.calculateButton.setDisable(true);
        leftPosition.setMouseTransparent(false);
        for(TextField t : textFields)
            t.setEditable(true);
        specifyView(true,
                1,
                true,
                true,
                true,
                false,
                true);
    }

    private void runwaySettingsGrayedOut() {
        if(Model.currentRunway ==  null) System.err.println("CurrentRunway cannot be null");
        populateRunwayDetails(Model.currentRunway);
        specifyView(true,
                0.5,
                false,
                false,
                false,
                true,
                false);
    }

    private void showOnlyMode(){
        leftPosition.setMouseTransparent(true);
        for(TextField t : textFields)
            t.setEditable(false);
        if(Model.currentRunway == null)
            hasRunwaysView();
        else {
            runwaySettingsGrayedOut();
        }
    }

    public void windowCloseProcedure() {
        edit = false;
        Model.leftScreenController.calculateButton.setDisable(false);
        if(Model.currentRunway != null)
            runwayConfig.setText(Model.currentRunway.toString());
        else runwayConfig.setText("Runway");
//        System.out.println("RUNWAY CLOSE:"+Model.currentRunway);
    }

    private void populateRunwayNames() {
        setChoiceBoxListenerEnabled(false);
        runwayChoiceBox.getItems().clear();
        if (Model.currentAirport != null)
            for (PhysicalRunWay p : Model.currentAirport.getRunways()){
                runwayChoiceBox.getItems().add(p.toString());
            }
        setChoiceBoxListenerEnabled(true);
    }

    private void choiceBoxUpdater(String newValue) {
        if(newValue != null) {
            for(PhysicalRunWay p : Model.currentAirport.getRunways()) {
                if (p.toString().equals(newValue)) {
                    Model.setCurrentRunway(p);
                    runwaySettingsGrayedOut();
                    Model.console.addLog("Runway selected: " + Model.currentRunway.toString());
                }
            }
        }
        updateVisualisation();
        Model.obstacleConfigController.windowCloseProcedure();
    }

    private void populateRunwayDetails(PhysicalRunWay runWay){
        LogicalRunWay left = runWay.getLeftRunway();
        LogicalRunWay right = runWay.getRightRunway();
        leftPosition.setValue(String.valueOf(left.getDirection()));
        leftDegreeTF.setText(String.valueOf(left.getDegree()));
        leftTodaTF.setText(String.valueOf(left.getTODA()));
        leftToraTF.setText(String.valueOf(left.getTORA()));
        leftAsdaTF.setText(String.valueOf(left.getASDA()));
        leftLdaTF.setText(String.valueOf(left.getLDA()));
        leftThresholdTF.setText((String.valueOf(left.getThreshold())));
        rightPositionLabel.setText(String.valueOf(right.getDirection()));
        rightDegreeLabel.setText(String.valueOf(right.getDegree()));
        rightTodaTF.setText(String.valueOf(right.getTODA()));
        rightToraTF.setText(String.valueOf(right.getTORA()));
        rightAsdaTF.setText(String.valueOf(right.getASDA()));
        rightLdaTF.setText(String.valueOf(right.getLDA()));
        rightThresholdTF.setText(String.valueOf(right.getThreshold()));
    }

    private void clearRunwayDetails(){
        leftPosition.setValue("L");
        leftDegreeTF.setText("");
        leftTodaTF.setText("");
        leftToraTF.setText("");
        leftAsdaTF.setText("");
        leftLdaTF.setText("");
        leftThresholdTF.setText("");

        rightPositionLabel.setText("R");
        rightDegreeLabel.setText("");
        rightTodaTF.setText("");
        rightToraTF.setText("");
        rightAsdaTF.setText("");
        rightLdaTF.setText("");
        rightThresholdTF.setText("");
    }

    @FXML
    private void newRunwayClick() {
        clearRunwayDetails();
        editableMode();
    }

    @FXML
    private void removeRunwayClick() {
        setChoiceBoxListenerEnabled(false);
        Model.currentAirport.getRunways().remove(Model.currentRunway);
        Model.console.addLog("Runway removed: " + Model.currentRunway.toString());
        runwayChoiceBox.getItems().remove(Model.currentRunway.toString());
        Model.currentRunway = null;
        Model.obstaclePlaced = false;
        Model.obstacleConfigController.windowCloseProcedure();
        runwayChoiceBox.setValue(null);
        setChoiceBoxListenerEnabled(true);
        if(Model.currentAirport.getRunways().isEmpty()) noRunwaysView();
        else hasRunwaysView();
        updateVisualisation();
    }

    @FXML
    private void editRunwayClick() {
        edit = true;
        editableMode();
    }

    @FXML
    private void cancelRunwayClick() {
        edit = false;
        Model.leftScreenController.calculateButton.setDisable(false);
        if(Model.currentAirport.getRunways().isEmpty())
            noRunwaysView();
        else
        showOnlyMode();
    }

    @FXML
    private void saveRunwayClick(){
        for(TextField t : textFields){
            if (t.getText().isEmpty()) {
                AlertController.showWarningAlert("Some textfields are empty.");
                return;
            }
        }
        if(rightDegreeLabel.getText().isEmpty()){
            AlertController.showWarningAlert("Wrong value of the runway degrees.");
            return;
        }
        Pair<LogicalRunWay,LogicalRunWay> p = parseRunwayDetails();
        if(p == null) return;
        LogicalRunWay left = p.getKey();
        LogicalRunWay right = p.getValue();
        if (edit){
            String previousRunway = runwayChoiceBox.getValue();
            saveEditedRunway(left,right);
            edit = false;
            Model.console.addLog("Runway " + previousRunway + " edited to: " + Model.currentRunway.toString());
        } else {
            saveNewRunway(left,right);
            Model.console.addLog("Runway " + Model.currentRunway.toString() + " added");
//            Model.console.addLog("Runway selected: " + Model.currentRunway.toString());

        }
        updateVisualisation();
        showOnlyMode();
        Model.leftScreenController.calculateButton.setDisable(false);
    }

    private Pair<LogicalRunWay,LogicalRunWay> parseRunwayDetails() {
        Direction leftDirection = Direction.valueOf(leftPosition.getValue());
        int leftDegree = Integer.parseInt(leftDegreeTF.getText());
        int leftAsda = Integer.parseInt(leftAsdaTF.getText());
        int leftLda = Integer.parseInt(leftLdaTF.getText());
        int leftToda = Integer.parseInt(leftTodaTF.getText());
        int leftTora = Integer.parseInt(leftToraTF.getText());
        int leftThreshold = Integer.parseInt(leftThresholdTF.getText());

        Direction rightDirection = Direction.valueOf(rightPositionLabel.getText());
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
        //TODO: Add error checking (return null)
        if(leftToda < leftTora || rightToda < rightTora){
            AlertController.showWarningAlert("TODA Cannot be smaller than TORA");
            return null;
        }
        LogicalRunWay left = new LogicalRunWay(leftDegree, leftDirection,leftTora, leftToda, leftAsda, leftLda, leftThreshold);
        LogicalRunWay right = new LogicalRunWay(rightDegree, rightDirection,rightTora, rightToda, rightAsda, rightLda, rightThreshold);
        return new Pair<>(left,right);
    }

    private void saveEditedRunway(LogicalRunWay left, LogicalRunWay right) {
        if(Model.currentRunway != null){
            Model.currentRunway.setLeftRunway(left);
            Model.currentRunway.setRightRunway(right);
            setChoiceBoxListenerEnabled(false);
            runwayChoiceBox.getItems().remove(runwayChoiceBox.getValue());
            runwayChoiceBox.getItems().add(Model.currentRunway.toString());
            runwayChoiceBox.setValue(Model.currentRunway.toString());
            setChoiceBoxListenerEnabled(true);
        }
        else System.err.println("Error RunwayController:378");
    }

    private void saveNewRunway(LogicalRunWay left, LogicalRunWay right){
        int id = 1;
        if(Model.currentAirport.getRunways() != null)
            for (PhysicalRunWay r : Model.currentAirport.getRunways())
                if(r.getRunwayID() > id) id = r.getRunwayID();

        PhysicalRunWay runWay = null;
        try { runWay = new PhysicalRunWay(id,left,right,null); }
        catch (Exception e) { e.printStackTrace(); }
        Model.currentAirport.addNewRunway(runWay);
        Model.setCurrentRunway(runWay);
        runwayChoiceBox.getItems().add(runWay.toString());
        runwayChoiceBox.setValue(runWay.toString());
    }

}
