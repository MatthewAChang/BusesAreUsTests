package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.exception.RouteException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Parser for bus data
public class BusParser {

    /**
     * Parse buses from JSON response produced by TransLink query.  All parsed buses are
     * added to the given stop.  Bus location data that is missing any of the required
     * fields (RouteNo, Latitude, Longitude, Destination, RecordedTime) is silently
     * ignored and not added to stop. Bus that is on route that does not pass through
     * this stop is silently ignored and not added to stop.
     *
     * @param stop            stop to which parsed buses are to be added
     * @param jsonResponse    the JSON response produced by Translink
     * @throws JSONException  when:
     * <ul>
     *     <li>JSON response does not have expected format (JSON syntax problem)</li>
     *     <li>JSON response is not a JSON array</li>
     * </ul>
     */
    public static void parseBuses(Stop stop, String jsonResponse) throws JSONException {
        JSONArray busLocations = new JSONArray(jsonResponse);

        for(int i = 0; i < busLocations.length(); i++)
        {
            JSONObject busInfo = busLocations.getJSONObject(i);
            Bus bus = parseBus(busInfo, stop);
            if(bus != null && stop.onRoute(bus.getRoute()))
            {
                try {
                    stop.addBus(bus);
                } catch(RouteException re) {

                }
            }
        }
    }

    private static Bus parseBus(JSONObject bus, Stop stop) throws JSONException
    {
        if(bus.has("RouteNo") && bus.has("Latitude") && bus.has("Longitude") && bus.has("Destination") && bus.has("RecordedTime"))
        {
            Route route = RouteManager.getInstance().getRouteWithNumber(bus.getString("RouteNo"));
            route.addStop(stop);
            Double lat = bus.getDouble("Latitude");
            Double lon = bus.getDouble("Longitude");
            String dest = bus.getString("Destination");
            String time = bus.getString("RecordedTime");
            return new Bus(route, lat, lon, dest, time);
        }
        return null;
    }
}
