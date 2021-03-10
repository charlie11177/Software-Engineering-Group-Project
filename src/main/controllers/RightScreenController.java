package main.controllers;

import javafx.scene.control.TextArea;
import main.Model;

public class RightScreenController {

    public TextArea allCalculationsTopTA;
    public TextArea allCalculationsButtonTA;

    public void initialize(){
        Model.rightScreenController = this;
    }
}
