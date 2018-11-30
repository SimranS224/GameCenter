package fall2018.csc2017.GameCentre.SlidingTiles;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {
    Board board;

    @Before
    public void setUp() {
        Board.setBoardSize(4);
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = Board.NUM_ROWS * Board.NUM_COLS;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum));
        }
        board = new Board(tiles);
    }

    @Test
    public void getTile() {
        setUp();
        assertEquals(1, board.getTile(0, 0).getId());
        assertEquals(5, board.getTile(1, 0).getId());
    }

    @Test
    public void getNumRows() {
        setUp();
        Board.setBoardSize(4);
        assertEquals(4, Board.getNumRows());
        Board.setBoardSize(3);
        assertEquals(3, Board.getNumRows());
        Board.setBoardSize(5);
        assertEquals(5, Board.getNumRows());
    }

    @Test
    public void getNumCols() {
        setUp();
        Board.setBoardSize(4);
        assertEquals(4, Board.getNumCols());
        Board.setBoardSize(3);
        assertEquals(3, Board.getNumCols());
        Board.setBoardSize(5);
        assertEquals(5, Board.getNumCols());
    }

    @Test
    public void setBoardSize() {
        setUp();
        assertEquals(4, board.getBoardSize());
        Board.setBoardSize(3);
        assertEquals(3, board.getBoardSize());
    }

    @Test
    public void setIMAGE() {
        setUp();
        assertEquals("Flower", Board.getIMAGE());
        Board.setIMAGE("UofT");
        assertEquals("UofT", Board.getIMAGE());
    }

    @Test
    public void setType() {
        setUp();
        assertEquals("number", Board.getIMAGE());
        Board.setIMAGE("image");
        assertEquals("image", Board.getIMAGE());
    }

    @Test
    public void getBoardSize() {
        setUp();
        assertEquals(4, board.getBoardSize());
        Board.setBoardSize(3);
        assertEquals(3, board.getBoardSize());
        Board.setBoardSize(5);
        assertEquals(5, board.getBoardSize());
    }


    @Test
    public void testToString() {
        setUp();
        assertEquals("Board{tiles={0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}}", board.toString());
    }

    @Test
    public void iterator() {
        setUp();
        Iterator it = board.iterator();
        int count = 0;
        assertTrue(it.hasNext());
        while (it.hasNext()) {
            it.next();
            count++;
        }
        assertFalse(it.hasNext());
        assertEquals(15, count);

    }

    @Test
    public void setTiles() {
        setUp();
        Tile tile = new Tile(100, 100);
        assertEquals(1, board.getTile(0, 0).getId());
        board.setTiles(tile, 0, 0);
        assertEquals(100, board.getTile(0, 0).getId());
    }
}