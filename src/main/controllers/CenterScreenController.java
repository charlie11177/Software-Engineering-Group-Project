package main.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.model.Model;

public class CenterScreenController {

    public TextArea console;
    public Pane topDownPane;
    public Pane sideOnPane;
    public Canvas topDowncanvas;
    public Canvas sideOnCanvas;
    private InvalidationListener widthListener1 = new InvalidationListener() {
        public void invalidated(Observable observable) {
            drawTopDown(topDowncanvas);
        }
    };
    private InvalidationListener heightListener1 = new InvalidationListener() {
        public void invalidated(Observable observable) {
            drawTopDown(topDowncanvas);
        }
    };

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
        topDowncanvas.widthProperty().addListener(widthListener1);
        topDowncanvas.heightProperty().addListener(heightListener1);

        //makes resizing work
        sideOnPane.getChildren().add(sideOnCanvas);
        sideOnCanvas.widthProperty().bind(sideOnPane.widthProperty());
        sideOnCanvas.heightProperty().bind(sideOnPane.heightProperty());
        sideOnCanvas.widthProperty().addListener(event -> drawSideOn(sideOnCanvas));
        sideOnCanvas.heightProperty().addListener(event -> drawSideOn(sideOnCanvas));
        drawTopDown(topDowncanvas);
        drawSideOn(sideOnCanvas);
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

    private void drawSideOn(Canvas canvas) {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //Colour Background
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(0, 0, width, height * 0.5);
        gc.setFill(Color.FORESTGREEN);
        gc.fillRect(0, height * 0.5, width, height * 0.5);

        if (Model.currentRunway != null)
            drawRunwaySideOn(gc, width, height);

        if (Model.obstaclePlaced)
            drawObstacleSideOn(gc, width, height);

        if (Model.recalculatedRunwayLeft != null && Model.recalculatedRunwayRight != null) {
            drawArrowsSideOn(gc, width, height);
            drawAnglesSideOn(gc, width, height);
        }
    }

