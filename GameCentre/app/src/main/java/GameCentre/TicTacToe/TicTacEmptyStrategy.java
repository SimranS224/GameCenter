package fall2018.csc2017.GameCentre.TicTacToe;

/**
 * Empty strategy for player vs player, strategy not needed, so it is empty
 */
public class TicTacEmptyStrategy extends TicTacStrategy{
    /**
     * calls superclass strategy
     * @param depth
     */
    public TicTacEmptyStrategy(int depth) {
        super(depth);
    }

    /**
     * gets the next move, since it is player vs player, it return -1 because the board is always
     * asking for taps
     * @param boardmanager
     * @param depth
     * @return
     */
    public int getNextMovement(TicTacBoardManager boardmanager, int depth) {
        return -1;
    }

    /**
     * returns if the strategy is valid (AI strats are valid, player vs player is not)
     * @return this returns false because it is player vs player
     */
    @Override
    public boolean isValid() {
        return false;
    }
}
