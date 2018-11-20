package fall2018.csc2017.GameCentre;

import android.content.Context;
import android.widget.Toast;


class SequencerMovementController {

    private SequencerBoardManager boardManager = null;

    SequencerMovementController() {
    }

    void setBoardManager(SequencerBoardManager boardManager) {
        this.boardManager = boardManager;
    }
    void processTapMovement(Context context, int position) {

        if (boardManager.isValidTap(position)) {
            Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show();
            boardManager.increaseScore();

        } else {
            Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}