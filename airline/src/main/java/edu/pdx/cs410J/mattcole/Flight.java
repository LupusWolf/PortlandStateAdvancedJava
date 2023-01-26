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
