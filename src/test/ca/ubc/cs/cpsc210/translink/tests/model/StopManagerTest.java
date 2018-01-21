package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.model.exception.StopException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Test the StopManager
 */
public class StopManagerTest {

    @BeforeEach
    public void setup() {
        StopManager.getInstance().clearStops();
    }

    @Test
    public void testConstructor()
    {
        assertEquals(0, StopManager.getInstance().getNumStops());
    }
    @Test
    public void testBasic() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop r = StopManager.getInstance().getStopWithNumber(9999);
        assertEquals(s9999, r);
    }

    @Test
    public void testGetStopWithNumber()
    {
        Stop s1 = new Stop(1, "UBC", new LatLon(1, 1));
        Stop s2 = new Stop(2, "UBC", new LatLon(2, 2));

        assertEquals(s1, StopManager.getInstance().getStopWithNumber(1));
        assertEquals(1, StopManager.getInstance().getNumStops());
        assertEquals(s2, StopManager.getInstance().getStopWithNumber(2, "Downtown", new LatLon(3, 2)));
        assertEquals(2, StopManager.getInstance().getNumStops());
        assertEquals(s2, StopManager.getInstance().getStopWithNumber(2));
        assertEquals(2, StopManager.getInstance().getNumStops());
    }

    @Test
    public void testSelect()
    {
        Stop s1 = new Stop(1, "UBC", new LatLon(1, 1));
        try{
            StopManager.getInstance().setSelected(s1);
        } catch (StopException se) {

        }
        try{
            StopManager.getInstance().getStopWithNumber(1);
            StopManager.getInstance().setSelected(s1);
            assertEquals(s1, StopManager.getInstance().getSelected());
        } catch (StopException se) {
            fail("Unexpected exception: " + se);
        }

        StopManager.getInstance().getStopWithNumber(2);
        StopManager.getInstance().getStopWithNumber(3);
        StopManager.getInstance().clearSelectedStop();
        assertEquals(3, StopManager.getInstance().getNumStops());
        assertEquals(null, StopManager.getInstance().getSelected());
    }

    @Test
    public void testFindNearestTo()
    {
        assertEquals(null, StopManager.getInstance().findNearestTo(new LatLon(-100, -100)));
        Stop s1 = new Stop(1, "UBC", new LatLon(10, 10));
        StopManager.getInstance().getStopWithNumber(1, "UBC", new LatLon(10, 10));
        assertEquals(s1, StopManager.getInstance().findNearestTo(new LatLon(10, 10)));
        assertEquals(null, StopManager.getInstance().findNearestTo(new LatLon(-100, -100)));
    }
}
