package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Parses data from a given input stream into an airline
 */
public class TextParser implements AirlineParser<Airline> {
  private final Reader reader;
  /**
   * The delimiter that is used by the parser and by the text dumper
   */
  public final static String delimiter = ";";

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * Parse data from reader into an airline
   * @return An airline parsed from the file
   * @throws ParserException Throws the parser exception if the data is malformed
   */
  @Override
  public Airline parse() throws ParserException {
    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {

      String airlineName = br.readLine();

      if (airlineName == null) {
        throw new ParserException("Missing airline name");
      }
      Airline airline = new Airline(airlineName);
      while (br.ready())
      {
        var splitLine = br.readLine().split(delimiter);
        var flightNumber = Integer.parseInt(splitLine[0]);
        var field = Flight.Fields.valueOf(splitLine[1]);
        var value = splitLine[2];
        airline.SetDetailOfFlight(flightNumber,field, value);
      }

      return airline;

    } catch (NumberFormatException | IOException e) {
      throw new ParserException("While parsing airline text", e);
    } catch (Flight.FlightParseDateTimeException e) {
      throw new ParserException("Error parsing date time in file");
    } catch (ArrayIndexOutOfBoundsException e) { //Really if we have any sort of error here we should just inform the user of a malformed
                            //file
      throw new ParserException("Error parsing file", e);
    } catch (Project4.ArrivesBeforeDeparts e) {
      throw new ParserException("Flight in file arrives before it departs", e);
    } catch (Project4.InvalidAirportCode e) {
      throw new ParserException("Invalid airport code", e);
    }
  }
}
