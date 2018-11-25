package fall2018.csc2017.GameCentre.TicTacToe;

import fall2018.csc2017.GameCentre.Manager;
import fall2018.csc2017.GameCentre.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class TicTacBoardManager implements Serializable, Manager {


    /**
     * movement controller
     */
    private TicTacMovementController mController;

    /**
     * strategy
     */
    private TicTacStrategy strategy;

    /**
     * score
     */
    private Long score;

    /**
     * The board being managed.
     */
    private TicTacBoard board;
    private TicTacMoveStack stack;

    /**
     * Manage a board that has been pre-populated.
     * THIS NEEDS TO BE KEPT AND IS USED WHEN CALLING THE AI STRATEGIES
     * @param board the board
     */
    public TicTacBoardManager(TicTacBoard board) {
        this.board = board;
        this.score = 0L;
        this.stack = new TicTacMoveStack();
    }

    /**
     * Return the current board.
     */
    TicTacBoard getBoard() {
        return board;
    }

    /**
     * Return the strategy
     */
    public TicTacStrategy getStrategy() {
        return this.strategy;
    }

    /**
     * Manage a new board.
     */
    TicTacBoardManager(TicTacStrategy strategy) {
        this.strategy = strategy;
        mController = new TicTacMovementController();

        List<TicTacMarker> ticTacMarkers = new ArrayList<>();
        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
                ticTacMarkers.add(new TicTacMarker(row, col, 0));
            }

        }
        // assume p1 always goes first
        this.board = new TicTacBoard(ticTacMarkers, true);
        this.score = 0L;
        stack = new TicTacMoveStack();
    }

    /**
     * A getter for the score
     * @return the score
     */
    public Long getScore() {
        return this.score;
    }

    /**
     * A setter for the score.
     * @param s The score to be set.
     */
    public void setScore(Long s) {
        score = s;
    }


    /**
     * Checks if all the markers have been filled
     * @return if the puzzle markers have been filled.
     */
    public boolean isOver() {
        int count = 0;
        //check if the game oover flag is set
        if (board.getGameOver()) {
            return true;
        }
        Iterator<TicTacMarker> iter = board.iterator();
        while (iter.hasNext()) {
            if (iter.next().getBackgroundId() != 0) {
                count++;
            }
        }
        if(count == (board.getCols()* board.getRows())) {
            // board is full and game is over (tie)
            board.setGameOver(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getSpecificName() {
        return "TicTacToe";
    }

    /**
     * Return whether the tiles are in row-major order.
     * @param position of the current player
     * @return True if the tiles are in row major order, false if otherwise.
     */
    boolean puzzleSolved(int position) {

        int row = position / TicTacBoard.NUM_COLS;
        int col = position % TicTacBoard.NUM_COLS;
        int id = board.getMarker(row, col).getBackgroundId();

        if (board.getMarker(row, 0).getBackgroundId() == id &&
                board.getMarker(row, 1).getBackgroundId() == id &&
                board.getMarker(row, 2).getBackgroundId() == id) {
            return true;
        }
        else if (board.getMarker(0, col).getBackgroundId() == id &&
                board.getMarker(1, col).getBackgroundId() == id &&
                board.getMarker(2, col).getBackgroundId() == id) {
            return true;
        }
        int d1 = 0;
        int d2 = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (r + c == 2 && board.getMarker(r, c).getBackgroundId() == id) {
                    d1++;
                }
                if (r == c && board.getMarker(r, c).getBackgroundId() == id) {
                    d2++;
                }
            }
        }
        return d1 == 3 || d2 == 3;
    }

    /**
     * Return whether the blank tile is used.
     *
     * @param position the tile to check
     * @return whether the tile at position is blank tile
     */
    boolean isValidTap(int position) {
        return board.isValidTap(position);
    }

    public ArrayList<Integer> getValidMoves() {
        return board.getValidMoves();
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     * @return the current player turn
     */
    public int touchMove(int position) {

        int row = position / TicTacBoard.NUM_COLS;
        int col = position % TicTacBoard.NUM_COLS;
        int current_player = board.getCurrentPlayer();
        if (isValidTap(position)) {
            //check whos turn it is and drop the marker accordingly
            //get row and column of blank tile
            if (board.getCurrentPlayer() == board.getPlayer1()) {
                board.setBackground(row, col, board.getP1Background());
            } else if ((board.getCurrentPlayer() == board.getPlayer2())) {
                board.setBackground(row, col, board.getP2Background());
            }
            // change player turns after tap
            board.changeTurns();
            // just a test
            //board.swapMarkers(row, col, row, col);

        }
        score++;
        return current_player;
    }

}
