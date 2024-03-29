package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.ParserException;
import main.java.edu.pdx.cs410J.mattcole.Airline;
import main.java.edu.pdx.cs410J.mattcole.ExceptionHolder;
import main.java.edu.pdx.cs410J.mattcole.XmlDumper;
import main.java.edu.pdx.cs410J.mattcole.XmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class XmlParserTest {
    @Test
    public void simpleTest(@TempDir File tempDir) throws ExceptionHolder.ArrivesBeforeDeparts, ExceptionHolder.InvalidAirportCode, ExceptionHolder.InvalidDateTime, IOException, ParserException {
        File file = new File(tempDir, "airline.xml");
        Airline startingAirline = XmlDumperTest.createSimpleAirline();
        XmlDumper xmlDumper = new XmlDumper(new FileWriter(file));
        xmlDumper.dump(startingAirline);

        XmlParser parser = new XmlParser(new FileReader(file));
        Airline afterParsing = parser.parse();
        assertThat(afterParsing.getName(), equalTo(startingAirline.getName()));

        File file2 = new File(tempDir, "airline2.xml");
        XmlDumper xmlDumper2 = new XmlDumper(new FileWriter(file2));
        xmlDumper2.dump(afterParsing);

        assertThat(Files.readString(file.toPath()), equalTo(Files.readString(file2.toPath())));
    }
}
