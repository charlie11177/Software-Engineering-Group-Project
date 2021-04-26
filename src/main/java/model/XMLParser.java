package model;

import controllers.ObstacleConfigController;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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

    /*
     *  Overloaded method to allow either File or Filename arguments
    */
    public ArrayList<model.Airport> importAirports(String filename) {
        return importAirports(new File(filename));
    }

    private ArrayList<model.Airport> getAirports(NodeList airportList) throws Exception {
        List<model.PhysicalRunWay> runways = new ArrayList<model.PhysicalRunWay>();
        ArrayList<model.Airport> airports = new ArrayList<model.Airport>();
        for (int i = 0; i < airportList.getLength(); i++) {
            runways = new ArrayList<model.PhysicalRunWay>();
                /*
                Get name of the airport
                first check that there is only one airport tag (should be root element) then get child and contents
                */
            Element airportElement = (Element) airportList.item(i);
            String name = airportElement.getChildNodes().item(1).getTextContent();
            String code = airportElement.getChildNodes().item(3).getTextContent();
            /*
             *Get runway information
             */
            NodeList runwayNodes = airportElement.getElementsByTagName("runway");
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
                model.LogicalRunWay leftRunway = new model.LogicalRunWay(degree_L, model.Direction.L, TORA_L, TODA_L, ASDA_L, LDA_L, THRESHOLD_L);
                model.LogicalRunWay rightRunway = new model.LogicalRunWay(degree_R, model.Direction.R, TORA_R, TODA_R, ASDA_R, LDA_R, THRESHOLD_R);
                runways.add(new model.PhysicalRunWay(ID, leftRunway, rightRunway, null));
            }
            //Airport has additional "code" value like LHR for London Heathrow
            airports.add(new model.Airport(name, code ,runways));
        }
        return airports;
    }

    private ArrayList<model.Obstacle> getObstacles(NodeList obstacleList) {
        ArrayList<model.Obstacle> obstacles = new ArrayList<>();
        for (int i = 0; i < obstacleList.getLength(); i++) {
            Element obstacleElement = (Element) obstacleList.item(i);

            // Get the obstacle attributes from xml tags
            String name = obstacleElement.getElementsByTagName("name").item(0).getTextContent();

            int height = Integer.parseInt(obstacleElement.getElementsByTagName("height").item(0).getTextContent());
            int width = Integer.parseInt(obstacleElement.getElementsByTagName("width").item(0).getTextContent());

            Element positionNode = (Element) obstacleElement.getElementsByTagName("position").item(0);
            int distanceToLeft = Integer.parseInt(positionNode.getElementsByTagName("distanceToLeft").item(0).getTextContent());
            int distanceToRight = Integer.parseInt(positionNode.getElementsByTagName("distanceToRight").item(0).getTextContent());
            int distanceToCL = Integer.parseInt(positionNode.getElementsByTagName("distanceFromCL").item(0).getTextContent());
            String directionFromCL = positionNode.getElementsByTagName("directionFromCL").item(0).getTextContent();

            model.Position position = new model.Position(distanceToLeft, distanceToRight, distanceToCL, directionFromCL);    //Position will probably need two more parameters

            // Add obstacle to the list
            obstacles.add(new model.Obstacle(name, height, width, position));
        }
        return obstacles;
    }

    public ArrayList<model.Airport> importAirports(File file) {
        model.Airport airport = null;
        List<model.PhysicalRunWay> runways = new ArrayList<model.PhysicalRunWay>();
        ArrayList<model.Airport> airports = new ArrayList<model.Airport>();
        try {
            // Validate the XML file
            if (!validateXML(XMLTypes.Airport, file)) throw new Exception("Invalid");

            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            /*
             * Get the list of airports
             */
            NodeList airportList = document.getElementsByTagName("airport");
            airports = getAirports(airportList);

        } catch(Exception e) {
//            e.printStackTrace();
            System.err.println(e.getMessage() + " " + e.getLocalizedMessage());
        }
        return airports;
    }

    /*
     *  Overloaded method to allow either File or Filename arguments
     */
    public ArrayList<model.Obstacle> importObstacle(String filename) {
        return importObstacle(new File(filename));
    }

    public ArrayList<model.Obstacle> importObstacle(File file) {
        ArrayList<model.Obstacle> obstacles = new ArrayList<>();
        try {
            // Validate XML
            if (!validateXML(XMLTypes.Obstacle, file)) throw new Exception("Invalid");

            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            /*
            * Get list of obstacles from XML
            */
            NodeList obstacleList = document.getElementsByTagName("obstacle");
            // Iterate over obstacles
            obstacles = getObstacles(obstacleList);
        } catch(Exception e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
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
            ArrayList<model.Airport> airportArrayList = model.Model.airports;

            Iterator<model.Airport> airportIter = airportArrayList.iterator();
            while(airportIter.hasNext()) {
                model.Airport airport = airportIter.next();
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
                List<model.PhysicalRunWay> runways = airport.getRunways();
                Iterator<model.PhysicalRunWay> runwayIter = runways.iterator();
                while(runwayIter.hasNext()) {
                    model.PhysicalRunWay physicalRunWay = runwayIter.next();
                    model.LogicalRunWay leftRunway = physicalRunWay.getLeftRunway();
                    model.LogicalRunWay rightRunway = physicalRunWay.getRightRunway();

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
                        //Threshold
                    Element Threshold_L = document.createElement("threshold");
                    Threshold_L.appendChild(document.createTextNode(Integer.toString(leftRunway.getThreshold())));
                    leftRunwayElement.appendChild(Threshold_L);


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
                        //Threshold
                    Element Threshold_R = document.createElement("threshold");
                    Threshold_R.appendChild(document.createTextNode(Integer.toString(rightRunway.getThreshold())));
                    rightRunwayElement.appendChild(Threshold_R);
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

            writer.close();

        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
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
            // Root element of XML is obstacles
            Element root = document.createElement("obstacles");
            document.appendChild(root);

            // Temporary model data
            ArrayList<model.Obstacle> obstacleList = model.Model.obstacles;
            Iterator<model.Obstacle> obstacleIter = obstacleList.iterator();
            while(obstacleIter.hasNext()) {
                model.Obstacle obstacle = obstacleIter.next();
                model.Position position = obstacle.getPosition();

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

                Element distanceFromCL = document.createElement("distanceFromCL");
                distanceFromCL.appendChild(document.createTextNode(Integer.toString(position.getDistanceFromCL())));
                positionElement.appendChild(distanceFromCL);

                Element directionFromCL = document.createElement("directionFromCL");
                directionFromCL.appendChild(document.createTextNode(position.getDirectionFromCL()));
                positionElement.appendChild(directionFromCL);
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

            writer.close();

        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public Pair<ArrayList<Airport>, ArrayList<Obstacle>> importConfiguration(File file) {
        List<model.PhysicalRunWay> runways = new ArrayList<model.PhysicalRunWay>();
        ArrayList<model.Airport> airports = new ArrayList<model.Airport>();
        ArrayList<model.Obstacle> obstacles = new ArrayList<>();
        try {
            if (!validateXML(XMLTypes.FullConfig, file)) throw new Exception("Invalid");
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList airportList = document.getElementsByTagName("airport");
            NodeList obstacleList = document.getElementsByTagName("obstacle");

            airports = getAirports(airportList);
            obstacles = getObstacles(obstacleList);

            return new Pair<ArrayList<Airport>, ArrayList<Obstacle>>(airports, obstacles);
        } catch(Exception e) {
            System.err.println(e.getMessage() + " " + e.getLocalizedMessage());
        }
        return null;
    }

    public void exportConfiguration(File file) {
        try {
            // Boiler plate code for creating an XML parser
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            // Root element of XML is obstacles
            Element root = document.createElement("data");
            document.appendChild(root);
            // Element for obstacles
            Element obstacles = document.createElement("obstacles");
            root.appendChild(obstacles);
            // Element for Airports
            Element airports = document.createElement("airports");
            root.appendChild(airports);

            // Adding Obstacles
            ArrayList<model.Obstacle> obstacleList = model.Model.obstacles;
            Iterator<model.Obstacle> obstacleIter = obstacleList.iterator();
            while(obstacleIter.hasNext()) {
                model.Obstacle obstacle = obstacleIter.next();
                model.Position position = obstacle.getPosition();

                // Obstacle element attributes
                Element obstacleElement = document.createElement("obstacle");
                obstacles.appendChild(obstacleElement);

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

                Element distanceFromCL = document.createElement("distanceFromCL");
                distanceFromCL.appendChild(document.createTextNode(Integer.toString(position.getDistanceFromCL())));
                positionElement.appendChild(distanceFromCL);

                Element directionFromCL = document.createElement("directionFromCL");
                directionFromCL.appendChild(document.createTextNode(position.getDirectionFromCL()));
                positionElement.appendChild(directionFromCL);
            }

            // Adding Airports
            ArrayList<model.Airport> airportArrayList = model.Model.airports;
            Iterator<model.Airport> airportIter = airportArrayList.iterator();
            while(airportIter.hasNext()) {
                model.Airport airport = airportIter.next();
                // Airport tags go (name, code, (runway: ID, (left: degree, TORA, ...), (right: ...))
                Element airportElement = document.createElement("airport");
                airports.appendChild(airportElement);

                // Adds airport name
                Element name = document.createElement("name");
                name.appendChild(document.createTextNode(airport.getName()));
                airportElement.appendChild(name);

                // Adds airport code
                Element code = document.createElement("code");
                code.appendChild(document.createTextNode(airport.getCode()));
                airportElement.appendChild(code);

                // PhysicalRunway loop
                List<model.PhysicalRunWay> runways = airport.getRunways();
                Iterator<model.PhysicalRunWay> runwayIter = runways.iterator();
                while(runwayIter.hasNext()) {
                    model.PhysicalRunWay physicalRunWay = runwayIter.next();
                    model.LogicalRunWay leftRunway = physicalRunWay.getLeftRunway();
                    model.LogicalRunWay rightRunway = physicalRunWay.getRightRunway();

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
                    //Threshold
                    Element Threshold_L = document.createElement("threshold");
                    Threshold_L.appendChild(document.createTextNode(Integer.toString(leftRunway.getThreshold())));
                    leftRunwayElement.appendChild(Threshold_L);


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
                    //Threshold
                    Element Threshold_R = document.createElement("threshold");
                    Threshold_R.appendChild(document.createTextNode(Integer.toString(rightRunway.getThreshold())));
                    rightRunwayElement.appendChild(Threshold_R);
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

            writer.close();

        } catch (Exception e)
        {
//            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    private enum XMLTypes {
        Airport, Obstacle, FullConfig
    }

    /**
     * Method to validate an XML file against the corresponding schema
     * @param XMLtype - Uses a private enum for readability
     * @param XMLFile - XML file to be validated
     * @return True if valid, false if an error occured
     */
    private boolean validateXML(XMLTypes XMLtype, File XMLFile) {
        try {
            SchemaFactory schemafactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema;

            // Code to access resource folder in maven
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource;
            switch (XMLtype) {
                case Airport:
                    resource = classLoader.getResource("xml/AirportSchema.xsd");
                    schema = schemafactory.newSchema(new File(resource.toURI()));
                    break;
                case Obstacle:
                    resource = classLoader.getResource("xml/ObstacleSchema.xsd");
                    schema = schemafactory.newSchema(new File(resource.toURI()));
                    break;
                case FullConfig:
                    resource = classLoader.getResource("xml/FullConfigSchema.xsd");
                    schema = schemafactory.newSchema(new File(resource.toURI()));
                    break;
                default:
                    throw new IOException("Invalid XML type selected somehow");
            }
            Validator schemaValidator = schema.newValidator();
            schemaValidator.validate(new StreamSource(XMLFile));
        } catch (IOException | SAXException | URISyntaxException e) {
            model.Model.console.addLog("---- Error validating XML ---- ");
            model.Model.console.addLogWithoutTime(e.getMessage());
            return false;
        }
        return true;
    }

}
