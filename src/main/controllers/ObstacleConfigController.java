package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Model;
import main.Obstacle;
import main.Position;

import java.util.Arrays;
import java.util.List;

public class ObstacleConfigController {

    public Obstacle currentObstacle;
    //public CheckBox obstacleCheckBox;
    public Label nameLabel;
    public VBox obstacleDetails;
    public CheckBox placeObstacleCB;
    public Separator separator;
    public VBox distancesEditorVbox;
    public VBox obstacleDetailsBox;
    private List<TextField> textFields;
    private Boolean edit;


    @FXML private HBox editButtonsName;
    @FXML private Button dynamicButtonLeft;
    @FXML private Button dynamicButtonRight;
    @FXML private VBox obstacleDistancesBox;
    @FXML private TextField obstacleHeightTF;
    @FXML private TextField obstacleWidthTF;
    @FXML private TextField obstacleNameTF;
    @FXML private ChoiceBox<String> obstacleChoiceBox;
    @FXML private TextField distanceFromLTF;
    @FXML private TextField distanceFromRTF;
    @FXML private TextField distanceFromCLTF;
    @FXML private ChoiceBox<String> dirFromCLChoiceBox;
    @FXML private Button saveButton;

    public ObstacleConfigController(){
        edit = false;
    }

    @FXML
    private void initialize(){
        Model.obstacleConfigController = this;
        setupTextFields();
        obstacleDistancesBox.setVisible(false);
        setEditable(false,obstacleNameTF,obstacleHeightTF,obstacleWidthTF);
        obstacleNameTF.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) change.setText("_");
            return change;
        }));
        setupDirBox();
        populateObstacleNames();
        addObstacleChoiceBoxListener();
        emptyObstaclesUpdate();
    }

    private void setupTextFields(){
        textFields = Arrays.asList(obstacleHeightTF,obstacleWidthTF,distanceFromLTF,distanceFromRTF,distanceFromCLTF);
        for(TextField t : textFields)
            t.setTextFormatter(new TextFormatter<>(change -> {
                // "^(\\d*\\.?\\d*)$"
               if (!change.getText().matches("[0-9]*")) change.setText("");
                return change;
            }));
    }

    public boolean isPlaceObstacleSelected(){
        return placeObstacleCB.isSelected();
    }

