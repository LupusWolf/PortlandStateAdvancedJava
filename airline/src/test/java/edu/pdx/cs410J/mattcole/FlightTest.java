package edu.pdx.cs410J.mattcole;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Flight} class.
 * You'll need to update these unit tests as you build out you program.
 */
public class FlightTest {

  /**
   * Creates a simple flight for use by other tests
   */
  Flight CreateSimpleFlight()
  {
    return new Flight(5, "source", "departure", "dest", "arrival");
  }

  /**
   * Tests to make sure that the flight number is stored and retrieved properly
   */
  @Test
  void CreateAndTestFlightNumber()
  {
    Flight flight = CreateSimpleFlight();
    assertThat(flight.getNumber(), equalTo(5));
  }
  /**
   * Tests to make sure that the flight source is stored and retrieved properly
   */
  @Test
  void CreateAndTestFlightSource()
  {
    Flight flight = CreateSimpleFlight();
    assertThat(flight.getSource(), equalTo("source"));
  }
  /**
   * Tests to make sure that the flight departure is stored and retrieved properly
   */
  @Test
  void CreateAndTestFlightDeparture()
  {
    Flight flight = CreateSimpleFlight();
    assertThat(flight.getDepartureString(), equalTo("departure"));
  }
  /**
   * Tests to make sure that the flight dest is stored and retrieved properly
   */
  @Test
  void CreateAndTestFlightDest()
  {
    Flight flight = CreateSimpleFlight();
    assertThat(flight.getDestination(), equalTo("dest"));
  }

  /**
   * Tests to make sure that the flight arrival is stored and retrieved properly
   */
  @Test
  void CreateAndTestFlightArrival()
  {
    Flight flight = CreateSimpleFlight();
    assertThat(flight.getArrivalString(), equalTo("arrival"));
  }
}
