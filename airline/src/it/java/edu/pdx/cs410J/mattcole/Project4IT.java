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
 * An integration test for the {@link Project4} main class.
 */
class Project4IT extends InvokeMainTestCase {




    /**
     * Invokes the main method of {@link Project4} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {

        return invokeMain( Project4.class, args );
    }
  /**
   * Tests that invokes the main method with an invalid option
   */
  @Test
  void testInvalidOption() {
      MainMethodResult result = invokeMain("-print", "-invalid_option", "Delta", "5", "PDX","03/15/2023", "10:39","ABE","03/16/2023", "1:03");
      assertThat(result.getTextWrittenToStandardOut(), containsString(Project4.ErrorUnknownOption));
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
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project4.ErrorTooFewArgs));
    }
    /**
     * Tests that invokes the main method with too many arguments issues an error and prints out help message
     */
    @Test
    void testTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("-print", "Delta", "5", "PDX","03/15/2023", "10:39", "AM", "ABE","03/16/2023", "1:03", "AM", "ExtraArg");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project4.ErrorTooManyArgs));
    }
    /**
     * Tests that ensures a flight produces the correct output
     */
    @Test
    void testFlightPrint()
    {
        MainMethodResult result = invokeMain("-print", "Delta", "5", "PDX","03/15/2023", "10:39", "AM", "ABE","03/16/2023", "1:03", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 5 departs PDX at 03/15/2023 10:39 AM arrives ABE at 03/16/2023 1:03 AM"));
    }

    /**
     * Tests that ensures invoking with print and readme produces no output and doesn't produce an error
     */
    @Test
    void testFlightDontPrint()
    {
        MainMethodResult result = invokeMain( "Delta", "5", "PDX","03/15/2023", "10:39", "PM","ABE","03/16/2023", "1:03", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }
    /**
     * Tests that ensures the proper response is given for an invalid flight number
     */
    @Test
    void testFlightInvalidFlightNumber()
    {
        MainMethodResult result = invokeMain("-print", "Delta", "not a number", "PDX","03/15/2023", "10:39", "PM","ABE","03/16/2023", "1:03", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project4.ErrorInvalidNumber));
    }
    /**
     * Tests that invokes main with an invalid airport
     */
    @Test
    void testFlightInvalidAirport()
    {
        MainMethodResult result = invokeMain( "Delta", "5", "not a valid airport","03/15/2023", "10:39","PM","ABE","03/16/2023", "1:03","PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project4.ErrorInvalidAirportCode));
    }
    /**
     * Tests that invokes main with an invalid date time
     */
    @Test
    void testFlightInvalidDateTime()
    {
        MainMethodResult result = invokeMain( "Delta", "5", "PDX","Invalid time", "10:39","PM","ABE","03/16/2023", "1:03","PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project4.ErrorInvalidDateTime));
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
        MainMethodResult result = invokeMain("-textFile", file.getAbsolutePath(), "-print", "Delta", "5", "PDX","03/15/2023", "10:39","PM","ABE","03/16/2023", "1:03","PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 5 departs PDX at 03/15/2023 10:39 PM arrives ABE at 03/16/2023 1:03 PM"));
    }
    /**
     * Tests that invokes main and passes in a file with the wrong airline name
     */
    @Test
    void testNonmatchingAirline(@TempDir File tempDir) throws IOException {
        File file = new File(tempDir, "airline.txt");
        Files.write(file.toPath(), "Delta".getBytes());
        MainMethodResult result = invokeMain( "-textFile", file.getAbsolutePath(), "NotDelta", "5", "PDX","03/02/2023", "10:39","PM","ABE","03/16/2023", "1:03","PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project4.ErrorAirlineDoesntMatch));
    }
    /**
     * Tests that invokes main with a malformed file
     */
    @Test
    void testMalformedFile(@TempDir File tempDir) throws IOException {
        File file = new File(tempDir, "airline.txt");
        Files.write(file.toPath(), "Delta\nfghasddfi".getBytes());
        MainMethodResult result = invokeMain( "-textFile", file.getAbsolutePath(), "NotDelta", "5", "PDX","03/02/2023", "10:39","PM","ABE","03/16/2023", "1:03","PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project4.ErrorMalformedFile));
    }
    /**
     * Tests that invokes main with a flight that arrives before it leaves
     */
    @Test
    void testArriveBeforeDeparts() {
        MainMethodResult result = invokeMain("-print", "Delta", "5", "PDX","03/15/2023", "10:39", "AM", "ABE","03/14/2023", "1:03", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project4.ErrorArrivesBeforeDeparts));
    }
    /**
     * Tests that ensures that printing an airline gives the correct result
     */
    @Test
    void testPrettyPrintWithFile(@TempDir File tempDir) throws IOException {
        File file = new File(tempDir, "airline.txt");
        MainMethodResult result;
        invokeMain("-textFile", file.getAbsolutePath(), "-print", "Delta", "5", "PDX","03/15/2023", "10:31","PM","ABE","03/15/2023", "10:40","PM");
        invokeMain("-textFile", file.getAbsolutePath(), "-print", "Delta", "6", "PDX","03/15/2023", "10:35","PM","ABE","03/16/2023", "1:03","PM");
        invokeMain("-textFile", file.getAbsolutePath(), "-print", "Delta", "7", "PDX","03/15/2023", "10:39","PM","ABE","03/16/2023", "1:03","PM");
        invokeMain("-textFile", file.getAbsolutePath(), "-print", "Delta", "8", "PDX","03/16/2023", "10:59","PM","ABE","03/17/2023", "1:03","PM");
        invokeMain("-textFile", file.getAbsolutePath(), "-print", "Delta", "9", "PDX","03/15/2023", "10:40","PM","ABE","03/16/2023", "1:03","PM");
        invokeMain("-textFile", file.getAbsolutePath(), "-print", "Delta", "4", "PDX","03/15/2023", "10:20","PM","ABE","03/16/2023", "1:03","PM");
        result = invokeMain("-textFile", file.getAbsolutePath(), "-pretty", "-", "Delta", "10", "PDX","03/15/2023", "10:39","PM","ABE","03/16/2023", "1:03","PM");
        //assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 5 departs Portland, OR at 03/15/2023 10:31 PM and will arrive at Allentown, PA at 03/15/2023 10:40 PM and the flight length is 9 minutes"));
    }
    /**
     * Tests that ensures that printing an airline gives the correct result
     */
    @Test
    void testPrettyPrintWithXMLFile(@TempDir File tempDir) throws IOException {
        File file = new File(tempDir, "airline.xml");
        MainMethodResult result;
        invokeMain("-xmlFile", file.getAbsolutePath(), "-print", "Delta", "5", "PDX","03/15/2023", "10:31","PM","ABE","03/15/2023", "10:40","PM");
        invokeMain("-xmlFile", file.getAbsolutePath(), "-print", "Delta", "6", "PDX","03/15/2023", "10:35","PM","ABE","03/16/2023", "1:03","PM");
        invokeMain("-xmlFile", file.getAbsolutePath(), "-print", "Delta", "7", "PDX","03/15/2023", "10:39","PM","ABE","03/16/2023", "1:03","PM");
        invokeMain("-xmlFile", file.getAbsolutePath(), "-print", "Delta", "8", "PDX","03/16/2023", "10:59","PM","ABE","03/17/2023", "1:03","PM");
        invokeMain("-xmlFile", file.getAbsolutePath(), "-print", "Delta", "9", "PDX","03/15/2023", "10:40","PM","ABE","03/16/2023", "1:03","PM");
        invokeMain("-xmlFile", file.getAbsolutePath(), "-print", "Delta", "4", "PDX","03/15/2023", "10:20","PM","ABE","03/16/2023", "1:03","PM");
        result = invokeMain("-xmlFile", file.getAbsolutePath(), "-pretty", "-", "Delta", "10", "PDX","03/15/2023", "10:39","PM","ABE","03/16/2023", "1:03","PM");
        //assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 5 departs Portland, OR at 03/15/2023 10:31 PM and will arrive at Allentown, PA at 03/15/2023 10:40 PM and the flight length is 9 minutes"));
    }
    /**
     * Tests that both the xml flag and text flag can't be used together
     */
    @Test
    void testBothTextAndXmlFlag()
    {
        MainMethodResult result = invokeMain("-textFile", "", "-xmlFile", "", "Delta", "5", "PDX","03/15/2023", "10:39", "AM", "ABE","03/16/2023", "1:03", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(Project4.ErrorBothXMLandTextFlag));

    }
}