package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import main.model.LogicalRunWay;
import main.model.Model;
import main.model.PhysicalRunWay;
import main.model.Position;

public class CenterScreenController {

    public TextArea console;
    public Pane topDownPane;
    public Pane sideOnPane;
    public Canvas topDowncanvas;
    public Canvas sideOnCanvas;
    public PhysicalRunWay runway;
    private final int ARR_SIZE = 5;
    private TopDownView topDownViewMode;

    @FXML
    private void initialize(){
        Model.centerScreenController = this;
        Model.console.update();
        setupTopDownCanvas();
        setupSideOnCanvas();
        topDownViewMode = TopDownView.DEFAULT;
    }

    private void setupTopDownCanvas(){
        topDowncanvas = new Canvas();
        topDownPane.getChildren().add(topDowncanvas);
        topDowncanvas.widthProperty().bind(topDownPane.widthProperty());
        topDowncanvas.heightProperty().bind(topDownPane.heightProperty());
        topDowncanvas.widthProperty().addListener(event -> draw());
        topDowncanvas.heightProperty().addListener(event -> draw());
    }

    private void setupSideOnCanvas(){
        sideOnCanvas = new Canvas();
        sideOnPane.getChildren().add(sideOnCanvas);
        sideOnCanvas.widthProperty().bind(sideOnPane.widthProperty());
        sideOnCanvas.heightProperty().bind(sideOnPane.heightProperty());
    }

    public void updateConsole(String text){
//        console.setText(text);
//        console.positionCaret(text.length());
        console.appendText(text + "\n");
    }

    public void updateVisualisation(TopDownView mode) {
        topDownViewMode = mode;
        topDownPane.getChildren().remove(topDowncanvas);
        setupTopDownCanvas();
        draw();
    }

    private void draw() {
        switch (topDownViewMode) {
            case DEFAULT -> drawTopDown(topDowncanvas);
            case RUNWAY -> drawRunway(topDowncanvas);
            //TODO: when an obstacle is placed, the calculations are not shown, only the placed obstacle in the correct position
            case REDECLAREDRUNWAY -> drawRunwayWithObstacle(topDowncanvas);
            //TODO: there should be a mode when a calculate button is clicked, showing the arrows and the calculated values
            case CALCULATIONS_RUNWAY -> drawRedeclaredRunway(topDowncanvas);
        }
    }

    private void drawTopDown(Canvas canvas) {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,width,height);

        //scaling the figures out
        Double scale = (width *3.26/100 ); //around 30*30 cm by default
        Double hscale = (height *6.55/100);

        //safe height & clear graded area
        gc.setStroke(Color.BLACK);
        gc.strokeRect(scale*3, hscale, canvas.getWidth() - (scale*6), canvas.getHeight() -(hscale*2));
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(scale*3, hscale, canvas.getWidth() - (scale*6), canvas.getHeight() -(hscale*2));
        gc.setFill(Color.TAN);
        double[] pointX = {scale*3,scale*9, scale*10, scale*22, scale*23, scale*27.6, scale*27.6, scale*23, scale*22, scale*10, scale*9, scale*3};
        double[] pointY = {hscale*5,hscale*5, hscale*4, hscale*4, hscale*5, hscale*5, hscale*11, hscale*11, hscale*12, hscale*12, hscale*11, hscale*11};
        gc.fillPolygon(pointX, pointY,12);

