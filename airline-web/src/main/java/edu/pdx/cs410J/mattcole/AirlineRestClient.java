package edu.pdx.cs410J.mattcole;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Map;

import static edu.pdx.cs410J.web.HttpRequestHelper.Response;
import static edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.
 */
public class AirlineRestClient
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final HttpRequestHelper http;


    /**
     * Creates a client to the airline REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AirlineRestClient( String hostName, int port )
    {
        this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
    }

    @VisibleForTesting
    AirlineRestClient(HttpRequestHelper http) {
      this.http = http;
    }



  /**
   * Returns an airline based on its name
   */
  public void writeOutAirline(String airline, Writer writer) throws IOException, ParserException {
    Response response = http.get(Map.of("airline", airline));
    if (response.getHttpStatusCode() == HttpServletResponse.SC_NOT_FOUND)
    {
        PrintWriter pw = new PrintWriter(writer);
        pw.append("404 error: no airline was present with this name");
        pw.flush();
        return;
    }
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();
    XmlParser parser = new XmlParser(new StringReader(content));
    PrettyPrinter printer = new PrettyPrinter(writer);
    printer.dump(parser.parse());
  }

    /**
     * Returns all flights from a given airline from src to dest
     */
    public void writeOutAirlineSearch(String airline, String src, String dest, Writer writer) throws IOException, ParserException {
        Response response = http.get(Map.of("airline", airline,"src",src,"dest",dest));

        if (response.getHttpStatusCode() == HttpServletResponse.SC_NOT_FOUND)
        {
            PrintWriter pw = new PrintWriter(writer);
            pw.append("404 error: no airline was present with this name");
            pw.flush();
            return;
        }
        throwExceptionIfNotOkayHttpStatus(response);
        String content = response.getContent();

        XmlParser parser = new XmlParser(new StringReader(content));
        PrettyPrinter printer = new PrettyPrinter(writer);
        printer.dump(parser.parse());
    }
    /**
     * Returns the definition for the given word
     */
    public void postAirline(String airline, int flightNumber, String src, String depart, String dest,
                            String arrive, Writer writer) throws IOException, ParserException {
        Response response = http.post(Map.of("airline", airline,"flightNumber", "" + flightNumber, "src",
                src,"depart", depart, "dest", dest, "arrive", arrive));

        throwExceptionIfNotOkayHttpStatus(response);
        String content = response.getContent();
        if (writer != null) {
            XmlParser parser = new XmlParser(new StringReader(content));
            PrettyPrinter printer = new PrettyPrinter(writer);
            printer.dump(parser.parse());
        }
    }


  private void throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getHttpStatusCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
  }

}
