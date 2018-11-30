package fall2018.csc2017.GameCentre.TicTacToe;

import fall2018.csc2017.GameCentre.Manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
public class TicTacBoardManager implements Serializable, Manager {

    /**
     * new instance of timer
     */
    public Timer time = new Timer();

    /**
     * p1wins variable
     */
    private boolean p1Wins = false;

    /**
     * global variable position
     */
    private int game_position = -1;

    /**
     * strategy
     */
    private TicTacStrategy strategy;

    /**
     * moveCounter
     */
    private Long moveCounter;
    /**
     * getwinner has been called
     */
    private boolean getWinnerHasBeenCalled = false;
    /**
     * The board being managed.
     */
    private TicTacBoard board;

    /**
     * Get timer
     */
    Timer getTimer() {
        return time;
    }
    /**
     * Manage a board that has been pre-populated.
     * THIS NEEDS TO BE KEPT AND IS USED WHEN CALLING THE AI STRATEGIES
     * @param board the board
     */
    public TicTacBoardManager(TicTacBoard board) {
        this.board = board;
        this.moveCounter = 0L;
        this.game_position = -1;
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
    public TicTacBoardManager(TicTacStrategy strategy) {
        this.strategy = strategy;

        List<TicTacMarker> ticTacMarkers = new ArrayList<>();
        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
                ticTacMarkers.add(new TicTacMarker(row, col, 0));
            }
        }
        this.board = new TicTacBoard(ticTacMarkers);
        this.moveCounter = 0L;
    }

    /**
     * A getter for the moveCounter
     * @return the moveCounter
     */
    public Long getMoveCounter() {
        return this.moveCounter;
    }

    /**
     * A setter for the moveCounter.
     * @param s The moveCounter to be set.
     */
    public void setMoveCounter(Long s) {
        moveCounter = s;
    }

    public int getGamePosition() {
        return this.game_position;
    }

    @Override
    public Long getCurrGameScore() {
        long longtime = time.getmTimeLeftInMillis();
        Long doubletime = (longtime / 1000);
        Long score = 100 - doubletime;
        return score;
    }

    /**
     * Checks if all the markers have been filled
     * @return if the puzzle markers have been filled.
     */
    public boolean isOver() {
        return (this.getBoard().getGameOver() && p1Wins);
    }

    @Override
    public String getSpecificName() {
        return "TicTacToe";
    }

    /**
     * Return whether the tiles are in row-major order. Uses the fact that a win can only come
     * from the last tap.
     * @param position of the current player
     * @return True if the tiles are in row major order, false if otherwise.
     */
    boolean getWinner(int position) {
        // Setup
        int row = position / TicTacBoard.NUM_COLS;
        int col = position % TicTacBoard.NUM_COLS;
        // The id of the recently tapped marker (The ID for the current player)
        int id = board.getMarker(row, col).getBackgroundId();

        //Checks the row for tapped marker
        if (board.getMarker(row, 0).getBackgroundId() == id &&
                board.getMarker(row, 1).getBackgroundId() == id &&
                board.getMarker(row, 2).getBackgroundId() == id) {
            if (this.getBoard().getCurrentPlayer() == this.getBoard().getPlayer1()) {
                this.p1Wins = true;
            }

            return true;
        }

        // Checks the column for tapped marker.
        else if (board.getMarker(0, col).getBackgroundId() == id &&
                board.getMarker(1, col).getBackgroundId() == id &&
                board.getMarker(2, col).getBackgroundId() == id) {
            if (this.getBoard().getCurrentPlayer() == this.getBoard().getPlayer1()) {
                this.p1Wins = true;
            }
            return true;
        }
        // Checks the diagonals.
        // Uses the fact that markers in a diagonal have coordinates that add up to 2
        // and the coordinates are the same for the other diagonal
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
        if (this.getBoard().getCurrentPlayer() == this.getBoard().getPlayer1() &&
                (d1 == 3 || d2 == 3)) {
            this.p1Wins = true;
        }
        getWinnerHasBeenCalled = true;
        return d1 == 3 || d2 == 3;
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
        this.game_position = -1;
        if (isValidTap(position)) {
            this.game_position = position;
            //check whos turn it is and drop the marker accordingly
            //get row and column of blank tile
            if (board.getCurrentPlayer() == board.getPlayer1()) {
                board.setBackground(row, col, board.getP1Background());
            } else if ((board.getCurrentPlayer() == board.getPlayer2())) {
                board.setBackground(row, col, board.getP2Background());
            }
            // change player turns after tap
            this.changeTurns();

        }
        moveCounter++;
        return current_player;
    }

    public boolean isGetWinnerHasBeenCalled() {
        return getWinnerHasBeenCalled;
    }

    public void setUserWins(boolean b) {
        this.p1Wins = true;
    }

    /**
     * Return whether the blank tile is used.
     *
     * @param position the tile to check
     * @return whether the tile at position is blank tile
     */
    boolean isValidTap(int position) {

        int row = position / board.NUM_COLS;
        int col = position % board.NUM_COLS;
        return (board.getMarker(row,col).getBackgroundId() == 0);
    }

    /**
     * returns the list of valid moves left on the board
     * @return the list of valid moves on the board
     */
    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        for (int position = 0; position < board.getRows() * board.getCols(); position++) {
            if (isValidTap(position)) {
                Integer IntPos = new Integer(position);
                validMoves.add(IntPos);
            }
        }
        return validMoves;
    }

    /**
     * change turns
     */
    public void changeTurns() {
        if (board.current_player == 0) {
            board.current_player = 1;
        } else {
            board.current_player = 0;
        }
    }

}
