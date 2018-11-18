package fall2018.csc2017.GameCentre.TicTacToe;

import java.util.ArrayList;
import java.util.Random;

public class TicTacMinimaxStrategy extends TicTacStrategy {

    TicTacMinimaxStrategy(int depth) {
        super(depth);
    }

    public int getNextMovement(TicTacBoardManager boardmanager, int depth) {
        ArrayList<Integer> availableMoves = boardmanager.getValidMoves();
        Random rand = new Random();
        int n = rand.nextInt(availableMoves.size());
        return availableMoves.get(n);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
