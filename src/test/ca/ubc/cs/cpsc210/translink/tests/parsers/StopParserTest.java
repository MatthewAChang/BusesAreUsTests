package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.StopParser;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Tests for the StopParser
 */

// TODO: Write more tests

public class StopParserTest {
    @BeforeEach
    public void setup() {
        StopManager.getInstance().clearStops();
    }

    @Test
    public void testStopParserNormal() throws StopDataMissingException, JSONException, IOException {
        StopParser p = new StopParser("stops.json");
        p.parse();
        assertEquals(8524, StopManager.getInstance().getNumStops());

        assertEquals(2, StopManager.getInstance().getStopWithNumber(54297).getRoutes().size());
        assertTrue(StopManager.getInstance().getStopWithNumber(54297).getRoutes().contains(new Route("232")));
        assertTrue(StopManager.getInstance().getStopWithNumber(54297).getRoutes().contains(new Route("247")));
    }
    @Test
    public void testStopParserException() throws StopDataMissingException, JSONException, IOException {
        StopParser p = new StopParser("stopsException.json");

        try {
            p.parse();
            fail("Should not reach");
        } catch(StopDataMissingException sdme) {
            System.out.println(sdme.toString());
        }
        assertEquals(8522, StopManager.getInstance().getNumStops());
    }
}