//    @FXML
//    private void noObstacleClick() {
//        if (obstacleCheckBox.isSelected()){
//            nameLabel.setVisible(false);
//            obstacleDetailsBox.setVisible(false);
//            placeObstacleCB.setVisible(false);
//            separator.setVisible(false);
//            obstacleDistancesBox.setVisible(false);
//        } else {
//            nameLabel.setVisible(true);
//            obstacleDetailsBox.setVisible(true);
//            placeObstacleCB.setVisible(true);
//            separator.setVisible(true);
//            if(placeObstacleCB.isSelected())
//                obstacleDistancesBox.setVisible(true);
//        }
//        emptyObstaclesUpdate();
//    }

    public void placeObstacleClick() {
        if(placeObstacleCB.isSelected()){
            obstacleDistancesBox.setVisible(true);
            obstacleDetailsBox.setDisable(true);
        } else {
            obstacleDetailsBox.setDisable(false);
            obstacleDistancesBox.setVisible(false);
            distanceFromLTF.setEditable(true);
            distanceFromRTF.setEditable(true);
            distanceFromCLTF.setEditable(true);
            dirFromCLChoiceBox.setDisable(false);
//            distanceFromLTF.setText("");
//            distanceFromRTF.setText("");
//            distanceFromCLTF.setText("");
//            dirFromCLChoiceBox.setValue("L");
            dirFromCLChoiceBox.setOpacity(1);
            distancesEditorVbox.setOpacity(1);
            saveButton.setText("Save");
        }
    }

    private void setupDirBox(){
        dirFromCLChoiceBox.getItems().add("L");
        dirFromCLChoiceBox.getItems().add("R");
        dirFromCLChoiceBox.setValue("L");
    }

    private void addObstacleChoiceBoxListener(){
        obstacleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue != null){
                currentObstacle = Model.getObstacleByName(newValue);
                populateObstacleDetails(currentObstacle);
            } else if (edit) {
                populateObstacleDetails(currentObstacle);
            } else {
                obstacleChoiceBox.setDisable(true);
                System.out.println(currentObstacle.getName());
                System.err.println("Warning newvalue null");
            }
            emptyObstaclesUpdate();
        });
    }

    private boolean emptyObstaclesUpdate(){
//        if(Model.obstacles.isEmpty() || obstacleCheckBox.isSelected()) {
        if (Model.obstacles.isEmpty()) {
            dynamicButtonRight.setDisable(true);
            obstacleChoiceBox.setDisable(true);
            obstacleDetails.setVisible(false);
            placeObstacleCB.setVisible(false);
            return true;
        } else {
            dynamicButtonRight.setDisable(false);
            obstacleChoiceBox.setDisable(false);
            obstacleDetails.setVisible(true);
            placeObstacleCB.setVisible(true);
            return false;
        }
    }

    private void populateObstacleNames(){
        obstacleChoiceBox.getItems().clear();
        if(!Model.obstacles.isEmpty()) {
            currentObstacle = Model.obstacles.get(0);
            populateObstacleDetails(currentObstacle);
            obstacleChoiceBox.setValue(Model.obstacles.get(0).getName());
            for (Obstacle o : Model.obstacles) {
                obstacleChoiceBox.getItems().add(o.getName());
            }
        } else emptyObstaclesUpdate();
    }

    private void populateObstacleDetails(Obstacle obstacle){
        obstacleNameTF.setText(obstacle.getName());
        obstacleWidthTF.setText(String.valueOf(obstacle.getWidth()));
        obstacleHeightTF.setText(String.valueOf(obstacle.getHeight()));
    }

    @FXML
    private void saveButtonClick() {
        if(saveButton.getText().equals("Save")){
            saveButton.setText("Edit");
            obstacleDetailsBox.setDisable(false);
            placeObstacleCB.setDisable(false);
            if(distanceFromLTF.getText().equals("") || distanceFromRTF.getText().equals("") ||
                    (distanceFromCLTF.getText().equals(""))) {
                //TODO: TEAM2 code for error popup for empty textfields here.
                System.out.println("Empty textfields");
                return;
            } else {
                int distancefromL = Integer.parseInt(distanceFromLTF.getText());
                int distanceFromR = Integer.parseInt(distanceFromRTF.getText());
                int distanceFromCL = Integer.parseInt(distanceFromCLTF.getText());
                String dirFromCL = dirFromCLChoiceBox.getValue();
                Position position = new Position(distancefromL,distanceFromR,distanceFromCL,dirFromCL);
                currentObstacle.setPosition(position);
            }
            distanceFromLTF.setEditable(false);
            distanceFromRTF.setEditable(false);
            distanceFromCLTF.setEditable(false);
            dirFromCLChoiceBox.setDisable(true);
            dirFromCLChoiceBox.setOpacity(0.75);
            distancesEditorVbox.setOpacity(0.75);
        } else {
            saveButton.setText("Save");
            placeObstacleCB.setDisable(true);
            obstacleDetailsBox.setDisable(true);
            distanceFromLTF.setEditable(true);
            distanceFromRTF.setEditable(true);
            distanceFromCLTF.setEditable(true);
            dirFromCLChoiceBox.setDisable(false);
            dirFromCLChoiceBox.setOpacity(1);
            distancesEditorVbox.setOpacity(1);
        }
    }

    // new button originally
    public void dynamicButtonLeftClick() {
        if(dynamicButtonLeft.getText().equals("New")){
            obstacleDetails.setVisible(true);
            placeObstacleCB.setDisable(true);
            setEditable(true,obstacleNameTF,obstacleHeightTF,obstacleWidthTF);
            dynamicButtonEditMode();
            obstacleNameTF.setText("");
            obstacleHeightTF.setText("");
            obstacleWidthTF.setText("");
            dynamicButtonRight.setDisable(false);
        } else this.saveObstacle();
    }

    @FXML
    private void dynamicButtonRightClick() {
        if(dynamicButtonRight.getText().equals("Edit")){
            //placeObstacleCB.setVisible(true);
            placeObstacleCB.setDisable(true);
            System.out.println("Name:" + currentObstacle.getName());
            edit = true;
            setEditable(true,obstacleNameTF,obstacleHeightTF,obstacleWidthTF);
            dynamicButtonEditMode();
        } else {
            //cancel
            if(edit) edit = false;
            if(!emptyObstaclesUpdate()){
                populateObstacleDetails(currentObstacle);
                placeObstacleCB.setDisable(false);
            }
            else {
                obstacleNameTF.setText("");
                obstacleHeightTF.setText("");
                obstacleWidthTF.setText("");
            }
            dynamicButtonViewMode();
        }
    }

    private void dynamicButtonEditMode(){
        obstacleHeightTF.setEditable(true);
        obstacleWidthTF.setEditable(true);
        obstacleNameTF.setEditable(true);
        obstacleDistancesBox.setVisible(false);
        //obstacleCheckBox.setDisable(true);
        obstacleChoiceBox.setDisable(true);
        dynamicButtonLeft.setText("Save");
        dynamicButtonLeft.setDefaultButton(true);
        dynamicButtonRight.setText("Cancel");
    }

    private void dynamicButtonViewMode(){
        //obstacleCheckBox.setDisable(false);
        if(placeObstacleCB.isSelected())
            obstacleDistancesBox.setVisible(true);
        dynamicButtonLeft.setText("New");
        dynamicButtonLeft.setDefaultButton(false);
        dynamicButtonRight.setText("Edit");
        obstacleHeightTF.setEditable(false);
        obstacleWidthTF.setEditable(false);
        obstacleNameTF.setEditable(false);

    }

    private void saveObstacle(){
        String name = obstacleNameTF.getText();
        if(name.equals("") || obstacleHeightTF.getText().equals("") || (obstacleWidthTF.getText().equals(""))) {
            //TODO: TEAM2 code for error popup for empty textfields here.
            System.out.println("Empty textfields");
            return;
        } else if (nameInUse(name, edit)){
            //TODO: TEAM2 code for error popup for same airport names here.
            System.out.println("Obstacle name already used");
            return;
        } else if (edit){
            int width = Integer.parseInt(obstacleWidthTF.getText());
            int height = Integer.parseInt(obstacleHeightTF.getText());
            obstacleChoiceBox.getItems().remove(currentObstacle.getName());
            currentObstacle.setName(name);
            currentObstacle.setWidth(width);
            currentObstacle.setHeight(height);
            obstacleChoiceBox.getItems().add(name);
            obstacleChoiceBox.setValue(name);
            obstacleChoiceBox.setDisable(false);
            edit = false;
        } else {
            int width = Integer.parseInt(obstacleWidthTF.getText());
            int height = Integer.parseInt(obstacleHeightTF.getText());
            Obstacle obstacle = new Obstacle(name, height, width, null);
            //currentObstacle = obstacle;
            Model.obstacles.add(obstacle);
            //currentObstacle = obstacle;
            obstacleChoiceBox.getItems().add(name);
        }
        placeObstacleCB.setDisable(false);
        obstacleChoiceBox.setValue(name);
        dynamicButtonViewMode();
    }

    public void setEditable(boolean value, TextField... textFields){
        for(TextField t : textFields)
            t.setEditable(value);
    }

    private boolean nameInUse(String desiredName, boolean edit){
        String[] data = obstacleChoiceBox.getItems().toArray(new String[0]);
        for (String str : data) {
            System.out.println(currentObstacle.getName() + " " + str);
            if(!edit && str.equals(desiredName)) return true;
            if (str.equals(desiredName) && !str.equals(currentObstacle.getName())) return true;
        }
        return false;
    }
}
