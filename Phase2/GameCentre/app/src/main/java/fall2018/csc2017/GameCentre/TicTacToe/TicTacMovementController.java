package fall2018.csc2017.GameCentre.TicTacToe;

import android.content.Context;
import android.widget.Toast;

class TicTacMovementController {
    private TicTacBoardManager boardManager;
    TicTacMovementController() {
    }

    public void setBoardManager(TicTacBoardManager boardManager) { this.boardManager = boardManager;}

    void processTapMovement(Context context, int position) {
        if (boardManager.getBoard().getGameOver()) {
            return;
        }
        if (!Timer.getmTimerRunning()) {
            boardManager.getBoard().setGameOver(true);
            Toast.makeText(context, "NO MORE TIME!", Toast.LENGTH_SHORT).show();
        } else
        if (boardManager.isValidTap(position)) {
            int current_player =  boardManager.touchMove(position);
            // check if game is solved
            // Toast.makeText(context, "Add code to check getWinner()", Toast.LENGTH_SHORT).show();
            if (boardManager.getWinner(position)) {
                boardManager.getTimer().pauseTimer();
                boardManager.getBoard().setGameOver(true);
                boardManager.setUserWins(true);
                Toast.makeText(context, "P1 WIN!", Toast.LENGTH_SHORT).show();
            }  else if (boardManager.getValidMoves().size() == 0) {
                boardManager.getTimer().pauseTimer();
                boardManager.getBoard().setGameOver(true);
                Toast.makeText(context, "Tie!", Toast.LENGTH_SHORT).show();
            } else {
                if (boardManager.getStrategy().isValid()) {
                    // strategy is valid so it is AI turn now
                    if (boardManager.getBoard().getCurrentPlayer() == boardManager.getBoard().getPlayer2()) {
                        position = boardManager.getStrategy().getNextMovement(boardManager, 0);
                        current_player = boardManager.touchMove(position);
                        if (boardManager.getWinner(position)) {
                            boardManager.getTimer().pauseTimer();
                            boardManager.getBoard().setGameOver(true);
                            Toast.makeText(context, "P2 WIN!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        }  else {
            Toast.makeText(context, "Invalid Tap: position:" + position , Toast.LENGTH_SHORT).show();
        }
    }
}