package fall2018.csc2017.GameCentre.Sequencer;

import java.io.Serializable;
import java.util.Observable;

import fall2018.csc2017.GameCentre.Manager;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class SequencerBoardManager extends Observable implements Serializable, Manager {
    /**
     * The number of rows.
     */
    static int NUM_ROWS = 4;

    /**
     * The number of rows.
     */
    static int NUM_COLS = 4;
    /**
     * Score of the game (number of correct taps)
     */
    private Long score;
    /**
     * The Sequence to be used for this game.
     */
    Sequence sequence;
    /**
     * Whether this game is over
     */
    private boolean gameOver = false;
    /**
     * Whether this board is currently talking (showing the sequence) or listening (waiting for the
     * user to repeat the sequence in the board), in which case the variable would be set to false.
     */
    boolean talking = true;

    /**
     * Constructor
     */
    SequencerBoardManager() {
        this.score = 1L;
        this.sequence = new Sequence();
    }

    /**
     * A getter for the score
     * @return the score
     */
    @Override
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
     * Increases the score by 1 and notifies the observers (so that the counter can get updated).
     */
    void increaseScore(){
        score += 1;
        setChanged();
        notifyObservers();
    }


    /**
     * Checks whether the tile that was just pressed corresponds with where in the sequence you are.
     * @param position Position where was tapped
     * @return Whether or not it was a correct tap. By calling get(), it also gets ready for the next call.
     */
    boolean isValidTap(int position) {
        return position == sequence.get();
    }

    /**
     * Sets that this game is over and announces it to the observers.
     */
    void setGameOver() {
        gameOver = true;
        setChanged();
        notifyObservers();
    }

    /**
     * Checks whether the game is over
     * @return Whether the game is over
     */
    @Override
    public boolean isOver() {
        return gameOver;
    }

    @Override
    public String getSpecificName() {
        return "Sequencer";
    }

}
