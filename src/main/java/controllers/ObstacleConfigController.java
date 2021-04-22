package controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Model;
import model.Obstacle;
import model.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObstacleConfigController {

    private boolean edit;
    private ChangeListener<String> choiceBoxListener;
    private List<TextField> textFields;

    public VBox obstacleRoot;
    @FXML private Label leftDistanceLabel;
    @FXML private Label rightDistanceLabel;
    @FXML private VBox topVbox;
    @FXML private VBox obstacleDetails;
    @FXML private VBox distancesEditorVbox;
    @FXML private CheckBox placeObstacleCB;
    @FXML private Button newObstacleButton;
    @FXML private Button deleteObstacleButton;
    @FXML private Button editObstacleButton;
    @FXML private TitledPane obstacleConfig;
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
        choiceBoxListener = (observable, oldValue, newValue) -> choiceBoxUpdater(newValue, oldValue);
        obstacleConfig.expandedProperty().addListener((observable, oldValue, newValue) -> update(newValue));
//        obstacleChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> choiceBoxUpdater(newValue));
        setChoiceBoxListenerEnabled(true);
    }

    public void updateVisualisation() {
        System.out.println("Visualisation for obstacle " + Model.currentObstacle + " that is placed " + Model.obstaclePlaced);
        //TODO: calls for visualisation methods for displaying runways can be placed here
        // something like this: Model.CenterScreenController.draw();
    }

    private void setChoiceBoxListenerEnabled(Boolean enable) {
        if (enable)
            obstacleChoiceBox.valueProperty().addListener(choiceBoxListener);
        else
            obstacleChoiceBox.valueProperty().removeListener(choiceBoxListener);
    }

    private void setupTextFields(){
        obstacleNameTF.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) change.setText("_");
            return change;
        }));
        textFields.addAll(Arrays.asList(distanceFromLTF,distanceFromRTF,distanceFromCLTF));
        for(TextField t : textFields)
            t.textProperty().addListener((observable, oldValue, newValue) -> {
                // "|[-\\+]?|[-\\+]?\\d+\\.?|[-\\+]?\\d+\\.?\\d+"
                if (!newValue.matches("|[-]?|[-]?\\d+")){
                    t.setText(oldValue);
                }
            });
        List<TextField> temp = Arrays.asList(obstacleHeightTF,obstacleWidthTF);
        for(TextField t : temp) {
            t.setTextFormatter(new TextFormatter<>(change -> {
                if (!change.getText().matches("(([0-9])*)")) change.setText("");
                return change;
            }));
        }
        textFields.addAll(temp);
        textFields.add(obstacleNameTF);
    }

    private void setupDirBox(){
        dirFromCLChoiceBox.getItems().add("L");
        dirFromCLChoiceBox.getItems().add("R");
        dirFromCLChoiceBox.setValue("L");
    }

    private void update(Boolean expanded) {
        if(expanded){
            obstacleConfig.setText("Obstacle");
            populateObstacleNames();
            if(Model.obstacles.isEmpty()){
                placeObstacleCB.setSelected(false);
                noObstaclesView();
            } else if (Model.currentObstacle == null){
                placeObstacleCB.setSelected(false);
                noSelectedObstacleView();
            } else {
                populateObstacleDetails(Model.currentObstacle);
                populateObstacleDimensions(Model.currentObstacle.getPosition());
                setChoiceBoxListenerEnabled(false);
                if(Model.obstaclePlaced){
                    obstacleChoiceBox.setValue(Model.currentObstacle.getName());
                    placeObstacleCB.setSelected(true);
                    placeObstacle();
                } else {
                    obstacleChoiceBox.setValue(Model.currentObstacle.getName());
                    placeObstacleCB.setSelected(false);
                    selectedObstacleView();
                }
                setChoiceBoxListenerEnabled(true);
            }
        } else {
            windowCloseProcedure();
        }
    }

    public void windowCloseProcedure() {
        edit = false;
//        Model.currentObstacle = Model.getObstacleByName(obstacleChoiceBox.getValue());
        Model.leftScreenController.calculateButton.setDisable(false);
        if(Model.currentObstacle != null){
            if(Model.obstaclePlaced)
                obstacleConfig.setText(Model.currentObstacle.getName() + ": placed");
            else
                obstacleConfig.setText(Model.currentObstacle.getName() + ": not placed");
        } else obstacleConfig.setText("Obstacle");
    }

    private void specifyView(boolean obstacleChoiceBoxDisabled, boolean editObstacleButtonDisabled, boolean newButtonDisabled,
                             boolean removeButtonDisabled, boolean topVboxDisable, boolean obstacleDetailsBoxVisible,
                             double obstacleDetailsBoxOpacity, boolean checkBoxVisible, boolean editButtonsBottomVisible,
                             boolean distancesEditorVboxVisible, boolean distanceEditorVboxDisabled) {
        obstacleChoiceBox.setDisable(obstacleChoiceBoxDisabled);
        editObstacleButton.setDisable(editObstacleButtonDisabled);
        newObstacleButton.setDisable(newButtonDisabled);
        deleteObstacleButton.setDisable(removeButtonDisabled);
        topVbox.setDisable(topVboxDisable);
        obstacleDetails.setVisible(obstacleDetailsBoxVisible);
        obstacleDetails.setOpacity(obstacleDetailsBoxOpacity);
        placeObstacleCB.setVisible(checkBoxVisible);
        editButtonsBottom.setVisible(editButtonsBottomVisible);
        distancesEditorVbox.setVisible(distancesEditorVboxVisible);
        distancesEditorVbox.setDisable(distanceEditorVboxDisabled);
    }

    private void noObstaclesView(){
        specifyView(true,
                true,
                false,
                true,
                false,
                false,
                0.5,
                false,
                false,
                false,
                true);
    }

    private void noSelectedObstacleView(){
        specifyView(false,
                true,
                false,
                true,
                false,
                false,
                0.5,
                false,
                false,
                false,
                true);
    }

    private void selectedObstacleView() {
        boolean showPlacementMenu;
        boolean checkBoxVisible;
        if(Model.currentRunway == null) {
            showPlacementMenu = false;
            leftDistanceLabel.setText("Distance from L");
            rightDistanceLabel.setText("Distance from R");
            placeObstacleCB.setDisable(true);
//            checkBoxVisible = false;
        }
        else {
            showPlacementMenu = true;
            leftDistanceLabel.setText("Distance from " + Model.currentRunway.getLeftRunway().toString());
            rightDistanceLabel.setText("Distance from " + Model.currentRunway.getRightRunway().toString());
            placeObstacleCB.setDisable(false);
//            checkBoxVisible = true;
        }
        obstacleNameTF.setEditable(false);
        obstacleWidthTF.setEditable(false);
        obstacleHeightTF.setEditable(false);
        specifyView(false,
                false,
                false,
                false,
                false,
                true,
                0.5,
                true,
                false,
                showPlacementMenu,
                false);
    }

    private void placedObstacleView(){
        specifyView(true,
                true,
                true,
                true,
                true,
                true,
                0.5,
                true,
                false,
                true,
                true);

    }

    private void obstacleEditView() {
        Model.leftScreenController.calculateButton.setDisable(true);
        obstacleNameTF.setEditable(true);
        obstacleWidthTF.setEditable(true);
        obstacleHeightTF.setEditable(true);
        specifyView(true,
                true,
                true,
                true,
                false,
                true,
                1,
                false,
                true,
                false,
                true);
    }

    private void populateObstacleNames(){
        setChoiceBoxListenerEnabled(false);
        obstacleChoiceBox.getItems().clear();
        for(Obstacle o : Model.getObstacles()){
            obstacleChoiceBox.getItems().add(o.getName());
        }
        setChoiceBoxListenerEnabled(true);
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
            distanceFromLTF.setText(String.valueOf(position.getDistanceToLeft()));
            distanceFromRTF.setText(String.valueOf(position.getDistanceToRight()));
            distanceFromCLTF.setText(String.valueOf(position.getDistanceFromCL()));
            dirFromCLChoiceBox.setValue(position.getDirectionFromCL());
        }
    }

    public void saveObstacleDimensions(Obstacle obstacle)  {
        Integer distancefromL = null;
        Integer distanceFromR = null;
        Integer distanceFromCL = null;

        if(distanceFromLTF.getText() != "")
            try { distancefromL = Integer.parseInt(distanceFromLTF.getText()); } catch (NumberFormatException e) {}
        else if (obstacle.getPosition() != null)
            distancefromL = obstacle.getPosition().getDistanceToLeft();

        if(distanceFromRTF.getText() != "")
            try { distanceFromR = Integer.parseInt(distanceFromRTF.getText()); } catch (NumberFormatException e) {}
        else if (obstacle.getPosition() != null)
            distanceFromR = obstacle.getPosition().getDistanceToRight();

        if(distanceFromCLTF.getText() != "")
            try { distanceFromCL = Integer.parseInt(distanceFromCLTF.getText()); } catch (NumberFormatException e) {}
        else if (obstacle.getPosition() != null)
            distanceFromCL = obstacle.getPosition().getDistanceFromCL();

        String dirFromCL = dirFromCLChoiceBox.getValue();

        obstacle.setPosition(new Position(distancefromL,distanceFromR,distanceFromCL,dirFromCL));
    }

    private boolean isObstacleCorrectlyPlaced(Obstacle obstacle){
        if(distanceFromLTF.getText().equals("") || distanceFromRTF.getText().equals("") || distanceFromCLTF.getText().equals(""))
            return false;
        else if(distanceFromLTF.getText().equals("-") || distanceFromRTF.getText().equals("-") || distanceFromCLTF.getText().equals("-"))
            return false;
        return true;
    }

    @FXML
    private void placeObstacleClick() {
        saveObstacleDimensions(Model.currentObstacle);
        if (placeObstacleCB.isSelected()){
            if(Model.currentRunway == null){
                AlertController.showWarningAlert("No runway selected!");
                placeObstacleCB.setSelected(false);
                return;
            } else if (!isObstacleCorrectlyPlaced(Model.currentObstacle)) {
                AlertController.showWarningAlert("Obstacle placement not properly specified !");
                placeObstacleCB.setSelected(false);
                return;
            }
            Model.console.addLog("Obstacle: " + Model.currentObstacle.getName() + " was placed on runway: " + Model.currentRunway.toString());
            placeObstacle();
            updateVisualisation();
        } else {
            Model.obstaclePlaced = false;
            Model.console.addLog("Obstacle: " + Model.currentObstacle.getName() + " was removed from runway: " + Model.currentRunway.toString());
            selectedObstacleView();
            updateVisualisation();
        }
    }

    private void placeObstacle(){
        Model.obstaclePlaced = true;
        saveObstacleDimensions(Model.currentObstacle);
        placedObstacleView();
    }

    private void choiceBoxUpdater(String newValue, String oldValue){
        if (oldValue != null) {
            for(Obstacle o : Model.getObstacles()) {
                if (o.getName().equals(oldValue)) {
                    saveObstacleDimensions(o);
                    break;
                }
            }
        }
        if (newValue != null) {
            for(Obstacle o : Model.getObstacles()) {
                if (o.getName().equals(newValue)) {
                    Model.currentObstacle = o;
                    populateObstacleDetails(o);
                    populateObstacleDimensions(o.getPosition());
                    selectedObstacleView();
                    break;
                }
            }
        }
        Model.console.addLog("Obstacle selected: " + Model.currentObstacle.getName());
    }

    @FXML
    private void newObstacleClick() {
        populateObstacleDetails(null);
        obstacleEditView();
    }

    @FXML
    private void deleteObstacleClick() {
        setChoiceBoxListenerEnabled(false);
        Model.obstacles.remove(Model.currentObstacle);
        Model.console.addLog("Obstacle removed: " + Model.currentObstacle.getName());
        obstacleChoiceBox.getItems().remove(Model.currentObstacle.getName());
        obstacleChoiceBox.setValue(null);
        Model.obstaclePlaced = false;
        Model.currentObstacle = null;
        setChoiceBoxListenerEnabled(true);
        if (Model.obstacles.isEmpty()) noObstaclesView();
        else noSelectedObstacleView();
    }

    @FXML
    private void editObstacleClick() {
        edit = true;
        obstacleEditView();
    }

    @FXML
    private void cancelButtonClick() {
        edit = false;
        Model.leftScreenController.calculateButton.setDisable(false);
        populateObstacleDetails(Model.currentObstacle);
        if (Model.obstacles.isEmpty()) noObstaclesView();
        else if (Model.currentObstacle == null) noSelectedObstacleView();
        else selectedObstacleView();
    }

    @FXML
    private void saveButtonClick() {
        String name = obstacleNameTF.getText();
        if(name.equals("") || obstacleHeightTF.getText().equals("") || (obstacleWidthTF.getText().equals(""))) {
            AlertController.showWarningAlert("Some textfields are empty!");
            return;
        } else if (nameInUse(name, edit)){
            AlertController.showWarningAlert("Obstacle name already used");
            return;
        } else if (edit){
            saveEditedObstacle(name);
        } else {
            saveNewObstacle(name);
        }
        Model.leftScreenController.calculateButton.setDisable(false);
        selectedObstacleView();
    }

    private void saveEditedObstacle(String name) {
        if(Model.currentObstacle != null) {
            Obstacle o = Model.currentObstacle;
            String previousName = obstacleChoiceBox.getValue();
            int width = Integer.parseInt(obstacleWidthTF.getText());
            int height = Integer.parseInt(obstacleHeightTF.getText());
            if(previousName.equals(name) && width == o.getWidth() && height == o.getHeight()) {
                AlertController.showInfoAlert("No changes were made.");
                return;
            }
            setChoiceBoxListenerEnabled(false);
            obstacleChoiceBox.getItems().remove(Model.currentObstacle.getName());
            Model.currentObstacle.setName(name);
            Model.currentObstacle.setWidth(width);
            Model.currentObstacle.setHeight(height);
            obstacleChoiceBox.getItems().add(name);
            obstacleChoiceBox.setValue(name);
            setChoiceBoxListenerEnabled(true);
            edit = false;
            Model.console.addLog("Obstacle " + previousName + " edited to: " + Model.currentObstacle.getName());
        }
        else System.err.println("Error ObstacleController:312");
    }

    private void saveNewObstacle(String name) {
        int width = Integer.parseInt(obstacleWidthTF.getText());
        int height = Integer.parseInt(obstacleHeightTF.getText());
        Obstacle obstacle = new Obstacle(name, height, width, null);
        Model.currentObstacle = obstacle;
        Model.obstacles.add(obstacle);
        setChoiceBoxListenerEnabled(false);
        obstacleChoiceBox.getItems().add(name);
        Model.console.addLog("Obstacle: " + Model.currentObstacle.getName() + " added");
        setChoiceBoxListenerEnabled(true);
        obstacleChoiceBox.setValue(name);
    }

    private boolean nameInUse(String desiredName, boolean edit){
        String[] data = obstacleChoiceBox.getItems().toArray(new String[0]);
        for (String str : data) {
            if(!edit && str.equals(desiredName))
                return true;
            else if (str.equals(desiredName) && !str.equals(Model.currentObstacle.getName()))
                return true;
        }
        return false;
    }

}