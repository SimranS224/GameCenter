package fall2018.csc2017.GameCentre.TicTacToe;
import java.util.Random;


import java.util.ArrayList;

public class TicTacRandomStrategy extends TicTacStrategy{

    public TicTacRandomStrategy(int depth) {
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