    private void drawRunwaySideOn(GraphicsContext gc, int width, int height) {
        double leftStopway = Model.currentRunway.getLeftRunway().getStopway();
        double leftClearway = Model.currentRunway.getLeftRunway().getClearway();
        double rightStopway = Model.currentRunway.getRightRunway().getStopway();
        double rightClearway = Model.currentRunway.getRightRunway().getClearway();

        double leftThreshold = Model.originalRunwayLeft.getTORA() - Model.originalRunwayLeft.getLDA();
        double rightThreshold = Model.originalRunwayRight.getTORA() - Model.originalRunwayRight.getLDA();
        double runway = Model.originalRunwayLeft.getTORA();

        //Draw main runway
        gc.setFill(Color.LIGHTGREY);
        gc.fillRect(width * 0.125, height * 0.5, width * 0.75, height * 0.05);

        //Draw Stopways and Clearways if there are any
        gc.setFill(Color.GREY);
        if (leftClearway != 0 && leftClearway != leftStopway)
            gc.fillRect(width * 0.045, height * 0.5, width * 0.08, height * 0.05);
        if (rightClearway != 0 && rightClearway != rightStopway)
            gc.fillRect(width * 0.875, height * 0.5, width * 0.08, height * 0.05);
        gc.setFill(Color.DARKGREY);
        if (leftStopway != 0)
            gc.fillRect(width * 0.085, height * 0.5, width * 0.04, height * 0.05);
        if (rightStopway != 0)
            gc.fillRect(width * 0.875, height * 0.5, width * 0.04, height * 0.05);

        //Draw Displaced Threshold
        gc.setFill(Color.BLUE);
        if (leftThreshold != 0)
        {
            gc.fillRect(((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) - (width * 0.01), height * 0.5, width * 0.01, height * 0.05);
        }
        if (rightThreshold != 0)
        {
            gc.fillRect((width * 0.875) - ((rightThreshold / runway) * width * 0.75), height * 0.5, width * 0.01, height * 0.05);
        }
    }

    private void drawObstacleSideOn(GraphicsContext gc, int width, int height) {
        double runway = Model.originalRunwayLeft.getTORA();
        double obstacleFromLeft = Model.currentObstacle.getPosition().getDistanceToLeft();
        double obstacleFromRight = Model.currentObstacle.getPosition().getDistanceToRight();
        double leftThreshold = Model.originalRunwayLeft.getTORA() - Model.originalRunwayLeft.getLDA();
        double rightThreshold = Model.originalRunwayRight.getTORA() - Model.originalRunwayRight.getLDA();
        double obstacleWidth = (runway - (leftThreshold + rightThreshold)) - (obstacleFromLeft + obstacleFromRight);

        gc.setFill(Color.RED);
        gc.fillRect(width * (0.125 + (((obstacleFromLeft + leftThreshold) * 0.75) / runway)), height * 0.42, Math.max((obstacleWidth / runway) * width * 0.75, width * 0.005), height * 0.21);
    }

    private void drawArrowsSideOn(GraphicsContext gc, int width, int height) {
        double obstacleFromLeft = Model.currentObstacle.getPosition().getDistanceToLeft();
        double obstacleFromRight = Model.currentObstacle.getPosition().getDistanceToRight();

        double rightTORA = Math.max(Model.recalculatedRunwayRight.getTORA(), 0);
        double rightTODA = Math.max(Model.recalculatedRunwayRight.getTODA(), 0);
        double rightLDA = Math.max(Model.recalculatedRunwayRight.getLDA(), 0);
        double rightASDA = Math.max(Model.recalculatedRunwayRight.getASDA(), 0);
        double rightStopway = Math.max(Model.originalRunwayRight.getStopway(), 0);
        double rightClearway = Math.max(Model.originalRunwayRight.getClearway(), 0);
        double rightThreshold = Model.originalRunwayRight.getTORA() - Model.originalRunwayRight.getLDA();

        double leftTORA = Math.abs(Math.max(Model.recalculatedRunwayLeft.getTORA(), 0));
        double leftTODA = Math.max(Model.recalculatedRunwayLeft.getASDA(), 0);
        double leftLDA = Math.abs(Math.max(Model.recalculatedRunwayLeft.getLDA(), 0));
        double leftASDA = Math.max(Model.recalculatedRunwayLeft.getASDA(), 0);
        double leftStopway = Math.max(Model.originalRunwayLeft.getStopway(), 0);
        double leftClearway = Math.max(Model.originalRunwayLeft.getClearway(), 0);
        double leftThreshold = Model.originalRunwayLeft.getTORA() - Model.originalRunwayLeft.getLDA();


        double runway = Model.originalRunwayLeft.getTORA();

        //Runway Name Placement
        gc.setStroke(Color.WHITE);
        gc.strokeText(Model.recalculatedRunwayLeft.getName() + " -> ", width * 0.01, height * 0.535);
        gc.strokeText(" <- " + Model.recalculatedRunwayRight.getName(), width * 0.96, height * 0.535);

        gc.setStroke(Color.BLACK);
        if (obstacleFromLeft > obstacleFromRight) {
            //Left Towards
            if(leftLDA <= leftTORA) {
                gc.strokeLine(width * 0.125, height * 0.5, width * 0.125, height * 0.075);
                gc.strokeLine(((width * 0.125) + ((leftThreshold / runway) * width * 0.75)), height * 0.5,(width * 0.125) + ((leftThreshold / runway) * width * 0.75), height * 0.375 );
                gc.strokeLine(((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) + ((leftLDA / runway) * width * 0.75), height * 0.5, ((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) + ((leftLDA / runway) * width * 0.75), height * 0.375);
                gc.strokeLine(width * (0.125 + (0.75 * leftTORA / runway)), height * 0.5, width * (0.125 + (0.75 * leftTORA / runway)), height * 0.075);
                fillArrow(gc, (width * 0.125) + ((leftThreshold / runway) * width * 0.75), height * 0.375, ((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) + ((leftLDA / runway) * width * 0.75), height * 0.375, "LDA : " + leftLDA);
                fillArrow(gc, width * 0.125, height * 0.075, width * (0.125 + (0.75 * leftASDA / runway)), height * 0.075, "ASDA : " + leftASDA);
                fillArrow(gc, width * 0.125, height * 0.175, width * (0.125 + (0.75 * leftTODA / runway)), height * 0.175, "TODA : " + leftTODA);
                fillArrow(gc, width * 0.125, height * 0.275, width * (0.125 + (0.75 * leftTORA / runway)), height * 0.275, "TORA : " + leftTORA);
            }
            else {
                gc.strokeLine(((width * 0.125) + ((leftThreshold / runway) * width * 0.75)), height * 0.5, (width * 0.125 + ((leftThreshold / runway) * width * 0.75)), height * 0.075);
                gc.strokeLine(width * 0.125, height * 0.5, width * 0.125, height * 0.175);
                gc.strokeLine(((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) + ((leftLDA / runway) * width * 0.75), height * 0.5, ((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) + ((leftLDA / runway) * width * 0.75), height * 0.075);
                gc.strokeLine(width * (0.125 + (0.75 * leftTORA / runway)), height * 0.5, width * (0.125 + (0.75 * leftTORA / runway)), height * 0.175);
                fillArrow(gc, (width * 0.125) + ((leftThreshold / runway) * width * 0.75), height * 0.075, (width * 0.125) + ((leftThreshold / runway) * width * 0.75) + ((leftLDA / runway) * width * 0.75), height * 0.075, "LDA : " + leftLDA);
                fillArrow(gc, width * 0.125, height * 0.175, width * (0.125 + (0.75 * leftASDA / runway)), height * 0.175, "ASDA : " + leftASDA);
                fillArrow(gc, width * 0.125, height * 0.275, width * (0.125 + (0.75 * leftTODA / runway)), height * 0.275, "TODA : " + leftTODA);
                fillArrow(gc, width * 0.125, height * 0.375, width * (0.125 + (0.75 * leftTORA / runway)), height * 0.375, "TORA : " + leftTORA);
            }

            //Right Away
            gc.strokeLine(width * (0.125 + (0.75 * rightTORA / runway)), height * 0.55, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.925);
            gc.strokeLine(width * (0.125 + (0.75 * rightLDA / runway)), height * 0.55, width * (0.125 + (0.75 * rightLDA / runway)), height * 0.625);
            if (rightStopway != 0 && rightClearway != 0 && rightStopway != rightClearway) {
                gc.strokeLine(width * 0.085, height * 0.55, width * 0.085, height * 0.825);
                gc.strokeLine(width * 0.045, height * 0.55, width * 0.045, height * 0.925);
                gc.strokeLine(width * 0.125, height * 0.55, width * 0.125, height * 0.725);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.725, width * 0.125, height * 0.725, "TORA : " + rightTORA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.825, width * 0.085, height * 0.825, "TODA : " + rightTODA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.925, width * 0.045, height * 0.925, "ASDA : " + rightASDA);
                fillArrow(gc, width * (0.125 + (0.75 * rightLDA / runway)), height * 0.625, width * 0.125, height * 0.625, "LDA : " + rightLDA);
            } else if (rightStopway != 0) {
                gc.strokeLine(width * 0.085, height * 0.55, width * 0.085, height * 0.925);
                gc.strokeLine(width * 0.125, height * 0.55, width * 0.125, height * 0.725);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.725, width * 0.125, height * 0.725, "TORA : " + rightTORA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.825, width * 0.085, height * 0.825, "TODA : " + rightTODA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.925, width * 0.085, height * 0.925, "ASDA : " + rightASDA);
                fillArrow(gc, width * (0.125 + (0.75 * rightLDA / runway)), height * 0.625, width * 0.125, height * 0.625, "LDA : " + rightLDA);
            } else if (rightClearway != 0) {
                gc.strokeLine(width * 0.045, height * 0.55, width * 0.045, height * 0.925);
                gc.strokeLine(width * 0.125, height * 0.55, width * 0.125, height * 0.825);
                gc.strokeLine(width * 0.125, height * 0.55, width * 0.125, height * 0.825);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.725, width * 0.125, height * 0.725, "TORA : " + rightTORA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.825, width * 0.125, height * 0.825, "TODA : " + rightTODA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.925, width * 0.045, height * 0.925, "ASDA : " + rightASDA);
                fillArrow(gc, width * (0.125 + (0.75 * rightLDA / runway)), height * 0.625, width * 0.125, height * 0.625, "LDA : " + rightLDA);
            } else {
                gc.strokeLine(width * 0.125, height * 0.55, width * 0.125, height * 0.925);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.725, width * 0.125, height * 0.725, "TORA : " + rightTORA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.825, width * 0.125, height * 0.825, "TODA : " + rightTODA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.925, width * 0.125, height * 0.925, "ASDA : " + rightASDA);
                fillArrow(gc, width * (0.125 + (0.75 * rightLDA / runway)), height * 0.625, width * 0.125, height * 0.625, "LDA : " + rightLDA);
            }
        }
        else {
            //Right Towards
            if(rightLDA <= rightTORA) {
                gc.strokeLine((width * 0.875) - ((rightThreshold / runway) * width * 0.75), height * 0.55,(width * 0.875) - ((rightThreshold / runway) * width * 0.75), height * 0.625 );
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.925);
                gc.strokeLine((width * 0.875) - ((width * 0.75 * (rightLDA / runway)) + ((rightThreshold / runway) * width * 0.75)), height * 0.55, (width * 0.875) - ((width * 0.75 * (rightLDA / runway)) + ((rightThreshold / runway) * width * 0.75)), height * 0.625);
                gc.strokeLine(width * (0.875 - (0.75 * rightTORA / runway)), height * 0.55, width * (0.875 - (0.75 * rightTORA / runway)), height * 0.925);
                fillArrow(gc, (width * 0.875) - ((rightThreshold / runway) * width * 0.75) , height * 0.625, (width * 0.875) - ((((rightThreshold / runway) * width * 0.75) + ((rightLDA / runway) * width * 0.75))), height * 0.625, "LDA : " + rightLDA);
                fillArrow(gc, width * 0.875, height * 0.925, width * (0.875 - (0.75 * rightASDA / runway)), height * 0.925, "ASDA : " + rightASDA);
                fillArrow(gc, width * 0.875, height * 0.825, width * (0.875 - (0.75 * rightTODA / runway)), height * 0.825, "TODA : " + rightTODA);
                fillArrow(gc, width * 0.875, height * 0.725, width * (0.875 - (0.75 * rightTORA / runway)), height * 0.725, "TORA : " + rightTORA);
            }
            else {
                gc.strokeLine((width * 0.875) - ((rightThreshold / runway) * width * 0.75), height * 0.55, (width * 0.875) - ((rightThreshold / runway) * width * 0.75), height * 0.925);
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.825);
                gc.strokeLine((width * 0.875) - ((width * 0.75 * (rightLDA / runway)) + ((rightThreshold / runway) * width * 0.75)), height * 0.55, (width * 0.875) - ((width * 0.75 * (rightLDA / runway)) + ((rightThreshold / runway) * width * 0.75)), height * 0.925);
                gc.strokeLine(width * (0.875 - (0.75 * rightTORA / runway)), height * 0.55, width * (0.875 - (0.75 * rightTORA / runway)), height * 0.825);
                fillArrow(gc, (width * 0.875) - ((rightThreshold / runway) * width * 0.75) , height * 0.925, (width * 0.875) - ((width * 0.75 * (rightLDA / runway)) + ((rightThreshold / runway) * width * 0.75)), height * 0.925, "LDA : " + rightLDA);
                fillArrow(gc, width * 0.875, height * 0.825, width * (0.875 - (0.75 * rightASDA / runway)), height * 0.825, "ASDA : " + rightASDA);
                fillArrow(gc, width * 0.875, height * 0.725, width * (0.875 - (0.75 * rightTODA / runway)), height * 0.725, "TODA : " + rightTODA);
                fillArrow(gc, width * 0.875, height * 0.625, width * (0.875 - (0.75 * rightTORA / runway)), height * 0.625, "TORA : " + rightTORA);
            }

            //Left Away
            gc.strokeLine(width * (0.875 - (0.75 * leftTORA / runway)), height * 0.55, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.075);
            gc.strokeLine(width * (0.875 - (0.75 * leftLDA / runway)), height * 0.55, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.375);
            if (leftStopway != 0 && leftClearway != 0 && leftStopway != leftClearway) {
                gc.strokeLine(width * 0.085, height * 0.55, width * 0.085, height * 0.175);
                gc.strokeLine(width * 0.045, height * 0.55, width * 0.045, height * 0.075);
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.275);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.275, width * 0.875, height * 0.275, "TORA : " + leftTORA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.175, width * 0.085, height * 0.175, "TODA : " + leftTODA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.075, width * 0.045, height * 0.075, "ASDA : " + leftASDA);
                fillArrow(gc, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.375, width * 0.875, height * 0.375, "LDA : " + leftLDA);
            } else if (leftStopway != 0) {
                gc.strokeLine(width * 0.085, height * 0.55, width * 0.085, height * 0.075);
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.275);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.275, width * 0.875, height * 0.275, "TORA : " + leftTORA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.175, width * 0.085, height * 0.175, "TODA : " + leftTODA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.075, width * 0.085, height * 0.075, "ASDA : " + leftASDA);
                fillArrow(gc, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.375, width * 0.875, height * 0.375, "LDA : " + leftLDA);
            } else if (leftClearway != 0) {
                gc.strokeLine(width * 0.045, height * 0.55, width * 0.045, height * 0.075);
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.175);
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.175);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.275, width * 0.875, height * 0.275, "TORA : " + leftTORA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.175, width * 0.875, height * 0.175, "TODA : " + leftTODA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.075, width * 0.045, height * 0.075, "ASDA : " + leftASDA);
                fillArrow(gc, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.375, width * 0.875, height * 0.375, "LDA : " + leftLDA);
            } else {
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.075);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.275, width * 0.875, height * 0.275, "TORA : " + leftTORA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.175, width * 0.875, height * 0.175, "TODA : " + leftTODA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.075, width * 0.875, height * 0.075, "ASDA : " + leftASDA);
                fillArrow(gc, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.375, width * 0.875, height * 0.375, "LDA : " + leftLDA);
            }
        }
    }

    private void drawAnglesSideOn(GraphicsContext gc, int width, int height) {
        double obstacleFromLeft = Model.currentObstacle.getPosition().getDistanceToLeft();
        double obstacleFromRight = Model.currentObstacle.getPosition().getDistanceToRight();
        double runway = Model.originalRunwayLeft.getTORA();
        double leftThreshold = Model.originalRunwayLeft.getTORA() - Model.originalRunwayLeft.getLDA();
        double rightThreshold = Model.originalRunwayRight.getTORA() - Model.originalRunwayRight.getLDA();
        double obstacleWidth = (runway - (leftThreshold + rightThreshold)) - (obstacleFromLeft + obstacleFromRight);
        double rightTORA = Math.max(Model.recalculatedRunwayRight.getTORA(), 0);
        double rightLDA = Math.max(Model.recalculatedRunwayRight.getLDA(), 0);
        double leftTORA = Math.abs(Math.max(Model.recalculatedRunwayLeft.getTORA(), 0));
        double leftLDA = Math.abs(Math.max(Model.recalculatedRunwayLeft.getLDA(), 0));

        double obstacleLeft = width * (0.125 + (((obstacleFromLeft + leftThreshold) * 0.75) / runway));
        double obstacleRight = obstacleLeft + Math.max((obstacleWidth / runway) * width * 0.75, width * 0.005);
        double obstacleHeight = height * 0.08;
        double thresholdDisplacement = Math.max((Model.currentObstacle.getHeight() * 50) + 60, 300);
        boolean usingResa = thresholdDisplacement == 300;

        gc.setStroke(Color.BLACK);
        if(obstacleFromLeft > obstacleFromRight)
        {
            gc.strokeLine(obstacleLeft, (height * 0.5) - obstacleHeight, (obstacleLeft - ((thresholdDisplacement/runway) * width * 0.75)), height * 0.5 );
            gc.strokeText("TOCS/ALS : ", obstacleLeft + 15, height * 0.425);
            fillArrow(gc, obstacleLeft, height * 0.6, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.6, "");
            gc.strokeText("Blast Protection : 300", obstacleLeft + 15, height * 0.61);

            if(usingResa)
            {
                fillArrow(gc, obstacleLeft, height * 0.525, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.525, "");
                gc.strokeText("Resa + Runway end : 240 + 60", obstacleLeft + 15, height * 0.531);
                gc.strokeLine(obstacleLeft - ((240/300) * (obstacleLeft - (width * (0.125 + (0.75 * rightTORA / runway))))), (height *0.525) + 10, obstacleLeft - ((240/300) * (obstacleLeft - (width * (0.125 + (0.75 * rightTORA / runway))))), (height * 0.525) - 10);
            }
            else
            {
                fillArrow(gc, obstacleLeft, height  * 0.525, width * (0.125 + (0.75 * rightLDA / runway)), height * 0.525, "");
                gc.strokeText("(Height * 50) + Runway end : " + (thresholdDisplacement - 60) + " + 60", obstacleLeft + 15, height * 0.531);
                gc.strokeLine(obstacleLeft - (((thresholdDisplacement - 60)/thresholdDisplacement) * (obstacleLeft - (width * (0.125 + (0.75 * rightLDA / runway))))), (height *0.525) + 10, obstacleLeft - (((thresholdDisplacement - 60)/thresholdDisplacement) * (obstacleLeft - (width * (0.125 + (0.75 * rightLDA / runway))))), (height * 0.525) - 10);
            }
        }
        else
        {
            gc.strokeLine(obstacleRight, (height * 0.5) - obstacleHeight, (obstacleRight + ((thresholdDisplacement/runway) * width * 0.75)), height * 0.5 );
            gc.strokeText("TOCS/ALS : ", obstacleRight - 85, height * 0.425);
            fillArrow(gc, obstacleRight, height * 0.6, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.6, "");
            gc.strokeText("Blast Protection : 300", obstacleRight - 135, height * 0.61);

            if(usingResa)
            {
                fillArrow(gc, obstacleRight, height * 0.525, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.525, "");
                gc.strokeText("Resa + Runway end : 240 + 60", obstacleRight - 190, height * 0.531);
                gc.strokeLine(obstacleRight + ((240/300) * ((width * (0.875 - (0.75 * leftTORA / runway))) - obstacleRight)), (height *0.525) + 10, obstacleRight - ((240/300) * ((width * (0.875 - (0.75 * leftTORA / runway))) - obstacleRight)), (height * 0.525) - 10);
            }
            else
            {
                fillArrow(gc, obstacleRight, height  * 0.525, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.525, "");
                gc.strokeText("(Height * 50) + Runway end : " + (thresholdDisplacement - 60) + " + 60", obstacleRight - 220, height * 0.531);
                gc.strokeLine(obstacleRight + (((thresholdDisplacement - 60)/thresholdDisplacement) * ((width * (0.875 - (0.75 * leftLDA / runway))) - obstacleRight)), (height *0.525) + 10, obstacleRight + (((thresholdDisplacement - 60)/thresholdDisplacement) * ((width * (0.875 - (0.75 * leftLDA / runway))) - obstacleRight)), (height * 0.525) - 10);
            }
        }
    }

    private void fillArrow(GraphicsContext gc, double x1, double y1, double x2, double y2, String label) {
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);

        if (x1 > x2) {
            gc.strokeText(label, x2 + ((x1-x2) / 2), y1 - 13);
        }
        else {
            gc.strokeText(label, x1 + ((x2 - x1) / 2), y1 - 13);
        }

        // Case for diagonal arrows isn't needed (i hope)
        if (x1 != x2 && y1 != y2) {

        }
        //Horizontal Arrow
        else if (x1 != x2) {
            gc.strokeLine(x1,y1,x2,y2);
            if (x1 < x2)
                gc.fillPolygon(new double[]{x2, x2-5, x2-5, x2-5},new double[]{y1, y1+5, y1, y1-5},4);
            else
                gc.fillPolygon(new double[]{x2, x2+5, x2+5, x2+5},new double[]{y1, y1+5, y1, y1-5},4);
        }
        //Vertical Arrow
        else if (y1 != y2) {
            gc.strokeLine(x1,y1,x2,y2);
            if (y1 < y2)
                gc.fillPolygon(new double[]{x1, x1+5, x1, x1-5},new double[]{y2, y2-5, y2-5, y2-5},4);
            else
                gc.fillPolygon(new double[]{x1, x1+5, x1, x1-5},new double[]{y2, y2+5, y2+5, y2+5},4);
        }
    }
}