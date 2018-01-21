package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BusTest {
    private Bus b1;
    private Bus b2;

    @BeforeEach
    public void setUp()
    {
        b1 = new Bus(new Route("100"), 10, 10, "UBC", "10:00");
        b2 = new Bus(new Route("200"), 20.22, 20, "UBC", "2:00");
    }

    @Test
    public void testConstructor()
    {
        assertEquals(new Route("100"), b1.getRoute());
        assertEquals(new Route("200"), b2.getRoute());

        assertEquals(new LatLon(10, 10), b1.getLatLon());
        assertEquals(new LatLon(20.22, 20), b2.getLatLon());

        assertEquals("UBC", b1.getDestination());
        assertEquals("10:00", b1.getTime());
    }
}
