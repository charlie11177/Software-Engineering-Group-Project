package controllers;

import app.App;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import model.*;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class MainWindowController {

    public CustomMenuItem savePDF;
    public CustomMenuItem savePNGSideOn;
    public CustomMenuItem savePNGTopDown;
    @FXML private MenuBar menuBar;

    private XMLParser xmlParser;

    public MainWindowController() {
        xmlParser = new XMLParser();
    }

    @FXML private void initialize() {
        Label lb= new Label("Menu Text");
        MenuItem myMenuItem = new MenuItem(null, lb);
        Tooltip tips = new Tooltip("Your tip text here");
        Tooltip.install(myMenuItem.getGraphic(), tips);
        Model.mainWindowController = this;
        Tooltip.install(savePDF.getContent(), new Tooltip("Calculations have to be performed before saving them as a PDF"));
        Tooltip.install(savePNGSideOn.getContent(), new Tooltip("Calculations have to be performed before saving the Recalculated views"));
        Tooltip.install(savePNGTopDown.getContent(), new Tooltip("Calculations have to be performed before saving the Recalculated views"));
//        Tooltip.install(savePNGSideOn.getGraphic(), new Tooltip("Calculations have to be performed before saving the Recalculated views"));
    }

    @FXML private void importXML (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files" , "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;
        Model.console.addLog("--- Importing Configuration from: " + xmlFile.getName() + " ---" );
        try {
            ArrayList<Airport> importedAirports = xmlParser.importAirports(xmlFile);
            ArrayList<Obstacle> importedObstacles = xmlParser.importObstacle(xmlFile);

            if (!importedAirports.isEmpty())
            {
                Model.airports = importedAirports;
                Model.console.addLogWithoutTime("--- Imported Airports and Runways ---");
                for (Airport a : Model.airports) {
                    Model.console.addLogWithoutTime(a.toString());
                    Model.console.addLogWithoutTime(a.getRunways().toString());
                }
            } else {
                Model.console.addLogWithoutTime("--- No Airports were found to import ---");
                Model.airports.clear();
            }
            Model.console.addLogWithoutTime("--- Finished Importing Airports ---");

            Model.console.addLogWithoutTime("--- Imported Obstacles ---");
            if (!importedObstacles.isEmpty())
            {
                Model.obstacles = importedObstacles;
                for (Obstacle o : Model.obstacles) {
                    Model.console.addLogWithoutTime(o.getName());
                }
            } else {
                Model.console.addLogWithoutTime("--- No Obstacles were found to import ---");
                Model.obstacles.clear();
            }
            if(importedObstacles.isEmpty() && importedAirports.isEmpty()) {
                throw (new Exception("Failed import"));
            }
            Model.console.addLogWithoutTime("--- Finished Importing Obstacles ---");
//            Model.console.addLogWithoutTime("Imported " + importedAirports.size() + " Airports");
//            Model.console.addLogWithoutTime("Imported " + importedObstacles.size() + " Obstacles");
            Model.console.addLog("--- Finished importing Configuration from: " + xmlFile.getName() + " ---" );
            resetMenus();   // whole UI has to be reset and user actions removed, since the data is rewritten
        }
        catch (Exception e) {
            Model.console.addLog("Failed an import - Invalid Configuration XML selected");
            AlertController.showErrorAlert("Import Failed - Invalid Configuration XML selected","Make sure that you have chosen the correct XML file");
        }
    }

    public void importAirports(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files" , "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;
        Model.console.addLog("--- Importing Airports from: " + xmlFile.getName() + " ---" );
        try {
            ArrayList<Airport> importedAirports = xmlParser.importAirports(xmlFile);
            if (!importedAirports.isEmpty()) {
                for(Airport a1 : Model.airports) {
                    importedAirports.removeIf(a2 -> a1.getName().equals(a2.getName()) || a1.getCode().equals(a2.getCode()));
                }
                Model.airports.addAll(importedAirports);
                if(importedAirports.isEmpty())
                    Model.console.addLogWithoutTime("--- No unique airports were found, no data was imported ---");
                else {
                    Model.console.addLogWithoutTime("--- Imported Airports and Runways ---");
                    for (Airport a : importedAirports) {
                        Model.console.addLogWithoutTime(a.toString());
                        Model.console.addLogWithoutTime(a.getRunways().toString());
                    }
                }
                Model.console.addLogWithoutTime("--- Finished importing airports from: " + xmlFile.getName() + " ---" );
//                resetMenus(); // doesnt need to call this since it only appends data
                updateUI();
            } else {
                throw (new Exception("Failed import"));
            }
        }  catch (Exception e) {
            Model.console.addLog("Failed an import - Invalid Airports XML selected");
            AlertController.showErrorAlert("Import Failed - Invalid Airports XML selected","Make sure that you have chosen the correct XML file");
        }
    }

    public void importObstacles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files" , "*.xml"));
        File xmlFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;
        Model.console.addLog("--- Importing Obstacles from: " + xmlFile.getName() + " ---" );
        try {
            ArrayList<Obstacle> importedObstacles = xmlParser.importObstacle(xmlFile);
            if (!importedObstacles.isEmpty()) {
                //Model.resetConfig();
                for(Obstacle o1 : Model.obstacles) {
                    importedObstacles.removeIf(o2 -> o1.getName().equals(o2.getName()));
                }
                Model.obstacles.addAll(importedObstacles);
                if(importedObstacles.isEmpty())
                    Model.console.addLogWithoutTime("--- No unique obstacles were found, no data was imported ---");
                else {
                    Model.console.addLogWithoutTime("--- Imported Objects ---");
                    for (Obstacle o : importedObstacles) {
                        Model.console.addLogWithoutTime(o.getName());
                    }
                }
                Model.console.addLog("--- Finished importing Obstacles from: " + xmlFile.getName() + " ---" );
//                resetMenus(); // doesnt need to call this since it only appends data
                updateUI();
            } else {
                throw new Exception("Failed import");
            }
        } catch (Exception e) {
            Model.console.addLog("Failed an import - Invalid Obstacles XML selected");
            AlertController.showErrorAlert("Import Failed - Invalid Obstacles XML selected","Make sure that you have chosen the correct XML file");
        }
    }

    /**
     * Removes all data selected by user, only used when importing configuration
     */
    private void resetMenus(){
        Model.resetConfig();
        Model.leftScreenController.calculateAllowedMode();
        updateUI();
    }

    /**
     * Updates the UI, so the imported data are visible
      */
    private void updateUI(){
        TitledPane currentPane = Model.leftScreenController.accordion.getExpandedPane();
        for(TitledPane t : Model.leftScreenController.accordion.getPanes()){
            // only doing this because each titledpane has a listener, which updates the data when a pane is expanded
            t.setExpanded(true);
        }
        // expands the pane which was chosen by user before expanding
        if (currentPane != null)
            currentPane.setExpanded(true);
        else Model.leftScreenController.accordion.getExpandedPane().setExpanded(false);
    }

    public void exportConfig() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files" , "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;

        try {
            Model.console.addLog("--- Exporting Configuration ---");
            xmlParser.exportAll(xmlFile);
            Model.console.addLog("--- Finished exporting to: " + xmlFile.getName() + " ---" );
        }
        catch (Exception e) {
            AlertController.showErrorAlert("Failed to export the current Configuration","");
            Model.console.addLog("--- Failed to export the Configuration ---");
        }
    }

    public void exportAirports() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files" , "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;

        try {
            Model.console.addLog("--- Exporting Airports ---");
            xmlParser.exportAirports(xmlFile);
            Model.console.addLog("--- Finished exporting to: " + xmlFile.getName() + " ---" );
        }
        catch (Exception e) {
            AlertController.showErrorAlert("Failed to export the current Airports","");
            Model.console.addLog("--- Failed to export the current Airports ---");
        }
    }

    public void exportObstacles(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files" , "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(xmlFile == null) return;

        try {
            Model.console.addLog("--- Exporting Obstacles ---");
            xmlParser.exportObstacles(xmlFile);
            Model.console.addLog("--- Finished exporting to: " + xmlFile.getName() + " ---" );
        }
        catch (Exception e) {
            AlertController.showErrorAlert("Failed to export the current Obstacles","");
            Model.console.addLog("--- Failed to export the current Obstacles ---");
        }
    }


    public void defaultFontClick() {
        Model.setCurrentFontSize(FontSize.DEFAULT);
        App.root.setStyle("-fx-font-size: 12 px");
        Model.centerScreenController.console.setFont(Font.font("Courier New",12.5));
        Model.rightScreenController.changeFontSize(12.5);
        Model.rightScreenController.topTableView.setPrefHeight(78);
        Model.rightScreenController.bottomTableView.setPrefHeight(78);
        Model.rightScreenController.rightScreen.setPrefWidth(296);
        Model.rightScreenController.originalValuesLabel.setFont(Font.font(20));
        Model.rightScreenController.recalculatedValuesLabel.setFont(Font.font(20));
        Model.rightScreenController.breakdownLabel.setFont(Font.font(20));
        if(App.stage.getHeight() < 768) App.stage.setHeight(768);
        App.stage.setMinHeight(768);
        Model.console.addLog("Fontsize set to: Default");
    }

    public void mediumFontClick() {
        Model.setCurrentFontSize(FontSize.MEDIUM);
        App.root.setStyle("-fx-font-size: 13 px");
        Model.centerScreenController.console.setFont(Font.font("Courier New",13));
        Model.rightScreenController.changeFontSize(13);
        Model.rightScreenController.topTableView.setPrefHeight(87);
        Model.rightScreenController.bottomTableView.setPrefHeight(87);
        Model.rightScreenController.rightScreen.setPrefWidth(296);
        Model.rightScreenController.originalValuesLabel.setFont(Font.font(21));
        Model.rightScreenController.recalculatedValuesLabel.setFont(Font.font(21));
        Model.rightScreenController.breakdownLabel.setFont(Font.font(21));
        if(App.stage.getHeight() < 832)  App.stage.setHeight(832);
        App.stage.setMinHeight(820);
        Model.console.addLog("Fontsize set to: Medium");
    }

    public void largeFontClick() {
        Model.setCurrentFontSize(FontSize.LARGE);
        App.root.setStyle("-fx-font-size: 14.5 px");
        Model.centerScreenController.console.setFont(Font.font("Courier New",14.5));
        Model.rightScreenController.changeFontSize(14);
        Model.rightScreenController.topTableView.setPrefHeight(95);
        Model.rightScreenController.bottomTableView.setPrefHeight(95);
        Model.rightScreenController.rightScreen.setPrefWidth(310);
        Model.rightScreenController.originalValuesLabel.setFont(Font.font(22.5));
        Model.rightScreenController.recalculatedValuesLabel.setFont(Font.font(22.5));
        Model.rightScreenController.breakdownLabel.setFont(Font.font(22.5));
        if(App.stage.getHeight() < 882) App.stage.setHeight(882);
        App.stage.setMinHeight(882);
        Model.console.addLog("Fontsize set to: Large");
    }

    public void savePDFClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files" , "*.pdf"));
        File dest = fileChooser.showSaveDialog(menuBar.getScene().getWindow());

        if (dest != null) {
            try{
                savePDF(dest.getPath());
                Model.console.addLog("Saved calculations as a PDF to: " + dest.getPath());
            }
            catch (FileNotFoundException e){
                AlertController.showErrorAlert("Could not save file","Please make sure that you have specified a unique name");
            }
        }
    }

    public void savePNGSideOnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as PNG");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files" , "*.png"));
        File dest = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if (dest != null) {
            try{
                savePNG(dest.getPath(),Model.centerScreenController.sideOnCanvas);
                Model.console.addLog("Saved Side-On View as a PNG to: " + dest.getPath());
            } catch (IOException e){
                AlertController.showErrorAlert("Could not save file","An unexpected error occurred while creating the file, try again");
            }
        }
    }

    public void savePNGTopDownClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as PNG");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
        File dest = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if (dest != null) {
            try {
                savePNG(dest.getPath(), Model.centerScreenController.topDowncanvas);
                Model.console.addLog("Saved Top-On View as a PNG to: " + dest.getPath());
            } catch(IOException e){
                    AlertController.showErrorAlert("Could not save file", "An unexpected error occurred while creating the file, try again");
                }
            }
    }

    private void savePNG(String path, Canvas canvas) throws IOException {
        WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
        canvas.snapshot(null, writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        File file = new File(path);
        ImageIO.write(renderedImage,"png",file);
    }

    private void savePDF(String path) throws FileNotFoundException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(path));
        Document doc = new Document(pdf).setHorizontalAlignment(HorizontalAlignment.CENTER);

        Color blue = new DeviceRgb(0, 124, 173 );
        Color blueText = new DeviceRgb(0, 0, 153);
        Color lightBlue = new DeviceRgb(205, 240, 254);

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"));

        doc.add(new Paragraph()
                .add("Runway redeclaration report")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(18).setMarginBottom(1f));
        doc.add(new Paragraph(date).setTextAlignment(TextAlignment.CENTER).setMarginBottom(10f));

        doc.add(new Paragraph().add("Airport").setBold().setUnderline().setFontColor(blueText).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph().add(Model.currentAirport.toString()).setMarginBottom(15f).setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph().add("Runway " + Model.currentRunway.getLeftRunway().toString()).setBold().setFontColor(blueText).setUnderline().setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph().add(Model.currentRunway.getLeftRunway().getData()).setMarginBottom(15f).setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph().add("Runway " + Model.currentRunway.getRightRunway().toString()).setBold().setUnderline().setFontColor(blueText).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph().add(Model.currentRunway.getRightRunway().getData()).setMarginBottom(15f).setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph().add("Obstacle").setBold().setUnderline().setFontColor(blueText).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph(Model.currentObstacle.toString()).setMarginBottom(15f).setTextAlignment(TextAlignment.CENTER));

        doc.add(new Paragraph().add("Calculation breakdowns").setBold().setUnderline().setFontColor(blueText).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph().add(new Paragraph(Model.rightScreenController.allCalculationsLeftTA.getText())).setTextAlignment(TextAlignment.CENTER).setMarginBottom(5f));
        doc.add(new Paragraph().add(new Paragraph(Model.rightScreenController.allCalculationsRightTA.getText())).setTextAlignment(TextAlignment.CENTER).setMarginBottom(15f));

        doc.add(new Paragraph().add("Original values").setBold().setTextAlignment(TextAlignment.CENTER)).setBottomMargin(1f);
        doc.add(createTable(blue,lightBlue,Model.originalRunwayLeft, Model.originalRunwayRight)).setBottomMargin(10f);

        doc.add(new Paragraph().add("Recalculated values").setBold().setTextAlignment(TextAlignment.CENTER)).setBottomMargin(1f);
        doc.add(createTable(blue,lightBlue,Model.recalculatedRunwayLeft, Model.recalculatedRunwayRight));

        doc.close();
    }

    private Table createTable(Color dark, Color light, LogicalRunWay l, LogicalRunWay r){
        Table table = new Table(6);
        table.addCell(new Cell().setBackgroundColor(dark).add(new Paragraph("Runway").setBold().setFontColor(ColorConstants.WHITE)));
        table.addCell(new Cell().setBackgroundColor(dark).add(new Paragraph("TORA").setBold().setFontColor(ColorConstants.WHITE)));
        table.addCell(new Cell().setBackgroundColor(dark).add(new Paragraph("TODA").setBold().setFontColor(ColorConstants.WHITE)));
        table.addCell(new Cell().setBackgroundColor(dark).add(new Paragraph("LDA").setBold().setFontColor(ColorConstants.WHITE)));
        table.addCell(new Cell().setBackgroundColor(dark).add(new Paragraph("ASDA").setBold().setFontColor(ColorConstants.WHITE)));
        table.addCell(new Cell().setBackgroundColor(dark).add(new Paragraph("Threshold").setBold().setFontColor(ColorConstants.WHITE)));

        table.addCell(new Cell().setBackgroundColor(ColorConstants.WHITE).add(new Paragraph(l.getName())));
        table.addCell(new Cell().setBackgroundColor(ColorConstants.WHITE).add(new Paragraph(String.valueOf(l.getTORA()))));
        table.addCell(new Cell().setBackgroundColor(ColorConstants.WHITE).add(new Paragraph(String.valueOf(l.getTODA()))));
        table.addCell(new Cell().setBackgroundColor(ColorConstants.WHITE).add(new Paragraph(String.valueOf(l.getLDA()))));
        table.addCell(new Cell().setBackgroundColor(ColorConstants.WHITE).add(new Paragraph(String.valueOf(l.getASDA()))));
        table.addCell(new Cell().setBackgroundColor(ColorConstants.WHITE).add(new Paragraph(String.valueOf(l.getThreshold()))));

        table.addCell(new Cell().setBackgroundColor(light).add(new Paragraph(r.getName())));
        table.addCell(new Cell().setBackgroundColor(light).add(new Paragraph(String.valueOf(r.getTORA()))));
        table.addCell(new Cell().setBackgroundColor(light).add(new Paragraph(String.valueOf(r.getTODA()))));
        table.addCell(new Cell().setBackgroundColor(light).add(new Paragraph(String.valueOf(r.getLDA()))));
        table.addCell(new Cell().setBackgroundColor(light).add(new Paragraph(String.valueOf(r.getASDA()))));
        table.addCell(new Cell().setBackgroundColor(light).add(new Paragraph(String.valueOf(r.getThreshold()))));
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        return table;
    }

    public void redColorblindness(ActionEvent actionEvent) {
        Model.centerScreenController.redBlindness();
    }
    public void greenColorblindness(ActionEvent actionEvent) {
        Model.centerScreenController.greenBlindness();
    }
    public void blueColorblindness(ActionEvent actionEvent) {
        Model.centerScreenController.blueBlindness();
    }
    public void noBlindness(ActionEvent actionEvent) {
        Model.centerScreenController.noBlindness();
        defaultFontClick();
    }

}
