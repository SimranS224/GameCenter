package fall2018.csc2017.GameCentre.Sequencer;

import java.io.Serializable;
import java.util.Observable;
import fall2018.csc2017.GameCentre.Manager;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
public class SequencerBoardManager extends Observable implements Serializable, Manager {
    /**
     * The number of rows.
     */
    static int NUM_ROWS = 4;

    /**
     * The number of rows.
     */
    static int NUM_COLS = 4;
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
     * The round until where in the sequence you have to remember.
     * First round: Remember the first element of the sequence
     * Second Round: Remember the first two elements of the sequence
     * etc.
     */
    private int round = 1;

    /**
     * Constructor
     */
    public SequencerBoardManager() {
        this.sequence = new Sequence();
    }

    /**
     * A getter for the score
     * @return the score
     */
    @Override
    public Long getCurrGameScore() {
        return (long) 100 - round;
    }


    /**
     * Checks whether the tile that was just pressed corresponds with where in the sequence you are.
     * @param position Position where was tapped
     * @return Whether or not it was a correct tap. By calling get(), it also gets ready for the next call.
     */
    public boolean isValidTap(int position) {
        return position == sequence.get();
    }

    /**
     * Sets that this game is over and announces it to the observers.
     */
    void setGameOver() {
        gameOver = true;
        update();
    }
    void increaseRound() {
        round += 1;
        update();

    }
    int getRound() {
        return round;
    }

    void update(){
        setChanged();
        notifyObservers();
    }

    /**
     * Checks whether the game is over
     * @return Whether the game is over
     */
    @Override
    public boolean isOver() {
        if (round == 100) {
            return true;
        }
        return gameOver;
    }

    /**
     * Name to identify which game this is
     * @return Name of this game.
     */
    @Override
    public String getSpecificName() {
        return "Sequencer";
    }

}
