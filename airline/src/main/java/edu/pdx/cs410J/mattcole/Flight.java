package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AbstractFlight;

/**
 * Flight class for the airline project! This holds basic info about flights
 */
public class Flight extends AbstractFlight {
  /**
   * Flight number
   */
  int number;
  /**
   * Where the flight came from
   */
  String source;
  /**
   * When the flight is to depart
   */
  String departure;
  /**
   * Where the flight is headed to
   */
  String destination;
  /**
   * When the flight is going to arrive at its destination
   */
  String arrival;

  /**
   * Each value in this enum corresponds to a piece of flight data that is written out to and retrieved from a
   * file. By using the name and valueof enum methods, we are able to cleanly specify a file format without the use
   * of long string switch cases.
   */
  public enum Fields {source, departure, destination, arrival }

  /**
   * This method allows us to set a field based on an enum value provided by the fields enum. When combined with the
   * Fields.ValueOf method, we are able to store data based on a string name provided in a file.
   * @param field field we want to set
   * @param value The value we want to set it too
   */
  public void setFieldByEnum(Fields field, String value)
  {
    switch (field)
    {
      case source:
        source = value;
        break;
      case departure:
        departure = value;
        break;
      case destination:
        destination = value;
        break;
      case arrival:
        arrival = value;
        break;
    }
  }

  /**
   * This allows us to access data in the flight based upon the enum provided in this class. This makes it easier to
   * store data to a file by allowing us to loop through the fields and get each one.
   * @param field Field we want to get from the flight
   * @return The value of the field
   */
  public String getFieldByEnum(Fields field)
  {
    switch (field)
    {
      case source:
        return source;
      case departure:
        return departure;
      case destination:
        return destination;
      case arrival:
        return arrival;
    }
    assert(false);
    return ""; //There is no way we should reach here
  }
  /**
   * Gets flight number
   * @return flight number
   */
  @Override
  public int getNumber() {
    return number;
  }

  /**
   * Returns where the flight is leaving from
   * @return Where the flight is leaving from
   */
  @Override
  public String getSource() {
    return source;
  }

  /**
   * Returns the time when the flight is leaving
   * @return Time when the flight is leaving
   */
  @Override
  public String getDepartureString() {
    return departure;
  }

  /**
   * Gets the intended destination of the flight
   * @return the destination of the flight
   */
  @Override
  public String getDestination() {
    return destination;
  }

  /**
   * Gets the time of arrival for the flight
   * @return time when the flight will land
   */
  @Override
  public String getArrivalString() {
    return arrival;
  }

  /**
   * Create flight just by number
   * @param number number of instantiated flight
   */
  public Flight(int number)
  {
    this.number = number;
  }
  /**
   * Creates a new flight with the given values
   * @param number flight number
   * @param source where the flight is coming from
   * @param departure when the flight is leaving
   * @param destination where the flight is heading
   * @param arrival when the flight is going to arrive at its destination
   */

  public Flight(int number, String source, String departure, String destination, String arrival)
  {
    this.number = number;
    this.source = source;
    this.departure = departure;
    this.destination = destination;
    this.arrival = arrival;
  }
}
