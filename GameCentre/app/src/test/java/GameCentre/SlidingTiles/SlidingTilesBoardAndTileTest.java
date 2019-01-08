package fall2018.csc2017.GameCentre.SlidingTiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SlidingTilesBoardAndTileTest {

    /** The slidingTilesBoard manager for testing. */
    private SlidingTilesBoardManager slidingTilesBoardManager;

    /**
     * Make a solved SlidingTilesBoard.
     */
    private void setUpCorrect() {
        SlidingTilesBoard.setBoardSize(4);
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = SlidingTilesBoard.NUM_ROWS * SlidingTilesBoard.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum));
        }
        SlidingTilesBoard slidingTilesBoard = new SlidingTilesBoard(tiles);
        slidingTilesBoardManager = new SlidingTilesBoardManager(slidingTilesBoard);
    }

    /**
     * Shuffle a few tiles.
     */
    private void swapFirstTwoTiles() {
        slidingTilesBoardManager.swapTiles(0, 0, 0, 1);
    }

    /**
     * Test whether swapping two tiles makes a solved slidingTilesBoard unsolved.
     */
    @Test
    public void testIsSolved() {
        setUpCorrect();
        assertTrue(slidingTilesBoardManager.isOver());
        swapFirstTwoTiles();
        assertFalse(slidingTilesBoardManager.isOver());
    }

    /**
     * Test whether swapping the first two tiles works.
     */
    @Test
    public void testSwapFirstTwo() {
        setUpCorrect();
        assertEquals(1, slidingTilesBoardManager.getSlidingTilesBoard().getTile(0, 0).getId());
        assertEquals(2, slidingTilesBoardManager.getSlidingTilesBoard().getTile(0, 1).getId());
        slidingTilesBoardManager.swapTiles(0, 0, 0, 1);
        assertEquals(2, slidingTilesBoardManager.getSlidingTilesBoard().getTile(0, 0).getId());
        assertEquals(1, slidingTilesBoardManager.getSlidingTilesBoard().getTile(0, 1).getId());
    }

    /**
     * Test whether swapping the last two tiles works.
     */
    @Test
    public void testSwapLastTwo() {
        setUpCorrect();
        assertEquals(15, slidingTilesBoardManager.getSlidingTilesBoard().getTile(3, 2).getId());
        assertEquals(16, slidingTilesBoardManager.getSlidingTilesBoard().getTile(3, 3).getId());
        slidingTilesBoardManager.swapTiles(3, 3, 3, 2);
        assertEquals(16, slidingTilesBoardManager.getSlidingTilesBoard().getTile(3, 2).getId());
        assertEquals(15, slidingTilesBoardManager.getSlidingTilesBoard().getTile(3, 3).getId());
    }

    /**
     * Test whether isValidHelp works.
     */
    @Test
    public void testIsValidTap() {
        setUpCorrect();
        assertTrue(slidingTilesBoardManager.isValidTap(14));
        assertFalse(slidingTilesBoardManager.isValidTap(10));
    }
}