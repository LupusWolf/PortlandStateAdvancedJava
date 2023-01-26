package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Airline class for the airline project. Stores a name and a list of flights
 */
public class Airline extends AbstractAirline<Flight> {
  /**
   * The name of the airline
   */
  private final String name;

  public ArrayList<Flight> flights;

  /**
   * Instantiates an airline from a name
   * @param name Airline name
   */
  public Airline(String name) {
    this.name = name;
    this.flights = new ArrayList<>();
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
    flights.add(flight);
  }

  /**
   * Gets the flights provided by the airline
   * @return collection of flights
   */
  @Override
  public Collection<Flight> getFlights() {
    return flights;
  }
}
