package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

import java.io.*;

/**
 * The main class for the CS410J airline Project
 */
public class Project4 {
  public static final String ErrorInvalidDateTime = "Invalid date time given. Date and time should be in the format:"
                                    + " mm/dd/yyyy hh:mm";
  public static final String ErrorInvalidAirportCode = "Airport code did not match any known airport.";
  public static final String ErrorInvalidNumber = "Invalid flight number. Flight number should be an integer";
  public static final String ErrorTooFewArgs = "Missing command line arguments";
  public static final String ErrorTooManyArgs = "Too many command line arguments";
  public static final String CALL_AGAIN_WITH_README_TO_SEE_USAGE = "Call again with -readme to see usage";
  public static final String ErrorUnknownOption = "Fatal error. Unknown option: ";
  public static final String ErrorAirlineDoesntMatch = "Airline provided did not match airline of file";
  public static final String ErrorMalformedFile = "Malformed input file!";
  public static final String ErrorArrivesBeforeDeparts = "A flight departure must be before arrival";
  public static final String ErrorBothXMLandTextFlag = "Can only read from a text OR an xml file not both!";


  /**
   * Prints a resource file line by line to standard out
   * @param name name of resource to print
   */
  static void printResource(String name)
  {
    //copied from the test
    try (
            InputStream resource = Project4.class.getResourceAsStream(name)
    ) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
      while (reader.ready())
      {
        System.out.println(reader.readLine());
      }
    } catch (IOException exception)
    {
        System.err.println("Unable to access resource");
    }
  }
/**
 * The main function. Asks the user to input a flight then prints it back out to the screen if desired.
 * Additionally, will optionally read/write airline data to a file.
 * @param args Used to pass in flight info to program
 */
  public static void main(String[] args) {
    if (args.length == 0) {
      printResource("help.txt");
      return;
    }
    ParsedCommandLine commandLine;
    try {
      commandLine = new ParsedCommandLine(args);

      var flight = new Flight(commandLine.number, commandLine.source, commandLine.departure, commandLine.destination,
              commandLine.arrival);
      Airline airline;
      File file = null;
      if (commandLine.textFileFlag || commandLine.xmlFileFlag)
      {
        file = new File(commandLine.pathToTextFile);
      }
      if (file != null && file.exists()) {
        AirlineParser<Airline> parser = null;
        if (commandLine.textFileFlag)
        {
          airline = new TextParser(new FileReader(file)).parse();
        } else {
          airline = new XmlParser(new FileReader(file)).parse();
        }

        if (!airline.getName().equals(commandLine.airlineName)) //Make sure airline in file matches given airline
        {
          System.out.println(ErrorAirlineDoesntMatch);
          System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
        }
      } else {
        airline = new Airline(commandLine.airlineName);
      }
      airline.addFlight(flight);
      if (commandLine.printFlag) {
        System.out.println(flight);
      }
      if (commandLine.textFileFlag) {
        new TextDumper(new FileWriter(file)).dump(airline);
      }
      if (commandLine.xmlFileFlag)
      {
        new XmlDumper(new FileWriter(file)).dump(airline);
      }
      if (commandLine.prettyFlag) {
        Writer writer;
        if (commandLine.prettyOutput.equals("-"))
        {
          writer = new PrintWriter(System.out);
        } else {
          writer = new FileWriter(commandLine.prettyOutput);
        }
        new PrettyPrinter(writer).dump(airline);
      }
    } catch (FileNotFoundException exception) {
      assert (false); //Shouldn't occur
    } catch (ParserException parserException) {
      System.out.println(ErrorMalformedFile); //Tell the user if we have been given a malformed file
    } catch (IOException e) {
      assert (false); //Shouldn't occur
      throw new RuntimeException(e);
    } catch (ReadmeOption e){ //I realize this is suboptimal, but it would be really hard to create a separate control
                              //flow for it
      printResource("README.txt");
    } catch (TooManyArgs e) {
      System.out.println(ErrorTooManyArgs);
      System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
    } catch (TooFewArgs e) {
      System.out.println(ErrorTooFewArgs);
      System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
    } catch (InvalidFlightNumber e) {
      System.out.println(ErrorInvalidNumber);
      System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
    } catch (InvalidAirportCode e) {
      System.out.println(ErrorInvalidAirportCode);
      System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
    } catch (InvalidOption e) {
      System.out.println(ErrorUnknownOption);
      System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
    } catch (Flight.FlightParseDateTimeException e) {
      System.out.println(ErrorInvalidDateTime);
      System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
    } catch (ArrivesBeforeDeparts e) {
      System.out.println(ErrorArrivesBeforeDeparts);
      System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
    } catch (BothxmlAndRegularFileSelected e) {
      System.out.println(ErrorBothXMLandTextFlag);
      System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
    }
  }
  public static class TooManyArgs extends Exception { }
  public static class TooFewArgs extends Exception {}
  public static class InvalidAirportCode extends Exception {}
  public static class InvalidFlightNumber extends Exception {}
  public static class InvalidOption extends Exception {}

  public static class ReadmeOption extends Exception {}
  public static class ArrivesBeforeDeparts extends Exception {}
  public static class BothxmlAndRegularFileSelected extends Exception {}

  /**
   * This class parses a command line into a set of fields
   */
  public static class ParsedCommandLine
  {
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
    public ParsedCommandLine(String[] args) throws ReadmeOption, InvalidOption, TooFewArgs, TooManyArgs,
            InvalidFlightNumber, InvalidAirportCode, BothxmlAndRegularFileSelected {
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
            throw new ReadmeOption();
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
            throw new InvalidOption();
        }
        counter++;
      }
      if (xmlFileFlag && textFileFlag)
      {
        throw new BothxmlAndRegularFileSelected();
      }
      if (args.length - counter < 10) throw new TooFewArgs();
      if (args.length - counter > 10) throw new TooManyArgs();
      //Seperate args into inputs for making the flight and validation. Use counter as an offset for the options
      airlineName = args[counter];
      try {
        number = Integer.parseInt(args[counter + 1]);
      } catch (NumberFormatException e) {
        throw new InvalidFlightNumber();
      }
      source = args[counter + 2];
      departure = args[counter + 3] + " " + args[counter + 4] + " " + args[counter + 5];
      destination = args[counter + 6];
      arrival = args[counter + 7] + " " + args[counter + 8] + " " + args[counter + 9];
      var airports = AirportNames.getNamesMap();
      if (!airports.containsKey(source) || !airports.containsKey(destination)) throw new InvalidAirportCode();
      this.printFlag = printFlag;
      this.textFileFlag = textFileFlag;
      this.pathToTextFile = pathToTextFile;
      this.prettyFlag = prettyFlag;
      this.prettyOutput = prettyOutput;
      this.xmlFileFlag = xmlFileFlag;
    }
  }



}