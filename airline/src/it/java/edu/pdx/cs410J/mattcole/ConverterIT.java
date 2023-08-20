package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ConverterIT extends InvokeMainTestCase {
    /**
     * Invokes the main method of the convert class with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {

        return invokeMain(Converter.class, args);
    }

    /**
     * Tests converter with valid input
     */
    @Test
    public void converterTest(@TempDir File tempDir) throws Project4.ArrivesBeforeDeparts, Project4.InvalidAirportCode, Flight.FlightParseDateTimeException, IOException, ParserException {
        var airline = XmlDumperTest.createSimpleAirline();
        File airlineText = new File(tempDir, "airline.txt");
        new TextDumper(new FileWriter(airlineText)).dump(airline);

        File xml = new File(tempDir, "airline.xml");
        new XmlDumper(new FileWriter(xml)).dump(airline);

        File xml2 = new File(tempDir, "airline2.xml");
        invokeMain(airlineText.getPath(), xml2.getPath());

        assertThat(Files.readString(xml.toPath()), equalTo(Files.readString(xml2.toPath())));

    }

    /**
     * Tests invalid args
     */
    @Test
    public void invalidArgsTest() {
        var results = invokeMain("", "", "");
        assert (results.getTextWrittenToStandardOut().contains("Incorrect number of args"));
    }
}
