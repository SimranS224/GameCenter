package fall2018.csc2017.GameCentre.TicTacToe;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;

/**
 * The sliding ticTacMarkers board .
 * Implements Serializable and Iterable<TicTacMarker> Interface
 */
public class TicTacBoard extends Observable implements Serializable, Iterable<TicTacMarker> {

    private static int P1_ID = 0;
    private static int P2_ID = 1;
    /**
     * The first player
     */
    // if true, p1 plays first, false p1 plays second
    private boolean p1_turn;

    /**
     * game over
     */
    // if true,game is over, false not over
    private boolean game_over;

    /**
     * The current player
     * current_player = 0 means player 1 turn
     * current_player = 1 means player 2 turn
     */
    private int current_player;

    /**
     * p1 marker color
     */
    private static int p1_background = 1;

    /**
     * p2 marker color
     */
    private static int p2_background = 2;

    /**
     * The number of rows.
     */
    static int NUM_ROWS = 3;

    /**
     * The number of rows.
     */
    static int NUM_COLS = 3;
    /**
     * Type of the board, number or image.
     */
    private static String TYPE = "number";

    /**
     * The ticTacMarkers on the board in row-major order.
     */
    private TicTacMarker[][] ticTacMarkers = new TicTacMarker[NUM_ROWS][NUM_COLS];

    /**
     * A new board of ticTacMarkers in row-major order.
     * Precondition: len(ticTacMarkers) == NUM_ROWS * NUM_COLS
     *
     * @param ticTacMarkers the ticTacMarkers for the board
     */
    TicTacBoard(List<TicTacMarker> ticTacMarkers, boolean p1_turn) {
        this.p1_turn = p1_turn;
        this.game_over = false;
        //set the players turn to player 1 or 2
        if (this.p1_turn == true) {
            this.current_player = 0;
        } else {
            this.current_player = 1;
        }
        Iterator<TicTacMarker> iter = ticTacMarkers.iterator();

        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
                this.ticTacMarkers[row][col] = iter.next();
            }
        }
    }

    /**
     * clone the board
     * @param board
     */
    public TicTacBoard(TicTacBoard board) {
        this.current_player = board.current_player;
        this.p1_turn = board.p1_turn;
        this.game_over = board.game_over;
        this.current_player = board.current_player;


        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
                this.ticTacMarkers[row][col] = new TicTacMarker(row, col, 0);
                this.ticTacMarkers[row][col].setBackground(board.ticTacMarkers[row][col].getBackgroundId());
                this.ticTacMarkers[row][col].setId(board.ticTacMarkers[row][col].getId());
            }
        }
    }

    /**
     * Return p1 background
     */
    public int getP1Background() {
        return this.p1_background;
    }

    /**
     * Return gameover
     */
    public boolean getGameOver() {
        return this.game_over;
    }

    /**
     * set gameover
     */
    public void setGameOver(boolean game_over) {
        this.game_over = game_over;
    }

    /**
     * Return p2 background
     */
    public int getP2Background() {
        return this.p2_background;
    }

    /**
     * Return current player
     */
    public int getCurrentPlayer() {
        return this.current_player;
    }

    /**
     * Return player1 id
     */
    public int getPlayer1() {
        return this.P1_ID;
    }

    /**
     * Return player2 id
     */
    public int getPlayer2() {
        return this.P2_ID;
    }

    /**
     * Return background
     */
    public int getPlayerBackground(int player) {
        if (player == 0) {
            return p1_background;
        } else {
            return p2_background;
        }
    }

    /**
     * change turns
     */
    public void changeTurns() {
        if (this.current_player == 0) {
            this.current_player = 1;
        } else {
            this.current_player = 0;
        }
    }

    /**
     * Return num rows
     */
    public int getRows() {
        return this.NUM_ROWS;
    }

    /**
     * Return num cols
     */
    public int getCols() {
        return this.NUM_COLS;
    }
    /**
     * Return the TicTacMarker at (row, col)
     *
     * @param row the TicTacMarker row
     * @param col the TicTacMarker column
     * @return the TicTacMarker at (row, col)
     */
    TicTacMarker getMarker(int row, int col) {
        return ticTacMarkers[row][col];
    }

    /**
     * Set the size of the board
     * @param size the size of the board
     */
    public static void setBoardSize(int size) {
        NUM_ROWS=size;
        NUM_COLS=size;
    }

    /**
     * Sets the type of the board.
     * @param type type of the board
     */
    public static void setType(String type){
        TYPE = type;
    }

    /**
     * Gets the type of the board.
     * @return returns the type of the board
     */
    public static String getType() {
        return TYPE;
    }

    /**
     * Get the size of the Board
     * @return the number of rows in the board
     */
    public int getBoardSize() {
        return NUM_ROWS;
    }

    // set background
    void setBackground(int row, int col, int backgroundid) {
        ticTacMarkers[row][col].setBackground(backgroundid); //to store original TicTacMarker
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return "Board{" +
                "ticTacMarkers=" + Arrays.toString(ticTacMarkers) +
                '}';
    }

    /**
     * Return whether the blank tile is used.
     *
     * @param position the tile to check
     * @return whether the tile at position is blank tile
     */
    boolean isValidTap(int position) {

        int row = position / this.NUM_COLS;
        int col = position % this.NUM_COLS;
        return (this.getMarker(row,col).getBackgroundId() == 0);
    }

    /**
     * returns the list of valid moves left on the board
     * @return the list of valid moves on the board
     */
    public ArrayList<Integer> getValidMoves() {
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        for (int position = 0; position < this.getRows() * this.getCols(); position++) {
            if (isValidTap(position)) {
                Integer IntPos = new Integer(position);
                validMoves.add(IntPos);
            }
        }
        return validMoves;
    }

    //implementing the iterator
    @NonNull
    @Override
    public Iterator<TicTacMarker> iterator() {
        return new BoardIterator();
    }

    /**
     * The iterator for the board that implements the Iterator<TicTacMarker> Interface
     */
    private class BoardIterator implements Iterator<TicTacMarker> {

        /**
         * Row of the next TicTacMarker in the board
         */
        int nextRow = 0;

        /**
         * Column of the next TicTacMarker in the board
         */
        int nextColumn = 0;

        @Override
        public boolean hasNext() {
            return nextRow < NUM_ROWS && nextColumn < NUM_COLS;
        }

        @Override
        public TicTacMarker next() {

            if (!this.hasNext()) {
                throw new NoSuchElementException("End of board reached");
            }
            //iterate through the rows of the board
            if (nextRow < NUM_ROWS) {

                //iterate through the columns of the board
                if (nextColumn < NUM_COLS) {

                    if (ticTacMarkers[nextRow][nextColumn] != null) {
                        TicTacMarker nextTicTacMarker = ticTacMarkers[nextRow][nextColumn];
                        nextColumn++;

                        //reset nextColumn and nextRow if end of column reached
                        if (nextColumn == NUM_COLS) {
                            nextColumn = 0;
                            nextRow++;
                        }
                        return nextTicTacMarker;
                    } else {
                        nextColumn++;
                        //reset nextColumn and nextRow if end of column reached
                        if (nextColumn == NUM_COLS) {
                            nextColumn = 0;
                            nextRow++;
                        }
                    }
                }
                nextRow++;
            }
            return null;
        }
    }
}
