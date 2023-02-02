package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * An integration test for the {@link Project2} main class.
 */
class Project2IT extends InvokeMainTestCase {




    /**
     * Invokes the main method of {@link Project2} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {

        return invokeMain( Project2.class, args );
    }
  /**
   * Tests that invokes the main method with an invalid option
   */
  @Test
  void testInvalidOption() {
      MainMethodResult result = invokeMain("-print", "-invalid_option", "Delta", "5", "PDX","3/15/2023", "10:39","AAA","03/2/2023", "1:03");
      assertThat(result.getTextWrittenToStandardOut(), containsString(Project2.ErrorUnknownOption));
  }
    /**
     * Tests that invokes the main method with no arguments issues an error and prints help message
     */
    @Test
    void testNoCommandLineArguments() {
        MainMethodResult result = invokeMain();
        assertThat(result.getTextWrittenToStandardOut(), containsString("usage"));
    }
    /**
     * Tests that invokes the main method with too few arguments issues an error and prints out help message
     */
    @Test
    void testTooFewCommandLineArguments() {
        MainMethodResult result = invokeMain("1", "2","3","4");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project2.ErrorTooFewArgs));
    }
    /**
     * Tests that invokes the main method with too many arguments issues an error and prints out help message
     */
    @Test
    void testTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("1", "2","3","4","5","6","7", "8", "9", "10");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project2.ErrorTooManyArgs));
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
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project2.ErrorInvalidNumber));
    }
    /**
     * Tests that invokes main with an invalid airport
     */
    @Test
    void testFlightInvalidAirport()
    {
        MainMethodResult result = invokeMain( "Delta", "5", "not a valid airport","3/15/2023", "10:39","AAA","03/2/2023", "1:03");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project2.ErrorInvalidAirportCode));
    }
    /**
     * Tests that invokes main with an invalid date time
     */
    @Test
    void testFlightInvalidDateTime()
    {
        MainMethodResult result = invokeMain( "Delta", "5", "pdx","Invalid time", "10:39","AAA","03/2/2023", "1:03");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project2.ErrorInvalidDateTime));
    }
    /**
     * Tests that readme gives the correct response
     */
    @Test
    void testReadMePrint()
    {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Matthew"));
    }
    /**
     * Tests that ensures a flight produces the correct output when used with file
     */
    @Test
    void testFlightPrintWithFile(@TempDir File tempDir)
    {
        File file = new File(tempDir, "airline.txt");
        MainMethodResult result = invokeMain("-textFile", file.getAbsolutePath(), "-print", "Delta", "5", "PDX","3/15/2023", "10:39","AAA","03/2/2023", "1:03");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 5 departs PDX at 3/15/2023 10:39 arrives AAA at 03/2/2023 1:03"));
    }
    /**
     * Tests that invokes main and passes in a file with the wrong airline name
     */
    @Test
    void testNonmatchingAirline(@TempDir File tempDir) throws IOException {
        File file = new File(tempDir, "airline.txt");
        Files.write(file.toPath(), "Delta".getBytes());
        MainMethodResult result = invokeMain( "-textFile", file.getAbsolutePath(), "NotDelta", "5", "pdx","03/2/2023", "10:39","AAA","03/2/2023", "1:03");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project2.ErrorAirlineDoesntMatch));
    }
    /**
     * Tests that invokes main with a malformed file
     */
    @Test
    void testMalformedFile(@TempDir File tempDir) throws IOException {
        File file = new File(tempDir, "airline.txt");
        Files.write(file.toPath(), "Delta\nfghasddfi".getBytes());
        MainMethodResult result = invokeMain( "-textFile", file.getAbsolutePath(), "NotDelta", "5", "pdx","03/2/2023", "10:39","AAA","03/2/2023", "1:03");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project2.ErrorMalformedFile));
    }

}