        //BASIC RUNWAY
        gc.setFill(Color.GRAY);
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
        gc.fillText("Clear Graded Area", scale*12 , hscale * 5, 200);
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
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,width,height);
        Double scale = (width *3.26/100 );
        Double hscale = (height *6.55/100);

        //RUNWAY DETAILS:
        this.runway= Model.currentRunway;
        LogicalRunWay leftRunway = runway.getLeftRunway();
        LogicalRunWay RightRunway = runway.getRightRunway();

        int lDegree = leftRunway .getDegree();
        int rDegree = RightRunway.getDegree();

        Integer theH = leftRunway.getThreshold();
        Integer RThershold = RightRunway.getThreshold();

        Integer Lstopway = leftRunway.getStopway();
        Integer Rstopway = RightRunway.getStopway();

        Integer Rclearway = RightRunway.getClearway();
        Integer Lclearway = leftRunway.getClearway();

        drawSafeHeight(canvas,gc,scale,hscale);
        drawClearway(gc,scale,hscale,Color.YELLOWGREEN);
        drawRunwayRoad(gc,scale,hscale,lDegree,rDegree);
        drawStopway(gc,scale,hscale,Color.BLACK);
        drawDistances(gc,scale,hscale,leftRunway, RightRunway);
        drawDistancesIndicator(gc,scale,hscale);


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
        gc.fillText("LANDING - TAKEOFF DIRECTION", scale*3, hscale*3);
        gc.fillText("LANDING - TAKEOFF DIRECTION", scale*19.8 , hscale*14);
        if (leftRunway.getThreshold() != 0){
            gc.setFill(Color.LIGHTGREY);
            gc.fillRect(scale*6, hscale*7, scale*1, hscale*2);
        }

    }

    public void drawRunwayWithObstacle(Canvas canvas){
        this.runway = Model.currentRunway;
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,width,height);
        LogicalRunWay leftRunway = runway.getLeftRunway();
        LogicalRunWay RightRunway = runway.getRightRunway();
        if( RightRunway.getThreshold() != 0){
            if(RightRunway.getThreshold() < leftRunway.getThreshold()){
                leftRunway = runway.getRightRunway();
                RightRunway = runway.getLeftRunway();
            }
        }

        int lDegree = leftRunway .getDegree();
        int rDegree = RightRunway.getDegree();


        Double scale = (width *3.26/100 );
        Double hscale = (height *6.55/100);
        drawSafeHeight(canvas,gc,scale,hscale);
        drawClearway(gc,scale,hscale,Color.YELLOWGREEN);
        drawRunwayRoad(gc,scale,hscale,lDegree,rDegree);
        drawStopway(gc,scale,hscale,Color.BLACK);
        drawDistances(gc,scale,hscale,leftRunway, RightRunway);
        drawDistancesIndicator(gc,scale,hscale);
        drawObstacle(gc, scale, hscale, runway);
    }

    public void drawRedeclaredRunway(Canvas canvas){

        this.runway = Model.currentRunway;
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,width,height);
        LogicalRunWay leftRunway = Model.recalculatedRunwayAway;
        LogicalRunWay RightRunway = Model.recalculatedRunwayTowards;

        if( RightRunway.getThreshold() != 0){
            if(RightRunway.getThreshold() < leftRunway.getThreshold()){
                leftRunway = Model.recalculatedRunwayTowards;
                RightRunway = Model.recalculatedRunwayAway;
            }
        }

        int lDegree = leftRunway .getDegree();
        int rDegree = RightRunway.getDegree();


        Double scale = (width *3.26/100 );
        Double hscale = (height *6.55/100);
        drawSafeHeight(canvas,gc,scale,hscale);
        drawClearway(gc,scale,hscale,Color.YELLOWGREEN);
        drawRunwayRoad(gc,scale,hscale,lDegree,rDegree);
        drawStopway(gc,scale,hscale,Color.BLACK);
        drawDistances(gc,scale,hscale,leftRunway, RightRunway);
        drawDistancesIndicator(gc,scale,hscale);
        drawObstacle(gc, scale, hscale, runway);

      /*
        Integer LTODA = leftRunway.getTODA();
        Integer LTORA = leftRunway.getTORA();
        Integer LASDA = leftRunway.getASDA();
        Integer LLDA = leftRunway.getLDA();

        Integer RTODA = RightRunway.getTODA();
        Integer RTORA = RightRunway.getTORA();
        Integer RASDA = RightRunway.getASDA();
        Integer RLDA = RightRunway.getLDA();


        // background
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(0,0,width,height);


        Double scale = (width *3.26/100 );
        Double hscale = (height *6.55/100);

        //safe height & clear graded area
        gc.setStroke(Color.BLACK);
        gc.strokeRect(scale*3, hscale, canvas.getWidth() - (scale*6), canvas.getHeight() -(hscale*2));
        gc.setFill(Color.WHEAT);
        gc.fillRect(scale*3, hscale, canvas.getWidth() - (scale*6), canvas.getHeight() -(hscale*2));

        gc.setFill(Color.TAN);
        double[] pointX = {scale*3,scale*9, scale*10, scale*22, scale*23, scale*27.6, scale*27.6, scale*23, scale*22, scale*10, scale*9, scale*3};
        double[] pointY = {hscale*5,hscale*5, hscale*4, hscale*4, hscale*5, hscale*5, hscale*11, hscale*11, hscale*12, hscale*12, hscale*11, hscale*11};
        gc.fillPolygon(pointX, pointY,12);

        //runway

        int thershold;
        if (lDegree <= rDegree) {
            thershold = lDegree;
        }else thershold = rDegree;

        //road
        gc.setFill(Color.GRAY);
        gc.fillRect(scale*6, hscale*7, scale*19, hscale*2);

        //thershold

        if (theH != null){
            theH = 70;
            gc.setFill(Color.TEAL);
            gc.fillRect((scale*6), hscale*7, scale*1, hscale*2);
        }
        //stopways
        if(Lstopway != null || Rstopway != null){
            drawStopway(gc, scale, hscale, Color.BLACK);
        }

        //clearways
        if(Rclearway != null){
            drawClearway(gc, scale ,hscale,Color.BLACK );
        }


        //Runway road strips
        Double d = 0.0;
        for(int i = 0;i <10;i ++){
            gc.setFill(Color.WHITE);
            gc.fillRect((scale)*(7),hscale*(7.3 + d), scale, hscale / 10);
            gc.fillRect((scale)*(23),hscale*(7.3 + d), scale, hscale / 10);
            d = (d + 0.15);
        }

        //Runways names
        gc.fillText(String.valueOf(lDegree) + "L",scale*8.5, hscale* 8.2);
        gc.setFill(Color.WHITE);
        gc.fillText(String.valueOf(rDegree) + "R",scale*22, hscale* 8.2);
        gc.setFill(Color.BLACK);
        gc.fillText("Clear Graded Area", scale*12 , hscale * 5, 200);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(scale*(25/100));
        gc.setLineDashes(3);
        gc.strokeLine(scale*9.2 ,hscale*8.1, scale*21.7, hscale*8.1 );
        gc.setLineDashes(0);

        //drawing values
        double TODA = scale*21.5;
        double ASDA =scale*20.1;
        double TORA = scale*19;
        double LDA = scale+70;
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);

        drawArrow(gc,(scale*6), hscale*7-(1.5*hscale), (scale*6)+TODA, hscale*7-(1.5*hscale), Color.GREEN);
        drawArrow(gc,(scale*6), hscale*7-(1.25*hscale), (scale*6)+ASDA, hscale*7-(1.25*hscale), Color.BLUE);
        drawArrow(gc,(scale*6), hscale*7-(0.75*hscale), (scale*6)+TORA, hscale*7-(0.75*hscale), Color.RED);
        drawArrow(gc,(scale*7), hscale*7-(0.25*hscale), (scale*6)+LDA, hscale*7-(0.25*hscale), Color.PURPLE);


        gc.strokeLine(scale*-2, hscale*5.3, scale*-2, hscale*2.3);
        //TODA imdecator
        gc.strokeLine(scale*-1, hscale*5, scale*-1, hscale*2.3);
        //UPLeft
        //gc.setStroke(Color.RED);
        gc.strokeLine(scale*0, hscale*0.5, scale*0, hscale*0);
        //LDA indecator
        gc.strokeLine(scale*-1, hscale*0.5, scale*-1, hscale*-1.3);
        //downRight
        gc.strokeLine(scale*18, hscale*5.3, scale*18, hscale*2.3);
        //upRight
        //red
        gc.setStroke(Color.RED);
        //red
        gc.strokeLine(scale*18, hscale*0.2, scale*18, hscale*-0.5);
        //blue
        gc.strokeLine(scale*19, hscale*0.2, scale*19, hscale*-1);
        //green
        gc.strokeLine(scale*20.6, hscale*0.2, scale*20.6, hscale*-1.3);

        //thersold inedcator
        drawArrow(gc,scale*7, hscale*10 ,scale*7,hscale*9,Color.RED);
        gc.fillText("Displaced Threshold", scale , hscale*-0.5 );


        //take off direction values
        //gc.fillRect(scale*6, scale*7, scale*19, scale*2);
        double TODAT= scale*19;
        double ASDAT =scale*20.1;
        double TORAT = scale*19;
        double LDAT = scale;
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        drawArrow(gc,(scale*25), hscale*10.5-(hscale), (scale*25)-LDA, hscale*10.5-(hscale));
        drawArrow(gc,(scale*25), hscale*10.5-(0.75*hscale), (scale*25)-TORA, hscale*10.5-(0.75*hscale));
        drawArrow(gc,(scale*25), hscale*10.5-(0.5*hscale), (scale*25)-ASDA, hscale*10.5-(0.5*hscale));
        drawArrow(gc,(scale*25), hscale*10.5-(0.25*hscale), (scale*25)-TODA, hscale*10.5-(0.25*hscale)); */
    }

    private void drawDistancesIndicator(GraphicsContext gc, Double scale, Double hscale){
        double TODA = scale*21.5;
        double ASDA =scale*20.1;
        double TORA = scale*19;
        double LDA = scale*18;
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        drawArrow(gc,(scale*6), hscale*7-(3*hscale), (scale*6)+TODA, hscale*7-(3*hscale), Color.GREEN); // GREEN
        drawArrow(gc,(scale*6), hscale*7-(2.5*hscale), (scale*6)+ASDA, hscale*7-(2.5*hscale), Color.BLUE);// BLUE
        drawArrow(gc,(scale*6), hscale*7-(2*hscale), (scale*6)+TORA, hscale*7-(2*hscale), Color.RED); // RED LDA
        drawArrow(gc,(scale*7), hscale*7-(1.5*hscale), (scale*7)+LDA, hscale*7-(1.5*hscale), Color.PURPLE); //
        //indicators
        //downLeft
        gc.strokeLine(scale*6, hscale*9, scale*6, hscale*13);
        //TODA
        gc.strokeLine(scale*5, hscale*9, scale*5, hscale*13.5);
        //UPLeft
        gc.strokeLine(scale*6, hscale*7, scale*6, hscale*4);
        //LDA
        gc.strokeLine(scale*7, hscale*7, scale*7, hscale*5.5);
        //downRight
        gc.strokeLine(scale*25, hscale*9, scale*25, hscale*13.5);
        //upRight
        //red
        //red
        gc.strokeLine(scale*25, hscale*7, scale*25, hscale*5);
        //blue
        gc.strokeLine(scale*26, hscale*7, scale*26, hscale*4.5);
        //green
        gc.strokeLine(scale*27.5, hscale*7, scale*27.5, hscale*4);
        //take off direction values
        //gc.fillRect(scale*6, scale*7, scale*19, scale*2);
        double TODAT= scale*19;
        double ASDAT =scale*20.1;
        double TORAT = scale*19;
        double LDAT = scale;
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        drawArrow(gc,(scale*25), hscale*10.5+(1.5*hscale), (scale*25)-TORA, hscale*10.5+(1.5*hscale),Color.PURPLE );
        drawArrow(gc,(scale*25), hscale*10.5+(2*hscale), (scale*25)-TORA, hscale*10.5+(2*hscale),Color.RED );
        drawArrow(gc,(scale*25), hscale*10.5+(2.5*hscale), (scale*25)-TORA, hscale*10.5+(2.5*hscale),Color.BLUE);
        drawArrow(gc,(scale*25), hscale*10.5+(3*hscale), (scale*25)-ASDA, hscale*10.5+(3*hscale),Color.GREEN );

        drawBArrow(gc,scale*3, hscale*2, scale*5, hscale*2, Color.BLACK);
        drawBArrow(gc,scale*27.7, hscale*13, scale*25.7, hscale*13, Color.BLACK);
        //threshold indicator
        drawArrow(gc,scale*6.5, hscale*10 ,scale*6.5,hscale*9,Color.RED);
        gc.fillText("Displaced Threshold", scale*6.5 , hscale*10.5, 70 );
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

    private void drawStopway(GraphicsContext gc, double scale, double hscale, Color c){
        gc.setFill(c);
        gc.fillRect(scale*5, hscale*7, scale, hscale*2);
        gc.fillRect(scale*25, hscale*7, scale, hscale*2);
        gc.setFill(Color.WHITE);
        gc.fillText("Stop Way", scale*24.6 , hscale *8, 80);
        gc.fillText("Stop Way", scale*3.8 , hscale *8, 80);

    }

    private void drawClearway(GraphicsContext gc, double scale, double hscale, Color c){
        gc.setFill(c);
        gc.fillRect(scale*25, hscale*6, scale*2.5, hscale*4);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        gc.fillText("Clear Way", scale*25.1 , hscale*9.6,80);
    }

    private final int ARR_SIZE2 = 20;

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
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(scale*3, hscale, canvas.getWidth() - (scale*6), canvas.getHeight() -(hscale*2));
        gc.setFill(Color.TAN);
        double[] pointX = {scale*3,scale*9, scale*10, scale*22, scale*23, scale*27.6, scale*27.6, scale*23, scale*22, scale*10, scale*9, scale*3};
        double[] pointY = {hscale*5,hscale*5, hscale*4, hscale*4, hscale*5, hscale*5, hscale*11, hscale*11, hscale*12, hscale*12, hscale*11, hscale*11};
        gc.fillPolygon(pointX, pointY,12);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 0.5*scale));
        gc.fillText("Clear Graded Area", scale*12 , hscale * 5, 200);
    }

    private void drawRunwayRoad(GraphicsContext gc, double scale, double hscale, Integer lDegree, Integer rDegree){
        gc.setFill(Color.GRAY);
        gc.fillRect(scale*6, hscale*7, scale*19, hscale*2);
        Double d = 0.0;
        for(int i = 0;i <10;i ++){
            gc.setFill(Color.WHITE);
            gc.fillRect((scale)*(7),hscale*(7.3 + d), scale, hscale / 10);
            gc.fillRect((scale)*(23),hscale*(7.3 + d), scale, hscale / 10);
            d = (d + 0.15);
        }

        gc.fillText(String.valueOf(lDegree) +"L",scale*8.5, hscale* 8.2);
        gc.setFill(Color.WHITE);
        gc.fillText(String.valueOf(rDegree) + "R",scale*22, hscale* 8.2);
        gc.setFill(Color.BLACK);
        //gc.setFont(new Font("Arial", scale-10));
        gc.setLineWidth(scale*(25/100));
        gc.setLineDashes(3);
        gc.setStroke(Color.WHITE);
        gc.strokeLine(scale*9.2 ,hscale*8.1, scale*21.7, hscale*8.1 );
        gc.setLineDashes(0);

    }

    private void drawObstacle(GraphicsContext gc, double scale, double hscale, PhysicalRunWay runway){
        Position p = runway.getObstacle().getPosition();
        double x = p.getDistanceToLeft();
        double y = p.getDistanceFromCL();
        double width = runway.getObstacle().getWidth();
        double height = runway.getObstacle().getHeight();
        gc.setFill(Color.DARKRED);
        //centre lines
        //gc.strokeLine(scale*9.2 ,hscale*8.1, scale*21.7, hscale*8.1 );
        gc.fillRect((scale*9.2)+ x,(hscale*8.1)- y, width*5,height*5);

    }


}
