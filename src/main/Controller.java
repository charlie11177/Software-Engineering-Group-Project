package main;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Controller {

    private Boolean editAirport;
    private String valueEdited;

    @FXML
    private Button calculateButton;
    @FXML
    private Button newAirportButton;
    @FXML
    private Button editAirportButton;
    @FXML
    private Button saveAirport;
    @FXML
    private Button cancelAirport;
    @FXML
    private TextField airportNameTextField;
    @FXML
    private TextField airportCodeTextField;
    @FXML
    private VBox aiportOptions;
    @FXML
    private ChoiceBox<String> aiportChoiceBox;
    @FXML
    private VBox aiportMainMenu;

    public Controller(){
        editAirport = false;
    }

    @FXML
    public void initialize(){
        aiportOptions.setVisible(false);
        populateAirportNames();
    }

    private void populateAirportNames(){
        aiportChoiceBox.getItems().add("London_Heathrow (LHR)");
        aiportChoiceBox.getItems().add("London_Gatwick (LGW)");
        aiportChoiceBox.setValue("London_Heathrow (LHR)");
    }

    @FXML
    private void calculateButtonClick() {
        System.out.println("Pressed");
    }

    @FXML
    private void newAirportClick(){
        editAirport = false;
        disableElements(aiportMainMenu);
        showOptions(aiportOptions);
    }

    @FXML
    private void editAirportClick(){
        editAirport = true;
        disableElements(aiportMainMenu);
        showOptions(aiportOptions);
        valueEdited = aiportChoiceBox.getValue();
        String[] data = aiportChoiceBox.getValue().split(" ");
        System.out.println(aiportChoiceBox.getValue());
        airportNameTextField.setText(data[0]);
        airportCodeTextField.setText(data[1].replaceAll("[\\()]",""));
    }

    /**
     * Method that saves the specified values of the textfields and saves them to the database
     */
    @FXML
    private void saveAirportClick(){
        String airportName = airportNameTextField.getText();
        String aiportCode = airportCodeTextField.getText();
        String choiceBoxItem = airportName + " " + "(" + aiportCode + ")";
        if(airportName.equals("") || aiportCode.equals("")) {
            //TODO: put code for error popup for empty textfields here.
            System.out.println("Empty textfields");
        } else if (editAirport == true){
            aiportChoiceBox.getItems().remove(valueEdited);
            editAirport = false;
            enableElements(aiportMainMenu);
            hideOptions(aiportOptions);
            aiportChoiceBox.getItems().add(choiceBoxItem);
            aiportChoiceBox.setValue(choiceBoxItem);
            //TODO: put code for saving a new airport name+code use the airportData variable
        } else if (containsNameOrCode(airportName, aiportCode) == ErrorEnum.NAME){
            //TODO: put code for error popup for same airport names here.
            System.out.println("Airport name already used");
        } else if (containsNameOrCode(airportName, aiportCode) == ErrorEnum.CODE){
            //TODO: put code for error popup for same airport names here.
            System.out.println("Airport code already used");
        } else {
            enableElements(aiportMainMenu);
            hideOptions(aiportOptions);
            aiportChoiceBox.getItems().add(choiceBoxItem);
            aiportChoiceBox.setValue(choiceBoxItem);
            //TODO: check for duplicate names here
            //TODO: put code for saving an edited airport name+code use the airportData variab
        }
        airportNameTextField.setText("");
        airportCodeTextField.setText("");
    }

    enum ErrorEnum {
        NONE,
        NAME,
        CODE
    }

    private ErrorEnum containsNameOrCode(String desiredName, String desiredCode){
        String[] data = aiportChoiceBox.getItems().toArray(new String[0]);
        for (String str : data) {
            String[] splitString = str.split(" ");
            if (splitString[0].equals(desiredName)){
                return ErrorEnum.NAME;
            }
            else if (splitString[1].replaceAll("[\\()]","").equals(desiredCode)) {
                return ErrorEnum.CODE;
            }
        }
        return ErrorEnum.NONE;
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
