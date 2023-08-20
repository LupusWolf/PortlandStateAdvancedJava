package main.java.edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.ParserException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextAirlineStorer implements AirlineStorageManager {
    final private String path;

    public TextAirlineStorer(String path)  {
        this.path = path;
    }

    @Override
    public Airline get(String defaultName) throws ParserException {
        try {
            return new TextParser(new FileReader(path)).parse();
        } catch (IOException e) {
            return new Airline(defaultName);
        }
    }

    @Override
    public void save(Airline airline) throws IOException {
        new TextDumper(new FileWriter(path)).dump(airline);
    }
}
