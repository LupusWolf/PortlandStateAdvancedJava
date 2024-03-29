package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.ParserException;
import main.java.edu.pdx.cs410J.mattcole.Airline;
import main.java.edu.pdx.cs410J.mattcole.TextParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextParserTest {

    @Test
    void validTextFileCanBeParsed() throws ParserException {
        InputStream resource = getClass().getResourceAsStream("valid-airline.txt");
        assertThat(resource, notNullValue());

        TextParser parser = new TextParser(new InputStreamReader(resource));
        Airline airline = parser.parse();
        assertThat(airline.getName(), equalTo("Test main.java.edu.pdx.cs410J.mattcole.Airline"));
    }

    @Test
    void invalidTextFileThrowsParserException() {
        InputStream resource = getClass().getResourceAsStream("empty-airline.txt");
        assertThat(resource, notNullValue());

        TextParser parser = new TextParser(new InputStreamReader(resource));
        assertThrows(ParserException.class, parser::parse);
    }
}
