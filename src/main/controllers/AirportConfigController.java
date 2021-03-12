package main.controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.Airport;
import main.Model;

import java.util.ArrayList;


public class AirportConfigController implements Observer {

    private Boolean edit;
    public Airport currentAirport;

    @FXML private ChoiceBox<String> airportChoiceBox;
    @FXML private TextField airportNameTextField;
    @FXML private TextField airportCodeTextField;
    @FXML private VBox aiportOptions;
    @FXML private VBox aiportMainMenu;
    @FXML private Button editAirportButton;
    private ChangeListener<String> choiceBoxListener;

    public AirportConfigController(){}

    @FXML
    private void initialize(){
        Model.airportConfigController = this;
        Model.attachAirportObserver(this);
        choiceBoxListener = (observable, oldValue, newValue) -> choiceBoxUpdate(newValue);
        addChoiceBoxListener(choiceBoxListener);
        setupTextFields();
        populateAirportNames();
        selectionView();
        edit = false;
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

    private void addChoiceBoxListener(ChangeListener<String> listener) {
        airportChoiceBox.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    private void blockChoiceBoxListener(ChangeListener<String> listener) {
        airportChoiceBox.getSelectionModel().selectedItemProperty().removeListener(listener);
    }

    private void selectionView() {
        aiportOptions.setVisible(false);
        aiportMainMenu.setDisable(false);
    }

    private void inputView() {
        airportNameTextField.setText("");
        airportCodeTextField.setText("");
        aiportOptions.setVisible(true);
        aiportMainMenu.setDisable(true);
    }

    public void emptyAirportsView(){
        if(Model.airports.isEmpty()) {
            editAirportButton.setDisable(true);
            airportChoiceBox.setDisable(true);
        } else {
            editAirportButton.setDisable(false);
            airportChoiceBox.setDisable(false);
        }
    }

    private void noAirportChosenView(){
        blockChoiceBoxListener(choiceBoxListener);
        airportChoiceBox.getSelectionModel().clearSelection();
        addChoiceBoxListener(choiceBoxListener);
    }


//    public void update() {
//        if(!Model.currentAirport.toString().equals(airportChoiceBox.getValue())) {
//            populateAirportNames();
//            blockChoiceBoxListener(choiceBoxListener);
//            airportChoiceBox.setValue(Model.currentAirport.toString());
//            addChoiceBoxListener(choiceBoxListener);
//        } else {
//            ArrayList<String> values = new ArrayList<>();
//            for(Airport a : Model.airports)
//                values.add(a.toString());
//            if(!airportChoiceBox.getItems().containsAll(values)){
//                populateAirportNames();
//                blockChoiceBoxListener(choiceBoxListener);
//                airportChoiceBox.setValue(Model.currentAirport.toString());
//                addChoiceBoxListener(choiceBoxListener);
//            }
//        }
//    }
    @Override
    public void update() {
        if (Model.airports.isEmpty()){
            emptyAirportsView();
        } else if (Model.currentAirport == null){
            populateAirportNames();
            noAirportChosenView();
        } else {
            populateAirportNames();
            blockChoiceBoxListener(choiceBoxListener);
            airportChoiceBox.setValue(Model.currentAirport.toString());
            addChoiceBoxListener(choiceBoxListener);
        }
    }

    public void choiceBoxUpdate(String newValue) {
        Airport airport = Model.getAirportByName(newValue.split(" ")[0]);
        Model.setCurrentAirport(airport);
    }

    public void populateAirportNames(){
        blockChoiceBoxListener(choiceBoxListener);
        airportChoiceBox.getItems().clear();
        if(!Model.airports.isEmpty()) {
            airportChoiceBox.getSelectionModel().clearSelection();
            for (Airport a : Model.airports) {
                airportChoiceBox.getItems().add(a.toString());
            }
        } else emptyAirportsView();
        addChoiceBoxListener(choiceBoxListener);
    }

    @FXML
    private void newAirportClick(){
        inputView();
    }

    @FXML
    private void editAirportClick(){
        edit = true;
        inputView();
        airportNameTextField.setText(Model.currentAirport.getName());
        airportCodeTextField.setText(Model.currentAirport.getCode());
    }

    /**
     * Method that saves the specified values of the textfields and saves them to the database
     */
    @FXML
    private void saveAirportClick(){
        String airportName = airportNameTextField.getText();
        String airportCode = airportCodeTextField.getText();
        if(airportName.equals("") || airportCode.equals("")) {
            AlertController.showWarningAlert("Name and Code fields cannot be empty!");
            return;
        } else if (nameInUse(airportName)){
            AlertController.showWarningAlert("An Airport with that name already exists!");
            return;
        } else if (codeInUse(airportCode)) {
            AlertController.showWarningAlert("An Airport with that code already exists!");
            return;
        } else if (Model.currentAirport != null && (airportName + " (" + airportCode + ")").equals(Model.currentAirport.toString())) {
            AlertController.showInfoAlert("No changes were made.");
            selectionView();
            return;
        } else if (edit){
            String previousAirportString = Model.currentAirport.toString();
            Model.currentAirport.setName(airportName);
            Model.currentAirport.setCode(airportCode);
            Model.console.addLog("Airport " + previousAirportString + " edited to: " + Model.currentAirport.toString());
            edit = false;
            update();
        } else { //Creating a new airport
            Airport a = new Airport(airportName,airportCode,new ArrayList<>());
            Model.setCurrentAirport(a);
            Model.addAirports(a);
            Model.console.addLog("Airport " + Model.currentAirport + " added");
            editAirportButton.setDisable(false);
            airportChoiceBox.setDisable(false);
        }
        selectionView();
    }

    private boolean nameInUse(String desiredName){
        for(Airport a : Model.getAirports()){
            if(a.getName().equals(desiredName) && !a.equals(Model.currentAirport))
                return true;
        }
        return false;
    }

    private boolean codeInUse(String desiredCode){
        for(Airport a : Model.getAirports()){
            if(a.getCode().equals(desiredCode) && !a.equals(Model.currentAirport))
                return true;
        }
        return false;
    }

    @FXML
    private void cancelAirportClick(){
        selectionView();
    }

}
