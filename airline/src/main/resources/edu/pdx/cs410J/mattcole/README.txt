Matthew Cole
Project 4
CS510Java
This program takes in a set of flight details, then produces a flight object, and finally creates an airline object
containing that flight object. It then prints the flight details back to the user if the user uses the option -print. If
the user specifies a file using -textFile or -xmlFile then the program will first read the airline in from that file and
then will write the airline back out to that file when the program exits (using either a custom text format or xml
respectively). The -pretty command can be used to do a pretty print of the produced airline.
The usage in the specifications were:

usage: java -jar target/airline-2023.0.0.jar [options] <args>
    args are (in this order):
        name                    The name of the airline
        flightNumber            The flight number
        src                     Three-letter code of departure airport
        depart                  Departure date time am/pm
        dest                    Three-letter code of arrival airport
        arrive                  Arrival date time am/pm
    options are (options may appear in any order):
        -xmlFile file           Where to read/write the airline info
        -textFile file          Where to read/write the airline info
        -pretty file            Pretty print the airline’s flights to
                                a text file or standard out (file -)
        -print                  Prints a description of the new flight
        -README                 Prints a README for this project and exits

Additional requirements:
    Dates and times should be in the format: mm/dd/yyyy hh:mm
    Airport codes MUST correspond to known airports
    Flights must depart before they arrive
    XML file must correspond to following dtd: "http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd"
