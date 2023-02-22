package edu.pdx.cs410J.mattcole;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test (and can capture data
 * written to {@link System#out} and the like.
 */
class Project3Test {

  /**
   * Ensure README can be read as a resource
   * @throws IOException Throws io exception when readme.txt doesn't exist
   */
  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project3.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("Matthew"));
    }
  }
  @Test
  void parseAirline() throws Project3.TooFewArgs, Project3.TooManyArgs, Project3.InvalidOption, Project3.ReadmeOption, Project3.InvalidAirportCode, Project3.InvalidFlightNumber {
    var args = new String[]{"-textFile", "file.txt", "-print", "Delta", "5", "PDX","03/15/2023", "10:31","PM","ABE","03/16/2023", "1:03","PM"};
    var parsedArgs = new Project3.ParsedCommandLine(args);
    assertThat(parsedArgs.airlineName, equalTo("Delta"));
    assertThat(parsedArgs.number, equalTo(5));
    assertThat(parsedArgs.source, equalTo("PDX"));
  }
}
