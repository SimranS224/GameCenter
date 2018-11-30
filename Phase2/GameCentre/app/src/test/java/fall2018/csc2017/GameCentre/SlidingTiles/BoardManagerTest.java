package fall2018.csc2017.GameCentre.SlidingTiles;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BoardManagerTest {
    Board board;
    BoardManager boardManager;
    @Before
    public void setUp() {
        Board.setBoardSize(4);
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = Board.NUM_ROWS * Board.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum));
        }
        board = new Board(tiles);
        boardManager = new BoardManager(board);
    }

    @Test
    public void getBoard() {
        setUp();
        assertSame(board, boardManager.getBoard());
    }

    @Test
    public void getSpecificName() {
        setUp();
        Board.setBoardSize(3);
        assertEquals("SlidingTilesThree", boardManager.getSpecificName());
        Board.setBoardSize(4);
        assertEquals("SlidingTilesFour", boardManager.getSpecificName());
        Board.setBoardSize(5);
        assertEquals("SlidingTilesFive", boardManager.getSpecificName());

    }

    @Test
    public void checkPuzzleSolvable() {
        Board.setBoardSize(4);
        setUp();
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = Board.NUM_ROWS * Board.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum));
        }
        assertTrue(boardManager.checkPuzzleSolvable(tiles));
        Tile temp = tiles.get(0);
        tiles.set(0, tiles.get(1));
        tiles.set(1, temp);
        assertFalse(boardManager.checkPuzzleSolvable(tiles));

    }

    @Test
    public void getCurrGameScore() {
        setUp();
        assertEquals(boardManager.getCurrGameScore(), (Long) 0L);
        boardManager.setScore(10L);
        assertEquals(boardManager.getCurrGameScore(), (Long) 10L);
    }

    @Test
    public void isOver() {
        setUp();
        assertTrue(boardManager.isOver());
        boardManager.swapTiles(0, 0, 0, 1);
        assertFalse(boardManager.isOver());
    }

    @Test
    public void isValidTap() {
        Board.setBoardSize(4);
        boardManager = new BoardManager();
        assertTrue(boardManager.isValidTap(14));
        assertFalse(boardManager.isValidTap(10));
    }

    @Test
    public void touchMove() {
        Board.setBoardSize(4);
        setUp();
        BoardManager temp = boardManager;
        boardManager.touchMove(0);
        assertEquals(16, boardManager.getBoard().getTile(3, 3).getId());
        boardManager.touchMove(14);
        assertEquals(15, boardManager.getBoard().getTile(3, 3).getId());
        assertNotEquals(temp.getCurrGameScore() , boardManager.getCurrGameScore());
    }

    @Test
    public void swapTiles() {
        setUp();
        assertEquals(15, boardManager.getBoard().getTile(3, 2).getId());
        assertEquals(16, boardManager.getBoard().getTile(3, 3).getId());
        boardManager.swapTiles(3, 3, 3, 2);
        assertEquals(16, boardManager.getBoard().getTile(3, 2).getId());
        assertEquals(15, boardManager.getBoard().getTile(3, 3).getId());
    }
}