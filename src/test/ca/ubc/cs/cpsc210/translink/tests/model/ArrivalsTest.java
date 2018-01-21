package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test Arrivals
 */
public class ArrivalsTest {
    private Route r;
    private Arrival a1;
    private Arrival a2;

    @BeforeEach
    public void setup() {
        r = RouteManager.getInstance().getRouteWithNumber("43");

        a1 = new Arrival(23, "Home", r);
        a2 = new Arrival(28, "UBC", r);
    }

    @Test
    public void testConstructor() {
        assertEquals(23, a1.getTimeToStopInMins());
        assertEquals("Home", a1.getDestination());
        assertEquals(r, a1.getRoute());
        assertEquals(" ", a1.getStatus());
    }

    @Test
    public void testMethods()
    {
        assertEquals(-5, a1.compareTo(a2));
        assertEquals(5, a2.compareTo(a1));

        a1.setStatus("+");
        assertEquals("+", a1.getStatus());
    }
}
