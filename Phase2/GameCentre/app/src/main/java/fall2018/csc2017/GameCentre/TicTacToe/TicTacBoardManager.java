package fall2018.csc2017.GameCentre.TicTacToe;

import fall2018.csc2017.GameCentre.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class TicTacBoardManager implements Serializable {


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
    private Integer score;

    /**
     * The board being managed.
     */
    private TicTacBoard board;
    private TicTacMoveStack stack;

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    TicTacBoardManager(TicTacBoard board) {
        this.board = board;
        this.score = 0;
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
        final int numMarkers = TicTacBoard.NUM_ROWS * TicTacBoard.NUM_COLS;
        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
                ticTacMarkers.add(new TicTacMarker(row, col, 0));
            }

        }
        // assume p1 always goes first
        this.board = new TicTacBoard(ticTacMarkers, true);
        this.score = 0;
        stack = new TicTacMoveStack();
    }

    /**
     * A getter for the score
     * @return the score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * A setter for the score.
     * @param s The score to be set.
     */
    public void setScore(int s) {
        score = s;
    }


    /**
     * Checks if all the markers have been filled
     * @return if the puzzle markers have been filled.
     */
    boolean isOver() {
        int count = 0;
        //check if the game oover flag is set
        if (board.getGameOver()) {
            return true;
        }
        while (count != (board.getCols()* board.getRows())) {
            if (board.iterator().next().getBackgroundId() != 0) {
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

    /**
     * Return whether the tiles are in row-major order.
     * @param current_player the current player
     * @return True if the tiles are in row major order, false if otherwise.
     */
    boolean puzzleSolved(int current_player) {
        int background_id = 0;
        // after turn, check board to see if anyone has one
        if ((this.board.getMarker(0,0).getBackgroundId() ==
                this.board.getMarker(0,1).getBackgroundId()) &&
                ((this.board.getMarker(0,0).getBackgroundId() ==
                this.board.getMarker(0,2).getBackgroundId()))) {
            background_id = this.board.getMarker(0,1).getBackgroundId();
        } else if ((this.board.getMarker(1,0).getBackgroundId() ==
                this.board.getMarker(1,1).getBackgroundId()) &&
                ((this.board.getMarker(1,0).getBackgroundId() ==
                        this.board.getMarker(1,2).getBackgroundId()))) {
            background_id = this.board.getMarker(1,0).getBackgroundId();
        } else if ((this.board.getMarker(2,0).getBackgroundId() ==
                this.board.getMarker(2,1).getBackgroundId()) &&
                ((this.board.getMarker(2,0).getBackgroundId() ==
                        this.board.getMarker(2,2).getBackgroundId()))) {
            background_id = this.board.getMarker(2,0).getBackgroundId();
        } else if ((this.board.getMarker(0,0).getBackgroundId() ==
                this.board.getMarker(1,0).getBackgroundId()) &&
                ((this.board.getMarker(2,0).getBackgroundId() ==
                        this.board.getMarker(1,0).getBackgroundId()))) {
            background_id = this.board.getMarker(0,0).getBackgroundId();
        } else if ((this.board.getMarker(0,1).getBackgroundId() ==
                this.board.getMarker(1,1).getBackgroundId()) &&
                ((this.board.getMarker(2,1).getBackgroundId() ==
                        this.board.getMarker(1,1).getBackgroundId()))) {
            background_id = this.board.getMarker(1,1).getBackgroundId();
        } else if ((this.board.getMarker(0,2).getBackgroundId() ==
                this.board.getMarker(1,2).getBackgroundId()) &&
                ((this.board.getMarker(2,2).getBackgroundId() ==
                        this.board.getMarker(1,2).getBackgroundId()))) {
            background_id = this.board.getMarker(2,2).getBackgroundId();
        } else if ((this.board.getMarker(0,0).getBackgroundId() ==
                this.board.getMarker(1,1).getBackgroundId()) &&
                ((this.board.getMarker(2,2).getBackgroundId() ==
                        this.board.getMarker(1,1).getBackgroundId()))) {
            background_id = this.board.getMarker(1,1).getBackgroundId();
        } else if ((this.board.getMarker(2,0).getBackgroundId() ==
                this.board.getMarker(1,1).getBackgroundId()) &&
                ((this.board.getMarker(0,2).getBackgroundId() ==
                        this.board.getMarker(1,1).getBackgroundId()))) {
            background_id = this.board.getMarker(1,1).getBackgroundId();

        }
        if (background_id == board.getPlayerBackground(current_player)) {
            // current player wins and game is over
            board.setGameOver(true);
            return true;
        }
        return false;
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
            if (board.getCurrentPlayer() == 0) {
                board.getMarker(row, col).setBackground(board.getP1Background());
            } else if ((board.getCurrentPlayer() == 1)) {
                board.getMarker(row, col).setBackground(board.getP2_background());
            }
            // change player turns after tap
            board.changeTurns();

 /*           int blankPos = 0;
            Iterator<TicTacMarker> iter = board.iterator();
            while (iter.next().getId() != blankId) {
                blankPos++;
            }
            int blankRow = blankPos / TicTacBoard.NUM_COLS;
            int blankCol = blankPos % TicTacBoard.NUM_COLS;

            //swap them
            board.swapMarkers(row, col, blankRow, blankCol);
            Integer[] c = {row, col, blankRow, blankCol};
            stack.add(c);*/
            // just a test
            board.swapMarkers(row, col, row, col);

        }
        score++;
        return current_player;
    }

}
