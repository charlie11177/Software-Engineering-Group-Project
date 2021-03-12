package main.controllers;

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
    public TextField distanceFromLTF;
    public TextField distanceFromRTF;
    public TextField distanceFromCLTF;
    public ChoiceBox<String> dirFromCLChoiceBox;

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
               if (!change.getText().matches("-?(([0-9])*)")) change.setText("");
                return change;
            }));
    }

    public boolean isPlaceObstacleSelected(){
        return placeObstacleCB.isSelected();
    }

    public void placeObstacleClick() {
        if(placeObstacleCB.isSelected()){
            if(Model.runwayConfigController.currentRunway == null){
                Alert emptyFields = new Alert(Alert.AlertType.WARNING, "No runway selected!");
                emptyFields.setHeaderText(null);
                emptyFields.showAndWait();
                placeObstacleCB.setSelected(false);
                return;
            }
            obstacleDistancesBox.setVisible(true);
            obstacleDetailsBox.setDisable(true);
            populateObstaclePlacement();
        } else {
            try {
                int distancefromL = Integer.parseInt(distanceFromLTF.getText());
                int distanceFromR = Integer.parseInt(distanceFromRTF.getText());
                int distanceFromCL = Integer.parseInt(distanceFromCLTF.getText());
                String dirFromCL = dirFromCLChoiceBox.getValue();
                currentObstacle.setPosition(new Position(distancefromL,distanceFromR,distanceFromCL,dirFromCL));
            } catch (NumberFormatException e){
            }
            obstacleDetailsBox.setDisable(false);
            obstacleDistancesBox.setVisible(false);
            distanceFromLTF.setEditable(true);
            distanceFromRTF.setEditable(true);
            distanceFromCLTF.setEditable(true);
            dirFromCLChoiceBox.setDisable(false);
            dirFromCLChoiceBox.setOpacity(1);
            distancesEditorVbox.setOpacity(1);
        }
    }

    public void populateObstaclePlacement(){
        if(currentObstacle.getPosition() != null){
            distanceFromLTF.setText(String.valueOf(currentObstacle.getPosition().getDistanceToLeft()));
            distanceFromRTF.setText(String.valueOf(currentObstacle.getPosition().getDistanceToRight()));
            distanceFromCLTF.setText(String.valueOf(currentObstacle.getPosition().getDistanceFromCL()));
            dirFromCLChoiceBox.setValue(currentObstacle.getPosition().getDirectionFromCL());
        } else {
            distanceFromLTF.setText("");
            distanceFromRTF.setText("");
            distanceFromCLTF.setText("");
            dirFromCLChoiceBox.setValue("");
        }
    }

    public void removeObstacle(){
        currentObstacle.setPosition(null);
        Model.getObstacleByName(currentObstacle.getName()).setPosition(null);
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

    public void dynamicButtonLeftClick() {
        if(dynamicButtonLeft.getText().equals("New")){
            Model.leftScreenController.calculateButton.setDisable(true);
            obstacleDetails.setVisible(true);
            placeObstacleCB.setDisable(true);
            setEditable(true,obstacleNameTF,obstacleHeightTF,obstacleWidthTF);
            dynamicButtonEditMode();
            obstacleNameTF.setText("");
            obstacleHeightTF.setText("");
            obstacleWidthTF.setText("");
            dynamicButtonRight.setDisable(false);
        } else {
            this.saveObstacle();
        }
    }

    @FXML
    private void dynamicButtonRightClick() {
        if(dynamicButtonRight.getText().equals("Edit")){
        Model.leftScreenController.calculateButton.setDisable(true);
            placeObstacleCB.setDisable(true);
            System.out.println("Name:" + currentObstacle.getName());
            edit = true;
            setEditable(true,obstacleNameTF,obstacleHeightTF,obstacleWidthTF);
            dynamicButtonEditMode();
        } else { //cancel
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
            Model.leftScreenController.calculateButton.setDisable(false);
        }
    }

    private void dynamicButtonEditMode(){
        obstacleHeightTF.setEditable(true);
        obstacleWidthTF.setEditable(true);
        obstacleNameTF.setEditable(true);
        obstacleDistancesBox.setVisible(false);
        obstacleChoiceBox.setDisable(true);
        dynamicButtonLeft.setText("Save");
        dynamicButtonLeft.setDefaultButton(true);
        dynamicButtonRight.setText("Cancel");
    }

    private void dynamicButtonViewMode(){
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
            currentObstacle = obstacle;
            Model.obstacles.add(obstacle);
            obstacleChoiceBox.getItems().add(name);
        }
        placeObstacleCB.setDisable(false);
        obstacleChoiceBox.setValue(name);
        dynamicButtonViewMode();
        Model.leftScreenController.calculateButton.setDisable(false);
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
