package fall2018.csc2017.GameCentre.TicTacToe;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TicTacBoardManagerTest {
    TicTacBoardManager boardManager;
    TicTacBoard board;
    @Before
    public void setUp() throws Exception {
        boardManager = new TicTacBoardManager(board);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tests whether validTap returns the expected method
     */
    @Test
    public void testisValidTap(){
        setUp();
        assertEquals(boardManager.isValidTap(1), true);
        boardManager.touchMove(1);
        assertEquals(boardManager.isValidTap(1), false);

        assertEquals(boardManager.isValidTap(20), false);
    }
    /**
     * Tests whether touchMove works fine
     */
    @Test
    public void testTouchMove() {

    }

    /**
     * Tests whether puzzleSolved works properly by feeding it the played position.
     */
    @Test
    public void testpuzzleSolved() {
        boardManager.touchMove(0);
        assertEquals(boardManager.puzzleSolved(0), false);

        boardManager.touchMove(1);
        assertEquals(boardManager.puzzleSolved(1), false);

        boardManager.touchMove(2);
        assertEquals(boardManager.puzzleSolved(2), true);
    }
}