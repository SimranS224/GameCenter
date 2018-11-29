package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController;


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
    public UserScores() {
        this.array = new ArrayList<>();
    }

    /**
     * Sets the given array to array.
     *
     * @param array The new array.
     */
    public void setArray(ArrayList<Scores> array) {
        this.array.clear();
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

    public boolean contains(Object aScore) {
        boolean check = false;
        if (aScore instanceof Scores) {
            Scores theScore = (Scores) aScore;
            for (int i = 0; i < array.size(); i++) {
                if ((array.get(i).getName().equals(theScore.getName()))) {
                    check = true;
                }
            }
        }

        return check;
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
    public void add(Object score) {

        Scores newOne = new Scores(score);
        array.add(newOne);
        sort();
    }

    /**
     * Returns whether or not the array is empty.
     *
     * @return True if the array is empty, False if the array is not empty.
     */
    public boolean isEmpty() {
        return array.size() == 0;
    }

    /**
     * If a person has a score already in firebase this method
     * Adds the higher Score to the list, removes the lower one of that person.
     *
     * @param score a score
     */
    public void addLowerScore(Scores score) {
        boolean checkIfLower = false;
        Scores theLowerOne = null;
        for (Scores s : this.array) {
            if (s.getName().equals(score.getName())) {
                if (s.getIntValue() > score.getIntValue()) {
                    checkIfLower = true;
                    theLowerOne = s;
                }
            }
        }
        if (checkIfLower) {
            array.remove(theLowerOne);
            array.add(score);
        }
        sort();
    }

    /**
     * Sorts the array; Only called within the class.
     */
    private void sort() {
        if (!this.isEmpty()) {
            Collections.sort(this.array);
        }
    }

    /**
     * remove all the elements from array.
     */
    public void empty() {
        this.array.clear();
    }

    /**
     * Return user with name name.
     *
     * @param name the name of the user to search for
     * @return the user with this name if they exist
     */
    public Scores getUser(String name) {
        Scores emptyOne = new Scores();
        emptyOne.setName("");
        emptyOne.setScore("0");
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getName().equals(name)) {
                return array.get(i);
            }
        }
        return emptyOne;
    }
}