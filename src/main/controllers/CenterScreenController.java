package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import main.Console;
import main.Model;

public class CenterScreenController {

    public TextArea console;

    @FXML
    private void initialize(){
        Model.centerScreenController = this;
        Model.console.update();
    }

    public void updateConsole(String text){
        console.setText(text);
        console.appendText("");
    }
}
