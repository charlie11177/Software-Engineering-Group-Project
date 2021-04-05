package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.model.Model;
import main.model.PhysicalRunWay;

public class CenterScreenController {

    public TextArea console;
    public Pane topDownPane;
    public Pane sideOnPane;
    public Canvas topDowncanvas;
    public Canvas sideOnCanvas;
    public PhysicalRunWay runway = Model.currentRunway;

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
        console.setText(text);
        console.appendText("");
    }

    //just examples how the resizing can be done, can be removed
    private void drawTopDown(Canvas canvas) {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        int lDegree = 0;
        int rDegree = 0;
        if(runway!=null){
            lDegree = runway.getLeftRunway().getDegree();
            rDegree = runway.getRightRunway().getDegree();
        }
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Double scale = (canvas.getWidth() *3.26/100 );
        // background
        gc.setFill(Color.GOLD);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        //safe height
        gc.setStroke(Color.BLACK);
        gc.strokeRect(scale*3, scale, canvas.getWidth() - (scale*6), canvas.getHeight() -(scale*2));
        gc.setFill(Color.WHEAT);
        gc.fillRect(scale*3, scale, canvas.getWidth() - (scale*6), canvas.getHeight() -(scale*2));

        gc.setFill(Color.TAN);
        double[] pointX = {scale*3,scale*9, scale*10, scale*22, scale*23, scale*27.6, scale*27.6, scale*23, scale*22, scale*10, scale*9, scale*3};
        double[] pointY = {scale*5,scale*5, scale*4, scale*4, scale*5, scale*5, scale*11, scale*11, scale*12, scale*12, scale*11, scale*11};
        gc.fillPolygon(pointX, pointY,12);
        //runway
        int thershold;
        if (lDegree > rDegree) {
            thershold = rDegree;
            rDegree = lDegree;
            lDegree = thershold;
        }
        gc.setFill(Color.GRAY);
        gc.fillRect(scale*6, scale*7, scale*19, scale*2);
        Double d = 0.0;
        for(int i = 0;i <10;i ++){
            gc.setFill(Color.WHITE);
            gc.fillRect((scale)*(7),scale*(7.3 + d), scale, scale / 10);
            gc.fillRect((scale)*(23),scale*(7.3 + d), scale, scale / 10);
            d = (d + 0.15);
        }
        //gc.setLineWidth(50);
        gc.fillText(String.valueOf(lDegree),scale*8.5, scale* 8.2);
        gc.setFill(Color.WHITE);
        gc.fillText(String.valueOf(rDegree),scale*22, scale* 8.2);
        gc.setFill(Color.BLACK);
        gc.fillText("Clear Graded Area", scale*12 , scale * 5, 200);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        //change line type to be dashed
        gc.strokeLine(scale*9.2 ,scale*8.1, scale*21.7, scale*8.1 );

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
        gc.fillOval(-30 + width, -30 + height,
                60, 60);
    }


}
