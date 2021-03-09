package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XMLParser {
    private final DocumentBuilderFactory documentBuilderFactory;
    private final TransformerFactory transformerFactory;

    public XMLParser() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        transformerFactory = TransformerFactory.newInstance();
    }

    // Temporary main method for testing, will delete when hooked up to UI or when no longer needed
    public static void main(String[] args) {
        XMLParser xml = new XMLParser();
        ArrayList<Airport> testAirports = xml.importAirports("src/main/xml/airportsimport.xml");
        ArrayList<Obstacle> testObstacles = xml.importObstacle("src/main/xml/obstaclesimport.xml");

        Iterator<Airport> iterA = testAirports.iterator();
        while (iterA.hasNext()) {
            System.out.println(iterA.next().toString());
        }
        Iterator<Obstacle> iterO = testObstacles.iterator();
        while (iterO.hasNext()) {
            System.out.println(iterO.next().getName());
        }

        xml.exportAirports("src/main/xml/airportexport.xml");
    }

    /*
     *  Currently using a string to parse in
     *  TODO change to File input and use JavaFX explorer window probably
    */
    public ArrayList<Airport> importAirports(String filename) {
        Airport airport = null;
        List<PhysicalRunWay> runways = new ArrayList<PhysicalRunWay>();
        ArrayList<Airport> airports = new ArrayList<Airport>();
        try {
            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filename));
            document.getDocumentElement().normalize();

            /*
             * Get the list of airports
             */
            NodeList airportList = document.getElementsByTagName("airport");
            if (airportList.getLength() == 0) throw new Exception("Airport tag not found in XML file");
            for (int i = 0; i < airportList.getLength(); i++) {

                /*
                Get name of the aiport
                first check that there is only one airport tag (should be root element) then get child and contents
                */
                Node airportElement = airportList.item(i);
                String name = airportElement.getChildNodes().item(1).getTextContent();
                String code = airportElement.getChildNodes().item(3).getTextContent();
                /*
                *Get runway information
                */
                NodeList runwayNodes = document.getElementsByTagName("runway");
                // Iterate over runways
                for (int j = 0; j < runwayNodes.getLength(); j++) {
                    // Gets current runway from list
                    Element runway = (Element) runwayNodes.item(j);

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
                //Airport has additional "code" value like LHR for London Heathrow
                airports.add(new Airport(name, code ,runways));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return airports;
    }

    public ArrayList<Obstacle> importObstacle(String filename) {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        try {
            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filename));
            document.getDocumentElement().normalize();

            /*
            * Get list of obstacles from XML
            */
            NodeList obstacleList = document.getElementsByTagName("obstacle");
            if (obstacleList.getLength() == 0) throw new Exception("Obstacle tag not found in xml file");

            // Iterate over obstacles
            for (int i = 0; i < obstacleList.getLength(); i++) {
                Element obstacleElement = (Element) obstacleList.item(i);

                // Get the obstacle attributes from xml tags
                String name = obstacleElement.getElementsByTagName("name").item(0).getTextContent();

                int height = Integer.parseInt(obstacleElement.getElementsByTagName("height").item(0).getTextContent());
                int width = Integer.parseInt(obstacleElement.getElementsByTagName("width").item(0).getTextContent());

                Element positionNode = (Element) obstacleElement.getElementsByTagName("position").item(0);
                int distanceToLeft = Integer.parseInt(positionNode.getElementsByTagName("distanceToLeft").item(0).getTextContent());
                int distanceToRight = Integer.parseInt(positionNode.getElementsByTagName("distanceToRight").item(0).getTextContent());

                Position position = new Position(distanceToLeft, distanceToRight);

                // Add obstacle to the list
                obstacles.add(new Obstacle(name, height, width, position));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return obstacles;
    }

    public void exportAirports(String filename) {
        try {
            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            // Root element of XML is airports
            Element root = document.createElement("airports");
            document.appendChild(root);

            /*
                Iterate over the airports and create the required XML tags/data
             */
            // Running demo to get airport data into the model
            Model.demo();
            ArrayList<Airport> airportArrayList = Model.airports;

            Iterator<Airport> airportIter = airportArrayList.iterator();
            while(airportIter.hasNext()) {
                Airport airport = airportIter.next();
                // Airport tags go (name, code, (runway: ID, (left: degree, TORA, ...), (right: ...))
                Element airportElement = document.createElement("airport");
                root.appendChild(airportElement);

                // Adds airport name
                Element name = document.createElement("name");
                name.appendChild(document.createTextNode(airport.getName()));
                airportElement.appendChild(name);

                // Adds airport code
                Element code = document.createElement("code");
                code.appendChild(document.createTextNode(airport.getCode()));
                airportElement.appendChild(code);
            }

            /*
            Write XML file to disk
             */
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            FileWriter writer = new FileWriter(new File(filename));
            StreamResult stream = new StreamResult(writer);

            // Options to make the output look nice
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // This writes the files to disk
            transformer.transform(source, stream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportObstacles(String filename) {}
}
