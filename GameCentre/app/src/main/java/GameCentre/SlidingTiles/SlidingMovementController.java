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
    private SlidingTilesBoardManager slidingTilesBoardManager = null;

    SlidingMovementController() {
    }

    /**
     * Sets a slidingTilesBoardManager
     *
     * @param slidingTilesBoardManager the boardManger to set to
     */
    void setSlidingTilesBoardManager(SlidingTilesBoardManager slidingTilesBoardManager) {
        this.slidingTilesBoardManager = slidingTilesBoardManager;
    }

    /**
     * Processes a tap movement
     *
     * @param context  the current context
     * @param position the position
     */
    void processTapMovement(Context context, int position) {
        if (slidingTilesBoardManager.isValidTap(position)) {
            slidingTilesBoardManager.touchMove(position);
            if (slidingTilesBoardManager.isOver()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
