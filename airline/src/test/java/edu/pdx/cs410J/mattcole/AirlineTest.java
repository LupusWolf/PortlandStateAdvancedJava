package edu.pdx.cs410J.mattcole;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AirlineTest {
    /**
     * first flight number for testing
     */
    final int flight1Numb = 44;
    /**
     * second flight number for testing
     */
    final int flight2Numb = 43;

    /**
     * Sets up a basic airline for testing
     */
    Airline setupBasicAirline() throws Flight.FlightParseDateTimeException, Project4.ArrivesBeforeDeparts, Project4.InvalidAirportCode {
        var airlineName = "name";
        var airline = new Airline(airlineName);

        var flight1 = new Flight(flight1Numb, "PDX", "03/15/2023 10:31 PM", "ABE", "03/16/2023 1:03 PM");
        var flight2 = new Flight(flight2Numb, "PDX", "03/15/2023 10:32 PM", "ABE", "03/16/2023 1:03 PM");
        airline.addFlight(flight1);
        airline.addFlight(flight2);
        return airline;
    }

    @Test
    void testAirlineName() throws Project4.ArrivesBeforeDeparts, Flight.FlightParseDateTimeException, Project4.InvalidAirportCode {
        var airline = setupBasicAirline();
        assertThat(airline.getName(), equalTo("name"));
    }

    /**
     * Makes sure that the collection returned by flights is of the correct size
     */
    @Test
    void testAirlineCount() throws Project4.ArrivesBeforeDeparts, Flight.FlightParseDateTimeException, Project4.InvalidAirportCode {
        var airline = setupBasicAirline();
        var flights = airline.getFlights();
        assertThat(flights.size(), equalTo(2));
    }

    /**
     * Makes sure that flights added to an airline can be retrieved properly
     */
    @Test
    void testAirlineNumber() throws Project4.ArrivesBeforeDeparts, Flight.FlightParseDateTimeException, Project4.InvalidAirportCode {
        var airline = setupBasicAirline();
        Collection<Flight> flights = airline.getFlights();
        var array = flights.toArray(new Flight[0]);
        ///Make sure order of flights is correct
        if (array[0].getNumber() < array[1].getNumber()) {
            var temp = array[0];
            array[0] = array[1];
            array[1] = temp;
        }
        assertThat(array[0].getNumber(), equalTo(flight1Numb));
        assertThat(array[1].getNumber(), equalTo(flight2Numb));
    }
}
