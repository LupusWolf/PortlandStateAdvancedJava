package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.UncaughtExceptionInMain;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.MethodOrderer.MethodName;

/**
 * An integration test for {@link Project5Test} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project5IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    void testGet() {
        MainMethodResult result = invokeMain( Project5.class, "-host", "localhost", "-port", "8080",
                "-search", "delta", "PDX", "ABE");
        assertThat(result.getTextWrittenToStandardOut(), containsString("flight number                 source airport"));
    }
    @Test
    void testPost() {
        MainMethodResult result = invokeMain( Project5.class, "-host", "localhost", "-port", "8080", "-print",
                "delta", "11", "AMA","03/15/2023", "10:39", "pm", "ABE","03/16/2023", "1:03", "pm");
        assertThat(result.getTextWrittenToStandardOut(), containsString("flight number                 source airport"));
    }
    @Test
    void testPostNoPrint() {
        MainMethodResult result = invokeMain( Project5.class, "-host", "localhost", "-port", "8080",
                "delta", "11", "AMA","03/15/2023", "10:39", "pm", "ABE","03/16/2023", "1:03", "pm");
        assertThat(result.getTextWrittenToStandardOut(), not(containsString("flight number                 source airport")));
    }
    void test0RemoveAllMappings() throws IOException {
      AirlineRestClient client = new AirlineRestClient(HOSTNAME, Integer.parseInt(PORT));
      //client.removeAllDictionaryEntries();
    }

    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project5Test.class );
        //assertThat(result.getTextWrittenToStandardError(), containsString(Project5.MISSING_ARGS));
    }


    void test2EmptyServer() {
        MainMethodResult result = invokeMain( Project5Test.class, HOSTNAME, PORT );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();
        //assertThat(out, out, containsString(PrettyPrinterOLD.formatWordCount(0)));
    }


    void test3NoDefinitionsThrowsAppointmentBookRestException() {
        String word = "WORD";
        try {
            invokeMain(Project5Test.class, HOSTNAME, PORT, word);
            fail("Should have thrown a RestException");

        } catch (UncaughtExceptionInMain ex) {
            RestException cause = (RestException) ex.getCause();
            assertThat(cause.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        }
    }


    void test4AddDefinition() {
        String word = "WORD";
        String definition = "DEFINITION";

        MainMethodResult result = invokeMain( Project5Test.class, HOSTNAME, PORT, word, definition );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();
        //assertThat(out, out, containsString(Messages.definedWordAs(word, definition)));

        result = invokeMain( Project5Test.class, HOSTNAME, PORT, word );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        out = result.getTextWrittenToStandardOut();
        //assertThat(out, out, containsString(PrettyPrinterOLD.formatDictionaryEntry(word, definition)));

        result = invokeMain( Project5Test.class, HOSTNAME, PORT );

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        out = result.getTextWrittenToStandardOut();
        //assertThat(out, out, containsString(PrettyPrinterOLD.formatDictionaryEntry(word, definition)));
    }
}