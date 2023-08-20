package edu.pdx.cs410J.mattcole;

import edu.pdx.cs410J.ParserException;
import main.java.edu.pdx.cs410J.mattcole.Airline;
import main.java.edu.pdx.cs410J.mattcole.TextParser;
import main.java.edu.pdx.cs410J.mattcole.XmlDumper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFormatToXMLConverter {
    /**
     * Class that can be called directly to convert a file in the old text format to xml
     *
     * @param args textfile xmlfile
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect number of args. Correct usage: " +
                    "java -cp target/airline-2023.0.0.jar TextFormatToXMLConverter textfile xmlfile");
        }
        try {
            var textFile = new File(args[0]);
            var xmlfile = new File(args[1]);
            Airline airline = new TextParser(new FileReader(textFile)).parse();
            new XmlDumper(new FileWriter(xmlfile)).dump(airline);
        } catch (IOException e) {
            System.out.println("Failed to read/write from file paths given. Check to confirm that paths are valid");
        } catch (ParserException e) {
            System.out.println("Failed to parse text file");
        }

    }
}
