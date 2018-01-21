package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.ArrivalsParser;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Test the ArrivalsParser
 */
public class ArrivalsParserTest {
    @Test
    public void testArrivalsParserNormal() throws JSONException, ArrivalsDataMissingException {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearArrivals();
        String data = "";
        try {
            data = new FileDataProvider("arrivals43.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the arrivals data");
        }

        ArrivalsParser.parseArrivals(s, data);
        int count = 0;
        for (Arrival a : s) {
            assertTrue(a.getTimeToStopInMins() <= 120);
            count++;
        }
        assertEquals(6, count);
    }
    @Test
    public void testArrivalParserException() throws JSONException, ArrivalsDataMissingException
    {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearArrivals();
        String data = "";
        try {
            data = new FileDataProvider("arrivals43Exception.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the arrivals data");
        }
        try {
            ArrivalsParser.parseArrivals(s, data);
            fail("Should not reach");
        } catch (ArrivalsDataMissingException adme) {
            System.out.println(adme.toString());
            // expected
        }
    }
    @Test
    public void testArrivalsParserMissingRouteNo() throws JSONException, ArrivalsDataMissingException {
        Stop s = StopManager.getInstance().getStopWithNumber(51479);
        s.clearArrivals();
        String data = "";
        try {
            data = new FileDataProvider("arrivals43MissingRouteNo.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read the arrivals data");
        }

        try {
            ArrivalsParser.parseArrivals(s, data);
            fail("Should not reach");
        } catch (ArrivalsDataMissingException adme) {
            System.out.println(adme.toString());
            // expected
        }
    }
}
