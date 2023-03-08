package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {

    public static final String ErrorInvalidNumber = "Invalid flight number. Flight number should be an integer";
    public static final String CALL_AGAIN_WITH_README_TO_SEE_USAGE = "Call again with -readme to see usage";
    public static final String ErrorUnknownOption = "Fatal error. Unknown option: ";
    public static final String ErrorHostnameAndPortAreRequired = "Hostname and port are required!";
    public static final String PORT_NUMBER_NEEDS_TO_BE_AN_INT = "Port number needs to be an int";
    public static final String ERROR_CONNECTING_TO_HOST = "Error connecting to host";
    public static final String SERVER_SENT_INVALID_RESPONSE = "Server sent invalid response";
    public static final String INVALID_COMMAND_LINE = "Invalid command line";


    /**
     * Prints a resource file line by line to standard out
     * @param name name of resource to print
     */
    static void printResource(String name)
    {
        //copied from the test
        try (
                InputStream resource = constHolder.class.getResourceAsStream(name)
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
        List<String> options = List.of(new String[]{"-search", "-print", "-README"});
        List<String> argOption = List.of(new String[]{"-host","-port"});
        Map<String,String> values = new HashMap<>();
        int counter = 0;
        while (counter < args.length && args[counter].startsWith("-")) //Check for options
        {
            if (options.contains(args[counter]))
            {
                values.put(args[counter], "");
            } else if (argOption.contains(args[counter]))
            {
                if (counter + 1 < args.length)
                {
                    values.put(args[counter], args[counter+1]);
                    counter++;
                } else {
                    System.out.println("Error: " + args[counter] + " requires an argument");
                    System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
                    return;
                }
            } else {
                System.out.println(ErrorUnknownOption);
                return;
            }
            counter++;

        }
        if (values.get("-README") != null)
        {
            printResource("README.txt");
            return;
        }
        if (values.get("-host") != null && values.get("-port") != null)
        {
            AirlineRestClient client;
            try {
                client = new AirlineRestClient(values.get("-host"), Integer.parseInt(values.get("-port")));
            } catch (NumberFormatException e) {
                System.out.println(PORT_NUMBER_NEEDS_TO_BE_AN_INT);
                System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
                return;
            }
            try {
                int remainingArgs = args.length - counter;
                if (remainingArgs == 1 && values.get("-search") != null)
                {
                    client.writeOutAirline(args[counter],new PrintWriter(System.out));
                } else if (remainingArgs == 3 && values.get("-search") != null)
                {
                    client.writeOutAirlineSearch(args[counter],args[counter+1],args[counter+2],
                            new PrintWriter(System.out));
                } else if (remainingArgs == 10)
                {
                    int number = 0;
                    var airlineName = args[counter];
                    try {
                        number = Integer.parseInt(args[counter + 1]);
                    } catch (NumberFormatException e) {
                        System.out.println(ErrorInvalidNumber);
                        System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
                        return;
                    }
                    var source = args[counter + 2];
                    var departure = args[counter + 3] + " " + args[counter + 4] + " " + args[counter + 5];
                    var destination = args[counter + 6];
                    var arrival = args[counter + 7] + " " + args[counter + 8] + " " + args[counter + 9];
                    Writer writer;
                    if (values.get("-print") != null)
                    {
                        writer = new PrintWriter(System.out);
                    } else {
                        writer = null;
                    }
                    client.postAirline(airlineName, number, source, departure, destination, arrival, writer);
                } else {
                    System.out.println(INVALID_COMMAND_LINE);
                    System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
                }
            } catch (IOException e) {
                System.out.println(ERROR_CONNECTING_TO_HOST);
            } catch (ParserException e) {
                System.out.println(SERVER_SENT_INVALID_RESPONSE);
            } catch (HttpRequestHelper.RestException e)
            {
                System.out.println(e.getMessage());
                System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
            }
        } else {
            System.out.println(ErrorHostnameAndPortAreRequired);
            System.out.println(CALL_AGAIN_WITH_README_TO_SEE_USAGE);
        }
    }
}