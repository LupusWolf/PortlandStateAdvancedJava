package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;
import main.java.edu.pdx.cs410J.mattcole.*;

import java.io.*;
import java.util.Arrays;
import java.util.function.Predicate;


/**
 * The main class for the CS410J airline Project
 */
public class Project4 {


    /**
     * Prints a resource file line by line to standard out
     *
     * @param name name of resource to print
     */
    static void printResource(String name) {
        //copied from the test
        try (
                InputStream resource = Project4.class.getResourceAsStream(name)
        ) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            while (reader.ready()) {
                System.out.println(reader.readLine());
            }
        } catch (IOException exception) {
            System.err.println("Unable to access resource");
        }
    }

    /**
     * The main function. Asks the user to input a flight then prints it back out to the screen if desired.
     * Additionally, will optionally read/write airline data to a file.
     *
     * @param args Used to pass in flight info to program
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            printResource("help.txt");
            return;
        }
        Predicate<String> isReadmeOption = i -> (i.equals("-README"));

        if (Arrays.stream(args).anyMatch(isReadmeOption)) {
            printResource("README.txt");
            return;
        }
        ParsedCommandLine commandLine;
        try {
            commandLine = new ParsedCommandLine(args);
            performActionGivenByCommandLine(commandLine);
        } catch (FileNotFoundException exception) {
            assert (false); //Shouldn't occur because we check before we read the file that it exists
        } catch (ParserException parserException) {
            System.out.println(MessageHolder.ErrorMalformedFile); //Tell the user if we have been given a malformed file
        } catch (IOException e) {
            assert (false); //Shouldn't occur for the same reason as file not found
            throw new RuntimeException(e);
        } catch (ExceptionHolder.InvalidInput e) {
            System.out.println(e.getMessage());
            System.out.println(MessageHolder.CALL_AGAIN_WITH_README_TO_SEE_USAGE);
        }
    }


    private static void performActionGivenByCommandLine(ParsedCommandLine commandLine) throws ExceptionHolder.InvalidDateTime, IOException, ParserException, ExceptionHolder.ArrivesBeforeDeparts, ExceptionHolder.InvalidAirportCode, ExceptionHolder.AirlineDoesntMatch {
        var flight = new Flight(commandLine.number, commandLine.source, commandLine.departure, commandLine.destination,
                commandLine.arrival);
        Airline airline;
        if (commandLine.textFileFlag) {
            airline = Airline.buildAirline(new TextAirlineStorer(commandLine.pathToTextFile), commandLine.airlineName);
        } else if (commandLine.xmlFileFlag) {
            airline = Airline.buildAirline(new XMLAirlineStorer(commandLine.pathToTextFile), commandLine.airlineName);
        } else {
            airline = new Airline(commandLine.airlineName);
        }

        airline.addFlight(flight);
        if (commandLine.printFlag) {
            System.out.println(flight);
        }
        if (commandLine.prettyFlag) {
            Writer writer;
            if (commandLine.prettyOutput.equals("-")) {
                writer = new PrintWriter(System.out);
            } else {
                writer = new FileWriter(commandLine.prettyOutput);
            }
            new PrettyPrinter(writer).dump(airline);
        }
        airline.write();
    }

    /**
     * This class parses a command line into a set of fields
     */
    public static class ParsedCommandLine {
        public final String airlineName;
        public final int number;
        public final String source;
        public final String departure;
        public final String destination;
        public final String arrival;
        public final boolean printFlag;
        public final boolean textFileFlag;
        public final String pathToTextFile;
        public final boolean prettyFlag;
        public final String prettyOutput;
        public final boolean xmlFileFlag;

        public ParsedCommandLine(String[] args) throws ExceptionHolder.InvalidOption, ExceptionHolder.TooFewArgs, ExceptionHolder.TooManyArgs,
                ExceptionHolder.InvalidFlightNumber, ExceptionHolder.InvalidAirportCode, ExceptionHolder.BothxmlAndRegularFileSelected {
            int counter = 0;
            boolean printFlag = false;

            boolean textFileFlag = false;
            String pathToTextFile = null;

            boolean prettyFlag = false;
            String prettyOutput = null;

            boolean xmlFileFlag = false;

            while (counter < args.length && args[counter].startsWith("-")) //Check for options
            {
                switch (args[counter]) {
                    case "-README":
                        throw new RuntimeException("Unhandled -README argument");
                    case "-print":
                        printFlag = true;
                        break;
                    case "-textFile":
                        counter++;
                        pathToTextFile = args[counter];
                        textFileFlag = true;
                        break;
                    case "-pretty":
                        counter++;
                        prettyOutput = args[counter];
                        prettyFlag = true;
                        break;
                    case "-xmlFile":
                        counter++;
                        pathToTextFile = args[counter];
                        xmlFileFlag = true;
                        break;
                    default:
                        throw new ExceptionHolder.InvalidOption();
                }
                counter++;
            }
            if (xmlFileFlag && textFileFlag) {
                throw new ExceptionHolder.BothxmlAndRegularFileSelected();
            }
            if (args.length - counter < 10) throw new ExceptionHolder.TooFewArgs();
            if (args.length - counter > 10) throw new ExceptionHolder.TooManyArgs();
            //Separate args into inputs for making the flight and validation. Use counter as an offset for the options
            airlineName = args[counter];
            try {
                number = Integer.parseInt(args[counter + 1]);
            } catch (NumberFormatException e) {
                throw new ExceptionHolder.InvalidFlightNumber();
            }
            source = args[counter + 2];
            departure = args[counter + 3] + " " + args[counter + 4] + " " + args[counter + 5];
            destination = args[counter + 6];
            arrival = args[counter + 7] + " " + args[counter + 8] + " " + args[counter + 9];
            var airports = AirportNames.getNamesMap();
            if (!airports.containsKey(source) || !airports.containsKey(destination))
                throw new ExceptionHolder.InvalidAirportCode();
            this.printFlag = printFlag;
            this.textFileFlag = textFileFlag;
            this.pathToTextFile = pathToTextFile;
            this.prettyFlag = prettyFlag;
            this.prettyOutput = prettyOutput;
            this.xmlFileFlag = xmlFileFlag;
        }
    }


}