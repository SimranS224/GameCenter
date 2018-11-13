package fall2018.csc2017.GameCentre.TicTacToe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.R;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class TicTacBoardManager implements Serializable {

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
     * Manage a new shuffled board.
     */
    TicTacBoardManager() {
        List<TicTacMarker> ticTacMarkers = new ArrayList<>();
        final int numMarkers = TicTacBoard.NUM_ROWS * TicTacBoard.NUM_COLS;
        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
                ticTacMarkers.add(new TicTacMarker(row, col, 0));
            }

        }
        //tiles.add(new TicTacMarker(24));
        this.board = new TicTacBoard(ticTacMarkers);
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
     * Return whether the tiles are in row-major order.
     *
     * @return True if the tiles are in row major order, false if otherwise.
     */
    boolean puzzleSolved() {
        Iterator<TicTacMarker> MarkerIterator = this.board.iterator();
        TicTacMarker past = MarkerIterator.next();
        boolean solved = true;
        while (true) {
            if (MarkerIterator.hasNext()) {
                TicTacMarker current = MarkerIterator.next();
                if (past.getId() > current.getId()) {
                    solved = false;
                    break;
                }
                else {
                    past = current;
                }
            }
            else {
                break;
            }
        }
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {

        int row = position / TicTacBoard.NUM_COLS;
        int col = position % TicTacBoard.NUM_COLS;
        return (board.getMarker(row,col).getBackground() == R.drawable.blank_marker);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {

        int row = position / TicTacBoard.NUM_COLS;
        int col = position % TicTacBoard.NUM_COLS;
        int blankId = 25;

        if (isValidTap(position)) {
            //check whos turn it is and drop the marker accordingly
            //get row and column of blank tile
            board.getMarker(row, col).setBackground(1);
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

    }

}
