package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import model.LogicalRunWay;
import model.Model;
import model.PhysicalRunWay;
import model.Position;

import javax.swing.plaf.ColorUIResource;

public class CenterScreenController {

    public TextArea console;
    public Pane topDownPane;
    public Pane sideOnPane;
    public Canvas topDowncanvas;
    public Canvas sideOnCanvas;
    public PhysicalRunWay runway;
    private final int ARR_SIZE = 5;
    public CheckBox matchCompasCB;
    private ViewMode viewMode;
    private ColourBlindMode colourBlindMode;
    @FXML private Slider sizeSlider;
    private double mouseDeltaX;
    private double mouseDeltaY;
    @FXML private StackPane topDownStackPane;

    private Color GRASS_COLOR;
    private Color DARKBLUE_COLOR;
    private Color RUNWAY_COLOR;
    private Color SKY_COLOR;
    private Color RED_COLOR;
    private Color ORANGE_COLOR;
    private Color YELLOW_COLOR;
    private Color BLUE_COLOR;
    private Color PURPLE_COLOR;
    private final int ARR_SIZE2 = 20;

    //TODO: What colors to use: (non colourblind mode)
    // Runway -> RUNWAY_COLOR
    // Stopway -> Color.BLACK
    // Displaced Threshold -> Color.YELLOW
    // Clear Way -> Color.ORANGE
    // Obstacle -> Color.RED

    @FXML
    private void initialize(){
        Model.centerScreenController = this;
        console.setWrapText(true);
        Model.console.update();
        defaultColors();
        setupTopDownCanvas();
        setupSideOnCanvas();
        viewMode = ViewMode.DEFAULT;
        colourBlindMode = ColourBlindMode.DEFAULT;
        sizeSlider.valueProperty().addListener((ob, oldValue, newValue) -> {
            if (newValue.doubleValue() <= 100) {
                setupSideOnCanvas();
            }
            sideOnCanvas.setScaleX((newValue.doubleValue()/100));
            sideOnCanvas.setScaleY((newValue.doubleValue()/100));
            topDowncanvas.setScaleX((newValue.doubleValue()/100));
            topDowncanvas.setScaleY((newValue.doubleValue()/100));
        });
        matchCompasCB.selectedProperty().addListener((ob, oldValue, newValue) -> {
            matchCompass(newValue);
        });
        setBackground();

//        topDownStackPane.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                mouseDeltaX = mouseEvent.getX();
//                mouseDeltaY = mouseEvent.getY();
//            }
//        });
//        topDownStackPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                if (sizeSlider.getValue() >= 100)
//                    topDowncanvas.getGraphicsContext2D().translate(((mouseEvent.getX() - mouseDeltaX)), (mouseEvent.getY()) - mouseDeltaY);
//                drawTopDown();
//                mouseDeltaX = mouseEvent.getX();
//                mouseDeltaY = mouseEvent.getY();
//            }
//        });
    }

    public void matchCompass(boolean isSelected){
        if(Model.currentRunway == null)
            return;
        if(isSelected){
            int degree = Model.currentRunway.getLeftRunway().getDegree();
            topDowncanvas.setRotate(degree*10-90);
        } else {
            topDowncanvas.setRotate(0);
        }
    }

    public void matchCompasClick() {
        if(Model.currentRunway == null)
            return;
        if(matchCompasCB.isSelected()){
            Model.console.addLog("Top Down visualisation orientation is set to match with compass heading");
        } else {
            Model.console.addLog("Top Down visualisation orientation is set to default");
        }
    }

    private void setupTopDownCanvas(){
        topDowncanvas = new Canvas();
        topDownPane.getChildren().add(topDowncanvas);
        topDowncanvas.widthProperty().bind(topDownPane.widthProperty());
        topDowncanvas.heightProperty().bind(topDownPane.heightProperty());
        topDowncanvas.widthProperty().addListener(event -> drawTopDown());
        topDowncanvas.heightProperty().addListener(event -> drawTopDown());
    }

