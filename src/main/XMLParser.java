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
        Airport testAirport = xml.importAirport("src/main/xml/airport1.xml");
        Obstacle testObstacle = xml.importObstacle("src/main/xml/obstacle1.xml");
        System.out.println(testAirport.getName());
        System.out.println(testAirport.getRunways());
        System.out.println(testObstacle.getName());
        System.out.println(testObstacle.getHeight());
        System.out.println(testObstacle.getWidth());
        System.out.println(testObstacle.getPosition());
    }

    /*
     *  Currently using a string to parse in
     *  TODO change to File input and use JavaFX explorer window probably
    */
    public Airport importAirport(String filename) {
        Airport airport = null;
        List<PhysicalRunWay> runways = new ArrayList<PhysicalRunWay>();
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

            /*
            Get runway information
             */
            NodeList runwayNodes = document.getElementsByTagName("runway");
            // Iterate over runways
            for (int i = 0; i < runwayNodes.getLength(); i++) {
                // Gets current runway from list
                Element runway = (Element) runwayNodes.item(i);

                // Gets the runway ID
                int ID = Integer.parseInt(runway.getElementsByTagName("ID").item(0).getTextContent());
                /*
                 Gets the runway attributes for each logical runway
                 Assumes left runway is always first child
                 */

                //Name of the runway was changed to a degree
                int degree_L = Integer.parseInt(runway.getElementsByTagName("degree").item(0).getTextContent());
                int degree_R = Integer.parseInt(runway.getElementsByTagName("degree").item(1).getTextContent());

                int TORA_L = Integer.parseInt(runway.getElementsByTagName("TORA").item(0).getTextContent());
                int TORA_R = Integer.parseInt(runway.getElementsByTagName("TORA").item(1).getTextContent());

                int TODA_L = Integer.parseInt(runway.getElementsByTagName("TODA").item(0).getTextContent());
                int TODA_R = Integer.parseInt(runway.getElementsByTagName("TODA").item(1).getTextContent());

                int ASDA_L = Integer.parseInt(runway.getElementsByTagName("ASDA").item(0).getTextContent());
                int ASDA_R = Integer.parseInt(runway.getElementsByTagName("ASDA").item(1).getTextContent());

                int LDA_L = Integer.parseInt(runway.getElementsByTagName("LDA").item(0).getTextContent());
                int LDA_R = Integer.parseInt(runway.getElementsByTagName("LDA").item(1).getTextContent());

                //Direction has been changed here from LogicalRunway.Direction.Left to Direction.Left
                LogicalRunWay leftRunway = new LogicalRunWay(degree_L, Direction.L, TORA_L, TODA_L, ASDA_L, LDA_L);
                LogicalRunWay rightRunway = new LogicalRunWay(degree_R, Direction.R, TORA_R, TODA_R, ASDA_R, LDA_R);
                runways.add(new PhysicalRunWay(ID, leftRunway, rightRunway, null));
            }
            //Aiport has additional "code" value like LHR for London Heathrow
           airport = new Airport(name, null ,runways);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return airport;
    }

    public Obstacle importObstacle(String filename) {
        Obstacle obstacle = null;
        try {
            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filename));
            document.getDocumentElement().normalize();

            /*
            Get name of the obstacle
            first check that there is only one obstacle tag (should be root element) then get child and contents
            */
            NodeList obstacleList = document.getElementsByTagName("obstacle");
            if (obstacleList.getLength() != 1) throw new Exception("Obstacle tag not found in xml file");
            Element obstacleElement = (Element) obstacleList.item(0);

            String name = obstacleElement.getChildNodes().item(1).getTextContent();

            int height = Integer.parseInt(obstacleElement.getElementsByTagName("height").item(0).getTextContent());
            int width = Integer.parseInt(obstacleElement.getElementsByTagName("width").item(0).getTextContent());

            Element positionNode = (Element) obstacleElement.getElementsByTagName("position").item(0);
            int distanceToLeft = Integer.parseInt(positionNode.getElementsByTagName("distanceToLeft").item(0).getTextContent());
            int distanceToRight = Integer.parseInt(positionNode.getElementsByTagName("distanceToRight").item(0).getTextContent());

            Position position = new Position(distanceToLeft, distanceToRight);
            obstacle = new Obstacle(name, height, width, position);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return obstacle;
    }

    public void exportAirport(String filename) {}

    public void exportObstacle(String filename) {}
}
