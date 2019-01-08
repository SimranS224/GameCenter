package fall2018.csc2017.GameCentre.SlidingTiles;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Stack that contains the previous moves with the most recent moves on the top
 */
public class MoveStack implements Serializable {
    /**
     * Contents of the stack. Each Item should be a Integer[] in the form {row1, col1, row2, col2}
     */
    private ArrayList<Integer[]> contents;
    /**
     * Number of Undos allowed. If set to -1, then unlimited undos are allowed (bonus).
     */
    private static Integer NUM_UNDOS;

    /**
     * The constuctor; Defaults the number of undos to be 3, and the contents of the
     * stack to be an empty arraylist. (Since no moves have taken place yet.)
     */
    MoveStack() {
        NUM_UNDOS = 3;
        this.contents = new ArrayList<>();
    }

    /**
     * Add set of coordinates to the top of the stack.
     *
     * @param c set of coordinates for the two tiles that were switched.
     */
    public void add(Integer[] c) {
        contents.add(c);
    }

    /**
     * Removes the move from the move stack if undos are available.
     *
     * @return The stack of the moves without the previous move if an undo
     * is available. Returns null is no undos are available. (canUndo())
     */
    Integer[] remove() {
        if (canUndo()) {
            if (NUM_UNDOS != -1) {
                NUM_UNDOS -= 1;
            }
            return contents.remove(contents.size() - 1);
        }
        return null;
    }

    /**
     * Getter for the size of the move stack.
     *
     * @return The size of the move stack.
     */
    public int size() {
        return contents.size();
    }

    /**
     * Determines if an undo is possible.
     *
     * @return True if an undo is possible, false otherwise.
     */
    boolean canUndo() {
        return contents.size() > 0 && (NUM_UNDOS == -1 || NUM_UNDOS > 0);
    }

    /**
     * Returns a string representation of the number of undos remaining.
     *
     * @return The string representation.
     */
    String getUndos() {

        if (NUM_UNDOS == -1) {
            return "Unlimited";
        }
        return "Undo uses left: " + Integer.toString(NUM_UNDOS);
    }

    /**
     * A setter for the number of undos.
     *
     * @param undos The number of undos to be set.
     */
    static void setNumUndos(int undos) {
        NUM_UNDOS = undos;
    }

}
