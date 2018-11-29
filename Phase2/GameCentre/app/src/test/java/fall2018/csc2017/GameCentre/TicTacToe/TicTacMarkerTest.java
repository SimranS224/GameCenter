package fall2018.csc2017.GameCentre.TicTacToe;

import org.junit.Before;
import org.junit.Test;

import fall2018.csc2017.GameCentre.R;

import static org.junit.Assert.*;

public class TicTacMarkerTest {
    TicTacMarker marker;

    @Before
    public void setUp() {
        marker = new TicTacMarker(0,0);
    }

    @Test
    public void testGetBackgroundId() {
        setUp();
        assertEquals(0, marker.getBackgroundId());
    }

    @Test
    public void testGetBackground() {
        setUp();
        assertEquals(R.drawable.blank_marker, marker.getBackground());
    }

    @Test
    public void testSetBackground() {
        setUp();
        marker.setBackground(0);
        assertEquals(R.drawable.blank_marker, marker.getBackground());
        marker.setBackground(1);
        assertEquals(R.drawable.red_marker, marker.getBackground());
        marker.setBackground(2);
        assertEquals(R.drawable.blue_marker, marker.getBackground());
    }

    @Test
    public void testGetId() {
        setUp();
        assertEquals(0, marker.getId());
    }

    @Test
    public void testSetId() {
        setUp();
        marker.setId(1);
        assertEquals(1, marker.getId());
    }

    @Test
    public void testCompareTo() {
        setUp();
        TicTacMarker o = new TicTacMarker(1,1);
        assertEquals(1, marker.compareTo(o));
    }

}