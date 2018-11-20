package fall2018.csc2017.GameCentre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class SequencerBoardManager implements Serializable {

    /**
     * score
     */
    private Integer score;

    /**
     * The board being managed.
     */
    private SequencerBoard board;
    MoveStack stack;
    Sequence sequence;
    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    SequencerBoardManager(SequencerBoard board) {
        this.board = board;
        this.score = 0;
        this.stack = new MoveStack();
    }

    /**
     * Return the current board.
     */
    SequencerBoard getBoard() {
        return board;
    }

    /**
     * Manage a new shuffled board.
     */
    SequencerBoardManager() {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = SequencerBoard.NUM_ROWS * SequencerBoard.NUM_COLS;
        for (int tileNum = 0; tileNum != (numTiles-1); tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        tiles.add(new Tile(24));
        Collections.shuffle(tiles);
        this.board = new SequencerBoard(tiles);
        this.score = 1;
        stack = new MoveStack();
        this.sequence = new Sequence();
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
    public void increaseScore(){ score += 1; }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return True if the tiles are in row major order, false if otherwise.
     */
    boolean puzzleSolved() {
        Iterator<Tile> TileIterator = this.board.iterator();
        Tile past = TileIterator.next();
        boolean solved = true;
        while (true) {
            if (TileIterator.hasNext()) {
                Tile current = TileIterator.next();
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
        return position == sequence.listenGet();
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {

        int row = position / SequencerBoard.NUM_ROWS;
        int col = position % SequencerBoard.NUM_COLS;
        int blankId = 25;

        if (isValidTap(position)) {

            //get row and column of blank tile
            int blankPos = 0;
            Iterator<Tile> iter = board.iterator();
            while (iter.next().getId() != blankId) {
                blankPos++;
            }
            int blankRow = blankPos / SequencerBoard.NUM_ROWS;
            int blankCol = blankPos % SequencerBoard.NUM_COLS;

            //swap them
            board.swapTiles(row, col, blankRow, blankCol);
            Integer[] c = {row, col, blankRow, blankCol};
            stack.add(c);
        }
        score++;

    }

}
