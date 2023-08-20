package main.java.edu.pdx.cs410J.mattcole;

/**
 * The exception holder
 */
public class ExceptionHolder {

    public static class InvalidInput extends Exception {
    }

    public static class TooManyArgs extends InvalidInput {
        public String getMessage() {
            return MessageHolder.ErrorTooManyArgs;
        }
    }

    public static class TooFewArgs extends InvalidInput {
        public String getMessage() {
            return MessageHolder.ErrorTooFewArgs;
        }
    }

    public static class InvalidAirportCode extends InvalidInput {
        public String getMessage() {
            return MessageHolder.ErrorInvalidAirportCode;
        }
    }

    public static class InvalidFlightNumber extends InvalidInput {
        public String getMessage() {
            return MessageHolder.ErrorInvalidFlightNumber;
        }
    }

    public static class AirlineDoesntMatch extends InvalidInput {
        public String getMessage() {
            return MessageHolder.ErrorAirlineDoesntMatch;
        }
    }

    public static class InvalidOption extends InvalidInput {
        public String getMessage() {
            return MessageHolder.ErrorInvalidOption;
        }
    }

    public static class ReadmeOption extends Exception {
    }

    public static class ArrivesBeforeDeparts extends InvalidInput {
        public String getMessage() {
            return MessageHolder.ErrorArrivesBeforeDeparts;
        }
    }

    public static class BothxmlAndRegularFileSelected extends InvalidInput {
        public String getMessage() {
            return MessageHolder.ErrorBothxmlAndRegularFileSelected;
        }
    }

    public static class InvalidDateTime extends InvalidInput {
        public String getMessage() {
            return MessageHolder.ErrorInvalidDateTime;
        }
    }
}