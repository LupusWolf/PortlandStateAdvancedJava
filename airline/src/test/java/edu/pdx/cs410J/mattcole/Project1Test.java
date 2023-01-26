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
class Project1Test {

  /**
   * Ensure README can be read as a resource
   * @throws IOException Throws io exception when readme.txt doesn't exist
   */
  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("Matthew"));
    }
  }
  /**
   * Test date time function to make sure it can correctly determine whether a string is a valid time
   * using the string: "Not a valid time"
   */
  @Test
  void dateAndTimeTest1()
  {
    assertThat(Project1.isValidDateAndTime("Not a valid time"), equalTo(false));
  }
  /**
   * Test date time function to make sure it can correctly determine whether a string is a valid time
   * using the string: "3/15/2023 10:39"
   */
  @Test
  void dateAndTimeTest2()
  {
    assertThat(Project1.isValidDateAndTime("3/15/2023 10:39"), equalTo(true));
  }
  /**
   * Test date time function to make sure it can correctly determine whether a string is a valid time
   * using the string: "03/2/2023 1:03"
   */
  @Test
  void dateAndTimeTest3()
  {
    assertThat(Project1.isValidDateAndTime("03/2/2023 1:03"), equalTo(true));
  }
  /**
   * Test date time function to make sure it can correctly determine whether a string is a valid time
   * using the string: "1/1/2000 4:05"
   */
  @Test
  void dateAndTimeTest4()
  {
    assertThat(Project1.isValidDateAndTime("1/1/2000 4:05"), equalTo(true));
  }
  /**
   * Test date time function to make sure it can correctly determine whether a string is a valid time
   * using the string: "4/12/200 1:05"
   */
  @Test
  void dateAndTimeTest5()
  {
    assertThat(Project1.isValidDateAndTime("4/12/200 1:05"), equalTo(false));
  }
}
