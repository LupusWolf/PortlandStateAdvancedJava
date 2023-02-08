package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AbstractAirline;

import java.util.*;

/**
 * Airline class for the airline project. Stores a name and a list of flights
 */
public class Airline extends AbstractAirline<Flight> {
  /**
   * The name of the airline
   */
  private final String name;
  /**
   * Used to build flights one property at a time
   */
  public HashMap<Integer, Flight> flightBuilder;
  /**
   * Stores flights in sorted order
   */
  public TreeSet<Flight> flights;

  /**
   * Instantiates an airline from a name
   * @param name Airline name
   */
  public Airline(String name) {
    this.name = name;
    this.flightBuilder = new HashMap<>();
    flights = new TreeSet<>();
  }

  /**
   * Gets the name of the airline
   * @return The name of the airline
   */
  @Override
  public String getName() {
    return this.name;
  }
  /**
   * Adds a flight to the airline
   * @param flight flight to add
   */
  @Override
  public void addFlight(Flight flight) {
    if (flight.isFullyDefined()) {
      flights.add(flight);
    }else {
      flightBuilder.put(flight.getNumber(),flight);
    }
  }

  /**
   * Gets the flights provided by the airline
   * @return collection of flights
   */
  @Override
  public Collection<Flight> getFlights() {
    return flights;
  }
  /**
   * Allows us to set an individual field of a flight based on its flight number. If the flight doesn't exist then it
   * will be created. This makes it much easier to parse files.
   * @param flightNumber The flight number of the data to store
   * @param field The field we are setting
   * @param value The value we are setting the field to
   */
  public void SetDetailOfFlight(int flightNumber, Flight.Fields field, String value) throws
          Flight.FlightParseDateTimeException, Project3.ArrivesBeforeDeparts, Project3.InvalidAirportCode {
    if (!flightBuilder.containsKey(flightNumber))
    {
      flightBuilder.put(flightNumber, new Flight(flightNumber));
    }
    Flight flight = flightBuilder.get(flightNumber);
    flight.setFieldByEnum(field,value);
    if (flight.isFullyDefined())
    {
      flights.add(flight);
      flightBuilder.remove(flight);
    }
  }

}
