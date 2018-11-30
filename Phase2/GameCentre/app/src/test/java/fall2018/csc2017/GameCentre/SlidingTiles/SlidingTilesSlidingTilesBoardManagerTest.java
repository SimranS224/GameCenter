package fall2018.csc2017.GameCentre.SlidingTiles;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SlidingTilesSlidingTilesBoardManagerTest {
    SlidingTilesBoard slidingTilesBoard;
    SlidingTilesBoardManager slidingTilesBoardManager;
    @Before
    public void setUp() {
        SlidingTilesBoard.setBoardSize(4);
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = SlidingTilesBoard.NUM_ROWS * SlidingTilesBoard.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum));
        }
        slidingTilesBoard = new SlidingTilesBoard(tiles);
        slidingTilesBoardManager = new SlidingTilesBoardManager(slidingTilesBoard);
    }

    @Test
    public void getBoard() {
        setUp();
        assertSame(slidingTilesBoard, slidingTilesBoardManager.getSlidingTilesBoard());
    }

    @Test
    public void getSpecificName() {
        setUp();
        SlidingTilesBoard.setBoardSize(3);
        assertEquals("SlidingTilesThree", slidingTilesBoardManager.getSpecificName());
        SlidingTilesBoard.setBoardSize(4);
        assertEquals("SlidingTilesFour", slidingTilesBoardManager.getSpecificName());
        SlidingTilesBoard.setBoardSize(5);
        assertEquals("SlidingTilesFive", slidingTilesBoardManager.getSpecificName());

    }

    @Test
    public void checkPuzzleSolvable() {
        SlidingTilesBoard.setBoardSize(4);
        setUp();
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = SlidingTilesBoard.NUM_ROWS * SlidingTilesBoard.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum));
        }
        assertTrue(slidingTilesBoardManager.checkPuzzleSolvable(tiles));
        Tile temp = tiles.get(0);
        tiles.set(0, tiles.get(1));
        tiles.set(1, temp);
        assertFalse(slidingTilesBoardManager.checkPuzzleSolvable(tiles));

    }

    @Test
    public void getCurrGameScore() {
        setUp();
        assertEquals(slidingTilesBoardManager.getCurrGameScore(), (Long) 0L);
        slidingTilesBoardManager.setScore(10L);
        assertEquals(slidingTilesBoardManager.getCurrGameScore(), (Long) 10L);
    }

    @Test
    public void isOver() {
        setUp();
        assertTrue(slidingTilesBoardManager.isOver());
        slidingTilesBoardManager.swapTiles(0, 0, 0, 1);
        assertFalse(slidingTilesBoardManager.isOver());
    }

    @Test
    public void isValidTap() {
        SlidingTilesBoard.setBoardSize(4);
        slidingTilesBoardManager = new SlidingTilesBoardManager();
        assertTrue(slidingTilesBoardManager.isValidTap(14));
        assertFalse(slidingTilesBoardManager.isValidTap(10));
    }

    @Test
    public void touchMove() {
        SlidingTilesBoard.setBoardSize(4);
        setUp();
        SlidingTilesBoardManager temp = slidingTilesBoardManager;
        slidingTilesBoardManager.touchMove(0);
        assertEquals(16, slidingTilesBoardManager.getSlidingTilesBoard().getTile(3, 3).getId());
        slidingTilesBoardManager.touchMove(14);
        assertEquals(15, slidingTilesBoardManager.getSlidingTilesBoard().getTile(3, 3).getId());
        assertNotEquals(temp.getCurrGameScore() , slidingTilesBoardManager.getCurrGameScore());
    }

    @Test
    public void swapTiles() {
        setUp();
        assertEquals(15, slidingTilesBoardManager.getSlidingTilesBoard().getTile(3, 2).getId());
        assertEquals(16, slidingTilesBoardManager.getSlidingTilesBoard().getTile(3, 3).getId());
        slidingTilesBoardManager.swapTiles(3, 3, 3, 2);
        assertEquals(16, slidingTilesBoardManager.getSlidingTilesBoard().getTile(3, 2).getId());
        assertEquals(15, slidingTilesBoardManager.getSlidingTilesBoard().getTile(3, 3).getId());
    }
}