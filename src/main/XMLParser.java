package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XMLParser {
    private DocumentBuilderFactory documentBuilderFactory;

    public XMLParser() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    /*
     *  Currently using a string to parse in
     *  TODO change to File input and use JavaFX explorer window probably
    */
    public Airport importAirport(String filename) {
        Airport airport = null;
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filename));
            Element root = document.getDocumentElement();
            System.out.println(root);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return airport;
    }

    public Obstacle importObstacle(String filename) {
        return null;
    }

    public void exportAirport(String filename) {}

    public void exportObstacle(String filename) {}
}
