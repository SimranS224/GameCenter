package fall2018.csc2017.GameCentre.Sequencer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import fall2018.csc2017.GameCentre.Tile;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class SequencerBoardManager extends Observable implements Serializable {
    /**
     * The number of rows.
     */
    static int NUM_ROWS = 4;

    /**
     * The number of rows.
     */
    static int NUM_COLS = 4;
    /**
     * score
     */
    private Integer score;
    /**
     * The Sequence to be used for this game.
     */
    Sequence sequence;
    /**
     * Whether this game is over
     */
    private boolean gameOver = false;

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */

    /**
     * Manage a new shuffled board.
     */
    SequencerBoardManager() {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = NUM_ROWS * NUM_COLS;
//        for (int tileNum = 0; tileNum != (numTiles-1); tileNum++) {
//            tiles.add(new Tile(tileNum));
//        }
        //tiles.add(new Tile(24));
        Collections.shuffle(tiles);
        this.score = 1;
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
    void increaseScore(){
        score += 1;
        setChanged();
        notifyObservers();
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return True if the tiles are in row major order, false if otherwise.
     */
//    boolean puzzleSolved() {
//        Iterator<Tile> TileIterator = this.board.iterator();
//        Tile past = TileIterator.next();
//        boolean solved = true;
//        while (true) {
//            if (TileIterator.hasNext()) {
//                Tile current = TileIterator.next();
//                if (past.getId() > current.getId()) {
//                    solved = false;
//                    break;
//                }
//                else {
//                    past = current;
//                }
//            }
//            else {
//                break;
//            }
//        }
//        return solved;
//    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {
        return position == sequence.listenGet();
    }
    void setGameOver() {
        gameOver = true;
        setChanged();
        notifyObservers();
    }
    boolean isOver() {
        return gameOver;
    }

}
