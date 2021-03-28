package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import main.model.Model;

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
