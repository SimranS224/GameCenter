package fall2018.csc2017.GameCentre.TicTacToe;

import android.content.Context;
import android.widget.Toast;


class TicTacMovementController {

    private TicTacBoardManager boardManager = null;

    TicTacMovementController() {
    }

    void setBoardManager(TicTacBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    void processTapMovement(Context context, int position) {
        if (boardManager.getBoard().getGameOver() == true) {
            return;
        }
        if (boardManager.isValidTap(position)) {
            int current_player =  boardManager.touchMove(position);
            // check if game is solved
            // Toast.makeText(context, "Add code to check puzzleSolved()", Toast.LENGTH_SHORT).show();
            if (boardManager.puzzleSolved(current_player)) {
                if(current_player == 0) {
                    Toast.makeText(context, "P1 WIN!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "P2 WIN!", Toast.LENGTH_SHORT).show();
                }
            } else if (boardManager.getStrategy().isValid()) {
                // strategy is valid so it is AI turn now
                if (boardManager.getBoard().getCurrentPlayer() == boardManager.getBoard().getPlayer2()) {
                    position = boardManager.getStrategy().getNextMovement(boardManager, 0);
                    current_player = boardManager.touchMove(position);
                    if (boardManager.puzzleSolved(current_player)) {
                        if (current_player == 0) {
                            Toast.makeText(context, "P1 WIN!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "P2 WIN!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        } else if (boardManager.isOver()) {
            Toast.makeText(context, "TIE", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Invalid Tap: position:" + position , Toast.LENGTH_SHORT).show();
        }
    }
}
