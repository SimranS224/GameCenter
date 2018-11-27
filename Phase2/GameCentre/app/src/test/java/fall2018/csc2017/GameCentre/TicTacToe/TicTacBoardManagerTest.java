package fall2018.csc2017.GameCentre.TicTacToe;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TicTacBoardManagerTest {
    TicTacBoardManager boardManager;
    TicTacBoard board;
    @Before
    public void setUp() {
        List<TicTacMarker> ticTacMarkers = new ArrayList<>();
        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
                ticTacMarkers.add(new TicTacMarker(row, col, 0));
            }
        }
        // assume p1 always goes first
        this.board = new TicTacBoard(ticTacMarkers);
        boardManager = new TicTacBoardManager(board);
    }

    @After
    public void tearDown() {
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
        setUp();
        int beforePlayer = boardManager.getBoard().getCurrentPlayer();
        boardManager.touchMove(20);
        assertEquals(beforePlayer, boardManager.getBoard().getCurrentPlayer());
        boardManager.touchMove(2);
        assertNotEquals(beforePlayer, boardManager.getBoard().getCurrentPlayer());
    }

    /**
     * Tests whether puzzleSolved works properly by feeding it the played position.
     */
    @Test
    public void testGetWinner() {
        boardManager.touchMove(0);
        assertEquals(boardManager.getWinner(0), false);

        boardManager.touchMove(1);
        assertEquals(boardManager.getWinner(1), false);

        boardManager.touchMove(2);
        assertEquals(boardManager.getWinner(2), true);
    }
    /**
     * Tests the getPossibleMoves function
     */
    @Test
    public void testGetValidMoves() {
        setUp();
        ArrayList<Integer> allMoves = new ArrayList<>();
        for (int i = 0; i < boardManager.getBoard().NUM_COLS * boardManager.getBoard().NUM_COLS; i++) {
            allMoves.add(i);
        }
        assertEquals(allMoves, boardManager.getValidMoves());
        boardManager.touchMove(3);
        allMoves.remove(2);
        assertEquals(allMoves, boardManager.getValidMoves());
    }
}