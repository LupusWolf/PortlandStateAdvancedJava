package main.java.edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.ParserException;

import java.io.IOException;

public interface AirlineStorageManager {
    Airline get(String defaultName) throws ParserException;

    void save(Airline airline) throws IOException;
}
