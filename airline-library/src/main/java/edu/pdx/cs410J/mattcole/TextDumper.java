package main.java.edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AirlineDumper;

import java.io.PrintWriter;
import java.io.Writer;


/**
 * Dumps data in airline into given output stream
 */
public class TextDumper implements AirlineDumper<Airline> {
    private final Writer writer;

    public TextDumper(Writer writer) {
        this.writer = writer;
    }

    /**
     * Writes out the data in an airline to the writer used to instantiate the class
     *
     * @param airline airline to write out
     */
    @Override
    public void dump(Airline airline) {
        try (
                PrintWriter pw = new PrintWriter(this.writer)
        ) {
            pw.println(airline.getName());
            for (Flight flight : airline.getFlights()) //Loop through every flight
            {
                for (Flight.Fields field : Flight.Fields.values()) //We loop through every field and write it out
                {
                    var flightNumber = flight.getNumber();
                    var value = flight.getFieldByEnum(field); //Use the get field by enum to get the data for this field
                    pw.println(flightNumber + TextParser.delimiter + field.name() + TextParser.delimiter + value);
                }
            }
            pw.flush();
        }
    }
}
