package main.controllers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.model.Airport;
import main.model.Model;

import java.util.ArrayList;


public class AirportConfigController {

    private Boolean edit;
    private ChangeListener<String> choiceBoxListener;

    @FXML private TitledPane airportConfig;
    @FXML private ChoiceBox<String> airportChoiceBox;
    @FXML private TextField airportNameTextField;
    @FXML private TextField airportCodeTextField;
    @FXML private VBox aiportOptions;
    @FXML private VBox aiportMainMenu;
    @FXML private Button editAirportButton;
    @FXML private Button deleteAirportButton;

    public AirportConfigController(){
        edit = false;
    }

    @FXML
    private void initialize() {
        Model.airportConfigController = this;
        setupTextFields();
        airportConfig.expandedProperty().addListener((observable, oldValue, newValue) -> update(newValue));
        choiceBoxListener = (observable, oldValue, newValue) -> choiceBoxUpdater(newValue);
    }

    private void setupTextFields(){
        airportNameTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) change.setText("_");
            return change;
        }));
        airportCodeTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) change.setText("_");
            else if(!change.getText().matches("[A-Za-z0-9]*")) change.setText("");
            change.setText(change.getText().toUpperCase());
            return change;
        }));
    }

    private void setChoiceBoxListenerEnabled(Boolean enable) {
        if (enable)
            airportChoiceBox.valueProperty().addListener(choiceBoxListener);
        else
            airportChoiceBox.valueProperty().removeListener(choiceBoxListener);
    }

    private void update(Boolean expanded){
        if(expanded){
            airportConfig.setText("Airport");
            aiportOptions.setVisible(false);
            populateAirports();
            if(Model.getAirports().isEmpty()){
                noAirportsView();
            } else if (Model.currentAirport == null) {
                notSelectedAirportView();
            } else {
                selectedAirportView();
            }
        } else {
            windowCloseProcedure();
            System.out.println("AIRPORT UPDATE:" + Model.currentRunway);
        }
    }

    private void choiceBoxUpdater(String newValue) {
        for(Airport a : Model.getAirports()) {
            if (a.toString().equals(newValue)) {
                Model.setCurrentAirport(a);
                selectedAirportView();
            }
        }
        Model.console.addLog("Airport selected: " + Model.currentAirport.getName());
        Model.runwayConfigController.windowCloseProcedure();
        Model.obstacleConfigController.windowCloseProcedure();
    }

    private void refreshChoiceBox(){
        airportChoiceBox.getItems().clear();
        for(Airport a : Model.getAirports())
            airportChoiceBox.getItems().add(a.toString());
        airportChoiceBox.setValue(Model.currentAirport.toString());
    }

    private void noAirportsView() {
        editAirportButton.setDisable(true);
        deleteAirportButton.setDisable(true);
        airportChoiceBox.setDisable(true);
        aiportOptions.setVisible(false);
        aiportMainMenu.setDisable(false);
    }

    private void notSelectedAirportView() {
        editAirportButton.setDisable(true);
        deleteAirportButton.setDisable(true);
        airportChoiceBox.setDisable(false);
        aiportOptions.setVisible(false);
        aiportMainMenu.setDisable(false);
    }

    private void selectedAirportView() {
        editAirportButton.setDisable(false);
        deleteAirportButton.setDisable(false);
        airportChoiceBox.setDisable(false);
        aiportOptions.setVisible(false);
        aiportMainMenu.setDisable(false);
    }

    private void populateAirports(){
        setChoiceBoxListenerEnabled(false);
        airportChoiceBox.getItems().clear();
        for (Airport a : Model.getAirports()){
            airportChoiceBox.getItems().add(a.toString());
        }
        if (Model.currentAirport != null)
            airportChoiceBox.setValue(Model.currentAirport.toString());
        else
            airportChoiceBox.setValue(null);
        setChoiceBoxListenerEnabled(true);
    }

    private void windowCloseProcedure(){
        edit = false;
        if(Model.currentAirport != null)
            airportConfig.setText(Model.currentAirport.toString());
        else airportConfig.setText("Airport");
        System.out.println(Model.getAirports().size());
        System.out.println(Model.currentRunway);
    }

    private void inputView() {
        airportNameTextField.setText("");
        airportCodeTextField.setText("");
        aiportOptions.setVisible(true);
        aiportMainMenu.setDisable(true);
    }

    private void editView() {
        airportNameTextField.setText(Model.currentAirport.getName());
        airportCodeTextField.setText(Model.currentAirport.getCode());
        aiportOptions.setVisible(true);
        aiportMainMenu.setDisable(true);
    }

    @FXML
    private void newAirportClick() {
        inputView();
    }

    @FXML
    private void editAirportClick() {
        edit = true;
        editView();
    }

    @FXML
    private void saveAirportClick(){
        String airportName = airportNameTextField.getText();
        String airportCode = airportCodeTextField.getText();
        if(airportName.equals("") || airportCode.equals("")) {
            AlertController.showWarningAlert("Name and Code fields cannot be empty!");
        } else if (nameInUse(airportName)){
            AlertController.showWarningAlert("An Airport with that name already exists!");
        } else if (codeInUse(airportCode)) {
            AlertController.showWarningAlert("An Airport with that code already exists!");
        } else if (edit && Model.currentAirport != null && (airportName + " (" + airportCode + ")").equals(Model.currentAirport.toString())) {
            AlertController.showInfoAlert("No changes were made.");
            selectedAirportView();
        } else if (edit){
            String previousAirportString = Model.currentAirport.toString();
            Model.currentAirport.setName(airportName);
            Model.currentAirport.setCode(airportCode);
            aiportOptions.setVisible(false);
            aiportMainMenu.setDisable(false);
            setChoiceBoxListenerEnabled(false);
            refreshChoiceBox();
            setChoiceBoxListenerEnabled(true);
            Model.console.addLog("Airport " + previousAirportString + " edited to: " + Model.currentAirport.toString());
            edit = false;
        } else { //Creating a new airport
            Airport a = new Airport(airportName,airportCode,new ArrayList<>());
            Model.addAirports(a);
            Model.setCurrentAirport(a);
            setChoiceBoxListenerEnabled(false);
            refreshChoiceBox();
            setChoiceBoxListenerEnabled(true);
            Model.console.addLog("Airport " + Model.currentAirport + " added");
            Model.console.addLog("Airport selected: " + Model.currentAirport.getName());
            aiportOptions.setVisible(false);
            aiportMainMenu.setDisable(false);
            selectedAirportView();
        }
    }

    private boolean codeInUse(String desiredCode){
        for(Airport a : Model.airports){
            if(a.getCode().equals(desiredCode) && !edit){
                return true;
            } else if (a.getCode().equals(desiredCode) && !a.equals(Model.currentAirport))
                return true;
        }
        return false;
    }

    private boolean nameInUse(String desiredName){
        for(Airport a : Model.airports){
            if(a.getName().equals(desiredName) && !edit){
                return true;
            } else if (a.getName().equals(desiredName) && !a.equals(Model.currentAirport))
                return true;
        }
        return false;
    }

    @FXML
    private void cancelAirportClick() {
        edit = false;
        aiportOptions.setVisible(false);
        aiportMainMenu.setDisable(false);
    }

    @FXML
    private void deleteAirportClick() {
        Model.airports.remove(Model.currentAirport);
        Model.console.addLog("Airport " + Model.currentAirport + " removed");
        Model.currentAirport = null;
        Model.currentRunway = null;
        Model.obstaclePlaced = false;
        Model.runwayConfigController.windowCloseProcedure();
        Model.obstacleConfigController.windowCloseProcedure();
        setChoiceBoxListenerEnabled(false);
        airportChoiceBox.getItems().remove(airportChoiceBox.getValue());
        airportChoiceBox.setValue(null);
        setChoiceBoxListenerEnabled(true);
        if(Model.airports.isEmpty()) noAirportsView();
        else notSelectedAirportView();
    }
}