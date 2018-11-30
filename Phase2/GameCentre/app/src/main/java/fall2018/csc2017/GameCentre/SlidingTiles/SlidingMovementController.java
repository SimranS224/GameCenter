package fall2018.csc2017.GameCentre.SlidingTiles;

import android.content.Context;
import android.widget.Toast;

/*
 * Model/View Code
 */

/**
 * A SlidingMovementController.
 */
class SlidingMovementController {

    /**
     * boardManger
     */
    private BoardManager boardManager = null;

    SlidingMovementController() {
    }

    /**
     * Sets a boardManager
     *
     * @param boardManager the boardManger to set to
     */
    void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    /**
     * Processes a tap movement
     *
     * @param context  the current context
     * @param position the position
     */
    void processTapMovement(Context context, int position) {
        if (boardManager.isValidTap(position)) {
            boardManager.touchMove(position);
            if (boardManager.isOver()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
