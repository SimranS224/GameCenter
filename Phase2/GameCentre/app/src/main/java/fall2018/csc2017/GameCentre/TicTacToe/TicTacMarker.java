package fall2018.csc2017.GameCentre.TicTacToe;

import android.support.annotation.NonNull;

import java.io.Serializable;

import fall2018.csc2017.GameCentre.R;

/**
 * A Tile in a sliding tiles puzzle.
 */
public class TicTacMarker implements Comparable<TicTacMarker>, Serializable {

    /**
     * The background id to find the tile image.
     */
    private int background;
    private int row;
    private int column;
    /**
     * The unique id. 0 is blank, 1 is player 1, 2 is player 2
     */
    private int id;

    /**
     * Return the background id.
     *
     * @return the background id
     */
    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        //this.background = background;
        switch (background) {
            case 0:
                this.background = R.drawable.blank_marker;
                break;
            case 1:
                this.background = R.drawable.red_marker;
                break;
            case 2:
                this.background = R.drawable.blue_marker;
                break;
            default:
                this.background = R.drawable.blank_marker;
        }

    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    /**
     * A Tile with id and background. The background may not have a corresponding image.
     *
     * @param id         the id
     * @param background the background
     */
    TicTacMarker(int id, int background) {
        this.id = id;
        this.background = background;
    }

    /**
     * A tile with a background id; look up and set the id.
     *
     * @param backgroundId The background id the Actual Id - 1.
     */
    TicTacMarker(int row, int column, int backgroundId) {
        this.row = row;
        this.column = column;
        this.id = backgroundId;
        setBackground(backgroundId);
    }

    @Override
    public int compareTo(@NonNull TicTacMarker o) {
        return o.id - this.id;
    }
}
