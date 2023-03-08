package edu.pdx.cs410J.mattcole;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.internal.Messages;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>Airline</code>.
 */
public class AirlineServlet extends HttpServlet {


  private final Map<String, Airline> airlines = new HashMap<>();

    /**
     * Gets an airline and creates a new airline if it doesn't exist
     */
  private Airline getAirline(String name)
  {
      if (!airlines.containsKey(name))
      {
          airlines.put(name, new Airline(name));
      }
      return airlines.get(name);
  }
  private boolean firstTime = true;
  private void first()
  {
      if (firstTime)
      {
          Airline airline = new Airline("delta");
          Flight flight1 = null;
          Flight flight2 = null;
          try {
              flight1 = new Flight(1, "PDX","03/15/2023 10:31 PM","ABE","03/16/2023 1:03 PM");
              flight2 = new Flight(2,  "ABE","03/15/2023 10:32 PM","PDX","03/16/2023 1:03 PM");
          } catch (Flight.FlightParseDateTimeException e) {
              throw new RuntimeException(e);
          } catch (constHolder.ArrivesBeforeDeparts e) {
              throw new RuntimeException(e);
          } catch (constHolder.InvalidAirportCode e) {
              throw new RuntimeException(e);
          }
          airline.addFlight(flight1);
          airline.addFlight(flight2);
          firstTime = false;
          airlines.put("delta", airline);
      }
  }
  /**
   * Handles an HTTP GET request from a client by writing an airlines data or by all flight from an airline that meet
   * certain criteria
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      first();
      response.setContentType( "text/plain" );

      String airline = getParameter( "airline", request );
      String src = getParameter( "src", request );
      String dest = getParameter( "dest", request );
      if (airline != null && src != null && dest != null)
      {
          writeAirlineSearch(airline, src.toUpperCase(), dest.toUpperCase(), response);
      } else if (airline != null) {
          writeAirline(airline, response);

      } else {
          response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
      }
  }

  /**
   * Handles an HTTP POST request by storing a given flight into a given airline
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      first();
      response.setContentType( "text/plain" );

      String airlineParam = getParameter( "airline", request);
      String flightNumber = getParameter( "flightNumber", request);
      String src = getParameter( "src", request);
      String dest = getParameter( "dest", request);
      String depart = getParameter( "depart", request);
      String arrive = getParameter( "arrive", request);
      for (String param : new String[]{"flightNumber","src","dest","depart","arrive"})
      {
          if (param == null) {
              missingRequiredParameter(response, param);
              return;
          }
      }

      try {
          Flight flight = new Flight(Integer.parseInt(flightNumber), src, depart, dest, arrive);

          Airline airline = new Airline(airlineParam);
          airline.addFlight(flight);

          getAirline(airlineParam).addFlight(flight);

          PrintWriter pw = response.getWriter();
          XmlDumper dumper = new XmlDumper(pw);
          dumper.dump(airline);

          response.setStatus(HttpServletResponse.SC_OK);
          return;
      } catch (NumberFormatException e) {
          response.getWriter().println(constHolder.ErrorInvalidNumber);
      } catch (Flight.FlightParseDateTimeException e)
      {
          response.getWriter().println(constHolder.ErrorInvalidDateTime);
      } catch (constHolder.ArrivesBeforeDeparts e) {
          response.getWriter().println(constHolder.ErrorArrivesBeforeDeparts);
      } catch (constHolder.InvalidAirportCode e)
      {
          response.getWriter().println(constHolder.ErrorInvalidAirportCode);
      }
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }

  /**
   * Handles an HTTP DELETE request by removing all dictionary entries.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.setContentType("text/plain");

      response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);

  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   *
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
      throws IOException
  {
      String message = String.format("The required parameter \"%s\" is missing", parameterName);
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Writes the definition of the given word to the HTTP response.
   *
   */
  private void writeAirline(String airlineName, HttpServletResponse response) throws IOException {
    Airline airline = airlines.get(airlineName);

    if (airline == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    } else {
      PrintWriter pw = response.getWriter();

      XmlDumper dumper = new XmlDumper(pw);
      dumper.dump(airline);

      response.setStatus(HttpServletResponse.SC_OK);
    }
  }
    private void writeAirlineSearch(String airlineName, String src, String dest, HttpServletResponse response) throws IOException {
        Airline airline = airlines.get(airlineName);
        if (airline == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } else {
            PrintWriter pw = response.getWriter();

            Airline subset = airline.getSubset(src, dest);
            XmlDumper dumper = new XmlDumper(pw);
            dumper.dump(subset);

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }



  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }

}
