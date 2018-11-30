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
    //TicTacMinimaxStrategy miniMax;
    TicTacBoardManager boardManager;
    //TicTacBoard board;
    @Before
    public void setUp() {
//        //miniMax = new TicTacMinimaxStrategy(0);
//        List<TicTacMarker> ticTacMarkers = new ArrayList<>();
//        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
//            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
//                ticTacMarkers.add(new TicTacMarker(row, col, 0));
//            }
//        }
        // assume p1 always goes first
        //this.board = new TicTacBoard(ticTacMarkers);
        boardManager = new TicTacBoardManager(new TicTacMinimaxStrategy(0));
    }

    @Test
    public void testGetNextMovement() {
        setUp();
        boardManager.changeTurns();
        int position = boardManager.getStrategy().getNextMovement(boardManager, 1);
        //assertEquals(2, position);
        boardManager.touchMove(position);

        assertTrue(boardManager.getBoard().getMarker(0, 0).getBackgroundId() == 2  || boardManager.getBoard().getMarker(0, 1).getBackgroundId() == 2);
        //assertEquals(2, boardManager.getBoard().getMarker(0, 0).getBackgroundId());
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
