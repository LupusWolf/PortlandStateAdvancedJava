package edu.pdx.cs410J.mattcole;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
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
    Airline setupBasicAirline()
    {
        var airlineName = "name";
        var airline = new Airline(airlineName);

        var flight1 = new Flight(flight1Numb, "source", "departure", "dest", "arrival");
        var flight2 = new Flight(flight2Numb, "source2", "departure2", "dest2", "arrival2");
        airline.addFlight(flight1);
        airline.addFlight(flight2);
        return airline;
    }
    @Test
    void testAirlineName()
    {
        var airline = setupBasicAirline();
        assertThat(airline.getName(), equalTo("name"));
    }
    /**
     * Makes sure that the collection returned by flights is of the correct size
     */
    @Test
    void testAirlineCount()
    {
        var airline = setupBasicAirline();
        var flights = airline.getFlights();
        assertThat(flights.size(), equalTo( 2));
    }

    /**
     * Makes sure that flights added to an airline can be retrieved properly
     */
    @Test
    void testAirlineNumber()
    {
        var airline = setupBasicAirline();
        Collection<Flight> flights = airline.getFlights();
        var array = flights.toArray(new Flight[0]);
        //Make sure order of flights is correct
        if (array[0].getNumber() < array[1].getNumber())
        {
            var temp = array[0];
            array[0] = array[1];
            array[1] = temp;
        }
        assertThat(array[0].getNumber(), equalTo(flight1Numb));
        assertThat(array[1].getNumber(), equalTo(flight2Numb));
    }
}
