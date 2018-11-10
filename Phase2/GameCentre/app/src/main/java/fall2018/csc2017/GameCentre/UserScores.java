package fall2018.csc2017.GameCentre;


import java.util.ArrayList;
import java.util.Collections;


/**
 * Class used for keeping track of users personal scores/highscores.
 */
public class UserScores {
    public ArrayList<Scores> array;

    /**
     * Initializes a new ArrayList.
     */
    UserScores() {
        this.array = new ArrayList<>();
    }

    /**
     * Sets the given array to array.
     *
     * @param array The new array.
     */
    public void setArray(ArrayList<Scores> array) {
        this.array = array;
    }

    /**
     * Getter for array;
     *
     * @return Return the stored array.
     */
    public ArrayList<Scores> getArray() {
        return array;
    }

    /**
     * Returns the size of the array
     *
     * @return the size of the array.
     */
    public int size() {
        return array.size();
    }

    /**
     * Returns the value in the array at the specified value.
     *
     * @param i The specified value position.
     * @return The specified value.
     */
    public Scores get(int i) {
        return this.array.get(i);
    }

    /**
     * Adds the specified Score to the array, and sorts the array.
     *
     * @param score The Score to add to the array.
     */
    public void add(Scores score) {
        array.add(score);
        sort();
    }

    /**
     * Returns whether or not the array is empty.
     *
     * @return True if the array is empty, False if the array is not empty.
     */
    boolean isEmpty() {
        return array.size() == 0;
    }

    /**
     * Sorts the array; Only called within the class.
     */
    private void sort() {
        if (!this.isEmpty()) {
            Collections.sort(this.array);
        }
    }
}