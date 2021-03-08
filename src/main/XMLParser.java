package main;

import org.w3c.dom.Document;

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

    public Airport importAirport(String filename) {
        Airport airport = null;
        try {
            File file = new File(filename);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
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
