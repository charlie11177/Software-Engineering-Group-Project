package controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.Model;

public class CenterScreenController {

    public TextArea console;
    public Pane topDownPane;
    public Pane sideOnPane;
    public Canvas topDowncanvas;
    public Canvas sideOnCanvas;

    @FXML
    private void initialize(){
        Model.centerScreenController = this;
        Model.console.update();
        topDowncanvas = new Canvas();
        sideOnCanvas = new Canvas();

        //makes resizing work
        topDownPane.getChildren().add(topDowncanvas);
        topDowncanvas.widthProperty().bind(topDownPane.widthProperty());
        topDowncanvas.heightProperty().bind(topDownPane.heightProperty());
        topDowncanvas.widthProperty().addListener(event -> drawTopDown(topDowncanvas));
        topDowncanvas.heightProperty().addListener(event -> drawTopDown(topDowncanvas));

        //makes resizing work
        sideOnPane.getChildren().add(sideOnCanvas);
        sideOnCanvas.widthProperty().bind(sideOnPane.widthProperty());
        sideOnCanvas.heightProperty().bind(sideOnPane.heightProperty());
        sideOnCanvas.widthProperty().addListener(event -> drawSideOn(sideOnCanvas));
        sideOnCanvas.heightProperty().addListener(event -> drawSideOn(sideOnCanvas));
        drawTopDown(topDowncanvas);
    }

    //Dont remove this please
    public void updateConsole(String text){
//        console.setText(text);
//        console.positionCaret(text.length());
        console.appendText(text + "\n");
    }

    //just examples how the resizing can be done, can be removed
    private void drawTopDown(Canvas canvas) {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        gc.setStroke(Color.RED);
        gc.strokeLine(0, 0, width, height);
        gc.strokeLine(0, height, width, 0);
        gc.setFill(Color.BLUE);
        gc.fillOval(-30, -30, 60, 60);
        gc.fillOval(-30 + width, -30, 60, 60);
        gc.fillOval(-30, -30 + height, 60, 60);
        gc.fillOval(-30 + width, -30 + height, 60, 60);
    }

    //just examples how the resizing can be done, can be removed
    private void drawSideOn(Canvas canvas) {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        gc.setStroke(Color.GREEN);
        gc.strokeLine(0, 0, width, height);
        gc.strokeLine(0, height, width, 0);
        gc.setFill(Color.ORANGE);
        gc.fillOval(-30, -30, 60, 60);
        gc.fillOval(-30 + width, -30, 60, 60);
        gc.fillOval(-30, -30 + height, 60, 60);
        gc.fillOval(-30 + width, -30 + height, 60, 60);
    }
}
