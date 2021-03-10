package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Airport;
import main.Model;

import java.util.ArrayList;


public class AirportConfigController {

    private Boolean edit;

    public Airport currentAirport;
    @FXML private ChoiceBox<String> aiportChoiceBox;
    @FXML private TextField airportNameTextField;
    @FXML private TextField airportCodeTextField;
    @FXML private VBox aiportOptions;
    @FXML private VBox aiportMainMenu;
    @FXML private Button editAirportButton;

    public AirportConfigController(){
        edit = false;
    }

    @FXML
    private void initialize(){
        Model.airportConfigController = this;
        aiportOptions.setVisible(false);
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
        //populateAriportNames() has to be before called before a listener is assigned
        populateAirportNames();
        addAirportChoiceBoxListener();
        emptyAirportsUpdate();
    }

    private void addAirportChoiceBoxListener(){
        aiportChoiceBox.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue != null){
                currentAirport = Model.getAirportByName(newValue.split(" ")[0]);
                //Model.runwayConfigController.runwayChoiceBoxChanger();
            } else if(!edit) {
                aiportChoiceBox.setDisable(true);
                editAirportButton.setDisable(true);
                System.err.println("Warning newvalue null");
            }
            emptyAirportsUpdate();
        });
    }

    private void emptyAirportsUpdate(){
        if(Model.airports.isEmpty()) {
            editAirportButton.setDisable(true);
            aiportChoiceBox.setDisable(true);
        } else {
            editAirportButton.setDisable(false);
            aiportChoiceBox.setDisable(false);
        }
    }

//    public String getSelectedAirport(){
//        return aiportChoiceBox.getValue().split(" ")[0];
//    }

    private void populateAirportNames(){
        if(!Model.airports.isEmpty()) {
            currentAirport = Model.airports.get(0);
            aiportChoiceBox.setValue(Model.airports.get(0).toString());
            for (Airport a : Model.airports) {
                aiportChoiceBox.getItems().add(a.toString());
            }
        } else emptyAirportsUpdate();
        //TODO: TEAM2 code for loading airport data from XML here
    }

    public void addAirports(Airport...airports){
        for(Airport a : airports){
            aiportChoiceBox.getItems().add(a.toString());
        }
    }

    @FXML
    private void newAirportClick(){
        edit = false;
        disableElements(aiportMainMenu);
        showOptions(aiportOptions);
    }

    @FXML
    private void editAirportClick(){
        edit = true;
        disableElements(aiportMainMenu);
        showOptions(aiportOptions);
        airportNameTextField.setText(currentAirport.getName());
        airportCodeTextField.setText(currentAirport.getCode());
    }

    /**
     * Method that saves the specified values of the textfields and saves them to the database
     */
    @FXML
    private void saveAirportClick(){
        String airportName = airportNameTextField.getText();
        String airportCode = airportCodeTextField.getText();
        String previousAirport = null;
        if(currentAirport != null) previousAirport = currentAirport.toString();
        if(airportName.equals("") || airportCode.equals("")) {
            Alert emptyFields = new Alert(Alert.AlertType.WARNING, "Name and Code fields cannot be empty!");
            emptyFields.setHeaderText(null);
            emptyFields.showAndWait();
            return;
        } else if (nameInUse(airportName, edit)){
            //TODO: TEAM2 code for error popup for same airport names here.
            Alert duplicateAirport = new Alert(Alert.AlertType.WARNING, "An Airport with that name already exists!");
            duplicateAirport.setHeaderText(null);
            duplicateAirport.showAndWait();
            return;
        } else if (codeInUse(airportCode, edit)) {
            Alert duplicateAirport = new Alert(Alert.AlertType.WARNING, "An Airport with that name already exists!");
            duplicateAirport.setHeaderText(null);
            duplicateAirport.showAndWait();
            return;
        } else if (currentAirport != null && (airportName + " (" + airportCode + ")").equals(previousAirport)) {
            cancelAirportClick();
            return;
        } else if (edit){
            aiportChoiceBox.getItems().remove(currentAirport.toString());
            currentAirport.setName(airportName);
            currentAirport.setCode(airportCode);
            aiportChoiceBox.getItems().add(currentAirport.toString());
            aiportChoiceBox.setValue(currentAirport.toString());
            Model.console.addLog("Airport " + previousAirport + " edited to: " + aiportChoiceBox.getValue());
            edit = false;
        } else {
            currentAirport = new Airport(airportName,airportCode,new ArrayList<>());
            Model.airports.add(currentAirport);
            aiportChoiceBox.getItems().add(currentAirport.toString());
            aiportChoiceBox.setValue(currentAirport.toString());
            Model.console.addLog("Airport " + aiportChoiceBox.getValue() + " added");
            editAirportButton.setDisable(false);
            aiportChoiceBox.setDisable(false);
        }
        enableElements(aiportMainMenu);
        hideOptions(aiportOptions);
        airportNameTextField.setText("");
        airportCodeTextField.setText("");
    }

    private boolean nameInUse(String desiredName, boolean edit){
        String[] data = aiportChoiceBox.getItems().toArray(new String[0]);
        for (String str : data) {
            String[] splitString = str.split(" ");
            if(!edit && splitString[0].equals(desiredName)) return true;
            if (splitString[0].equals(desiredName) && !splitString[0].equals(currentAirport.getName()))
                return true;
        }
        return false;
    }

    private boolean codeInUse(String desiredCode, boolean edit){
        String[] data = aiportChoiceBox.getItems().toArray(new String[0]);
        for (String str : data) {
            String[] splitString = str.split(" ");
            String code = splitString[1].replaceAll("[()]","");
            if(!edit && code.equals(desiredCode)) return true;
            if (code.equals(desiredCode) && !code.equals(currentAirport.getCode())) return true;
        }
        return false;
    }

    @FXML
    private void cancelAirportClick(){
        enableElements(aiportMainMenu);
        hideOptions(aiportOptions);
        airportNameTextField.setText("");
        airportCodeTextField.setText("");
    }

    private void disableElements(Pane elements){
        elements.setDisable(true);
    }

    private void enableElements(Pane elements){
        elements.setDisable(false);
    }

    private void showOptions(Pane options){
        options.setVisible(true);
    }

    private void hideOptions(Pane options){
        options.setVisible(false);
    }
}
