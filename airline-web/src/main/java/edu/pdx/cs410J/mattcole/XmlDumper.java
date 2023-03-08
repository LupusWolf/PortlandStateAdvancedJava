package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AirlineDumper;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Calendar;


public class XmlDumper implements AirlineDumper<Airline> {
    private final Writer writer;

    public XmlDumper(Writer writer) {
        this.writer = writer;
    }

    /**
     * Writes out the data in an airline to the writer used to instantiate the class in XML format
     * @param airline airline to write out
     */
    @Override
    public void dump(Airline airline) {

        try (
                PrintWriter pw = new PrintWriter(this.writer)
        ) {
            String publicID = AirlineXmlHelper.PUBLIC_ID;
            String systemID = AirlineXmlHelper.SYSTEM_ID;
            // Create an empty Document
            Document doc = null;
            try {
                DocumentBuilderFactory factory =
                        DocumentBuilderFactory.newInstance();
                factory.setValidating(true);
                DocumentBuilder builder =
                        factory.newDocumentBuilder();
                builder.setEntityResolver(new AirlineXmlHelper());
                DOMImplementation dom =
                        builder.getDOMImplementation();
                DocumentType docType =
                        dom.createDocumentType("airline", publicID, systemID);
                doc = dom.createDocument(null, "airline",
                        docType);
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException("Parser configuration error");
            } catch (DOMException ex) {
                throw new RuntimeException("Dom exception");
            }
            // Construct the DOM tree
            try {
                Element root = doc.getDocumentElement();
                Element name = doc.createElement("name");
                root.appendChild(name);
                String br = airline.getName();
                name.appendChild(doc.createTextNode(br));
                for (Flight flight : airline.getFlights())
                {
                    dumpFlightToDom(doc, root,flight);
                }
            } catch (DOMException ex) {
                throw new RuntimeException("Dom exception");

            }
            try {
                Source src = new DOMSource(doc);
                Result res = new StreamResult(writer);
                TransformerFactory xFactory =
                        TransformerFactory.newInstance();
                Transformer xform = xFactory.newTransformer();
                xform.setOutputProperty(OutputKeys.INDENT,
                        "yes");
                xform.setOutputProperty(
                        OutputKeys.DOCTYPE_SYSTEM, systemID);
                xform.transform(src, res);
            } catch (TransformerException ex) {
                throw new RuntimeException("Transformer exception");
            }

        }
    }

    /**
     * Dump a flight into the DOM at a given root and with a given flight
     * @param doc document to add it to
     * @param root where to insert the new element
     * @param flight the flight to insert
     */
    private void dumpFlightToDom(Document doc, Element root, Flight flight)
    {
        Element flightElement = doc.createElement("flight");
        root.appendChild(flightElement);

        Element currentElement = doc.createElement("number");
        flightElement.appendChild(currentElement);
        currentElement.appendChild(doc.createTextNode("" + flight.getNumber()));

        dumpSimpleFieldToDom(doc, flightElement, flight, Flight.Fields.src);
        dumpDateFieldToDom(doc, flightElement, flight, Flight.Fields.depart);
        dumpSimpleFieldToDom(doc, flightElement, flight, Flight.Fields.dest);
        dumpDateFieldToDom(doc, flightElement, flight, Flight.Fields.arrive);

    }

    /**
     * Inserts a simple field into the dom. A simple field is a field with just a string value (E.G. Dest/Src)
     * @param doc document to add it to
     * @param flightElement flight to add field to
     * @param flight flight to get data from
     * @param field field to copy over
     */
    private void dumpSimpleFieldToDom(Document doc, Element flightElement, Flight flight, Flight.Fields field)
    {
        var value = flight.getFieldByEnum(field); //Use the get field by enum to get the data for this field
        Element currentElement = doc.createElement(field.name());
        flightElement.appendChild(currentElement);
        currentElement.appendChild(doc.createTextNode(value));
    }
    /**
     * Inserts a date field into the dom.
     * @param doc document to add it to
     * @param flightElement flight to add field to
     * @param flight flight to get data from
     * @param field field to copy over
     */
    private void dumpDateFieldToDom(Document doc, Element flightElement, Flight flight, Flight.Fields field)
    {
        Calendar calendar = Calendar.getInstance();
        switch (field)
        {
            case arrive:
                calendar.setTime(flight.getArrival());
                break;
            case depart:
                calendar.setTime(flight.getDeparture());
                break;
        }
        Element currentElement = doc.createElement(field.name());
        flightElement.appendChild(currentElement);
        Element date = doc.createElement("date");

        date.setAttribute("day", "" + calendar.get(Calendar.DAY_OF_MONTH));
        date.setAttribute("month", "" + calendar.get(Calendar.MONTH));
        date.setAttribute("year", "" + calendar.get(Calendar.YEAR));
        currentElement.appendChild(date);

        Element time = doc.createElement("time");
        time.setAttribute("hour", "" + calendar.get(Calendar.HOUR_OF_DAY));
        time.setAttribute("minute", "" + calendar.get(Calendar.MINUTE));
        currentElement.appendChild(time);


    }
}
