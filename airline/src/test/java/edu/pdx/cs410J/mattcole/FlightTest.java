package edu.pdx.cs410J.mattcole;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Flight} class.
 * You'll need to update these unit tests as you build out you program.
 */
public class FlightTest {

    /**
     * Creates a simple flight for use by other tests
     */
    Flight CreateSimpleFlight() {
        return new Flight(5);
    }

    /**
     * Tests to make sure that the flight number is stored and retrieved properly
     */
    @Test
    void CreateAndTestFlightNumber() {
        Flight flight = CreateSimpleFlight();
        assertThat(flight.getNumber(), equalTo(5));
    }
}
