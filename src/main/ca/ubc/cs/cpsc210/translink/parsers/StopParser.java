package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {

    private String filename;

    public StopParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse stop data from the file and add all stops to stop manager.
     *
     */
    public void parse() throws IOException, StopDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }
    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)</li>
     *     <li>JSON data is not an array</li>
     * </ul>
     * If a JSONException is thrown, no stops should be added to the stop manager
     * @throws StopDataMissingException when
     * <ul>
     *  <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop</li>
     * </ul>
     * If a StopDataMissingException is thrown, all correct stops are first added to the stop manager.
     */

    public void parseStops(String jsonResponse)
            throws JSONException, StopDataMissingException {
        // TODO: Task 4: Implement this method
        JSONArray allStops = new JSONArray(jsonResponse);

        int exceptionCounter = 0;
        for(int i = 0; i < allStops.length(); i++)
        {
            JSONObject stopInfo = allStops.getJSONObject(i);
            try {
                if(!stopInfo.has("Name") || !stopInfo.has("StopNo") || !stopInfo.has("Latitude") || !stopInfo.has("Longitude") || !stopInfo.has("Routes"))
                    throw new StopDataMissingException();
                Stop stop = parseStop(stopInfo);
                parseRoute(stopInfo, stop);
            } catch(StopDataMissingException sdme) {
                exceptionCounter++;
            }
        }
        if(exceptionCounter > 0)
            throw new StopDataMissingException("Missing a field");
    }

    private Stop parseStop(JSONObject stopInfo) throws JSONException
    {
        String name = stopInfo.getString("Name");
        int stopNo = stopInfo.getInt("StopNo");
        Double lat = stopInfo.getDouble("Latitude");
        Double lon = stopInfo.getDouble("Longitude");
        return StopManager.getInstance().getStopWithNumber(stopNo, name, new LatLon(lat, lon));
    }

    private void parseRoute(JSONObject stopInfo, Stop stop) throws JSONException
    {
        String routes = stopInfo.getString("Routes");
        String routeArray[] = routes.split(", ");
        for(String route: routeArray)
            stop.addRoute(RouteManager.getInstance().getRouteWithNumber(route));
    }
}
