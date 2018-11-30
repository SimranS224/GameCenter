package fall2018.csc2017.GameCentre.TicTacToe;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.GameCentre.R;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TicTacMinimaxStrategyTest {
    TicTacBoardManager boardManager;

    /**
     * sets up the minimax strategy for the board manager
     */
    @Before
    public void setUp() {
        boardManager = new TicTacBoardManager(new TicTacMinimaxStrategy(0));
    }

    /**
     *
     */
    @Test
    public void testGetNextMovement() {
        setUp();
        boardManager.changeTurns();
        int position = boardManager.getStrategy().getNextMovement(boardManager, 1);
        boardManager.touchMove(position);

        assertTrue(boardManager.getBoard().getMarker(0, 0).getBackgroundId() == 2
                || boardManager.getBoard().getMarker(0, 1).getBackgroundId() == 2);
    }

    @Test
    public void isValid() {
        TicTacMinimaxStrategy start = (TicTacMinimaxStrategy) boardManager.getStrategy();
        assertEquals(true, start.isValid());
    }

    @Test
    public void isNotValid() {
        TicTacBoardManager boardManagerTwo = new TicTacBoardManager(new TicTacEmptyStrategy(0));

        TicTacMinimaxStrategy start = (TicTacMinimaxStrategy) boardManager.getStrategy();
        assertNotEquals(start, boardManagerTwo.getStrategy());
    }
}
