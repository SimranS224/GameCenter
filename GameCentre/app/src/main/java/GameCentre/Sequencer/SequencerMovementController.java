package fall2018.csc2017.GameCentre.Sequencer;

// Mode/View


import android.content.Context;
import android.widget.Toast;


class SequencerMovementController {

    private SequencerBoardManager boardManager;

    SequencerMovementController() {
    }

    void setBoardManager(SequencerBoardManager boardManager) {
        this.boardManager = boardManager;
    }
    void processTapMovement(Context context, int position) {
        /*
          If the tap is correct, increase the score. Notice that the isValidTap method checks
          whether it is a correct by calling sequence.get(), which automatically gets ready for the
          next call by increasing the position within the sequence.
         */
        if (boardManager.talking) {
            Toast.makeText(context, "Wait until the sequence finishes showing", Toast.LENGTH_SHORT).show();
        }
        else if (boardManager.isValidTap(position)) {
            Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show();
            boardManager.update();

        } else {
            Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT).show();
            boardManager.setGameOver();
        }
    }
}