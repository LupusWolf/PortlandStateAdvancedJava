package main.java.edu.pdx.cs410J.mattcole;

public class MessageHolder {
    public static final String ErrorInvalidDateTime = "Invalid date time given. Date and time should be in the format:"
            + " mm/dd/yyyy hh:mm";
    public static final String ErrorInvalidAirportCode = "Airport code did not match any known airport.";
    public static final String ErrorInvalidFlightNumber = "Invalid flight number. Flight number should be an integer";
    public static final String ErrorTooFewArgs = "Missing command line arguments";
    public static final String ErrorTooManyArgs = "Too many command line arguments";
    public static final String CALL_AGAIN_WITH_README_TO_SEE_USAGE = "Call again with -readme to see usage";
    public static final String ErrorInvalidOption = "Fatal error. Unknown option: ";
    public static final String ErrorAirlineDoesntMatch = "Airline provided did not match airline of file";
    public static final String ErrorMalformedFile = "Malformed input file!";
    public static final String ErrorArrivesBeforeDeparts = "A flight departure must be before arrival";
    public static final String ErrorBothxmlAndRegularFileSelected = "Can only read from a text OR an xml file not both!";
}
