package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoutePatternTest {
    private RoutePattern r1;

    @BeforeEach
    public void setUp()
    {
        r1 = new RoutePattern("100", "UBC", "East", new Route("100"));

    }

    @Test
    public void testConstructor()
    {
        assertEquals("100", r1.getName());
        assertEquals("UBC", r1.getDestination());
        assertEquals("East", r1.getDirection());
        assertTrue(r1.getPath().isEmpty());
    }

    @Test
    public void testEquals()
    {
        RoutePattern r2 = new RoutePattern("100", "Joyce Station", "South-East", new Route("200"));
        RoutePattern r3 = new RoutePattern("200", "UBC", "East", new Route("100"));
        assertTrue(r1.equals(r2));
        assertFalse(r1.equals(r3));
    }

    @Test
    public void testSetPath()
    {
        List<LatLon> latlon = new ArrayList<>();
        r1.setPath(latlon);
        assertTrue(r1.getPath().isEmpty());

        latlon.add(new LatLon(10, 10));
        latlon.add(new LatLon(100, 100));
        r1.setPath(latlon);
        assertFalse(r1.getPath().isEmpty());
        assertEquals(new LatLon(10, 10), r1.getPath().get(0));
        assertEquals(new LatLon(100, 100), r1.getPath().get(1));
    }

    @Test
    public void testSet()
    {
        r1.setDirection("South");
        r1.setDestination("Metrotown");
        assertEquals("South", r1.getDirection());
        assertEquals("Metrotown", r1.getDestination());
    }

    @Test
    public void testHashCode()
    {
        assertEquals(r1.getName().hashCode(), r1.hashCode());
    }
}
