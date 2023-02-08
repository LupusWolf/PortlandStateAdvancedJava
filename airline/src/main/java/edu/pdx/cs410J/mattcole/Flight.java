package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirportNames;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Flight class for the airline project! This holds basic info about flights
 */
public class Flight extends AbstractFlight implements java.lang.Comparable<Flight> {
  private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h':'mm a");

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
  Date departure;
  /**
   * Where the flight is headed to
   */
  String destination;
  /**
   * When the flight is going to arrive at its destination
   */
  Date arrival;
  /**
   * Duration of the flight. Has the value -1 before it is set
   */
  long durationInMinutes = -1;

  @Override
  public int compareTo(Flight o) {
    int stringCompare = source.compareTo(o.getSource());
    if (stringCompare != 0)
    {
      return stringCompare;
    }
    return o.getDeparture().compareTo(departure);
  }

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
  public void setFieldByEnum(Fields field, String value) throws FlightParseDateTimeException, Project3.ArrivesBeforeDeparts, Project3.InvalidAirportCode {
    try {
      switch (field) {
        case source:
          if (!AirportNames.getNamesMap().containsKey(value)) throw new Project3.InvalidAirportCode();
          source = value;
          break;
        case departure:
          departure = dateFormat.parse(value);
          break;
        case destination:
          if (!AirportNames.getNamesMap().containsKey(value)) throw new Project3.InvalidAirportCode();
          destination = value;
          break;
        case arrival:
          arrival = dateFormat.parse(value);
          break;
      }
      if (durationInMinutes == -1 && arrival != null && departure != null)
      {
        if (arrival.compareTo(departure) < 0)
        {
          throw new Project3.ArrivesBeforeDeparts();
        }
        var timeAsLong = arrival.getTime() - departure.getTime();
        var millisecondsInMinute = 1000 * 60;
        durationInMinutes = timeAsLong/millisecondsInMinute;
      }
    } catch (ParseException e)
    {
      throw new FlightParseDateTimeException();
    }
  }
  public long getDurationInMinutes()
  {
    return durationInMinutes;
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
        if (departure == null) return null;
        return dateFormat.format(departure);
      case destination:
        return destination;
      case arrival:
        if (arrival == null) return null;
        return dateFormat.format(arrival);
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
   * Returns the time when the flight is leaving as a string
   * @return Time when the flight is leaving as a string
   */
  @Override
  public String getDepartureString() {
    return dateFormat.format(departure);
  }
  /**
   * Returns the time when the flight is leaving as a date
   *
   * @return Time when the flight is leaving as a date
   */
  @Override
  public Date getDeparture() {
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
   * Gets the time of arrival for the flight as a string
   * @return time when the flight will land as a string
   */
  @Override
  public String getArrivalString() {
    return dateFormat.format(arrival);
  }
  /**
   * Gets the time of arrival for the flight as a date
   * @return time when the flight will land as a date
   */
  @Override
  public Date getArrival() {
    return arrival;
  }

  /**
   * Returns a boolean if every field has been assigned too
   * @return if every field in the flight has been assigned
   */
  public boolean isFullyDefined()
  {
    for (Fields field : Fields.values())
    {
      if (getFieldByEnum(field) == null)
      {
        return false;
      }
    }
    return true;
  }
  /**
   * Pretty print flight with given format. Format is given as 'Source is %source%'
   */
  public String prettyFlightString(String format)
  {
    assert(isFullyDefined());
    var current = format;
    for (Fields field : Fields.values())
    {
      var fieldValue = getFieldByEnum(field);
      if (field == Fields.source || field == Fields.destination) fieldValue=AirportNames.getNamesMap().get(fieldValue);
      current = current.replace("%" + field.name() + "%",fieldValue);
    }
    current = current.replace("%number%", "" + number);
    current = current.replace("%duration%", "" + durationInMinutes);
    return current;
  }
  /**
   * Create flight just by number
   * @param number number of instantiated flight
   */
  public Flight(int number)
  {
    this.number = number;
  }
  public static class FlightParseDateTimeException extends Exception {}
  /**
   * Creates a new flight with the given values
   * @param number flight number
   * @param source where the flight is coming from
   * @param departure when the flight is leaving
   * @param destination where the flight is heading
   * @param arrival when the flight is going to arrive at its destination
   */

  public Flight(int number, String source, String departure, String destination, String arrival) throws FlightParseDateTimeException, Project3.ArrivesBeforeDeparts, Project3.InvalidAirportCode {
    this.number = number;
    setFieldByEnum(Fields.source,source);
    setFieldByEnum(Fields.departure,departure);
    setFieldByEnum(Fields.destination,destination);
    setFieldByEnum(Fields.arrival,arrival);
  }
}
