package ca.ubc.cs.cpsc210.translink.tests.parsers;

import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.parsers.RouteParser;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for the RouteParser
 */
// TODO: Write more tests

public class RouteParserTest {
    @BeforeEach
    public void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    @Test
    public void testRouteParserNormal() throws RouteDataMissingException, JSONException, IOException {
        RouteParser p = new RouteParser("allroutes.json");
        p.parse();
        assertEquals(229, RouteManager.getInstance().getNumRoutes());
        assertEquals("MACDONALD/DOWNTOWN ", RouteManager.getInstance().getRouteWithNumber("002").getName());
        assertEquals(23, RouteManager.getInstance().getRouteWithNumber("250").getPatterns().size());
        assertEquals(7, RouteManager.getInstance().getRouteWithNumber("027").getPatterns().size());
    }

    @Test
    public void testRouteParserException() throws RouteDataMissingException, JSONException, IOException
    {
        RouteParser p = new RouteParser("allroutesException.json");
        try {
            p.parse();
            fail("Should not reach");
        } catch(RouteDataMissingException rdme) {
            System.out.println(rdme.toString());
        }
        assertEquals(228, RouteManager.getInstance().getNumRoutes());


    }
}
