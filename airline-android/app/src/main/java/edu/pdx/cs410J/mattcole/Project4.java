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
  public static final String CALL_AGAIN_WITH_README_TO_SEE_USAGE = "Click help to see help";
  public static final String ErrorUnknownOption = "Fatal error. Unknown option: ";
  public static final String ErrorAirlineDoesntMatch = "Airline provided did not match airline of file";
  public static final String ErrorMalformedFile = "Malformed input file!";
  public static final String ErrorArrivesBeforeDeparts = "A flight departure must be before arrival";
  public static final String ErrorBothXMLandTextFlag = "Can only read from a text OR an xml file not both!";


  /**
   * Prints a resource file line by line to standard out
   * @param name name of resource to print
   */
  static void printResource(String name,PrintWriter outwriter)
  {
    //copied from the test
    try (
            InputStream resource = Project4.class.getResourceAsStream(name)
    ) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
      while (reader.ready())
      {
        outwriter.println(reader.readLine());
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
  public static boolean main(String[] args,PrintWriter outwriter) {
    if (args.length == 0) {
      printResource("help.txt",outwriter);
      return true;
    }
    File file = null;
    ParsedCommandLine commandLine;
    try {
      commandLine = new ParsedCommandLine(args);
      Flight flight = null;
      if (commandLine.createFlight) {
        flight = new Flight(commandLine.number, commandLine.source, commandLine.departure, commandLine.destination,
                commandLine.arrival);
      }
      Airline airline;

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
          outwriter.println(ErrorAirlineDoesntMatch);
          outwriter.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
        }
      } else {
        airline = new Airline(commandLine.airlineName);
      }
      if (commandLine.createFlight) {
        airline.addFlight(flight);
        if (commandLine.printFlag) {
          outwriter.println(flight);
        }
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
        if (commandLine.searchFlag)
        {
          airline = airline.getSubset(commandLine.searchSource, commandLine.searchDest);
        }
        if (commandLine.prettyOutput.equals("-"))
        {
          writer = new PrintWriter(outwriter);
        } else {
          writer = new FileWriter(commandLine.prettyOutput);
        }
        if (airline.getFlights().stream().count() == 0)
        {
          writer.append("No flights were available for the given airline/search");
          writer.flush();
        } else {
          new PrettyPrinter(writer).dump(airline);
        }

      }
    } catch (FileNotFoundException exception) {
      assert (false); //Shouldn't occur
    } catch (ParserException parserException) {
      //outwriter.println(ErrorMalformedFile); //Tell the user if we have been given a malformed file
      file.delete();
      main(args, outwriter);

    } catch (IOException e) {
      assert (false); //Shouldn't occur
      throw new RuntimeException(e);
    } catch (ReadmeOption e){ //I realize this is suboptimal, but it would be really hard to create a separate control
                              //flow for it
      printResource("README.txt",outwriter);
    } catch (TooManyArgs e) {
      outwriter.println(ErrorTooManyArgs);
      outwriter.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      return false;
    } catch (TooFewArgs e) {
      outwriter.println(ErrorTooFewArgs);
      outwriter.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      return false;
    } catch (InvalidFlightNumber e) {
      outwriter.println(ErrorInvalidNumber);
      outwriter.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      return false;
    } catch (InvalidAirportCode e) {
      outwriter.println(ErrorInvalidAirportCode);
      outwriter.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      return false;
    } catch (InvalidOption e) {
      outwriter.println(ErrorUnknownOption);
      outwriter.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      return false;
    } catch (Flight.FlightParseDateTimeException e) {
      outwriter.println(ErrorInvalidDateTime);
      outwriter.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      return false;
    } catch (ArrivesBeforeDeparts e) {
      outwriter.println(ErrorArrivesBeforeDeparts);
      outwriter.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      return false;
    } catch (BothxmlAndRegularFileSelected e) {
      outwriter.println(ErrorBothXMLandTextFlag);
      outwriter.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      return false;
    }
    return true;
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
    public String airlineName;
    public int number;
    public String source;
    public String departure;
    public String destination;
    public String arrival;
    public boolean printFlag;
    public boolean textFileFlag;
    public String pathToTextFile;
    public boolean prettyFlag;
    public String prettyOutput;
    public boolean xmlFileFlag;
    public boolean createFlight;
    public String searchSource;
    public String searchDest;
    public boolean searchFlag = false;
    public ParsedCommandLine(String[] args) throws ReadmeOption, InvalidOption, TooFewArgs, TooManyArgs,
            InvalidFlightNumber, InvalidAirportCode, BothxmlAndRegularFileSelected {
      int counter = 0;
      printFlag = false;
      textFileFlag = false;
      pathToTextFile = null;

      prettyFlag = false;
      prettyOutput = null;

      xmlFileFlag = false;

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
          case "-search":
            counter++;
            searchDest = args[counter];
            counter++;
            searchSource = args[counter];
            searchFlag = true;
            java.util.Map<String, String> airports = AirportNames.getNamesMap();
            if (!airports.containsKey(searchSource) || !airports.containsKey(searchDest)) throw new InvalidAirportCode();
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
      if (prettyFlag)
      {
        if (args.length - counter == 1)
        {
          airlineName = args[counter];
          this.createFlight = false;
          return;
        }
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
      java.util.Map<String, String> airports = AirportNames.getNamesMap();
      if (!airports.containsKey(source) || !airports.containsKey(destination)) throw new InvalidAirportCode();
      this.createFlight = true;
    }
  }



}