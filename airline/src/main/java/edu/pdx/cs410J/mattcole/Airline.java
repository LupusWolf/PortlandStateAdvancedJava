package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Airline class for the airline project. Stores a name and a list of flights
 */
public class Airline extends AbstractAirline<Flight> {
  /**
   * The name of the airline
   */
  private final String name;

  public HashMap<Integer, Flight> flights;

  /**
   * Instantiates an airline from a name
   * @param name Airline name
   */
  public Airline(String name) {
    this.name = name;
    this.flights = new HashMap<>();
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
    flights.put(flight.getNumber(),flight);
  }

  /**
   * Gets the flights provided by the airline
   * @return collection of flights
   */
  @Override
  public Collection<Flight> getFlights() {
    return flights.values();
  }

  /**
   * Allows us to set an individual field of a flight based on its flight number. If the flight doesn't exist then it
   * will be created. This makes it much easier to parse files.
   * @param flightNumber The flight number of the data to store
   * @param field The field we are setting
   * @param value The value we are setting the field to
   */
  public void SetDetailOfFlight(int flightNumber, Flight.Fields field, String value)
  {
    if (!flights.containsKey(flightNumber))
    {
      flights.put(flightNumber, new Flight(flightNumber));
    }
    flights.get(flightNumber).setFieldByEnum(field,value);
  }
}
