package main.controllers;

import javafx.fxml.FXML;
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

    public AirportConfigController(){
        edit = false;
    }

    @FXML public void initialize(){
        Model.airportConfigController = this;
        aiportOptions.setVisible(false);
        airportNameTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) change.setText("_");
            return change;
        }));
        airportCodeTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) change.setText("_");
            change.setText(change.getText().toUpperCase());
            return change;
        }));
        //populateAriportNames() has to be before called before a listener is assigned
        populateAirportNames();
        addAirportChoiceBoxListener();
    }

    private void addAirportChoiceBoxListener(){
        aiportChoiceBox.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            currentAirport = Model.getAirportByName(newValue.split(" ")[0]);
            Model.runwayConfigController.runwayChoiceBoxChanger();
        });
    }

//    public String getSelectedAirport(){
//        return aiportChoiceBox.getValue().split(" ")[0];
//    }

    private void populateAirportNames(){
        if(!Model.airports.isEmpty()){
            currentAirport = Model.airports.get(0);
            aiportChoiceBox.setValue(Model.airports.get(0).toString());
        }
        for (Airport a : Model.airports) {
            aiportChoiceBox.getItems().add(a.toString());
        }
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
        String choiceBoxItem = null;
        if(airportName.equals("") || airportCode.equals("")) {
            //TODO: TEAM2 code for error popup for empty textfields here.
            System.out.println("Empty textfields");
            return;
        } else if (nameInUse(airportName)){
            //TODO: TEAM2 code for error popup for same airport names here.
            System.out.println("Airport name already used");
            return;
        } else if (codeInUse(airportCode)){
            //TODO: TEAM2 code for error popup for same airport names here.
            System.out.println("Airport code already used");
            return;
        } else if (edit){
            aiportChoiceBox.getItems().remove(currentAirport.toString());
            currentAirport.setName(airportName);
            currentAirport.setCode(airportCode);
            aiportChoiceBox.getItems().add(currentAirport.toString());
            aiportChoiceBox.setValue(currentAirport.toString());
            Model.console.addLog("Airport edited: " + currentAirport.toString() + "\t=>\t" + aiportChoiceBox.getValue());
            edit = false;
        } else {
            currentAirport = new Airport(airportName,airportCode,new ArrayList<>());
            Model.airports.add(currentAirport);
            aiportChoiceBox.getItems().add(currentAirport.toString());
            aiportChoiceBox.setValue(currentAirport.toString());
            Model.console.addLog("Airport " + aiportChoiceBox.getValue() + " added ");
        }
        enableElements(aiportMainMenu);
        hideOptions(aiportOptions);
        airportNameTextField.setText("");
        airportCodeTextField.setText("");
    }

    private boolean nameInUse(String desiredName){
        String[] data = aiportChoiceBox.getItems().toArray(new String[0]);
        for (String str : data) {
            String[] splitString = str.split(" ");
            if (splitString[0].equals(desiredName)){
                return true;
            }
        }
        return false;
    }

    private boolean codeInUse(String desiredCode){
        String[] data = aiportChoiceBox.getItems().toArray(new String[0]);
        for (String str : data) {
            String[] splitString = str.split(" ");
            if (splitString[1].replaceAll("[\\()]","").equals(desiredCode)) {
                return true;
            }
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
