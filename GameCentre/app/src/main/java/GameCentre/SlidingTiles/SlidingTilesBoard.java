package fall2018.csc2017.GameCentre.SlidingTiles;

import android.support.annotation.NonNull;

import java.util.NoSuchElementException;
import java.util.Observable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The sliding tiles board .
 * Implements Serializable and Iterable<Tile> Interface
 */
public class SlidingTilesBoard extends Observable implements Serializable, Iterable<Tile> {

    /**
     * The number of rows.
     */
    public static int NUM_ROWS = 4;

    /**
     * The number of rows.
     */
    public static int NUM_COLS = 4;
    /**
     * Type of the board, number or image.
     */
    private static String TYPE = "number";
    /**
     * The image to be used if it is an image board.
     */
    private static String IMAGE = "Flower";

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles = new Tile[NUM_ROWS][NUM_COLS];

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    SlidingTilesBoard(List<Tile> tiles) {

        Iterator<Tile> iter = tiles.iterator();

        for (int row = 0; row != SlidingTilesBoard.NUM_ROWS; row++) {
            for (int col = 0; col != SlidingTilesBoard.NUM_COLS; col++) {
                this.tiles[row][col] = iter.next();
            }
        }

    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Get number of rows of the board
     *
     * @return the number of rows of the board
     */
    static int getNumRows() {
        return SlidingTilesBoard.NUM_ROWS;
    }

    /**
     * Get number of cols of the board
     *
     * @return the number of cols of the board
     */
    static int getNumCols() {
        return SlidingTilesBoard.NUM_COLS;
    }

    /**
     * Set the size of the board
     *
     * @param size the size of the board
     */
    public static void setBoardSize(int size) {
        NUM_ROWS = size;
        NUM_COLS = size;
    }

    /**
     * Set image of the board.
     *
     * @param image Image to be set
     */
    static void setIMAGE(String image) {
        IMAGE = image;
    }

    /**
     * Gets the image of this board.
     *
     * @return the image of this board.
     */
    static String getIMAGE() {
        return IMAGE;
    }

    /**
     * Sets the type of the board.
     *
     * @param type type of the board
     */
    public static void setType(String type) {
        TYPE = type;
    }

    /**
     * Gets the type of the board.
     *
     * @return returns the type of the board
     */
    public static String getType() {
        return TYPE;
    }

    /**
     * Get the size of the SlidingTilesBoard
     *
     * @return the number of rows in the board
     */
    public int getBoardSize() {
        return NUM_ROWS;
    }

    @Override
    public String toString() {
        return "SlidingTilesBoard{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    //implementing the iterator
    @NonNull
    @Override
    public Iterator<Tile> iterator() {
        return new BoardIterator();
    }

    /**
     * The iterator for the board that implements the Iterator<Tile> Interface
     */
    private class BoardIterator implements Iterator<Tile> {

        /**
         * Row of the next tile in the board
         */
        int nextRow = 0;

        /**
         * Column of the next tile in the board
         */
        int nextColumn = 0;

        @Override
        public boolean hasNext() {
            return nextRow < NUM_ROWS && nextColumn < NUM_COLS;
        }

        @Override
        public Tile next() {

            if (!this.hasNext()) {
                throw new NoSuchElementException("End of board reached");
            }
            //iterate through the rows of the board
            if (nextRow < NUM_ROWS) {

                //iterate through the columns of the board
                if (nextColumn < NUM_COLS) {

                    if (tiles[nextRow][nextColumn] != null) {
                        Tile nextTile = tiles[nextRow][nextColumn];
                        nextColumn++;

                        //reset nextColumn and nextRow if end of column reached
                        if (nextColumn == NUM_COLS) {
                            nextColumn = 0;
                            nextRow++;
                        }
                        return nextTile;
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
    void setTiles(Tile tile, int row, int col) {
        tiles[row][col] = tile;
        setChanged();
        notifyObservers();
    }
}
