package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.BusParser;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BusParserTest {
    @Test
    public void testBusLocationsParserNormal() throws JSONException {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearBuses();
        String data = "";

        try {
            data = new FileDataProvider("buslocations.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the bus locations data");
        }

        BusParser.parseBuses(s, data);

        assertEquals(4, s.getBuses().size());

        List<Bus> bus = s.getBuses();
        for(Bus b: bus)
            assertTrue(s.getRoutes().contains(b.getRoute()));
        assertTrue(bus.get(0).getLatLon().equals(new LatLon(49.264067, -123.167150)));
        assertTrue(bus.get(0).getDestination().equals("HASTINGS"));
        assertTrue(bus.get(0).getTime().equals("12:52:08 pm"));

        assertEquals(s, bus.get(0).getRoute().getStops().get(0));

        assertEquals(bus.get(1).getRoute(), bus.get(2).getRoute());

        assertEquals(RouteManager.getInstance().getRouteWithNumber("004"), bus.get(2).getRoute());
    }
    @Test
    public void testBusLocationsParseMissingField() throws JSONException
    {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearBuses();
        String data = "";

        try {
            data = new FileDataProvider("buslocationsMissingField.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the bus locations data");
        }

        BusParser.parseBuses(s, data);

        assertEquals(3, s.getBuses().size());
    }
    @Test
    public void testBusLocationsParseException() throws JSONException
    {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearBuses();
        String data = "";

        try {
            data = new FileDataProvider("buslocationsException.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the bus locations data");
        }

        try {
            BusParser.parseBuses(s, data);
            fail("Should not reach");
        } catch (JSONException j) {
            System.out.println(j.toString());
        }
    }
}
