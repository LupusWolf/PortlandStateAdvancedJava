package edu.pdx.cs410J.mattcole;

/**
 * Holds Various Consts And Errors
 */
public class constHolder {
  public static final String ErrorInvalidDateTime = "Invalid date time given. Date and time should be in the format:"
                                    + " mm/dd/yyyy hh:mm";
  public static final String ErrorInvalidAirportCode = "Airport code did not match any known airport.";
  public static final String ErrorInvalidNumber = "Invalid flight number. Flight number should be an integer";
  public static final String ErrorArrivesBeforeDeparts = "A flight departure must be before arrival";


  public static class InvalidAirportCode extends Exception {}

  public static class ArrivesBeforeDeparts extends Exception {}





}