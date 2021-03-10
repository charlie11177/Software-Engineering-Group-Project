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

        Model.demo();

        Iterator<Airport> iterA = testAirports.iterator();
        while (iterA.hasNext()) {
            System.out.println(iterA.next().toString());
        }
        Iterator<Obstacle> iterO = testObstacles.iterator();
        while (iterO.hasNext()) {
            System.out.println(iterO.next().getName());
        }

        xml.exportAirports("src/main/xml/airportexport.xml");
        xml.exportObstacles("src/main/xml/obstacleexport.xml");
    }

    /*
     *  Overloaded method to allow either File or Filename arguments
    */
    public ArrayList<Airport> importAirports(String filename) {
        return importAirports(new File(filename));
    }

    public ArrayList<Airport> importAirports(File file) {
        Airport airport = null;
        List<PhysicalRunWay> runways = new ArrayList<PhysicalRunWay>();
        ArrayList<Airport> airports = new ArrayList<Airport>();
        try {
            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
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

                    int THRESHOLD_L = Integer.parseInt(runway.getElementsByTagName("threshold").item(0).getTextContent());
                    int THRESHOLD_R = Integer.parseInt(runway.getElementsByTagName("threshold").item(0).getTextContent());

                    //Direction has been changed here from LogicalRunway.Direction.Left to Direction.Left
                    LogicalRunWay leftRunway = new LogicalRunWay(degree_L, Direction.L, TORA_L, TODA_L, ASDA_L, LDA_L, THRESHOLD_L);
                    LogicalRunWay rightRunway = new LogicalRunWay(degree_R, Direction.R, TORA_R, TODA_R, ASDA_R, LDA_R, THRESHOLD_R);
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

    /*
     *  Overloaded method to allow either File or Filename arguments
     */
    public ArrayList<Obstacle> importObstacle(String filename) {
        return importObstacle(new File(filename));
    }

    public ArrayList<Obstacle> importObstacle(File file) {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        try {
            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
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

    /*
     *  Overloaded method to allow either File or Filename arguments
     */
    public void exportAirports(String filename) {
        exportAirports(new File(filename));
    }
    public void exportAirports(File file) {
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

                // PhysicalRunway loop
                List<PhysicalRunWay> runways = airport.getRunways();
                Iterator<PhysicalRunWay> runwayIter = runways.iterator();
                while(runwayIter.hasNext()) {
                    PhysicalRunWay physicalRunWay = runwayIter.next();
                    LogicalRunWay leftRunway = physicalRunWay.getLeftRunway();
                    LogicalRunWay rightRunway = physicalRunWay.getRightRunway();

                    // Runway element that holds left and right
                    Element runwayElement = document.createElement("runway");
                    airportElement.appendChild(runwayElement);

                    // ID of runway
                    Element ID = document.createElement("ID");
                    ID.appendChild(document.createTextNode(Integer.toString(physicalRunWay.getRunwayID())));
                    runwayElement.appendChild(ID);

                    /*
                     Logical runways (probably a nicer way of doing this but it works for now
                     */
                    // Left
                    Element leftRunwayElement = document.createElement("left");
                    runwayElement.appendChild(leftRunwayElement);
                        // degree
                    Element degree_L = document.createElement("degree");
                    degree_L.appendChild(document.createTextNode(Integer.toString(leftRunway.getDegree())));
                    leftRunwayElement.appendChild(degree_L);
                        // TORA
                    Element TORA_L = document.createElement("TORA");
                    TORA_L.appendChild(document.createTextNode(Integer.toString(leftRunway.getTORA())));
                    leftRunwayElement.appendChild(TORA_L);
                        // TODA
                    Element TODA_L = document.createElement("TODA");
                    TODA_L.appendChild(document.createTextNode(Integer.toString(leftRunway.getTODA())));
                    leftRunwayElement.appendChild(TODA_L);
                        // ASDA
                    Element ASDA_L = document.createElement("ASDA");
                    ASDA_L.appendChild(document.createTextNode(Integer.toString(leftRunway.getASDA())));
                    leftRunwayElement.appendChild(ASDA_L);
                        //LDA
                    Element LDA_L = document.createElement("LDA");
                    LDA_L.appendChild(document.createTextNode(Integer.toString(leftRunway.getLDA())));
                    leftRunwayElement.appendChild(LDA_L);


                    // Right
                    Element rightRunwayElement = document.createElement("right");
                    runwayElement.appendChild(rightRunwayElement);
                        // degree
                    Element degree_R = document.createElement("degree");
                    degree_R.appendChild(document.createTextNode(Integer.toString(rightRunway.getDegree())));
                    rightRunwayElement.appendChild(degree_R);
                        // TORA
                    Element TORA_R = document.createElement("TORA");
                    TORA_R.appendChild(document.createTextNode(Integer.toString(rightRunway.getTORA())));
                    rightRunwayElement.appendChild(TORA_R);
                        // TODA
                    Element TODA_R = document.createElement("TODA");
                    TODA_R.appendChild(document.createTextNode(Integer.toString(rightRunway.getTODA())));
                    rightRunwayElement.appendChild(TODA_R);
                        // ASDA
                    Element ASDA_R = document.createElement("ASDA");
                    ASDA_R.appendChild(document.createTextNode(Integer.toString(rightRunway.getASDA())));
                    rightRunwayElement.appendChild(ASDA_R);
                        //LDA
                    Element LDA_R = document.createElement("LDA");
                    LDA_R.appendChild(document.createTextNode(Integer.toString(rightRunway.getLDA())));
                    rightRunwayElement.appendChild(LDA_R);
                }
            }

            /*
            Write XML file to disk
             */
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            FileWriter writer = new FileWriter(file);
            StreamResult stream = new StreamResult(writer);

            // Options to make the output look nice
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // This writes the files to disk
            transformer.transform(source, stream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *  Overloaded method to allow either File or Filename arguments
     */
    public void exportObstacles(String filename) {
        exportObstacles(new File(filename));
    }
    public void exportObstacles(File file) {
        try {
            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            // Root element of XML is airports
            Element root = document.createElement("obstacles");
            document.appendChild(root);

            // Temporary model data
            ArrayList<Obstacle> obstacleList = Model.obstacles;
            Iterator<Obstacle> obstacleIter = obstacleList.iterator();
            while(obstacleIter.hasNext()) {
                Obstacle obstacle = obstacleIter.next();
                Position position = obstacle.getPosition();

                // Obstacle element attributes
                Element obstacleElement = document.createElement("obstacle");
                root.appendChild(obstacleElement);

                Element name = document.createElement("name");
                name.appendChild(document.createTextNode(obstacle.getName()));
                obstacleElement.appendChild(name);

                Element height = document.createElement("height");
                height.appendChild(document.createTextNode(Integer.toString(obstacle.getHeight())));
                obstacleElement.appendChild(height);

                Element width = document.createElement("width");
                width.appendChild(document.createTextNode(Integer.toString(obstacle.getWidth())));
                obstacleElement.appendChild(width);

                // Position element attributes
                Element positionElement = document.createElement("position");
                obstacleElement.appendChild(positionElement);

                Element distToLeft = document.createElement("distanceToLeft");
                distToLeft.appendChild(document.createTextNode(Integer.toString(position.getDistanceToLeft())));
                positionElement.appendChild(distToLeft);

                Element distToRight = document.createElement("distanceToRight");
                distToRight.appendChild(document.createTextNode(Integer.toString(position.getDistanceToRight())));
                positionElement.appendChild(distToRight);
            }

            /*
            Write XML file to disk
             */
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            FileWriter writer = new FileWriter(file);
            StreamResult stream = new StreamResult(writer);

            // Options to make the output look nice
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // This writes the files to disk
            transformer.transform(source, stream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
