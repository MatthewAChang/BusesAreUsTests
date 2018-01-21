package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.exception.RouteException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StopTest {
    private Stop s1;
    private Stop s2;
    private Route r1;
    private Route r2;
    private Bus b1;
    private Bus b2;
    private Arrival a1;
    private Arrival a2;

    @BeforeEach
    public void setup() {
        s1 = new Stop(100, "100", new LatLon(10, 10));
        s2 = new Stop(100, "100", new LatLon(10, 10));
    }

    @Test
    public void testConstructor() {
        assertEquals(100, s1.getNumber());
        assertEquals("100", s1.getName());
        assertEquals(new LatLon(10, 10), s1.getLocn());
        assertTrue(s1.getRoutes().isEmpty());
        assertTrue(s1.getBuses().isEmpty());
    }

    @Test
    public void testRoute()
    {
        initializeVar();
        s1.addRoute(r1);
        assertTrue(s1.onRoute(r1));
        assertFalse(s1.onRoute(r2));
        s1.addRoute(r2);
        assertTrue(s1.onRoute(r1));
        assertTrue(s1.onRoute(r2));

        s1.removeRoute(new Route("100"));
        assertFalse(s1.onRoute(r1));
        assertTrue(s1.onRoute(r2));
    }

    @Test
    public void testBus()
    {
        initializeVar();
        try{
            s1.addBus(b1);
            assertEquals(b1, s1.getBuses().get(0));
            assertEquals(1, s1.getBuses().size());

        } catch(RouteException re) {
            fail("Unexpected exception: " + re);
        }
        try{
            s1.addBus(b2);
            assertEquals(1, s1.getBuses().size());
        } catch(RouteException re) {

        }
        s1.clearBuses();
        assertEquals(0, s1.getBuses().size());
    }
    @Test
    public void testArrival()
    {
        initializeVar();
        assertFalse(s1.iterator().hasNext());
        s1.addArrival(a1);
        assertTrue(s1.iterator().hasNext());
        assertEquals(s1.iterator().next(), a1);

        for(Arrival arrival: s1)
        {
            assertEquals(a1, arrival);
        }
        s1.clearArrivals();
        assertFalse(s1.iterator().hasNext());
    }

    @Test
    public void testSetters()
    {
        s1.setName("200");
        assertEquals("200", s1.getName());
        s1.setLocn(new LatLon(1, 1));
        assertEquals(new LatLon(1, 1), s1.getLocn());
    }

    @Test
    public void testHashCode()
    {
        assertEquals(s1.getNumber(), s1.hashCode());
    }

    @Test
    public void testAddRemove()
    {
        initializeVar();
        assertTrue(s1.onRoute(r1));
        assertTrue(r1.getStops().contains(s1));

        s1.removeRoute(r1);
        assertFalse(s1.onRoute(r1));
        assertFalse(r1.getStops().contains(s1));
    }

    public void initializeVar()
    {
        r1 = new Route("100");
        r1.addStop(s1);
        r1.addStop(s2);
        r2 = new Route("200");
        b1 = new Bus(r1, 10, 10, "UBC", "10:00");
        b2 = new Bus(r2, 10, 10, "UBC", "11:00");
        a1 = new Arrival(10, "UBC", r1);
    }
}
