package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * An integration test for the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {

        return invokeMain( Project1.class, args );
    }

  /**
   * Tests that invokes the main method with no arguments issues an error and prints help message
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertThat(result.getTextWrittenToStandardOut(), containsString("Missing command line arguments"));
      assertThat(result.getTextWrittenToStandardOut(), containsString("airline-2023.0.0.jar"));
  }

    /**
     * Tests that invokes the main method with too many arguments issues an error and prints out help message
     */
    @Test
    void testTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("1", "2","3","4","5","6","7", "8", "9", "10");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Too many command line arguments"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("airline-2023.0.0.jar"));
    }
    /**
     * Tests that ensures a flight produces the correct output
     */
    @Test
    void testFlightPrint()
    {
        MainMethodResult result = invokeMain("-print", "Delta", "5", "PDX","3/15/2023", "10:39","AAA","03/2/2023", "1:03");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 5 departs PDX at 3/15/2023 10:39 arrives AAA at 03/2/2023 1:03"));
    }
    /**
     * Tests that ensures invoking with print and readme produces no output and doesn't produce an error
     */
    @Test
    void testFlightDontPrint()
    {
        MainMethodResult result = invokeMain( "Delta", "5", "PDX","3/15/2023", "10:39","AAA","03/2/2023", "1:03");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }
    /**
     * Tests that ensures the proper response is given for an invalid flight number
     */
    @Test
    void testFlightInvalidFlightNumber()
    {
        MainMethodResult result = invokeMain("-print", "Delta", "not a number", "PDX","3/15/2023", "10:39","AAA","03/2/2023", "1:03");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Invalid flight number. Flight number should be an integer"));
    }
    /**
     * Tests that invokes main with an invalid airport
     */
    @Test
    void testFlightInvalidAirport()
    {
        MainMethodResult result = invokeMain( "Delta", "5", "not a valid airport","3/15/2023", "10:39","AAA","03/2/2023", "1:03");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Invalid airport code. Airport codes should be 3 letters eg. PDX"));
    }
    /**
     * Tests that invokes main with an invalid date time
     */
    @Test
    void testFlightInvalidDateTime()
    {
        MainMethodResult result = invokeMain( "Delta", "5", "pdx","Invalid time", "10:39","AAA","03/2/2023", "1:03");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Invalid date time given"));
    }
    /**
     * Tests that readme gives the correct response
     */
    @Test
    void testReadMePrint()
    {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("A simple program"));
    }


}