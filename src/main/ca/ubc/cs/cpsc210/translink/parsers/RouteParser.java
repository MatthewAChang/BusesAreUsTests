package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;

    public RouteParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }
    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)
     *     <li>JSON data is not an array
     * </ul>
     * If a JSONException is thrown, no routes should be added to the route manager
     *
     * @throws RouteDataMissingException when
     * <ul>
     *  <li>JSON data is missing RouteNo, Name, or Patterns element for any route</li>
     *  <li>The value of the Patterns element is not an array for any route</li>
     *  <li>JSON data is missing PatternNo, Destination, or Direction element for any route pattern</li>
     * </ul>
     * If a RouteDataMissingException is thrown, all correct routes are first added to the route manager.
     */

    public void parseRoutes(String jsonResponse)
            throws JSONException, RouteDataMissingException {
        // TODO: Task 4: Implement this method
        JSONArray allRoutes = new JSONArray(jsonResponse);

        int exceptionCounter = 0;
        for(int i = 0; i < allRoutes.length(); i++)
        {
            JSONObject routeInfo = allRoutes.getJSONObject(i);
            try {
                parseRoute(routeInfo);
            } catch(RouteDataMissingException rdme) {
                exceptionCounter++;
            }
        }
        if(exceptionCounter > 0)
            throw new RouteDataMissingException("Missing a field");
    }

    private void parseRoute(JSONObject routeInfo) throws JSONException, RouteDataMissingException
    {
        if(!routeInfo.has("RouteNo") || !routeInfo.has("Name") || !routeInfo.has("Patterns"))
            throw new RouteDataMissingException();
        String number = routeInfo.getString("RouteNo");
        String name = routeInfo.getString("Name");

        RouteManager.getInstance().getRouteWithNumber(number, name);
        parseRoutePattern(routeInfo, number);


    }

    private void parseRoutePattern(JSONObject routeInfo, String number) throws JSONException, RouteDataMissingException
    {
        JSONArray routePatternsInfo = routeInfo.getJSONArray("Patterns");
        for(int i = 0; i < routePatternsInfo.length(); i++)
        {
            JSONObject routePatterns = routePatternsInfo.getJSONObject(i);
            if(!routePatterns.has("PatternNo") || !routePatterns.has("Destination") || !routePatterns.has("Direction"))
                throw new RouteDataMissingException();
            String name = routePatterns.getString("PatternNo");
            String destination = routePatterns.getString("Destination");
            String direction = routePatterns.getString("Direction");
            RoutePattern routePattern = new RoutePattern(name, destination, direction, RouteManager.getInstance().getRouteWithNumber(number));
            RouteManager.getInstance().getRouteWithNumber(number).addPattern(routePattern);
        }
    }
}
