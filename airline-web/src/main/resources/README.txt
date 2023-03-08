Matthew Cole
Project 5
CS510Java
This simple commandline interface allows you to interact with the airline servlet. It allows you to do the following three
things.
1. It allows you to search for all flights offered by an airline. To do this, run the jar with the search arguement and
the airline name. The servlet will then return the requested airline.
2. It allows you to search for all flights offered by an airline that go between two cities. To do this, run the jar with
the search argument, the airline name, the source airport, and the destination airport.
3. It allows you to add a new flight to an airlines. If you provide all of the fields specified below, it will POST the
new flight to the servlet and be added to the specified airline. The -print option can be used to view the flight you
just created.


usage: java -jar target/airline-client.jar [options] <args>
    args are (in this order):
        airline         The name of the airline
        flightNumber    The flight number
        src             Three-letter code of departure airport
        depart          Departure date/time
        dest            Three-letter code of arrival airport
        arrive          Arrival date/time
    options are (options may appear in any order):
        -host               hostname Host computer on which the server runs
        -port               port Port on which the server is listening
        -search             Search for flights
        -print              Prints a description of the new flight
        -README             Prints a README for this project and exits

Additional requirements:
    Dates and times should be in the format: mm/dd/yyyy hh:mm
    Airport codes MUST correspond to known airports
    Flights must depart before they arrive
    XML file must correspond to following dtd: "http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd"
    Ports must be an integer
    You must provide a valid host and port for every request
