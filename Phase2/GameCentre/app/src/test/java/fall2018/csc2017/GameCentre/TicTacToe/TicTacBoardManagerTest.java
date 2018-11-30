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

        assertEquals(boardManager.isValidTap(2), true);
    }
    /**
     * Tests whether touchMove works fine
     */
    @Test
    public void testTouchMove() {
        setUp();
        int beforePlayer = boardManager.getBoard().getCurrentPlayer();
        boardManager.touchMove(2);
        assertEquals(beforePlayer, boardManager.getBoard().getMarker(0, 2).getBackgroundId()-1);
        boardManager.touchMove(2);
        assertEquals(beforePlayer, boardManager.getBoard().getMarker(0, 2).getBackgroundId()-1);
    }

    /**
     * Tests whetherr puzzleSolved works properly by feeding it the played position.
     */
    @Test
    public void testGetWinner() {
        boardManager.touchMove(0);
        assertEquals(boardManager.getWinner(0), false);

        boardManager.touchMove(8);

        boardManager.touchMove(1);
        assertEquals(boardManager.getWinner(1), false);

        boardManager.touchMove(7);

        boardManager.touchMove(2);
        assertEquals(boardManager.getWinner(2), true);
    }
//    /**
//     * Tests the getPossibleMoves function
//     */
//    @Test
//    public void testGetValidMoves() {
//        setUp();
//        ArrayList<Integer> allMoves = new ArrayList<>();
//        for (int i = 0; i < boardManager.getBoard().NUM_COLS * boardManager.getBoard().NUM_COLS; i++) {
//            allMoves.add(i);
//        }
//        assertEquals(allMoves, boardManager.getValidMoves());
//        boardManager.touchMove(3);
//        allMoves.remove(2);
//        assertEquals(allMoves, boardManager.getValidMoves());
//    }

    /**
     * Tests when the board changes turns,
     * when change turns is called, p1 turn should now be p2's turn
     */
    @Test
    public void testChangeTurns() {
        setUp();
        assertEquals(board.getCurrentPlayer(), board.getPlayer1());
        boardManager.changeTurns();
        assertEquals(board.getCurrentPlayer(), board.getPlayer2());
    }

//    /**
//     * tests that all position are valid when the board is empty
//     */
//    @Test
//    public void testIsValidTap() {
//        setUp();
//        //empty board all positions are valid
//        for (int i = 0; i != board.getBoardSize(); i++) {
//            assertEquals(true, boardManager.isValidTap(i));
//        }
//    }

    /**
     * Checks when a marker is taken, and when a marker is taken, isValidTap should return false
     */
    @Test
    public void testIsNotValidTap() {
        setUp();
        board.setBackground(0,0, board.getP1Background());
        //empty board all positions are valid
        for (int i = 0; i != board.getBoardSize(); i++) {
            if (i == 0) {
                assertEquals(false, boardManager.isValidTap(0));
            } else {
                assertEquals(true, boardManager.isValidTap(i));
            }
        }
    }

    /**
     * tests to see if the list valid moves are all valid (backgroundid is 0)
     */
    @Test
    public void testGetValidMoves() {
        setUp();
        ArrayList<Integer> moves = boardManager.getValidMoves();
        // empty board should expect board row * board col size
        assertEquals(board.getCols() * board.getRows(), moves.size());
        for (int i = 0; i != moves.size(); i++) {
            // every position is valid
            assertEquals(true, boardManager.isValidTap(moves.get(i)));
        }
    }

    /**
     * occupies a position in the board
     * @param position
     */
    private void positionOccupied(int position) {
        int row = position / TicTacBoard.NUM_COLS;
        int col = position % TicTacBoard.NUM_COLS;
        board.setBackground(row, col, board.getP1Background());
    }

    @Test
    public void testGetValidMoves2() {
        setUp();
        positionOccupied(2);
        ArrayList<Integer> moves = boardManager.getValidMoves();
        // empty board should expect board row * board col size
        assertEquals((board.getCols() * board.getRows() - 1), moves.size());
        for (int i = 0; i != moves.size(); i++) {
            // every position is valid
            assertEquals(true, moves.get(i) != 2);
        }
    }
}