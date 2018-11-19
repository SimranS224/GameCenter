package fall2018.csc2017.GameCentre.TicTacToe;

public class TicTacEmptyStrategy extends TicTacStrategy{
    TicTacEmptyStrategy(int depth) {
        super(depth);
    }

    public int getNextMovement(TicTacBoardManager boardmanager, int depth) {
        return -1;
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
