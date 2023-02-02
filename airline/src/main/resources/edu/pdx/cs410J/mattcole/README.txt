Matthew Cole
Project 2
CS510Java
This program takes in a set of flight details, then produces a flight object, and finally creates an airline object
containing that flight object. It then prints the flight details back to the user if the user uses the option -print. If
the user specifies a file using -textFile then the program will first read the airline in from that file and then will
write the airline back out to that file when the program exits. The usage in the specifications were:

usage: java -jar target/airline-2023.0.0.jar [options] <args>
    args are (in this order):
        airline The name of the airline
        flightNumber The flight number
        src Three-letter code of departure airport
        depart Departure date and time (24-hour time)
        dest Three-letter code of arrival airport
        arrive Arrival date and time (24-hour time)
    options are (options may appear in any order):
        -textFile file Where to read/write the airline info
        -print Prints a description of the new flight
        -README Prints a README for this project and exits
        Dates and times should be in the format: mm/dd/yyyy hh:mm