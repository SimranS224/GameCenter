package fall2018.csc2017.GameCentre.TicTacToe;

import java.util.ArrayList;
/**
 * @param boardmanager is the gamestate
 * @param depth is a value to prolong the game as long as possible
 * @return a selected movement
 */
public abstract class TicTacStrategy {
    /**
     * depth used in minimax to calculate next move
     */
    protected int depth;

    /**
     * gets the next move for the AI player
     * @param boardmanager looks at the board
     * @param depth used to calculate next move
     * @return the move for the ai
     */
    public abstract int getNextMovement(TicTacBoardManager boardmanager, int depth);

    /**
     * returns if the strategy is valid (AI strats are valid, player vs player is not)
     * @return (AI strats are true, player vs player is false)
     */
    public abstract boolean isValid();

    /**
     * constructor for strategy, needing depth
     */
    TicTacStrategy(int depth) {
        this.depth = depth;
    }

}
