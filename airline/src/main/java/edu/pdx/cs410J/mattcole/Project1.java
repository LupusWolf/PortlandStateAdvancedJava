package edu.pdx.cs410J.mattcole;

import com.google.common.annotations.VisibleForTesting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {
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
            InputStream resource = Project1.class.getResourceAsStream(name)
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
 * The main function. Asks the user to input a flight then prints it back out to the screen if desired
 * @param args Used to pass in flight info to program
 */
  public static void main(String[] args) {
    int counter = 0;
    boolean readmeFlag = false;
    boolean printFlag = false;
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
        System.out.println("Missing command line arguments");
        printResource("help.txt");
      } else {
        System.out.println("Too many command line arguments");
        printResource("help.txt");
      }
      for (String arg : args) {
        System.out.println(arg);
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
        System.out.println("Invalid date time given");
        printResource("help.txt");
      } else if (!(source.length() == 3 && destination.length() == 3)) //Check if airport codes are valid
      {
        System.out.println("Invalid airport code. Airport codes should be 3 letters eg. PDX");
        printResource("help.txt");
      } else if (!isInteger(args[counter + 1]))
      {
        System.out.println("Invalid flight number. Flight number should be an integer");
      } else { //Otherwise we are good to go!
        int number = Integer.parseInt(args[counter + 1]);
        var flight = new Flight(number, source, departure, destination, arrival);
        var airline = new Airline(airlineName);
        airline.addFlight(flight);
        if (printFlag) {
          System.out.println(flight);
        }
      }
    }
  }


}