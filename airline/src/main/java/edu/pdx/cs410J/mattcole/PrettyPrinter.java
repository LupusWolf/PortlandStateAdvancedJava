package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Prints out an airline in a pretty format
 */
public class PrettyPrinter implements AirlineDumper<Airline> {

    private final Writer writer;
    private final String prettyFormat = "\"Flight %number% departs %source% at %departure% and will arrive at " +
            "%destination% at %arrival% and the flight length is %duration% minutes\"";

    public PrettyPrinter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Writes out the data in an airline to the writer used to instantiate the class
     * @param airline airline to write out
     */
    @Override
    public void dump(Airline airline) {
        try (
                PrintWriter pw = new PrintWriter(this.writer)
        ) {
            pw.println(airline + "\n");
            for (Flight flight : airline.getFlights()) {
                pw.println(flight.prettyFlightString(prettyFormat));
            }
            pw.flush();
        }
    }
}
