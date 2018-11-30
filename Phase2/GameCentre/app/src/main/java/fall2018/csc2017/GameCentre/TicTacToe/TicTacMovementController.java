package fall2018.csc2017.GameCentre.TicTacToe;

import android.content.Context;
import android.widget.Toast;

/**
 * MODEL/VIEW - did not test because tests would be for MODEL/VIEW, which did not need to be implemented
 */

/**
 * This is the TicTacToe movement controller, meant to take in a position (a
 * tap on the screen) and process the move for the given player from board
 * manager. This class also takes care of taking turns of player 2, whether it be
 * an AI or otherwise;
 */
class TicTacMovementController {
    private TicTacBoardManager boardManager;
    TicTacMovementController() {
    }

    /**
     * Setter for the boardManager.
     * @param boardManager the given boardManager.
     */
    public void setBoardManager(TicTacBoardManager boardManager) { this.boardManager = boardManager;}


    /**
     * Processes the tap movement for player 1 (always a human), and then
     * process the movement for player 2 afterwards if the game is not over.
     * Also displays the message to the screen in any situation, varying
     * depending on who won, ties, or the user ran out of time.
     * @param context the context of the activity.
     * @param position the tap on the screen to be processed.
     */
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