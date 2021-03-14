package main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Model;
import main.Obstacle;
import main.PhysicalRunWay;
import main.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObstacleConfigController {

    private List<TextField> textFields;
    private boolean edit;

//    @FXML private Label nameLabel;
    @FXML private VBox topVbox;
    @FXML private VBox obstacleDetails;
    @FXML private VBox distancesEditorVbox;
    @FXML private CheckBox placeObstacleCB;
//    @FXML private Separator separator;
    @FXML private Button newObstacleButton;
    @FXML private Button editObstacleButton;
    @FXML private TitledPane obstacleConfig;
//    @FXML private HBox editButtonsTop;
    @FXML private HBox editButtonsBottom;
    @FXML private TextField obstacleHeightTF;
    @FXML private TextField obstacleWidthTF;
    @FXML private TextField obstacleNameTF;
    @FXML private ChoiceBox<String> obstacleChoiceBox;
    @FXML private TextField distanceFromLTF;
    @FXML private TextField distanceFromRTF;
    @FXML private TextField distanceFromCLTF;
    @FXML private ChoiceBox<String> dirFromCLChoiceBox;

    public ObstacleConfigController(){
        edit = false;
        textFields = new ArrayList<>();
    }

    @FXML
    private void initialize(){
        Model.obstacleConfigController = this;
        setupTextFields();
        setupDirBox();
        obstacleConfig.expandedProperty().addListener((observable, oldValue, newValue) -> update(newValue));
        obstacleChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> choiceBoxUpdater(newValue));
    }

    private void setupTextFields(){
        obstacleNameTF.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) change.setText("_");
            return change;
        }));
        textFields.addAll(Arrays.asList(obstacleHeightTF,obstacleWidthTF,distanceFromLTF,distanceFromRTF,distanceFromCLTF));
        for(TextField t : textFields)
            t.setTextFormatter(new TextFormatter<>(change -> {
                if (!change.getText().matches("-?(([0-9])*)")) change.setText("");
                return change;
            }));
        textFields.add(obstacleNameTF);
    }

    private void setupDirBox(){
        dirFromCLChoiceBox.getItems().add("L");
        dirFromCLChoiceBox.getItems().add("R");
        dirFromCLChoiceBox.setValue("L");
    }

    private void update(Boolean expanded) {
        if(expanded){
            populateObstacleNames();
            if(Model.obstacles.isEmpty()){
                placeObstacleCB.setSelected(false);
                noObstaclesView();
            } else if (Model.currentObstacle == null){
                placeObstacleCB.setSelected(false);
                noSelectedObstacleView();
            } else {
                if(Model.obstaclePlaced){
                    obstacleChoiceBox.setValue(Model.currentObstacle.getName());
                    placeObstacleCB.setSelected(true);
                    placeObstacle();
                } else {
                    obstacleChoiceBox.setValue(Model.currentObstacle.getName());
                    placeObstacleCB.setSelected(false);
                    selectedObstacleView();
                }
            }
        } else {
            windowCloseProcedure();
        }
    }

    private void windowCloseProcedure() {
        edit = false;
//        Model.currentObstacle = Model.getObstacleByName(obstacleChoiceBox.getValue());
        System.out.println("Current obstacle: " + Model.currentObstacle);
    }

    private void specifyView(boolean obstacleChoiceBoxDisabled, boolean editObstacleButtonDisabled, boolean newButtonDisabled,
                             boolean topVboxDisable, boolean obstacleDetailsBoxVisible, boolean checkBoxVisible,
                             boolean editButtonsBottomVisible, boolean distancesEditorVboxVisible) {
        obstacleChoiceBox.setDisable(obstacleChoiceBoxDisabled);
        editObstacleButton.setDisable(editObstacleButtonDisabled);
        newObstacleButton.setDisable(newButtonDisabled);
        topVbox.setDisable(topVboxDisable);
        obstacleDetails.setVisible(obstacleDetailsBoxVisible);
        placeObstacleCB.setVisible(checkBoxVisible);
        editButtonsBottom.setVisible(editButtonsBottomVisible);
        distancesEditorVbox.setVisible(distancesEditorVboxVisible);
    }

    private void noObstaclesView(){
        specifyView(true,
                true,
                false,
                false,
                false,
                false,
                false,
                false);
    }

    private void noSelectedObstacleView(){
        specifyView(false,
                false,
                false,
                false,
                false,
                false,
                false,
                false);
    }

    private void selectedObstacleView() {
        specifyView(false,
                false,
                false,
                false,
                true,
                true,
                false,
                false);
    }

    private void placedObstacleView(){
        specifyView(true,
                true,
                false,
                true,
                true,
                true,
                false,
                true);
    }

    private void obstacleEditView() {
        specifyView(true,
                true,
                true,
                false,
                true,
                false,
                true,
                false);
    }

    private void populateObstacleNames(){
        obstacleChoiceBox.getItems().clear();
        for(Obstacle o : Model.getObstacles()){
            obstacleChoiceBox.getItems().add(o.getName());
        }
    }

    private void populateObstacleDetails(Obstacle obstacle) {
        if(obstacle == null) {
            obstacleNameTF.setText("");
            obstacleHeightTF.setText("");
            obstacleWidthTF.setText("");
        } else {
            obstacleNameTF.setText(obstacle.getName());
            obstacleHeightTF.setText(String.valueOf(obstacle.getHeight()));
            obstacleWidthTF.setText(String.valueOf(obstacle.getWidth()));
        }
    }

    private void populateObstacleDimensions(Position position) {
       if(position == null) {
           distanceFromLTF.setText("");
           distanceFromRTF.setText("");
           distanceFromCLTF.setText("");
           dirFromCLChoiceBox.setValue("L");
       } else {
           Position p = Model.currentObstacle.getPosition();
           distanceFromLTF.setText(String.valueOf(p.getDistanceToLeft()));
           distanceFromRTF.setText(String.valueOf(p.getDistanceToRight()));
           distanceFromCLTF.setText(String.valueOf(p.getDistanceFromCL()));
           dirFromCLChoiceBox.setValue(p.getDirectionFromCL());
       }
    }

    private void saveObstacleDimensions(Obstacle obstacle){
        try{
            int distancefromL = Integer.parseInt(distanceFromLTF.getText());
            int distanceFromR = Integer.parseInt(distanceFromRTF.getText());
            int distanceFromCL = Integer.parseInt(distanceFromCLTF.getText());
            String dirFromCL = dirFromCLChoiceBox.getValue();
            obstacle.setPosition(new Position(distancefromL,distanceFromR,distanceFromCL,dirFromCL));
            Model.console.addLog("Obstacle's: " + Model.currentObstacle.getName() + " position data was saved");
        } catch (NumberFormatException e){
            Model.console.addLog("Obstacle's: " + Model.currentObstacle.getName() + " position data was not saved");
        }

    }

    @FXML
    private void placeObstacleClick() {
        if (placeObstacleCB.isSelected()){
            if(Model.currentRunway == null){
                AlertController.showWarningAlert("No runway selected!");
                placeObstacleCB.setSelected(false);
                return;
            }
            placeObstacle();
        } else {
            Model.obstaclePlaced = false;
            saveObstacleDimensions(Model.currentObstacle);
            selectedObstacleView();
        }
    }

    private void placeObstacle(){
        Model.obstaclePlaced = true;
        placedObstacleView();
        populateObstacleDimensions(Model.currentObstacle.getPosition());
    }

    private void choiceBoxUpdater(String newValue){
        if(newValue != null) {
            for(Obstacle o : Model.getObstacles()) {
                if (o.getName().equals(newValue)) {
                    selectedObstacleView();
                    populateObstacleDetails(o);
                    Model.currentObstacle = o;
                }
            }
        }
    }

    @FXML
    private void newObstacleClick() {
        populateObstacleDetails(null);
        obstacleEditView();
    }

    @FXML
    private void editObstacleClick() {
        obstacleEditView();
    }

    @FXML
    private void cancelButtonClick() {
        populateObstacleDetails(Model.currentObstacle);
        selectedObstacleView();
    }

    @FXML
    private void saveButtonClick() {
        String name = obstacleNameTF.getText();
        if(name.equals("") || obstacleHeightTF.getText().equals("") || (obstacleWidthTF.getText().equals(""))) {
            AlertController.showWarningAlert("Empty texfields!");
            return;
        } else if (nameInUse(name, edit)){
            AlertController.showWarningAlert("Obstacle name already used");
            return;
        } else if (edit){
            saveEditedObstacle(name);
            edit = false;
        } else {
            saveNewObstacle(name);
        }
        selectedObstacleView();
    }

    private void saveEditedObstacle(String name) {
        if(Model.currentObstacle != null) {
            int width = Integer.parseInt(obstacleWidthTF.getText());
            int height = Integer.parseInt(obstacleHeightTF.getText());
            Model.currentObstacle.setName(name);
            Model.currentObstacle.setWidth(width);
            Model.currentObstacle.setHeight(height);
            obstacleChoiceBox.getItems().remove(Model.currentObstacle.getName());
            obstacleChoiceBox.getItems().add(name);
            obstacleChoiceBox.setValue(name);
            edit = false;
        }
        else System.err.println("Error ObstacleController:312");
    }

    private void saveNewObstacle(String name) {
        int width = Integer.parseInt(obstacleWidthTF.getText());
        int height = Integer.parseInt(obstacleHeightTF.getText());
        Obstacle obstacle = new Obstacle(name, height, width, null);
        Model.currentObstacle = obstacle;
        Model.obstacles.add(obstacle);
        obstacleChoiceBox.getItems().add(name);
        obstacleChoiceBox.setValue(name);
    }

    private boolean nameInUse(String desiredName, boolean edit){
        String[] data = obstacleChoiceBox.getItems().toArray(new String[0]);
        for (String str : data) {
            if(!edit && str.equals(desiredName)) return true;
            if (str.equals(desiredName) && !str.equals(Model.currentObstacle.getName())) return true;
        }
        return false;
    }

}
