package main.controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.Airport;
import main.Model;

import java.util.ArrayList;


public class AirportConfigController {

    private Boolean edit;
    @FXML private TitledPane airportConfig;

    @FXML private ChoiceBox<String> airportChoiceBox;
    @FXML private TextField airportNameTextField;
    @FXML private TextField airportCodeTextField;
    @FXML private VBox aiportOptions;
    @FXML private VBox aiportMainMenu;
    @FXML private Button editAirportButton;
    @FXML private ChangeListener<String> choiceBoxListener;

    public AirportConfigController(){}

    @FXML
    private void initialize() {
        edit = false;
        setupTextFields();
        Model.airportConfigController = this;
        airportConfig.expandedProperty().addListener((observable, oldValue, newValue) -> update(newValue));
        airportChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> choiceBoxUpdater(newValue));
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

    private void update(Boolean expanded){
        if(expanded){
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
        }
    }

    private void noAirportsView() {
        editAirportButton.setDisable(true);
        airportChoiceBox.setDisable(true);
        aiportOptions.setVisible(false);
        aiportMainMenu.setDisable(false);
    }

    private void notSelectedAirportView() {
        editAirportButton.setDisable(true);
        airportChoiceBox.setDisable(false);
        aiportOptions.setVisible(false);
        aiportMainMenu.setDisable(false);
    }

    private void selectedAirportView() {
        editAirportButton.setDisable(false);
        airportChoiceBox.setDisable(false);
        aiportOptions.setVisible(false);
        aiportMainMenu.setDisable(false);
    }

    private void populateAirports(){
        airportChoiceBox.getItems().clear();
        for (Airport a : Model.getAirports()){
            airportChoiceBox.getItems().add(a.toString());
        }
        if (Model.currentAirport != null)
            airportChoiceBox.setValue(Model.currentAirport.toString());
        else
            airportChoiceBox.setValue(null);
    }

    private void windowCloseProcedure(){
        edit = false;
        System.out.println(Model.getAirports().size());
        System.out.println(Model.currentAirport);
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

    public void newAirportClick() {
        inputView();
    }

    public void editAirportClick() {
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
            refreshComboBox();
            Model.console.addLog("Airport " + previousAirportString + " edited to: " + Model.currentAirport.toString());
            edit = false;
        } else { //Creating a new airport
            Airport a = new Airport(airportName,airportCode,new ArrayList<>());
            Model.currentRunway = null;
            Model.airports.add(a);
            Model.setCurrentAirport(a);
            refreshComboBox();
            Model.console.addLog("Airport " + Model.currentAirport + " added");
            aiportOptions.setVisible(false);
            aiportMainMenu.setDisable(false);
            selectedAirportView();
        }
    }

//    private boolean nameInUse(String desiredName){
//        for(Airport a : Model.airports){
//            if(a.getName().equals(desiredName) && !edit){
//                System.out.println("HERE");
//                return true;
//            }
//            else if (edit && !Model.currentAirport.equals(a) && a.getName().equals(desiredName)){
//                System.out.println("THERE");
//                return true;
//            }
//        }
//        return false;
//    }

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

    public void cancelAirportClick() {
        edit = false;
        aiportOptions.setVisible(false);
        aiportMainMenu.setDisable(false);
    }

    private void refreshComboBox(){
        airportChoiceBox.getItems().clear();
        for(Airport a : Model.getAirports())
            airportChoiceBox.getItems().add(a.toString());
        airportChoiceBox.setValue(Model.currentAirport.toString());
    }

    public void choiceBoxUpdater(String newValue) {
        for(Airport a : Model.getAirports()) {
            if (a.toString().equals(newValue)) {
                Model.setCurrentAirport(a);
                selectedAirportView();
            }
        }
    }

}