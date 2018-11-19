package fall2018.csc2017.GameCentre.TicTacToe;

import java.util.ArrayList;
/**
 * @param boardmanager is the gamestate
 * @param depth is a value to prolong the game as long as possible
 * @return a selected movement
 */
public abstract class TicTacStrategy {
    protected int depth;

    public abstract int getNextMovement(TicTacBoardManager boardmanager, int depth);

    public abstract boolean isValid();

    TicTacStrategy(int depth) {
        this.depth = depth;
    }

}
