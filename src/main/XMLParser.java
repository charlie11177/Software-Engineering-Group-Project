package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    private final DocumentBuilderFactory documentBuilderFactory;

    public XMLParser() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    public static void main(String[] args) {
        XMLParser xml = new XMLParser();
        xml.importAirport("C:\\Dev\\Java\\comp2211-airport\\src\\main\\xml\\airport1.xml");
    }

    /*
     *  Currently using a string to parse in
     *  TODO change to File input and use JavaFX explorer window probably
    */
    public Airport importAirport(String filename) {
        Airport airport = null;
        List<PhysicalRunWay> runways = null;
        try {
            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filename));
            document.getDocumentElement().normalize();

            /*
            Get name of the aiport
            first check that there is only one airport tag (should be root element) then get child and contents
            */
            NodeList airportList = document.getElementsByTagName("airport");
            if (airportList.getLength() != 1) throw new Exception("Airport tag not found in XML file");
            Node airportElement = airportList.item(0);
            String name = airportElement.getChildNodes().item(1).getTextContent();

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
