package main.controllers;

import javafx.beans.InvalidationListener;
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

public class CenterScreenController {

    public TextArea console;
    public Pane topDownPane;
    public Pane sideOnPane;
    public Canvas topDowncanvas;
    public Canvas sideOnCanvas;
    public PhysicalRunWay runway;
    private final int ARR_SIZE = 5;
    private TopDownView previousMode;

    private final InvalidationListener defaultListener = (obs) -> drawTopDown(topDowncanvas);
    private final InvalidationListener runwayListener = (obs) -> drawRunway(topDowncanvas);
    private final InvalidationListener redeclaredRunwayListener = (obs) -> drawRedeclaredRunway(topDowncanvas);

    @FXML
    private void initialize(){
        Model.centerScreenController = this;
        Model.console.update();
        setupTopDownCanvas();
        setupSideOnCanvas();
        previousMode = TopDownView.DEFAULT;
        updateVisualisation(TopDownView.DEFAULT);
    }

    private void setupTopDownCanvas(){
        topDowncanvas = new Canvas();
        topDownPane.getChildren().add(topDowncanvas);
        topDowncanvas.widthProperty().bind(topDownPane.widthProperty());
        topDowncanvas.heightProperty().bind(topDownPane.heightProperty());
    }

    private void setupSideOnCanvas(){
        sideOnCanvas = new Canvas();
        sideOnPane.getChildren().add(sideOnCanvas);
        sideOnCanvas.widthProperty().bind(sideOnPane.widthProperty());
        sideOnCanvas.heightProperty().bind(sideOnPane.heightProperty());
    }

    //Dont remove this please
    public void updateConsole(String text){
//        console.setText(text);
//        console.positionCaret(text.length());
        console.appendText(text + "\n");
    }

    public void updateVisualisation(TopDownView newMode) {
        topDownPane.getChildren().remove(topDowncanvas);
        setupTopDownCanvas();
        switch (previousMode) {
            case DEFAULT -> {
                topDowncanvas.widthProperty().removeListener(defaultListener);
                topDowncanvas.heightProperty().removeListener(defaultListener);
            }
            case RUNWAY -> {
                topDowncanvas.widthProperty().removeListener(runwayListener);
                topDowncanvas.heightProperty().removeListener(runwayListener);
            }
            case REDECLAREDRUNWAY -> {
                topDowncanvas.widthProperty().removeListener(redeclaredRunwayListener);
                topDowncanvas.heightProperty().removeListener(redeclaredRunwayListener);
            }
        }

        switch (newMode) {
            case DEFAULT -> {
                topDowncanvas.widthProperty().addListener(defaultListener);
                topDowncanvas.heightProperty().addListener(defaultListener);
                drawTopDown(topDowncanvas);
            }
            case RUNWAY -> {
                topDowncanvas.widthProperty().addListener(runwayListener);
                topDowncanvas.heightProperty().addListener(runwayListener);
                drawRunway(topDowncanvas);
            }
            case REDECLAREDRUNWAY -> {
                topDowncanvas.widthProperty().addListener(redeclaredRunwayListener);
                topDowncanvas.heightProperty().addListener(redeclaredRunwayListener);
                drawRedeclaredRunway(topDowncanvas);
            }
        }
        previousMode = newMode;
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

        // i think i need to add height scale too
       // Double scale = (canvas.getWidth() *3.26/100 );
        // background
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        //safe height

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
        gc.setFill(Color.GRAY);
        gc.fillRect(scale*6, hscale*7, scale*19, hscale*2);
        Double d = 0.0;
        for(int i = 0;i <10;i ++){
            gc.setFill(Color.WHITE);
            gc.fillRect((scale)*(7),hscale*(7.3 + d), scale, hscale / 10);
            gc.fillRect((scale)*(23),hscale*(7.3 + d), scale, hscale / 10);
            d = (d + 0.15);
        }
        //gc.setLineWidth(50);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", scale-10));
        gc.fillText("Clear Graded Area", scale*12 , hscale * 5, 200);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(scale*(25/100));
        //change line type to be dashed
        //gc.setLineDashes(3);
        gc.strokeLine(scale*9.2 ,hscale*8.1, scale*21.7, hscale*8.1 );

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

    public void drawRunway(Canvas canvas){
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        System.out.println(width);
        System.out.println(height);
        this.runway= Model.currentRunway;
        int lDegree = runway.getLeftRunway().getDegree();
        int rDegree = runway.getRightRunway().getDegree();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // i think i need to add height scale too
        Double scale = (canvas.getWidth() *3.26/100 );
        // background
        gc.setFill(Color.LIGHTSKYBLUE);
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
        gc.setFont(new Font("Arial", scale-10));
        gc.fillText("Clear Graded Area", scale*12 , scale * 5, 200);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(scale*(25/100));
        gc.setLineDashes(3);
        gc.strokeLine(scale*9.2 ,scale*8.1, scale*21.7, scale*8.1 );


    }


    public void drawRedeclaredRunway(Canvas canvas){

        this.runway = Model.currentRunway;
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
        GraphicsContext gc = canvas.getGraphicsContext2D();


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
        //TODO: check if this the correct way to do it
        int thershold;
        if (lDegree <= rDegree) {
            thershold = lDegree;
        }else thershold = rDegree;

        //road
        gc.setFill(Color.GRAY);
        gc.fillRect(scale*6, hscale*7, scale*19, hscale*2);

        //thershold
        //TODO: DRAW THERSHOLD INDICATOR
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
        //TODO: STROKE TAKE OFF DIRECTIONS LINES
        double TODA = scale*21.5;
        double ASDA =scale*20.1;
        double TORA = scale*19;
        double LDA = scale+70;
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        //todo:: chnage lda according to the thershold if there's one
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
        drawArrow(gc,(scale*25), hscale*10.5-(0.25*hscale), (scale*25)-TODA, hscale*10.5-(0.25*hscale));
    }

    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        gc.setFill(Color.BLACK);

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);

        Transform transform = Transform.translate(x1, y1);
        transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
        gc.setTransform(new Affine(transform));

        gc.strokeLine(0, 0, len, 0);
        gc.fillPolygon(new double[]{len, len - ARR_SIZE, len - ARR_SIZE, len}, new double[]{0, -ARR_SIZE, ARR_SIZE, 0},
                4);
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
    }

    private void drawStopway(GraphicsContext gc, double scale, double hscale, Color c){
        gc.setFill(c);
        gc.fillRect(scale*5, hscale*7, scale, hscale*2);
        gc.fillRect(scale*25, hscale*7, scale, hscale*2);
        gc.setFill(Color.WHITE);
        // gc.rotate(90);
        //TODO:Rotate the text
        gc.fillText("Stop Way", scale*24.6 , hscale *8, 200);
        gc.fillText("Stop Way", scale*4.6 , hscale *8, 200);

    }

    private void drawClearway(GraphicsContext gc, double scale, double hscale, Color c){
        gc.setStroke(c);
        gc.strokeRect(scale*25, hscale*6, scale*2.6, hscale*4);
        gc.setFill(Color.WHITE);
        gc.fillText("Clear Way", scale*26 , hscale*9.6, 200);


    }

    private final int ARR_SIZE2 = 35;
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
    }



}
