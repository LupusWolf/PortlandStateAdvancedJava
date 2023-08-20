package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.util.Calendar;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XmlParser implements AirlineParser<Airline> {
    private final Reader reader;

    public XmlParser(Reader reader) {
        this.reader = reader;
    }

    @Override
    public Airline parse() throws ParserException {
        // Parse the XML file to create a DOM tree
        String publicID = AirlineXmlHelper.PUBLIC_ID;
        String systemID = AirlineXmlHelper.SYSTEM_ID;

        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new AirlineXmlHelper());
            DOMImplementation dom = builder.getDOMImplementation();
            DocumentType docType = dom.createDocumentType("airline", publicID, systemID);
            doc = builder.parse(new InputSource(reader));//, systemID

        } catch (ParserConfigurationException | SAXException e) {
            throw new ParserException("Error when parsing xml: " + e.getMessage());
        } catch (IOException e) {
            throw new ParserException("IO error when parsing xml: " + e.getMessage());
        }
        Element root = (Element) doc.getDocumentElement();
        try {
            return parseAirline(root);
        } catch (Project4.ArrivesBeforeDeparts | Project4.InvalidAirportCode | Flight.FlightParseDateTimeException
                 | NumberFormatException e) {
            throw new ParserException("Error when parsing xml: " + e.getMessage());
        }
    }

    /**
     * Gets children of a node as a list of nodes
     *
     * @param root root to get children of
     * @return returns collections of children
     */
    private Collection<Node> getChildren(Node root) {
        NodeList nodeList = root.getChildNodes();
        return IntStream.rangeClosed(1, nodeList.getLength() - 1).mapToObj(i ->
                nodeList.item(i)).collect(Collectors.toList()); //Converts node list to regular list of nodes
    }

    /**
     * Gets value of a text node child of given root
     *
     * @param root
     * @return string value of child node
     */
    private String getValOfChild(Node root) {
        return root.getChildNodes().item(0).getNodeValue();
    }

    /**
     * Parses an airline from root
     *
     * @param root
     * @return an airline parse from the root
     * @throws Project4.ArrivesBeforeDeparts
     * @throws Project4.InvalidAirportCode
     * @throws Flight.FlightParseDateTimeException
     */
    private Airline parseAirline(Element root) throws Project4.ArrivesBeforeDeparts, Project4.InvalidAirportCode, Flight.FlightParseDateTimeException {
        Airline airline = null;
        for (Node children : getChildren(root)) {
            //System.out.println(children.getNodeName());
            switch (children.getNodeName()) {
                case "name":
                    var textnode = children.getChildNodes().item(0);
                    airline = new Airline(textnode.getNodeValue());
                    break;
                case "flight":
                    airline.addFlight(parseFlight(children));
                    break;
                default:
                    break;
            }
        }
        return airline;
    }

    /**
     * Parse flight from given root
     *
     * @param root
     * @return parsed flight
     * @throws Project4.ArrivesBeforeDeparts
     * @throws Project4.InvalidAirportCode
     * @throws Flight.FlightParseDateTimeException
     */
    private Flight parseFlight(Node root) throws Project4.ArrivesBeforeDeparts, Project4.InvalidAirportCode, Flight.FlightParseDateTimeException {
        Flight flight = null;
        for (Node child : getChildren(root)) {
            //System.out.println(children.getNodeName());
            switch (child.getNodeName()) {
                case "number":
                    flight = new Flight(Integer.parseInt(getValOfChild(child)));
                    break;
                case "dest":
                case "src":
                    var value = getValOfChild(child);
                    flight.setFieldByEnum(Flight.Fields.valueOf(child.getNodeName()), value);
                    break;
                case "depart":
                case "arrive":
                    Calendar calendar = Calendar.getInstance();
                    for (Node date : getChildren(child)) {
                        var map = date.getAttributes();
                        switch (date.getNodeName()) {
                            case "date":
                                calendar.set(Calendar.YEAR, Integer.parseInt(map.getNamedItem("year").getNodeValue()));
                                calendar.set(Calendar.MONTH, Integer.parseInt(map.getNamedItem("month").getNodeValue()));
                                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(map.getNamedItem("day").getNodeValue()));
                                break;
                            case "time":
                                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(map.getNamedItem("hour").getNodeValue()));
                                calendar.set(Calendar.MINUTE, Integer.parseInt(map.getNamedItem("minute").getNodeValue()));
                                break;
                            default:
                                break;
                        }
                    }
                    flight.SetDateFieldByEnum(Flight.Fields.valueOf(child.getNodeName()), calendar.getTime());
                    break;
                default:
                    break;
            }
        }
        return flight;
    }
}
