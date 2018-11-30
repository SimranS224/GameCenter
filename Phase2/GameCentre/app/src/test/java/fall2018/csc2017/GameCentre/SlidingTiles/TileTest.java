package fall2018.csc2017.GameCentre.SlidingTiles;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TileTest {

    /**
     * Tile for testing.
     */
    private Tile testOne;

    /**
     * Another Tile for testing.
     */
    private Tile anotherOne;

    /**
     * Creates two Tile objects for testing.
     */
    @Before
    public void setUp() {
        testOne = new Tile(5, 10);
        anotherOne = new Tile(5, 10);

    }

    /**
     * Tests to make sure the background is stored properly in the Tile.
     */
    @Test
    public void getBackground() {
        assertEquals(10, testOne.getBackground());
    }

    /**
     * Tests to make sure the id is stored properly in the Tile.
     */
    @Test
    public void getId() {
        assertEquals(5, testOne.getId());
    }

    /**
     * Tests to make sure Tile objects can be compared as desired.
     */
    @Test
    public void compareTo() {
        assertEquals(0, testOne.compareTo(anotherOne));
    }
}