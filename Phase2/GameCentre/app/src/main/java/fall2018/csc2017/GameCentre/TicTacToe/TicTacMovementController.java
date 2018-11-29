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
        if (!TicTacGameActivity.getmTimerRunning()) {
            boardManager.getBoard().setGameOver(true);
            Toast.makeText(context, "NO MORE TIME!", Toast.LENGTH_SHORT).show();
        } else
        if (boardManager.isValidTap(position)) {
            int current_player =  boardManager.touchMove(position);
            // check if game is solved
            // Toast.makeText(context, "Add code to check getWinner()", Toast.LENGTH_SHORT).show();
            if (boardManager.getWinner(position)) {
                boardManager.getBoard().setGameOver(true);
                boardManager.setUserWins(true);
                Toast.makeText(context, "P1 WIN!", Toast.LENGTH_SHORT).show();
                TicTacGameActivity.pauseTimer();

            }  else if (boardManager.getValidMoves().size() == 0) {
                boardManager.getBoard().setGameOver(true);
                Toast.makeText(context, "Tie!", Toast.LENGTH_SHORT).show();
                TicTacGameActivity.pauseTimer();
            } else {
                if (boardManager.getStrategy().isValid()) {
                    // strategy is valid so it is AI turn now
                    if (boardManager.getBoard().getCurrentPlayer() == boardManager.getBoard().getPlayer2()) {
                        position = boardManager.getStrategy().getNextMovement(boardManager, 0);
                        current_player = boardManager.touchMove(position);
                        if (boardManager.getWinner(position)) {
                            boardManager.getBoard().setGameOver(true);
                            Toast.makeText(context, "P2 WIN!", Toast.LENGTH_SHORT).show();
                            TicTacGameActivity.pauseTimer();
                        }
                    }
                }

            }
        }  else {
            Toast.makeText(context, "Invalid Tap: position:" + position , Toast.LENGTH_SHORT).show();
        }
    }

//    private void showDialog(Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage("Game Over~")
//                .setCancelable(false)
//                .setPositiveButton("Go to Tic Tac Toe Dashboard", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent intent = new Intent(TicTacMovementController.this, TicTacMainActivity.class );
//                    }
//                })
//                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }



}
