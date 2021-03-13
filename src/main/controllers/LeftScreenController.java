package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import main.*;

import java.util.ArrayList;

public class LeftScreenController {

    public Button calculateButton;
    public Accordion accordion;

    @FXML
    private void initialize() {
        Model.leftScreenController = this;
    }

    @FXML
    private void calculateButtonClick() {
    }

}
