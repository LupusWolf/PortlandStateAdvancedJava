package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Prints out an airline in a pretty format
 */
public class PrettyPrinter implements AirlineDumper<Airline> {

    private final Writer writer;
    private final String prettyFormat = "%number%\t\t\t\t\t| %src%\t\t| %depart%\t| %dest%\t\t\t| %arrive%\t| %duration%\t\t\t\t|";
    private final int padding = 30;

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
            pw.println("Flight list for airline: " + airline + "\n");
            String[] headers = new String[] {"flight number","source airport","departure date/time","destination airport"
                    ,"arrival date/time","duration (in minutes)"};
            for (String header : headers)
            {
                pw.print(center("", header));
            }
            pw.print("\n");
            pw.print("_".repeat(padding * headers.length) + "\n");
            for (Flight flight : airline.getFlights()) {
                pw.print(center("", "" + flight.getNumber()));
                for (Flight.Fields field : Flight.Fields.values())
                {
                    String value;
                    switch (field)
                    {
                        case src:
                        case dest:
                            value = AirportNames.getName(flight.getFieldByEnum(field));
                            break;
                        default:
                            value = flight.getFieldByEnum(field);

                    }
                    pw.print(center("|", value));
                }
                pw.print(center("|", "" + flight.getDurationInMinutes()) + "\n");
            }
            pw.flush();
        }
    }

    /**
     * Centers text
     * @param prefix text to put before center text
     * @param value text to center
     * @return a string with a given string in the center
     */
    private String center(String prefix, String value)
    {
        value = prefix + " ".repeat((padding - value.length() - prefix.length())/2) + value;
        value += " ".repeat(padding - value.length());
        return value;
    }
}
