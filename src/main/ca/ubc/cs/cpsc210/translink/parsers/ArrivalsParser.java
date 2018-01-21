package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {

    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop             stop to which parsed arrivals are to be added
     * @param jsonResponse    the JSON response produced by Translink
     * @throws JSONException  when:
     * <ul>
     *     <li>JSON response does not have expected format (JSON syntax problem)</li>
     *     <li>JSON response is not an array</li>
     * </ul>
     * @throws ArrivalsDataMissingException  when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {
        // TODO: Task 4: Implement this method
        JSONArray allArrivals = new JSONArray(jsonResponse);
        int exceptionCounter = 0;
        for(int i = 0; i < allArrivals.length(); i++)
        {
            JSONObject arrivalInfo = allArrivals.getJSONObject(i);
            try {
                parseArrival(stop, arrivalInfo);
            } catch (ArrivalsDataMissingException rdme) {
                exceptionCounter++;
            }
        }
        if(exceptionCounter > 0)
            throw new ArrivalsDataMissingException("Missing Fields");
    }

    private static void parseArrival(Stop stop, JSONObject arrivalInfo) throws JSONException, ArrivalsDataMissingException
    {
        if(!arrivalInfo.has("RouteNo"))
            throw new ArrivalsDataMissingException();

        String routeNo = arrivalInfo.getString("RouteNo");

        int properArrivals = 0;
        JSONArray schedules = arrivalInfo.getJSONArray("Schedules");
        for(int i = 0; i < schedules.length(); i++)
        {
            JSONObject schedule = schedules.getJSONObject(i);
            if(schedule.has("ExpectedCountdown") && schedule.has("Destination") && schedule.has("ScheduleStatus"))
            {
                int timeToStop = schedule.getInt("ExpectedCountdown");
                String destination = schedule.getString("Destination");
                String scheduleStatus = schedule.getString("ScheduleStatus");
                Arrival arrival = new Arrival(timeToStop, destination, RouteManager.getInstance().getRouteWithNumber(routeNo));
                arrival.setStatus(scheduleStatus);
                stop.addArrival(arrival);
                properArrivals++;
            }
        }
        if(properArrivals == 0)
            throw new ArrivalsDataMissingException();
    }
}
