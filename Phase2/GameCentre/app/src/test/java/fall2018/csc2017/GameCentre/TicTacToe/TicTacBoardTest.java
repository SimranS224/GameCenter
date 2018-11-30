//package fall2018.csc2017.GameCentre.TicTacToe;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class TicTacBoardTest {
//    TicTacBoard board;
//
//    /**
//     * set up an empty board and p1 is player 1 turn
//     */
//    @Before
//    public void setUp(){
//        List<TicTacMarker> ticTacMarkers = new ArrayList<>();
//        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
//            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
//                ticTacMarkers.add(new TicTacMarker(row, col, 0));
//            }
//        }
//        // assume p1 always goes first
//        this.board = new TicTacBoard(ticTacMarkers);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    /**
//     * Tests which background is associated with the player
//     * p1 background associated with p1 and p2background with p2
//     */
//    @Test
//    public void testGetPlayerBackground() {
//        setUp();
//        assertEquals(board.getPlayerBackground(board.getPlayer1()), board.getP1Background());
//        assertEquals(board.getPlayerBackground(board.getPlayer2()), board.getP2Background());
//
//    }
//
//    /**
//     * Tests when the board changes turns,
//     * when change turns is called, p1 turn should now be p2's turn
//     */
//    @Test
//    public void testChangeTurns() {
//        setUp();
//        assertEquals(board.getCurrentPlayer(), board.getPlayer1());
//        board.changeTurns();
//        assertEquals(board.getCurrentPlayer(), board.getPlayer2());
//    }
//
//    /**
//     * tests that all position are valid when the board is empty
//     */
//    @Test
//    public void testIsValidTap() {
//        setUp();
//        //empty board all positions are valid
//        for (int i = 0; i != board.getBoardSize(); i++) {
//            assertEquals(true, board.isValidTap(i));
//        }
//    }
//
//    /**
//     * Checks when a marker is taken, and when a marker is taken, isValidTap should return false
//     */
//    @Test
//    public void testIsNotValidTap() {
//        setUp();
//        board.setBackground(0,0, board.getP1Background());
//        //empty board all positions are valid
//        for (int i = 0; i != board.getBoardSize(); i++) {
//            if (i == 0) {
//                assertEquals(false, board.isValidTap(0));
//            } else {
//                assertEquals(true, board.isValidTap(i));
//            }
//        }
//    }
//
//    /**
//     * tests to see if the list valid moves are all valid (backgroundid is 0)
//     */
//    @Test
//    public void testGetValidMoves() {
//        setUp();
//        ArrayList<Integer> moves = board.getValidMoves();
//        // empty board should expect board row * board col size
//        assertEquals(board.getCols() * board.getRows(), moves.size());
//        for (int i = 0; i != moves.size(); i++) {
//            // every position is valid
//            assertEquals(true, board.isValidTap(moves.get(i)));
//        }
//    }
//
//    /**
//     * occupies a position in the board
//     * @param position
//     */
//    private void positionOccupied(int position) {
//        int row = position / TicTacBoard.NUM_COLS;
//        int col = position % TicTacBoard.NUM_COLS;
//        board.setBackground(row, col, board.getP1Background());
//    }
//
//    @Test
//    public void testGetValidMoves2() {
//        setUp();
//        positionOccupied(2);
//        ArrayList<Integer> moves = board.getValidMoves();
//        // empty board should expect board row * board col size
//        assertEquals((board.getCols() * board.getRows() - 1), moves.size());
//        for (int i = 0; i != moves.size(); i++) {
//            // every position is valid
//            assertEquals(true, moves.get(i) != 2);
//        }
//    }
//
//    @Test
//    public void iterator() {
//        setUp();
//        int count = 0;
//        Iterator<TicTacMarker> iter = board.iterator();
//        while (iter.hasNext()) {
//            if (iter.next().getBackgroundId() == 0) {
//                count++;
//            }
//        }
//        assertEquals((board.getCols() * board.getRows()), count);
//    }
//}