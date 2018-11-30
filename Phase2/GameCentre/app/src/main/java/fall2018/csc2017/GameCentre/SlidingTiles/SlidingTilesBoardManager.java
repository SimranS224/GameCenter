package fall2018.csc2017.GameCentre.SlidingTiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import fall2018.csc2017.GameCentre.Manager;

/**
 * Manage a slidingTilesBoard, including swapping tiles, checking for a win, and managing taps.
 */
public class SlidingTilesBoardManager extends Observable implements Serializable, Manager {

    /**
     * score
     */
    private Long score;

    /**
     * The slidingTilesBoard being managed.
     */
    private SlidingTilesBoard slidingTilesBoard;
    MoveStack stack;

    /**
     * Manage a slidingTilesBoard that has been pre-populated.
     *
     * @param slidingTilesBoard the slidingTilesBoard
     */
    SlidingTilesBoardManager(SlidingTilesBoard slidingTilesBoard) {
        this.slidingTilesBoard = slidingTilesBoard;
        this.score = 0L;
        this.stack = new MoveStack();
    }

    /**
     * Return the current slidingTilesBoard.
     */
    SlidingTilesBoard getSlidingTilesBoard() {
        return slidingTilesBoard;
    }

    /**
     * Manage a new shuffled slidingTilesBoard.
     */
    SlidingTilesBoardManager() {
        List<Tile> tiles = new ArrayList<>();

        final int numTiles = SlidingTilesBoard.NUM_COLS * SlidingTilesBoard.NUM_ROWS;
        for (int tileNum = 0; tileNum != (numTiles - 1); tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        tiles.add(new Tile(24));

        //check if puzzle is solvable
        boolean solvable = false;

        while (!solvable) {
            Collections.shuffle(tiles);
            solvable = checkPuzzleSolvable(tiles);
        }

        this.slidingTilesBoard = new SlidingTilesBoard(tiles);
        this.score = 0L;
        stack = new MoveStack();
    }

    /**
     * Return the name corresponding to the size.
     *
     * @return the name corresponding to the size
     */
    @Override
    public String getSpecificName() {
        if (slidingTilesBoard.getBoardSize() == 3) {
            return "SlidingTilesThree";
        } else if (slidingTilesBoard.getBoardSize() == 4) {
            return "SlidingTilesFour";
        } else {
            return "SlidingTilesFive";
        }
    }

    /**
     * An algorithm to see whether the slidingTilesBoard is solvable
     *
     * @return boolean, True if solvable, False otherwise
     */
    boolean checkPuzzleSolvable(List<Tile> tiles) {
        // calculate number of inversions
        int inversions = 0;
        boolean solvable = false;
        for (int i = 0; i != tiles.size() - 1; i++) {
            //get the ith iteration of the from the tiles list
            //use it as a reference

            for (int j = i + 1; j < tiles.size(); j++) {
                //check whether the ith iteration from tiles list is
                // greater than jth iteration from tiles list
                if ((tiles.get(i).getId() > tiles.get(j).getId()) && (tiles.get(j).getId() != 25)
                        && (tiles.get(i).getId() != 25))
                    inversions += 1;
            }
        }
        //check if number of rows is odd or even
        // If the grid width is odd, then the number of inversions in a solvable situation is even.
        if (SlidingTilesBoard.getNumRows() % 2 != 0) {
            if (inversions % 2 == 0) {
                solvable = true;
            }
        } else {
            // the number of rows is even
            // If the grid width is even, and the blank is on an odd row counting from the bottom
            // (first, third-last etc), then the number of inversions in a solvable situation
            // is odd
            // check row position of blank tile
            int position = 0;
            for (int i = 0; i < tiles.size(); i++) {
                if (tiles.get(i).getId() == 25) {
                    position = i;
                    break;
                }
            }

            // get row position
            int row_pos = position / SlidingTilesBoard.getNumCols();
            solvable = ((row_pos % 2 == 0) && (inversions % 2 != 0)) ||
                    ((row_pos % 2 != 0) && (inversions % 2 == 0));
        }
        return solvable;
    }

    /**
     * A getter for the score
     *
     * @return the score
     */
    @Override
    public Long getCurrGameScore() {
        return this.score;
    }

    /**
     * A setter for the score.
     *
     * @param s The score to be set.
     */
    public void setScore(Long s) {
        score = s;
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return True if the tiles are in row major order, false if otherwise.
     */
    @Override
    public boolean isOver() {
        Iterator<Tile> TileIterator = this.slidingTilesBoard.iterator();
        Tile past = TileIterator.next();
        boolean solved = true;
        while (true) {
            if (TileIterator.hasNext()) {
                Tile current = TileIterator.next();
                if (past.getId() > current.getId()) {
                    solved = false;
                    break;
                } else {
                    past = current;
                }
            } else {
                break;
            }
        }
        return solved || score == 2;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {

        int row = position / SlidingTilesBoard.NUM_COLS;
        int col = position % SlidingTilesBoard.NUM_COLS;
        int blankId = 25;
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : slidingTilesBoard.getTile(row - 1, col);
        Tile below = row == SlidingTilesBoard.NUM_ROWS - 1 ? null : slidingTilesBoard.getTile(row + 1, col);
        Tile left = col == 0 ? null : slidingTilesBoard.getTile(row, col - 1);
        Tile right = col == SlidingTilesBoard.NUM_COLS - 1 ? null : slidingTilesBoard.getTile(row, col + 1);
        System.out.println(right.getId());
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the slidingTilesBoard, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {

        int row = position / SlidingTilesBoard.NUM_ROWS;
        int col = position % SlidingTilesBoard.NUM_COLS;
        int blankId = 25;

        if (isValidTap(position)) {

            //get row and column of blank tile
            int blankPos = 0;
            Iterator<Tile> iter = slidingTilesBoard.iterator();
            while (iter.next().getId() != blankId) {
                blankPos++;
            }
            int blankRow = blankPos / SlidingTilesBoard.NUM_ROWS;
            int blankCol = blankPos % SlidingTilesBoard.NUM_COLS;

            //swap them
            swapTiles(row, col, blankRow, blankCol);
            Integer[] c = {row, col, blankRow, blankCol};
            stack.add(c);
            score++;
        }

    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    void swapTiles(int row1, int col1, int row2, int col2) {
        Tile tile1 = slidingTilesBoard.getTile(row1, col1); //to store original tile
        Tile tile2 = slidingTilesBoard.getTile(row2, col2);
        slidingTilesBoard.setTiles(tile1, row2, col2);
        slidingTilesBoard.setTiles(tile2, row1, col1);
        setChanged();
        notifyObservers();
    }

}
