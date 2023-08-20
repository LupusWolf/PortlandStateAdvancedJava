package main.java.edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.ParserException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class XMLAirlineStorer implements AirlineStorageManager {
    final private String path;

    public XMLAirlineStorer(String path) {
        this.path = path;
    }

    @Override
    public Airline get(String defaultName) throws ParserException {
        try {
            return new XmlParser(new FileReader(path)).parse();
        } catch (IOException e) {
            return new Airline(defaultName);
        }

    }

    @Override
    public void save(Airline airline) throws IOException {
        new XmlDumper(new FileWriter(path)).dump(airline);
    }
}
