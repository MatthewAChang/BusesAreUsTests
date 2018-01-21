package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteTest {
    private Route r;
    private RoutePattern rp1;
    private RoutePattern rp2;
    private Stop s1;
    private Stop s2;
    private Stop s3;

    @BeforeEach
    public void setUp()
    {
        r = new Route("100");
        rp1 = new RoutePattern("200", "UBC", "West", r);
        rp2 = new RoutePattern("300", "UBC", "West", r);
        s1 = new Stop(100, "100", new LatLon(10, 10));
        s2 = new Stop(101, "101", new LatLon(20, 20));
        s3 = new Stop(102, "102", new LatLon(30, 30));
    }

    @Test
    public void testConstructor()
    {
        assertEquals("100", r.getNumber());
        assertEquals("", r.getName());
        assertTrue(r.getStops().isEmpty());
        assertTrue(r.getPatterns().isEmpty());
    }

    @Test
    public void testAddPattern()
    {
        r.addPattern(rp1);
        assertEquals(rp1, r.getPatterns().get(0));
        r.addPattern(rp1);
        assertEquals(1, r.getPatterns().size());
        r.addPattern(rp2);
        assertEquals(rp2, r.getPatterns().get(1));
        assertEquals(2, r.getPatterns().size());
    }

    @Test
    public void testStop()
    {
        r.addStop(s1);
        assertTrue(r.hasStop(s1));
        r.addStop(s2);
        r.addStop(s3);
        assertTrue(r.hasStop(s2));
        assertTrue(r.hasStop(s3));

        r.addStop(s1);
        assertEquals(s1, r.getStops().get(0));
        assertEquals(s2, r.getStops().get(1));
        assertEquals(s3, r.getStops().get(2));

        r.removeStop(s2);
        assertFalse(r.hasStop(s2));
        assertEquals(s1, r.getStops().get(0));
        assertEquals(s3, r.getStops().get(1));
    }

    @Test
    public void testEquals()
    {
        Route r2 = new Route("100");
        assertTrue(r.equals(r2));
        r2.addPattern(rp1);
        assertTrue(r.equals(r2));
        Route r3 = new Route("200");
        assertFalse(r.equals(r3));

        assertFalse(r.equals(null));
        assertFalse(r.equals(rp1));
    }

    @Test
    public void testGetPatternThreeParam()
    {
        r.getPattern("100", "UBC", "West");
        assertEquals(1, r.getPatterns().size());
        r.getPattern("100", "Joyce", "East");
        assertEquals(1, r.getPatterns().size());

        r.getPattern("200", "UBC", "West");
        assertEquals(2, r.getPatterns().size());
    }
    @Test
    public void testGetPatternOneParam()
    {
        r.getPattern("100");
        assertEquals(1, r.getPatterns().size());
        r.getPattern("100");
        assertEquals(1, r.getPatterns().size());

        r.getPattern("200");
        assertEquals(2, r.getPatterns().size());
    }

    @Test
    public void testHashCode()
    {
        assertEquals(r.getNumber().hashCode(), r.hashCode());
    }

    @Test
    public void testToString()
    {
        assertEquals("Route " + r.getNumber(), r.toString());
    }

    @Test
    public void testIterator()
    {
        for(Stop stop: r)
            assertEquals(1, stop.getRoutes().size());
    }
}
