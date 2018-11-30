package fall2018.csc2017.GameCentre.SlidingTiles;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class SlidingTilesBoardTest {
    SlidingTilesBoard slidingTilesBoard;

    @Before
    public void setUp() {
        SlidingTilesBoard.setBoardSize(4);
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = SlidingTilesBoard.NUM_ROWS * SlidingTilesBoard.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum));
        }
        slidingTilesBoard = new SlidingTilesBoard(tiles);
    }

    @Test
    public void getTile() {
        setUp();
        assertEquals(1, slidingTilesBoard.getTile(0, 0).getId());
        assertEquals(5, slidingTilesBoard.getTile(1, 0).getId());
    }

    @Test
    public void getNumRows() {
        setUp();
        SlidingTilesBoard.setBoardSize(4);
        assertEquals(4, SlidingTilesBoard.getNumRows());
        SlidingTilesBoard.setBoardSize(3);
        assertEquals(3, SlidingTilesBoard.getNumRows());
        SlidingTilesBoard.setBoardSize(5);
        assertEquals(5, SlidingTilesBoard.getNumRows());
    }

    @Test
    public void getNumCols() {
        setUp();
        SlidingTilesBoard.setBoardSize(4);
        assertEquals(4, SlidingTilesBoard.getNumCols());
        SlidingTilesBoard.setBoardSize(3);
        assertEquals(3, SlidingTilesBoard.getNumCols());
        SlidingTilesBoard.setBoardSize(5);
        assertEquals(5, SlidingTilesBoard.getNumCols());
    }

    @Test
    public void setBoardSize() {
        setUp();
        assertEquals(4, slidingTilesBoard.getBoardSize());
        SlidingTilesBoard.setBoardSize(3);
        assertEquals(3, slidingTilesBoard.getBoardSize());
    }

    @Test
    public void setIMAGE() {
        setUp();
        assertEquals("Flower", SlidingTilesBoard.getIMAGE());
        SlidingTilesBoard.setIMAGE("UofT");
        assertEquals("UofT", SlidingTilesBoard.getIMAGE());
    }

    @Test
    public void setType() {
        setUp();
        assertEquals("number", SlidingTilesBoard.getType());
        SlidingTilesBoard.setType("image");
        assertEquals("image", SlidingTilesBoard.getType());
    }

    @Test
    public void getBoardSize() {
        setUp();
        assertEquals(4, slidingTilesBoard.getBoardSize());
        SlidingTilesBoard.setBoardSize(3);
        assertEquals(3, slidingTilesBoard.getBoardSize());
        SlidingTilesBoard.setBoardSize(5);
        assertEquals(5, slidingTilesBoard.getBoardSize());
    }


    @Test
    public void testToString() {
        setUp();
        assertTrue(slidingTilesBoard.toString().startsWith("SlidingTilesBoard{tiles="));
    }

    @Test
    public void iterator() {
        setUp();
        Iterator it = slidingTilesBoard.iterator();
        int count = 0;
        assertTrue(it.hasNext());
        while (it.hasNext()) {
            it.next();
            count++;
        }
        assertFalse(it.hasNext());
        assertEquals(16, count);

    }

    @Test
    public void setTiles() {
        setUp();
        Tile tile = new Tile(100, 100);
        assertEquals(1, slidingTilesBoard.getTile(0, 0).getId());
        slidingTilesBoard.setTiles(tile, 0, 0);
        assertEquals(100, slidingTilesBoard.getTile(0, 0).getId());
    }
}