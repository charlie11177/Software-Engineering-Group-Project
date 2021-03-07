package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class Controller {

    @FXML
    private Button calculateButton;

    public Controller(){}

    @FXML
    public void initialize(){

    }

    @FXML
    private void calculateButtonClick() {
        System.out.println("Pressed");
    }
}
