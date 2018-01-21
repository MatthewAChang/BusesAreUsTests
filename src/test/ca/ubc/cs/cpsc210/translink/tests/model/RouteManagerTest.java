package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test the RouteManager
 */
public class RouteManagerTest {

    @BeforeEach
    public void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    @Test
    public void testConstructor()
    {
        assertEquals(0, RouteManager.getInstance().getNumRoutes());
    }

    @Test
    public void testBasic() {
        Route r43 = new Route("43");
        Route r = RouteManager.getInstance().getRouteWithNumber("43");
        assertEquals(r43, r);
    }

    @Test
    public void testGetRouteWithNumber()
    {
        Route r1 = new Route("100");
        Route r2 = new Route("200");
        r2.setName("UBC");
        assertEquals(r1, RouteManager.getInstance().getRouteWithNumber("100"));
        assertEquals(1, RouteManager.getInstance().getNumRoutes());
        assertEquals(r1, RouteManager.getInstance().getRouteWithNumber("100"));
        assertEquals(1, RouteManager.getInstance().getNumRoutes());

        assertEquals(r2, RouteManager.getInstance().getRouteWithNumber("200", "UBC"));
        assertEquals(2, RouteManager.getInstance().getNumRoutes());
        assertEquals(r2, RouteManager.getInstance().getRouteWithNumber("200", "UBC"));
        assertEquals(2, RouteManager.getInstance().getNumRoutes());

    }
}
