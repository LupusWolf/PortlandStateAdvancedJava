package edu.pdx.cs410J.mattcole;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;

import java.io.*;

/**
 * The main class for the CS410J airline Project
 */
public class Project2 {
  public static final String ErrorInvalidDateTime = "Invalid date time given. Date and time should be in the format:"
                                    + " mm/dd/yyyy hh:mm";
  public static final String ErrorInvalidAirportCode = "Invalid airport code. Airport codes should be 3 letters eg PDX";
  public static final String ErrorInvalidNumber = "Invalid flight number. Flight number should be an integer";
  public static final String ErrorTooFewArgs = "Missing command line arguments";
  public static final String ErrorTooManyArgs = "Too many command line arguments";
  public static final String CALL_AGAIN_WITH_README_TO_SEE_USAGE = "Call again with -readme to see usage";
  public static final String ErrorUnknownOption = "Fatal error. Unknown option: ";
  public static final String ErrorAirlineDoesntMatch = "Airline provided did not match airline of file";
  public static final String ErrorMalformedFile = "Malformed input file!";

  /**
   * Determines whether a string contains an integer
   * @param input the string to check
   * @return whether the string is an integer
   */
  static boolean isInteger(String input)
  {
    try {
      Integer.parseInt(input);
    } catch (Exception e)
    {
      return false;
    }
    return true;
  }

  /**
   * Prints a resource file line by line to standard out
   * @param name name of resource to print
   */
  public static void printResource(String name)
  {
    //copied from the test
    try (
            InputStream resource = Project2.class.getResourceAsStream(name)
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
   * Checks whether a string contains a valid date and time
   * @param dateAndTime string to check
   * @return returns true if the string contains a valid date and time
   */
  @VisibleForTesting
  static boolean isValidDateAndTime(String dateAndTime) {
    try {
      var isValid = true; //Used to store whether the string is valid after each step
      var spaceSeperated = dateAndTime.split(" "); //Seperate date and time
      var date = spaceSeperated[0];
      var time = spaceSeperated[1];

      var dateSeperated = date.split("/"); //Seperate date into chunks then check if each chunk is valid
      isValid &= isInteger(dateSeperated[0]);
      isValid &= dateSeperated[0].length() <= 2;
      isValid &= dateSeperated[0].length() != 0;
      isValid &= isInteger(dateSeperated[1]);
      isValid &= dateSeperated[1].length() <= 2;
      isValid &= dateSeperated[1].length() != 0;
      isValid &= isInteger(dateSeperated[2]);
      isValid &= dateSeperated[2].length() == 4;

      var timeSeperated = time.split(":"); //Seperate time into chunks then check if each chunk is valid
      isValid &= isInteger(timeSeperated [0]);
      isValid &= timeSeperated[0].length() <= 2;
      isValid &= timeSeperated[0].length() != 0;
      isValid &= isInteger(timeSeperated [1]);
      isValid &= timeSeperated[1].length() <= 2;
      isValid &= timeSeperated[1].length() != 0;
      return isValid;

    } catch (Exception exception)
    {
      return false; //If we run into an error while parsing then it is not a valid date time
    }

  }
/**
 * The main function. Asks the user to input a flight then prints it back out to the screen if desired.
 * Additionally, will optionally read/write airline data to a file.
 * @param args Used to pass in flight info to program
 */
  public static void main(String[] args) {
    int counter = 0;
    boolean readmeFlag = false;
    boolean printFlag = false;
    boolean textFileFlag = false;
    String pathToTextFile = "";
    if (args.length == 0)
    {
      printResource("help.txt");
      return;
    }
    while (counter < args.length && args[counter].startsWith("-")) //Check for options
    {
      switch (args[counter])
      {
        case "-README":
          readmeFlag = true;
          break;
        case "-print":
          printFlag = true;
          break;
        case "-textFile":
          counter++;
          pathToTextFile = args[counter];
          textFileFlag = true;
          break;
        default:
          System.out.println(ErrorUnknownOption + args[counter]);
          return;
      }
      counter++;
    }
    if (readmeFlag) //If we see the readme option, print then exit
    {
        printResource("README.txt");
    } else if (args.length - counter != 8) //If we have an incorrect number of args respond appropriately
    {
      if (args.length - counter < 8)
      {
        System.out.println(ErrorTooFewArgs);
        System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      } else {
        System.out.println(ErrorTooManyArgs);
        System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      }
    } else {
      //Seperate args into inputs for making the flight and validation. Use counter as an offset for the options
      String airlineName = args[counter];
      String source = args[counter + 2];
      String departure = args[counter + 3] + " " + args[counter + 4];
      String destination = args[counter + 5];
      String arrival = args[counter + 6] + " " + args[counter + 7];

      if (!(isValidDateAndTime(departure) && isValidDateAndTime(arrival))) //Check if date and time are valid
      {
        System.out.println(ErrorInvalidDateTime);
        System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      } else if (!(source.length() == 3 && destination.length() == 3)) //Check if airport codes are valid
      {
        System.out.println(ErrorInvalidAirportCode);
        System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
      } else if (!isInteger(args[counter + 1]))
      {
        System.out.println(ErrorInvalidNumber);
      } else { //Otherwise we are good to go!
        int number = Integer.parseInt(args[counter + 1]);
        var flight = new Flight(number, source, departure, destination, arrival);
        Airline airline;
        File file = null;
        if (textFileFlag)
        {
          file = new File(pathToTextFile);
          try {
            var fileReader = new FileReader(file);
            airline = new TextParser(fileReader).parse();
            fileReader.close();
            if (!airline.getName().equals(airlineName)) //Make sure airline in file matches given airline
            {
              System.out.println(ErrorAirlineDoesntMatch);
              System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
              return;
            }
          } catch (FileNotFoundException exception) {
            airline = new Airline(airlineName); //If it doesn't exist just create new airline
          } catch (ParserException parserException)
          {
            System.out.println(ErrorMalformedFile); //Tell the user if we have been given a malformed file
            return;
          } catch (IOException e) {
            assert(false); //Shouldn't occur
            throw new RuntimeException(e);
          }
        } else {
          airline = new Airline(airlineName);
        }
        airline.addFlight(flight);
        if (printFlag) {
          System.out.println(flight);
        }
        if (textFileFlag)
        {
          assert(file != null); //The file shouldn't be null if textFileFlag is True
          try {
            FileWriter fileWriter = new FileWriter(file);
            new TextDumper(fileWriter).dump(airline);
            fileWriter.close();
          } catch (IOException e) {
            System.out.println("Unable to write to file");
          }
        }
      }
    }
  }


}