package fall2018.csc2017.GameCentre.TicTacToe;
import java.util.Random;


import java.util.ArrayList;

/**
 * Random strategy class for AI
 */
public class TicTacRandomStrategy extends TicTacStrategy{

    /**
     * call super class Strategy
     * @param depth
     */
    public TicTacRandomStrategy(int depth) {
       super(depth);
    }

    /**
     * gets the next move for the AI player
     * @param boardmanager looks at the board
     * @param depth used to calculate next move
     * @return the move for the ai
     */
    public int getNextMovement(TicTacBoardManager boardmanager, int depth) {
        ArrayList<Integer> availableMoves = boardmanager.getValidMoves();
        Random rand = new Random();
        int n = rand.nextInt(availableMoves.size());
        return availableMoves.get(n);
    }

    /**
     * returns if the strategy is valid
     * @return true because random strategy is AI strategy
     */
    @Override
    public boolean isValid() {
        return true;
    }
}
