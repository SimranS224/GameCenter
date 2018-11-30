package fall2018.csc2017.GameCentre.TicTacToe;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class for testing methods in RandomStrategy
 */
public class TicTacRandomStrategyTest {
    //TicTacRandomStrategy random;
    TicTacBoardManager boardManager;
    //TicTacBoard board;
    @Before
    public void setUp() {
        //random = new TicTacRandomStrategy(0);
        List<TicTacMarker> ticTacMarkers = new ArrayList<>();
        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
                ticTacMarkers.add(new TicTacMarker(row, col, 0));
            }
        }
        // assume p1 always goes first
        //this.board = new TicTacBoard(ticTacMarkers);
        boardManager = new TicTacBoardManager(new TicTacRandomStrategy(0));
    }

    @Test
    public void testGetNextMovement() {
        setUp();
        boardManager.changeTurns();
        int position = boardManager.getStrategy().getNextMovement(boardManager, 1);
        boardManager.touchMove(position);
        assertEquals(8, boardManager.getValidMoves().size());
    }

    @Test
    public void isValid() {
        TicTacRandomStrategy start = (TicTacRandomStrategy) boardManager.getStrategy();
        assertEquals(true, start.isValid());
    }

    @Test
    public void isNotValid() {
        TicTacBoardManager boardManagerTwo = new TicTacBoardManager(new TicTacEmptyStrategy(0));

        TicTacRandomStrategy start = (TicTacRandomStrategy) boardManager.getStrategy();
        assertNotEquals(start, boardManagerTwo.getStrategy());
    }
}