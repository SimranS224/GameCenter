package fall2018.csc2017.GameCentre.TicTacToe;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class TicTacBoardTest {
    TicTacBoard board;

    /**
     * set up an empty board and p1 is player 1 turn
     */
    @Before
    public void setUp(){
        List<TicTacMarker> ticTacMarkers = new ArrayList<>();
        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
                ticTacMarkers.add(new TicTacMarker(row, col, 0));
            }
        }
        // assume p1 always goes first
        this.board = new TicTacBoard(ticTacMarkers);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tests which background is associated with the player
     * p1 background associated with p1 and p2background with p2
     */
    @Test
    public void testGetPlayerBackground() {
        setUp();
        assertEquals(board.getPlayerBackground(board.getPlayer1()), board.getP1Background());
        assertEquals(board.getPlayerBackground(board.getPlayer2()), board.getP2Background());

    }

    /**
     * Tests if the iterator goes through the board properly
     */
    @Test
    public void testIterator() {
        setUp();
        int count = 0;
        Iterator<TicTacMarker> iter = board.iterator();
        while (iter.hasNext()) {
            if (iter.next().getBackgroundId() == 0) {
                count++;
            }
        }
        assertEquals((board.getCols() * board.getRows()), count);
    }
}