    private void setupSideOnCanvas(){
        sideOnCanvas = new Canvas();
        sideOnPane.getChildren().clear();
        sideOnPane.getChildren().add(sideOnCanvas);
        sideOnCanvas.widthProperty().bind(sideOnPane.widthProperty());
        sideOnCanvas.heightProperty().bind(sideOnPane.heightProperty());
        sideOnCanvas.widthProperty().addListener(event -> drawSideOn(sideOnCanvas));
        sideOnCanvas.heightProperty().addListener(event -> drawSideOn(sideOnCanvas));

        sideOnCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseDeltaX = mouseEvent.getX();
                mouseDeltaY = mouseEvent.getY();
            }
        });
        sideOnCanvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (sizeSlider.getValue() >= 100)
                    sideOnCanvas.getGraphicsContext2D().translate(((mouseEvent.getX() - mouseDeltaX)), (mouseEvent.getY()) - mouseDeltaY);
                drawSideOn(sideOnCanvas);
                mouseDeltaX = mouseEvent.getX();
                mouseDeltaY = mouseEvent.getY();
            }
        });

        drawSideOn(sideOnCanvas);
    }

    public void updateConsole(String text){
//        console.setText(text);
//        console.positionCaret(text.length());
        console.appendText(text + "\n");
    }

    public void updateVisualisation(ViewMode mode) {
        viewMode = mode;
        if (mode == ViewMode.CALCULATIONS_RUNWAY) {
            Model.mainWindowController.savePDF.setDisable(false);
            Model.mainWindowController.savePNGSideOn.setDisable(false);
            Model.mainWindowController.savePNGTopDown.setDisable(false);
        } else {
            Model.mainWindowController.savePDF.setDisable(true);
            Model.mainWindowController.savePNGSideOn.setDisable(true);
            Model.mainWindowController.savePNGTopDown.setDisable(true);

        }
//        switch (Model.colorBlindMode){
//            case DEFAULT -> defaultColors();
//            case PROTANOPE -> redBlindness();
//            case DEUTERANOPE -> greenBlindness();
//            case TRITANOPE -> blueBlindness();
//        }
        topDownPane.getChildren().remove(topDowncanvas);
        setupTopDownCanvas();
        drawTopDown();
        drawSideOn(sideOnCanvas);
        if (Model.currentRunway == null){
            matchCompasCB.setDisable(true);
//            matchCompasCB.setSelected(false);
        } else {
            matchCompasCB.setDisable(false);
            matchCompass(matchCompasCB.isSelected());
        }
        sideOnCanvas.setScaleX((sizeSlider.getValue()/100));
        sideOnCanvas.setScaleY((sizeSlider.getValue()/100));
        topDowncanvas.setScaleX((sizeSlider.getValue()/100));
        topDowncanvas.setScaleY((sizeSlider.getValue()/100));
    }

    private void drawTopDown() {
        switch (viewMode) {
            case DEFAULT:
                drawTopDown(topDowncanvas);
                break;
            case RUNWAY:
                drawRunway(topDowncanvas);
                break;
            case OBSTACLE_PLACED_RUNWAY:
                drawRunwayWithObstacle(topDowncanvas);
                break;
            case CALCULATIONS_RUNWAY:
                drawRedeclaredRunway(topDowncanvas);
                break;
        }
    }

    private void drawSideOn(Canvas canvas) {

        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //Colour Background
        gc.setFill(SKY_COLOR);
        gc.fillRect(-width, -height, width*3, height*3);
        gc.setFill(GRASS_COLOR);
        gc.fillRect(-width, height * 0.5, width*3, height * 3);
        gc.setFill(RUNWAY_COLOR);
        gc.fillRect(width * 0.125, height * 0.5, width * 0.75, height * 0.05);

        if (Model.currentRunway != null)
            drawRunwaySideOn(gc, width, height);

        if (Model.obstaclePlaced)
            drawObstacleSideOn(gc, width, height);

        if (!Model.leftScreenController.calculateAllowed) {
            drawArrowsSideOn(gc, width, height);
            drawAnglesSideOn(gc, width, height);
        }
    }

    private void drawRunwaySideOn(GraphicsContext gc, int width, int height) {
        double rightStopway = Model.currentRunway.getLeftRunway().getStopway();
        double rightClearway = Model.currentRunway.getLeftRunway().getClearway();
        double leftStopway = Model.currentRunway.getRightRunway().getStopway();
        double leftClearway = Model.currentRunway.getRightRunway().getClearway();

        double leftThreshold = Model.currentRunway.getLeftRunway().getTORA() - Model.currentRunway.getLeftRunway().getLDA();
        double rightThreshold = Model.currentRunway.getRightRunway().getTORA() - Model.currentRunway.getRightRunway().getLDA();
        double runway = Model.currentRunway.getLeftRunway().getTORA();

        //Draw main runway
        gc.setFill(RUNWAY_COLOR);
        gc.fillRect(width * 0.125, height * 0.5, width * 0.75, height * 0.05);

        //Draw Stopways and Clearways if there are any
        gc.setFill(ORANGE_COLOR);
        if (leftClearway != 0 && leftClearway != leftStopway)
            gc.fillRect(width * 0.045, height * 0.5, width * 0.08, height * 0.05);
        if (rightClearway != 0 && rightClearway != rightStopway)
            gc.fillRect(width * 0.875, height * 0.5, width * 0.08, height * 0.05);
        gc.setFill(Color.BLACK);
        if (leftStopway != 0)
            gc.fillRect(width * 0.085, height * 0.5, width * 0.04, height * 0.05);
        if (rightStopway != 0)
            gc.fillRect(width * 0.875, height * 0.5, width * 0.04, height * 0.05);

        //Draw Displaced Threshold
        gc.setFill(YELLOW_COLOR);
        if (leftThreshold != 0)
        {
            gc.fillRect(((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) - (width * 0.01), height * 0.5, width * 0.01, height * 0.05);
        }
        if (rightThreshold != 0)
        {
            gc.fillRect((width * 0.875) - ((rightThreshold / runway) * width * 0.75), height * 0.5, width * 0.01, height * 0.05);
        }

        //Runway Name Placement
        gc.setStroke(Color.BLACK);
        gc.strokeText(Model.currentRunway.getLeftRunway().getName() + " -> ", width * 0.01, height * 0.1);
        gc.strokeText(" <- " + Model.currentRunway.getRightRunway().getName(), width * 0.94, height * 0.9);
    }

    private void drawObstacleSideOn(GraphicsContext gc, int width, int height) {
        double runway = Model.currentRunway.getLeftRunway().getTORA();
        double obstacleFromLeft = Model.currentObstacle.getPosition().getDistanceToLeft();
        double obstacleFromRight = Model.currentObstacle.getPosition().getDistanceToRight();
        double leftThreshold = Model.currentRunway.getLeftRunway().getTORA() - Model.currentRunway.getLeftRunway().getLDA();
        double rightThreshold = Model.currentRunway.getRightRunway().getTORA() - Model.currentRunway.getRightRunway().getLDA();
        double obstacleWidth = (runway - (leftThreshold + rightThreshold)) - (obstacleFromLeft + obstacleFromRight);

        gc.setFill(RED_COLOR);
        gc.fillRect(width * (0.125 + (((obstacleFromLeft + leftThreshold) * 0.75) / runway)), height * 0.42, Math.max((obstacleWidth / runway) * width * 0.75, width * 0.005), height * 0.21);
    }

    private void drawArrowsSideOn(GraphicsContext gc, int width, int height) {
        double obstacleFromLeft = Model.currentObstacle.getPosition().getDistanceToLeft();
        double obstacleFromRight = Model.currentObstacle.getPosition().getDistanceToRight();

        int rightTORA = Math.max(Model.recalculatedRunwayRight.getTORA(), 0);
        int rightTODA = Math.max(Model.recalculatedRunwayRight.getTODA(), 0);
        int rightLDA = Math.max(Model.recalculatedRunwayRight.getLDA(), 0);
        int rightASDA = Math.max(Model.recalculatedRunwayRight.getASDA(), 0);
        int rightStopway = Model.currentRunway.getRightRunway().getStopway();
        int rightClearway = Model.currentRunway.getRightRunway().getClearway();
        int rightThreshold = Model.currentRunway.getRightRunway().getTORA() - Model.currentRunway.getRightRunway().getLDA();

        int leftTORA = Math.abs(Math.max(Model.recalculatedRunwayLeft.getTORA(), 0));
        int leftTODA = Math.max(Model.recalculatedRunwayLeft.getASDA(), 0);
        int leftLDA = Math.abs(Math.max(Model.recalculatedRunwayLeft.getLDA(), 0));
        int leftASDA = Math.max(Model.recalculatedRunwayLeft.getASDA(), 0);
        int leftStopway = Model.currentRunway.getLeftRunway().getStopway();
        int leftClearway = Model.currentRunway.getLeftRunway().getClearway();
        int leftThreshold = Model.currentRunway.getLeftRunway().getTORA() - Model.currentRunway.getLeftRunway().getLDA();


        double runway = Model.currentRunway.getLeftRunway().getTORA();

        gc.setStroke(Color.BLACK);
        if (obstacleFromLeft > obstacleFromRight) {
            //Left Towards
            if(leftLDA <= leftTORA) {
                gc.strokeLine(width * 0.125, height * 0.5, width * 0.125, height * 0.075);
                gc.strokeLine(((width * 0.125) + ((leftThreshold / runway) * width * 0.75)), height * 0.5,(width * 0.125) + ((leftThreshold / runway) * width * 0.75), height * 0.375 );
                gc.strokeLine(((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) + ((leftLDA / runway) * width * 0.75), height * 0.5, ((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) + ((leftLDA / runway) * width * 0.75), height * 0.375);
                gc.strokeLine(width * (0.125 + (0.75 * leftTORA / runway)), height * 0.5, width * (0.125 + (0.75 * leftTORA / runway)), height * 0.075);
                fillArrow(gc, (width * 0.125) + ((leftThreshold / runway) * width * 0.75), height * 0.375, ((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) + ((leftLDA / runway) * width * 0.75), height * 0.375, "LDA : " + leftLDA);
                fillArrow(gc, width * 0.125, height * 0.075, width * (0.125 + (0.75 * leftTODA / runway)), height * 0.075, "TODA : " + leftTODA);
                fillArrow(gc, width * 0.125, height * 0.175, width * (0.125 + (0.75 * leftASDA / runway)), height * 0.175, "ASDA : " + leftASDA);
                fillArrow(gc, width * 0.125, height * 0.275, width * (0.125 + (0.75 * leftTORA / runway)), height * 0.275, "TORA : " + leftTORA);
            }
            else {
                gc.strokeLine(((width * 0.125) + ((leftThreshold / runway) * width * 0.75)), height * 0.5, (width * 0.125 + ((leftThreshold / runway) * width * 0.75)), height * 0.075);
                gc.strokeLine(width * 0.125, height * 0.5, width * 0.125, height * 0.175);
                gc.strokeLine(((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) + ((leftLDA / runway) * width * 0.75), height * 0.5, ((width * 0.125) + ((leftThreshold / runway) * width * 0.75)) + ((leftLDA / runway) * width * 0.75), height * 0.075);
                gc.strokeLine(width * (0.125 + (0.75 * leftTORA / runway)), height * 0.5, width * (0.125 + (0.75 * leftTORA / runway)), height * 0.175);
                fillArrow(gc, (width * 0.125) + ((leftThreshold / runway) * width * 0.75), height * 0.075, (width * 0.125) + ((leftThreshold / runway) * width * 0.75) + ((leftLDA / runway) * width * 0.75), height * 0.075, "LDA : " + leftLDA);
                fillArrow(gc, width * 0.125, height * 0.175, width * (0.125 + (0.75 * leftASDA / runway)), height * 0.175, "TODA : " + leftTODA);
                fillArrow(gc, width * 0.125, height * 0.275, width * (0.125 + (0.75 * leftTODA / runway)), height * 0.275, "ASDA : " + leftASDA);
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
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.825, width * 0.085, height * 0.825, "ASDA : " + rightASDA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.925, width * 0.045, height * 0.925, "TODA : " + rightTODA);
                fillArrow(gc, width * (0.125 + (0.75 * rightLDA / runway)), height * 0.625, width * 0.125, height * 0.625, "LDA : " + rightLDA);
            } else if (rightStopway != 0) {
                gc.strokeLine(width * 0.085, height * 0.55, width * 0.085, height * 0.925);
                gc.strokeLine(width * 0.125, height * 0.55, width * 0.125, height * 0.725);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.725, width * 0.125, height * 0.725, "TORA : " + rightTORA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.825, width * 0.085, height * 0.825, "ASDA : " + rightASDA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.925, width * 0.085, height * 0.925, "TODA : " + rightTODA);
                fillArrow(gc, width * (0.125 + (0.75 * rightLDA / runway)), height * 0.625, width * 0.125, height * 0.625, "LDA : " + rightLDA);
            } else if (rightClearway != 0) {
                gc.strokeLine(width * 0.045, height * 0.55, width * 0.045, height * 0.925);
                gc.strokeLine(width * 0.125, height * 0.55, width * 0.125, height * 0.825);
                gc.strokeLine(width * 0.125, height * 0.55, width * 0.125, height * 0.825);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.725, width * 0.125, height * 0.725, "TORA : " + rightTORA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.825, width * 0.125, height * 0.825, "ASDA : " + rightASDA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.925, width * 0.045, height * 0.925, "TODA : " + rightTODA);
                fillArrow(gc, width * (0.125 + (0.75 * rightLDA / runway)), height * 0.625, width * 0.125, height * 0.625, "LDA : " + rightLDA);
            } else {
                gc.strokeLine(width * 0.125, height * 0.55, width * 0.125, height * 0.925);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.725, width * 0.125, height * 0.725, "TORA : " + rightTORA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.825, width * 0.125, height * 0.825, "ASDA : " + rightASDA);
                fillArrow(gc, width * (0.125 + (0.75 * rightTORA / runway)), height * 0.925, width * 0.125, height * 0.925, "TODA : " + rightTODA);
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
                fillArrow(gc, width * 0.875, height * 0.925, width * (0.875 - (0.75 * rightASDA / runway)), height * 0.925, "TODA : " + rightTODA);
                fillArrow(gc, width * 0.875, height * 0.825, width * (0.875 - (0.75 * rightTODA / runway)), height * 0.825, "ASDA : " + rightASDA);
                fillArrow(gc, width * 0.875, height * 0.725, width * (0.875 - (0.75 * rightTORA / runway)), height * 0.725, "TORA : " + rightTORA);
            }
            else {
                gc.strokeLine((width * 0.875) - ((rightThreshold / runway) * width * 0.75), height * 0.55, (width * 0.875) - ((rightThreshold / runway) * width * 0.75), height * 0.925);
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.825);
                gc.strokeLine((width * 0.875) - ((width * 0.75 * (rightLDA / runway)) + ((rightThreshold / runway) * width * 0.75)), height * 0.55, (width * 0.875) - ((width * 0.75 * (rightLDA / runway)) + ((rightThreshold / runway) * width * 0.75)), height * 0.925);
                gc.strokeLine(width * (0.875 - (0.75 * rightTORA / runway)), height * 0.55, width * (0.875 - (0.75 * rightTORA / runway)), height * 0.825);
                fillArrow(gc, (width * 0.875) - ((rightThreshold / runway) * width * 0.75) , height * 0.925, (width * 0.875) - ((width * 0.75 * (rightLDA / runway)) + ((rightThreshold / runway) * width * 0.75)), height * 0.925, "LDA : " + rightLDA);
                fillArrow(gc, width * 0.875, height * 0.825, width * (0.875 - (0.75 * rightASDA / runway)), height * 0.825, "TODA : " + rightTODA);
                fillArrow(gc, width * 0.875, height * 0.725, width * (0.875 - (0.75 * rightTODA / runway)), height * 0.725, "ASDA : " + rightASDA);
                fillArrow(gc, width * 0.875, height * 0.625, width * (0.875 - (0.75 * rightTORA / runway)), height * 0.625, "TORA : " + rightTORA);
            }

            //Left Away
            gc.strokeLine(width * (0.875 - (0.75 * leftTORA / runway)), height * 0.55, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.075);
            gc.strokeLine(width * (0.875 - (0.75 * leftLDA / runway)), height * 0.55, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.375);
            if (leftStopway != 0 && leftClearway != 0 && leftStopway != leftClearway) {
                gc.strokeLine(width * 0.915, height * 0.55, width * 0.915, height * 0.175);
                gc.strokeLine(width * 0.955, height * 0.55, width * 0.955, height * 0.075);
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.275);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.275, width * 0.875, height * 0.275, "TORA : " + leftTORA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.175, width * 0.915, height * 0.175, "ASDA : " + leftASDA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.075, width * 0.955, height * 0.075, "TODA : " + leftTODA);
                fillArrow(gc, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.375, width * 0.875, height * 0.375, "LDA : " + leftLDA);
            } else if (leftStopway != 0) {
                gc.strokeLine(width * 0.915, height * 0.55, width * 0.915, height * 0.075);
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.275);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.275, width * 0.875, height * 0.275, "TORA : " + leftTORA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.175, width * 0.915, height * 0.175, "ASDA : " + leftASDA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.075, width * 0.915, height * 0.075, "TODA : " + leftTODA);
                fillArrow(gc, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.375, width * 0.875, height * 0.375, "LDA : " + leftLDA);
            } else if (leftClearway != 0) {
                gc.strokeLine(width * 0.955, height * 0.55, width * 0.955, height * 0.075);
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.175);
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.175);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.275, width * 0.875, height * 0.275, "TORA : " + leftTORA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.175, width * 0.875, height * 0.175, "ASDA : " + leftASDA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.075, width * 0.045, height * 0.075, "TODA : " + leftTODA);
                fillArrow(gc, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.375, width * 0.875, height * 0.375, "LDA : " + leftLDA);
            } else {
                gc.strokeLine(width * 0.875, height * 0.55, width * 0.875, height * 0.075);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.275, width * 0.875, height * 0.275, "TORA : " + leftTORA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.175, width * 0.875, height * 0.175, "ASDA : " + leftASDA);
                fillArrow(gc, width * (0.875 - (0.75 * leftTORA / runway)), height * 0.075, width * 0.875, height * 0.075, "TODA : " + leftTODA);
                fillArrow(gc, width * (0.875 - (0.75 * leftLDA / runway)), height * 0.375, width * 0.875, height * 0.375, "LDA : " + leftLDA);
            }
        }
    }

    private void drawAnglesSideOn(GraphicsContext gc, int width, int height) {
        double obstacleFromLeft = Model.currentObstacle.getPosition().getDistanceToLeft();
        double obstacleFromRight = Model.currentObstacle.getPosition().getDistanceToRight();
        double runway = Model.currentRunway.getLeftRunway().getTORA();
        double leftThreshold = Model.currentRunway.getLeftRunway().getTORA() - Model.currentRunway.getLeftRunway().getLDA();
        double rightThreshold = Model.currentRunway.getRightRunway().getTORA() - Model.currentRunway.getRightRunway().getLDA();
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
            gc.strokeText("TOCS/ALS : " + (Model.recalculatedRunwayLeft.getTOCS_ALS()), obstacleLeft + 15, height * 0.425);
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
            gc.strokeText("TOCS/ALS : " + (Model.recalculatedRunwayRight.getTOCS_ALS()), obstacleRight - 85, height * 0.425);
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

    private void drawTopDown(Canvas canvas) {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Double scale = (width *3.26/100 ); //around 30*30 cm by default
        Double hscale = (height *6.55/100);

        gc.setFill(GRASS_COLOR);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(DARKBLUE_COLOR);
        double[] pointX = {scale*3,scale*9, scale*10, scale*22, scale*23, scale*27.6, scale*27.6, scale*23, scale*22, scale*10, scale*9, scale*3};
        double[] pointY = {hscale*5,hscale*5, hscale*4, hscale*4, hscale*5, hscale*5, hscale*11, hscale*11, hscale*12, hscale*12, hscale*11, hscale*11};
        gc.fillPolygon(pointX, pointY,12);

        //BASIC RUNWAY
        gc.setFill(RUNWAY_COLOR);
        gc.fillRect(scale*6, hscale*7, scale*19, hscale*2);
        Double d = 0.0;
        for(int i = 0;i <10;i ++){
            gc.setFill(Color.WHITE);
            gc.fillRect((scale)*(7),hscale*(7.3 + d), scale, hscale / 10);
            gc.fillRect((scale)*(23),hscale*(7.3 + d), scale, hscale / 10);
            d = (d + 0.15);
        }
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", scale-10));
        gc.fillText("Clear Graded Area", scale*13.5 , hscale * 5, 200);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(scale*(25/100));
        gc.setLineDashes(3);
        gc.strokeLine(scale*9.2 ,hscale*8.1, scale*21.7, hscale*8.1 );
        gc.setLineDashes(0);
    }

    public void drawRunway(Canvas canvas){
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Double scale = (width *3.26/100 );
        Double hscale = (height *6.55/100);

        //RUNWAY DETAILS:
        this.runway= Model.currentRunway;
        LogicalRunWay leftRunway = runway.getLeftRunway();
        LogicalRunWay RightRunway = runway.getRightRunway();

        drawSafeHeight(canvas,gc,scale,hscale);
        drawDistancesIndicator2(gc, scale,hscale, leftRunway,RightRunway);
       // drawDistances(gc,scale,hscale,leftRunway, RightRunway);
    }

    private void drawDistances(GraphicsContext gc, Double scale, Double hscale, LogicalRunWay leftRunway, LogicalRunWay RightRunway) {
        Integer LTODA = leftRunway.getTODA();
        Integer LTORA = leftRunway.getTORA();
        Integer LASDA = leftRunway.getASDA();
        Integer LLDA = leftRunway.getLDA();

        Integer RTODA = RightRunway.getTODA();
        Integer RTORA = RightRunway.getTORA();
        Integer RASDA = RightRunway.getASDA();
        Integer RLDA = RightRunway.getLDA();
        //Values indicators
        //TODA
        if(!Model.leftScreenController.calculateAllowed){
            gc.setFill(Color.BLACK);
            gc.fillText("TODA: " + LTODA + "m", scale*6, hscale*3.9); //left
            gc.fillText("TODA: " + RTODA + "m", scale*6, hscale*13.4);//right
            //ASDA UP
            gc.fillText("ASDA: " + LASDA + "m", scale*6.1, hscale*4.4);
            //ASDA DOWN
            gc.fillText("ASDA: " + RASDA + "m", scale*7, hscale*12.9);
            //TORA
            gc.fillText("TORA: " + LTORA + "m", scale*6.1, hscale*4.9);
            //TORA DOWN
            gc.fillText("TORA: " + RTORA + "m", scale*7, hscale*12.4);
            //LDA UP
            gc.fillText("LDA: " + LLDA + "m", scale*7, hscale*5.4);
            //LDA DOWN
            gc.fillText("LDA: " + RLDA + "m", scale*7, hscale*11.9);
            //draw landing directions
        }
        gc.fillText("LANDING - TAKEOFF DIRECTION", scale*3, hscale*3);
        gc.fillText("LANDING - TAKEOFF DIRECTION", scale*17.8 , hscale*14);
    }

    public void drawRunwayWithObstacle(Canvas canvas){
        this.runway = Model.currentRunway;
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        LogicalRunWay leftRunway = runway.getLeftRunway();
        LogicalRunWay RightRunway = runway.getRightRunway();
       /* if( RightRunway.getThreshold() != 0){
            if(RightRunway.getThreshold() < leftRunway.getThreshold()){
                leftRunway = runway.getRightRunway();
                RightRunway = runway.getLeftRunway();
            }
        }*/

        Double scale = (width *3.26/100 );
        Double hscale = (height *6.55/100);
        drawSafeHeight(canvas,gc,scale,hscale);
        drawDistancesIndicator2(gc, scale,hscale, leftRunway,RightRunway);
        //drawDistances(gc,scale,hscale,leftRunway, RightRunway);
        drawObstacle(gc, scale, hscale, runway);
    }

    public void drawRedeclaredRunway(Canvas canvas){

        this.runway = Model.currentRunway;
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        LogicalRunWay leftRunway = Model.recalculatedRunwayLeft;
        LogicalRunWay RightRunway = Model.recalculatedRunwayRight;

      /*  if( RightRunway.getThreshold() != 0){
            if(RightRunway.getThreshold() < leftRunway.getThreshold()){
                leftRunway = Model.recalculatedRunwayRight;
                RightRunway = Model.recalculatedRunwayLeft;
            }
        }*/

        Double scale = (width *3.26/100 );
        Double hscale = (height *6.55/100);
        drawSafeHeight(canvas,gc,scale,hscale);
        drawDistancesIndicator2(gc, scale,hscale, leftRunway,RightRunway);
        //drawDistances(gc,scale,hscale,leftRunway, RightRunway);
        drawObstacle(gc, scale, hscale, runway);

    }


    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2, Color c){
        gc.setFill(c);

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);

        Transform transform = Transform.translate(x1, y1);
        transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
        gc.setTransform(new Affine(transform));

        gc.strokeLine(0, 0, len, 0);
        gc.fillPolygon(new double[]{len, len - ARR_SIZE, len - ARR_SIZE, len}, new double[]{0, -ARR_SIZE, ARR_SIZE, 0},
                4);

        gc.setTransform(new Affine(transform.createConcatenation(Transform.rotate(Math.toDegrees(-angle), 0, 0))));
        transform = Transform.translate(0, 0);
        gc.setTransform(new Affine(transform.createConcatenation(Transform.rotate(Math.toDegrees(0), 0, 0))));
    }
    private void drawBArrow(GraphicsContext gc, double x1, double y1, double x2, double y2, Color C) {
        gc.setFill(C);

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);

        Transform transform = Transform.translate(x1, y1);
        transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
        gc.setTransform(new Affine(transform));

        gc.strokeLine(0, 0, len, 0);
        gc.fillPolygon(new double[]{len, len - ARR_SIZE2, len - ARR_SIZE2, len}, new double[]{0, -ARR_SIZE2, ARR_SIZE2, 0},
                4);
        gc.setTransform(new Affine(transform.createConcatenation(Transform.rotate(Math.toDegrees(-angle), 0, 0))));
        transform = Transform.translate(0, 0);
        gc.setTransform(new Affine(transform.createConcatenation(Transform.rotate(Math.toDegrees(0), 0, 0))));
    }
    //to draw safe height and a clear graded area
    private void drawSafeHeight(Canvas canvas,GraphicsContext gc, double scale, double hscale){
        gc.setStroke(Color.BLACK);
        gc.strokeRect(scale*3, hscale, canvas.getWidth() - (scale*6), canvas.getHeight() -(hscale*2));
        gc.setFill(GRASS_COLOR);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(DARKBLUE_COLOR);
        double[] pointX = {scale*3,scale*9, scale*10, scale*22, scale*23, scale*27.6, scale*27.6, scale*23, scale*22, scale*10, scale*9, scale*3};
        double[] pointY = {hscale*5,hscale*5, hscale*4, hscale*4, hscale*5, hscale*5, hscale*11, hscale*11, hscale*12, hscale*12, hscale*11, hscale*11};
        gc.fillPolygon(pointX, pointY,12);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", scale-10));
        gc.fillText("Clear Graded Area", scale*13.5 , hscale * 5, 200);
    }
    private void drawObstacle(GraphicsContext gc, double scale, double hscale, PhysicalRunWay rw){
        Position p = Model.currentObstacle.getPosition();
        double lengthR = Model.currentRunway.getLeftRunway().getTORA();
        double widthR = (scale*2)/ (Model.currentObstacle.getWidth());
        double scale1 = ((scale*19)/lengthR);
        double x = p.getDistanceToLeft() *scale1;
        double z = p.getDistanceToRight() *scale1 ;
        double y = p.getDistanceFromCL() *widthR;
        double width = Model.currentObstacle.getWidth() *scale1;
        double height = Model.currentObstacle.getHeight() *widthR;
        double r = RED_COLOR.getRed() *255;
        double b = RED_COLOR.getBlue()*255;
        double g = RED_COLOR.getGreen()*255;
        gc.setFill(Color.rgb((int)r,(int)g,(int)b,0.85));
        if (p.getDistanceFromCL().equals("R")){
            gc.fillRect((scale * 5) + x , (hscale * 8.1)+y, (scale*19)-z, height );
        }
        else{
            gc.fillRect((scale * 5) + x , (hscale * 8.1)-y, (scale*19)-z, height );
        }
//

    }
    protected void blueBlindness(){
        defaultColors();
        DARKBLUE_COLOR =  Color.rgb(71, 209, 71);
        ORANGE_COLOR = Color.PURPLE;
        SKY_COLOR = Color.DODGERBLUE;
        GRASS_COLOR = Color.CORNSILK;
        RED_COLOR = Color.RED;
        YELLOW_COLOR = Color.HOTPINK;
        setBackground();
        setColourBlindMode(ColourBlindMode.TRITANOPE);
        updateVisualisation(viewMode);
    }
    // Please set the colors for the updated color scheme, used in defaultColors(), sorry about this
    protected void redBlindness(){
        defaultColors();
        RED_COLOR = Color.rgb(64, 138, 255);
        ORANGE_COLOR = Color.DARKRED;
        SKY_COLOR = Color.YELLOW;
        YELLOW_COLOR = Color.rgb(59, 30, 176);
        DARKBLUE_COLOR = Color.rgb(245, 71, 225);
        GRASS_COLOR = Color.CORNSILK;
        setBackground();
        setColourBlindMode(ColourBlindMode.PROTONOPE);
        updateVisualisation(viewMode);
    }
    protected void greenBlindness(){
        defaultColors();
        SKY_COLOR = Color.YELLOW;
        RED_COLOR = Color.ORANGE;
        ORANGE_COLOR = Color.DEEPPINK;
        YELLOW_COLOR = Color.rgb(23, 124, 212);
        DARKBLUE_COLOR = Color.rgb(71, 209, 71);
        GRASS_COLOR = Color.CORNSILK;
        setBackground();
        setColourBlindMode(ColourBlindMode.DEUTERANOPE);
        updateVisualisation(viewMode);
    }
    protected void noBlindness(){
        defaultColors();
        setColourBlindMode(ColourBlindMode.DEFAULT);
        updateVisualisation(viewMode);
    }
    private void defaultColors(){
        GRASS_COLOR = Color.rgb(71, 209, 71);   //LIGHT GREEN
        DARKBLUE_COLOR = Color.rgb(0, 122, 238);   //MEDIUM BLUE
        RUNWAY_COLOR = Color.rgb(77, 77, 77);   //DARK GREY, probably doesn't need to be changed
        SKY_COLOR = Color.LIGHTSKYBLUE;
        RED_COLOR = Color.RED;
        ORANGE_COLOR = Color.ORANGE;
        YELLOW_COLOR = Color.YELLOW;
        BLUE_COLOR = Color.BLUE;
        PURPLE_COLOR = Color.PURPLE;
        setBackground();
    }
    private void drawRunwayRoad2(GraphicsContext gc, double scale, double hscale, LogicalRunWay left, LogicalRunWay right){
        gc.setFill(Color.rgb(255,255,255,0.5));
        gc.fillRect(scale*3, hscale*7, scale*24.5, hscale*2);
        gc.setFill(RUNWAY_COLOR);
        gc.fillRect(scale*6, hscale*7, scale*19, hscale*2);
        Double d = 0.0;
        for(int i = 0;i <10;i ++){
            gc.setFill(Color.WHITE);
            gc.fillRect((scale)*(7),hscale*(7.3 + d), scale, hscale / 10);
            gc.fillRect((scale)*(23),hscale*(7.3 + d), scale, hscale / 10);
            d = (d + 0.15);
        }

        gc.fillText(left.getName(),scale*8.5, hscale* 8.2);
        gc.setFill(Color.WHITE);
        gc.fillText(right.getName(),scale*22, hscale* 8.2);
        gc.setFill(Color.BLACK);
        gc.setLineWidth(scale*(25/100));
        gc.setLineDashes(3);
        gc.setStroke(Color.WHITE);
        gc.strokeLine(scale*9.2 ,hscale*8.1, scale*21.7, hscale*8.1 );
        gc.setLineDashes(0);

    }
    private void drawDistancesIndicator2(GraphicsContext gc, Double scale, Double hscale, LogicalRunWay left, LogicalRunWay right){

        double lStopway = left.getStopway();
        double lClearway = right.getClearway();
        double lThreshold = left.getThreshold();

        double rStopway = right.getStopway();
        double rClearway = left.getClearway();
        double rThreshold = right.getThreshold();



        double scale0 = scale*19 /right.getTORA();
        double rTODA = right.getTODA()*scale0;
        double rASDA = right.getASDA()*scale0;
        double rTORA = right.getTORA()*scale0;
        double rLDA = right.getLDA()*scale0;

        double scale1 = scale*19 /left.getTORA();
        double lTODA = left.getTODA()*scale1;
        double lASDA = left.getASDA()*scale1;
        double lTORA = left.getTORA()*scale1;
        double lLDA = left.getLDA()*scale1 ;



        if (rClearway != 0 ) {
            rTODA = rTODA - (rClearway*scale0) + hscale*2;
            gc.setFill(ORANGE_COLOR);
            gc.fillRect(scale*25, hscale*6, scale*2.5, hscale*4);
            gc.setStroke(Color.BLACK);
            gc.setFill(Color.BLACK);
            gc.fillText("Clear Way", scale*25.1 , hscale*9.6,80);

            if ((!Model.leftScreenController.calculateAllowed) && left.getTODA()> 0){
                gc.setLineWidth(1);
                gc.setStroke(Color.BLACK);
                fillArrow2(gc,(scale*6), hscale*4, (scale*8)+rTODA, hscale*4, "TODA:"+Double.toString(left.getTODA()) + "m"); // GREEN
                gc.strokeLine((scale*8)+rTODA, hscale*7, (scale*8)+rTODA, hscale*4);
            }
        }
        if (rClearway == 0 && ((!Model.leftScreenController.calculateAllowed)) && left.getTODA() > 0){
            gc.strokeLine((scale*6)+rTODA, hscale*7, (scale*6)+rTODA, hscale*4);
            fillArrow2(gc,scale*6, hscale*4, (scale*6)+rTODA, hscale*4,"TODA:"+Double.toString(left.getTODA()) + "m");
        }
        if (rStopway !=0){
            rASDA = rASDA -(rStopway*scale0) + scale;
            gc.setFill(Color.BLACK);
            gc.fillRect(scale*25, hscale*7, scale, hscale*2);
            gc.setFill(Color.WHITE);
            gc.fillText("Stop Way", scale*25 , hscale *8, 80);
            gc.setFill(Color.BLACK);
            if ((!Model.leftScreenController.calculateAllowed) && left.getASDA()> 0){
                System.out.println((scale*8)+rASDA);
                System.out.println(scale*26);
                gc.strokeLine((scale*6)+rASDA, hscale*7, (scale*6)+rASDA, hscale*4.5);
                fillArrow2(gc,(scale*6), hscale*7-(2.5*hscale), (scale*6)+rASDA, hscale*7-(2.5*hscale), "ASDA:"+Double.toString(left.getASDA()) + "m");// BLUE
            }
        }
        if (rStopway ==0 && ((!Model.leftScreenController.calculateAllowed)) && left.getASDA() > 0){
            gc.strokeLine((scale*6)+rASDA, hscale*7, (scale*6)+rASDA, hscale*4.5);
            fillArrow2(gc,scale*6, hscale*4.5, (scale*6)+rASDA, hscale*4.5, "ASDA:"+Double.toString(left.getASDA()) + "m");
        }

        if (lClearway != 0 ){
            lTODA = lTODA - (lClearway*scale0) + hscale*2.5;
            gc.setFill(ORANGE_COLOR);
            gc.fillRect(scale*4, hscale*6, scale*2.5, hscale*4);
            gc.setStroke(Color.BLACK);
            gc.setFill(Color.BLACK);
            gc.fillText("Clear Way", scale*3.1 , hscale*9.6,80);

            if ((!Model.leftScreenController.calculateAllowed) && right.getTODA()>0 ){
                gc.setLineWidth(1);
                gc.setStroke(Color.BLACK);
                fillArrow2(gc,scale*25, hscale*13.5, (scale*26)-lTODA, hscale*13.5,"TODA:"+Double.toString(right.getTODA()) + "m");
                gc.strokeLine((scale*26)-lTODA, hscale*10, (scale*26)-lTODA, hscale*13.5);
            }

        }
        if (lClearway == 0 && (!Model.leftScreenController.calculateAllowed) && right.getTODA()>0){
            gc.setLineWidth(1);
            gc.setStroke(Color.BLACK);
            fillArrow2(gc,(scale*25), hscale*13.5, (scale*25)-lTODA , hscale*13.5,"TODA:"+Double.toString(right.getTODA()) + "m");
            gc.strokeLine((scale*25)-lTODA , hscale*9, (scale*25)-lTODA , hscale*13.5);

        }
        if (lStopway != 0 ) {
            lASDA = lASDA -(lStopway*scale0) + scale;
            gc.setFill(Color.BLACK);
            gc.fillRect(scale * 5, hscale * 7, scale, hscale * 2);
            gc.setFill(Color.WHITE);
            gc.fillText("Stop Way", scale * 2, hscale * 8, 80);
            gc.setFill(Color.BLACK);
            if ((!Model.leftScreenController.calculateAllowed ) && right.getTODA() >0){
                gc.setLineWidth(1);
                gc.setStroke(Color.BLACK);
                gc.strokeLine((scale * 25) - lASDA, hscale * 9, (scale * 25) - lASDA, hscale * 13);
                fillArrow2(gc, (scale * 25), hscale * 13, (scale * 25) - lASDA, hscale * 13, "ASDA:"+Double.toString(right.getTODA()) + "m");
            }
        }
        if (lStopway ==0 && (!Model.leftScreenController.calculateAllowed) && right.getTODA() >0){
            gc.setLineWidth(1);
            gc.setStroke(Color.BLACK);
            fillArrow2(gc,scale*25, hscale*13, (scale*25) - lASDA, hscale*13,"ASDA:"+Double.toString(right.getTODA()) + "m");
            gc.strokeLine((scale*25) - lASDA, hscale*9, (scale*25) - lASDA, hscale*13);
        }
        drawRunwayRoad2(gc,scale,hscale,left,right);
        if (lThreshold !=0 ){
            lLDA =lLDA - (lThreshold*scale0) + (scale*1);
            gc.setFill(YELLOW_COLOR);
            gc.fillRect(scale*6, hscale*7, scale*1, hscale*2);
            gc.setStroke(Color.BLACK);
            drawArrow(gc,scale*6.5, hscale*10 ,scale*6.5,hscale*9,YELLOW_COLOR);
            gc.setFill(Color.BLACK);
            gc.fillText("Displaced Threshold", scale*6.5 , hscale*10.5, 200 );
            if ((!Model.leftScreenController.calculateAllowed) && left.getLDA() >0 ){
                gc.setStroke(Color.BLACK);
                fillArrow2(gc, (scale * 7), hscale * 7 - (1.5 * hscale), (scale * 10) + lLDA, hscale * 7 - (1.5 * hscale), "LDA:"+Double.toString(left.getLDA()) + "m"); //
                gc.strokeLine(scale * 7, hscale * 7, scale * 7, hscale * 5.5);

            }

        }
        if(lThreshold ==0 && (!Model.leftScreenController.calculateAllowed) && left.getLDA() >0){
            gc.setStroke(Color.BLACK);
            fillArrow2(gc,(scale*6), hscale*7-(1.5*hscale), (scale*8)+lLDA, hscale*7-(1.5*hscale), "LDA:"+Double.toString(left.getLDA()) + "m"); //
            gc.strokeLine(scale*6, hscale*7, scale*6, hscale*5.5);
        }

        if (rThreshold != 0 ){
            rLDA =rLDA - (rThreshold*scale0) + (scale*1);
            gc.setFill(YELLOW_COLOR);
            gc.fillRect(scale*24, hscale*7, scale*1, hscale*2);
            gc.setFill(Color.BLACK);
            drawArrow(gc,scale*24.5, hscale*10 ,scale*24.5,hscale*9,YELLOW_COLOR);
            gc.setFill(Color.BLACK);
            gc.fillText("Displaced Threshold", scale*24.5 , hscale*10.5, 200 );
            if ((!Model.leftScreenController.calculateAllowed) && right.getLDA() >0){
                gc.setLineWidth(1);
                gc.setStroke(Color.BLACK);
                gc.strokeLine(scale*24, hscale*9, scale*24, hscale*12);
                if (scale*24 -rLDA >= 0 ) {
                    System.out.println(right.getLDA());
                    System.out.println(rLDA);
                    System.out.println(scale*24 -rLDA);
                    fillArrow2(gc, (scale * 24), hscale * 10.5 + (1.5 * hscale), scale * 24 - rLDA, hscale * 10.5 + (1.5 * hscale), "LDA:" + Double.toString(right.getLDA()) + "m");
                    gc.strokeLine(scale*24 - rLDA, hscale*9, scale*24 - rLDA, hscale*12);
                }
                else fillArrow2(gc, (scale * 24), hscale * 10.5 + (1.5 * hscale), scale * 6, hscale * 10.5 + (1.5 * hscale), "LDA:" + Double.toString(right.getLDA()) + "m");

            }

        }
        if (rThreshold ==0 && (!Model.leftScreenController.calculateAllowed) && right.getLDA() >0){
            gc.setLineWidth(1);
            gc.setStroke(Color.BLACK);
            fillArrow2(gc,(scale*25), hscale*10.5+(1.5*hscale), (scale*24)-rLDA, hscale*10.5+(1.5*hscale),"LDA:"+Double.toString(right.getLDA()) + "m" );
            gc.strokeLine((scale*24)-rLDA, hscale*9, (scale*24)-rLDA, hscale*12);
        }


        //drawing tora and other indicators
        if (!Model.leftScreenController.calculateAllowed){
            gc.setLineWidth(1);
            gc.setStroke(Color.BLACK);
            gc.strokeLine(scale*6, hscale*9, scale*6, hscale*12.5);
            gc.strokeLine(scale*6, hscale*7, scale*6, hscale*4);
            gc.strokeLine(scale*25, hscale*9, scale*25, hscale*13.5);

            if (left.getTORA() >0 ) {
                fillArrow2(gc, (scale * 6), hscale * 5, (scale * 24), hscale * 5, "TORA:" + Double.toString(left.getTORA()) + "m");
                gc.strokeLine(scale*24, hscale*7, scale*24, hscale*5);
            }
            if (right.getTORA() >0 ) {
                fillArrow2(gc, (scale * 25), hscale * 12.5, scale*6, hscale * 12.5, "TORA:" + Double.toString(right.getTORA()) + "m");
            }
        }
        drawBArrow(gc,scale*3, hscale*2, scale*5, hscale*2, Color.BLACK);
        drawBArrow(gc,scale*27.7, hscale*13, scale*25.7, hscale*13, Color.BLACK);

        gc.fillText("LANDING - TAKEOFF DIRECTION", scale*3, hscale*3);
        gc.fillText("LANDING - TAKEOFF DIRECTION", scale*17.8 , hscale*14);

    }
    private void setBackground(){
        String skyHex = "#" + SKY_COLOR.toString().substring(2,SKY_COLOR.toString().length()-2);
        String grassHex = "#" + GRASS_COLOR.toString().substring(2,GRASS_COLOR.toString().length()-2);

        sideOnPane.setStyle("-fx-background-color: linear-gradient(" + skyHex+ " 0%, " + skyHex + " 50%, " + grassHex + " 50%, " + grassHex + " 100%);");
        topDownPane.setBackground(new Background(new BackgroundFill(GRASS_COLOR, null, null)));
    }

    public ViewMode getViewMode() {
        return viewMode;
    }

    public ColourBlindMode getColourBlindMode() {
        return colourBlindMode;
    }

    public void setColourBlindMode(ColourBlindMode colourBlindMode) {
        this.colourBlindMode = colourBlindMode;
    }

    private void fillArrow2(GraphicsContext gc, double x1, double y1, double x2, double y2, String label) {
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);

        if (x1 > x2) {
            gc.fillText(label, x2 + ((x1-x2) / 9), y1 - 2);
        }
        else {
            gc.fillText(label, x1 + ((x2 - x1) / 9), y1 - 2);
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