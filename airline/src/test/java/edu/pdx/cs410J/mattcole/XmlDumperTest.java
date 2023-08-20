package edu.pdx.cs410J.mattcole;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Writer;

public class XmlDumperTest {
    public static Airline createSimpleAirline() throws Project4.ArrivesBeforeDeparts, Project4.InvalidAirportCode, Flight.FlightParseDateTimeException {
        Airline airline = new Airline("Alaska");
        airline.addFlight(new Flight(5, "PDX", "03/13/2023 10:39 AM", "ABE", "03/14/2023 1:03 AM"));
        airline.addFlight(new Flight(6, "ABE", "03/13/2023 10:39 AM", "PDX", "03/14/2023 1:03 AM"));
        return airline;
    }

    @Test
    public void simpleXMLTest() throws Project4.ArrivesBeforeDeparts, Project4.InvalidAirportCode, Flight.FlightParseDateTimeException, IOException {
        StringBuilder output = new StringBuilder();
        XmlDumper xmlDumper = new XmlDumper(new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                output.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {

            }

            @Override
            public void close() throws IOException {

            }
        });
        xmlDumper.dump(createSimpleAirline());
        assert (output.toString().contains("Alaska"));
    }
}
