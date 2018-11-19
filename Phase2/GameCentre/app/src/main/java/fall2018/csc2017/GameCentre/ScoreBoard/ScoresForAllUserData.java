package fall2018.csc2017.GameCentre.ScoreBoard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Parent abstract class for the current user scores.
 */
public class ScoresForAllUserData {
    private ArrayList<String> allCurrentUserScores;

    /**
     * Initiates the class
     */
    ScoresForAllUserData() {
        allCurrentUserScores = new ArrayList<>();
    }

    /**
     * Gets every current users' scores
     * @return ArrayList of all current user scores
     */
    ArrayList<String> getAllCurrentUserScores() {
        return allCurrentUserScores;
    }

    /**
     * Gets the size of the user scores list
     * @return integer representing the size
     */
    public int getSize() {
        return allCurrentUserScores.size();
    }

    /**
     * Sorts the list according the the custom comparator (lowest score first)
     */
    public void sortTheList() {
        if (this.getSize() > 1) {
            allCurrentUserScores.sort(new CustomComparator());
        }
    }

    /**
     * Adds socre to current user scores
     * @param s score to add
     */
    public void add(String s) {
        if (this.getSize() < 5) {
            allCurrentUserScores.add(s);
        }else{
            allCurrentUserScores.remove(this.getSize() - 1);
            allCurrentUserScores.add(s);

        }
        sortTheList();
    }

    /**
     * Gets the item at index i
     * @param i index of the item
     * @return The item
     */
    public String getAtIndex(int i) {
        return allCurrentUserScores.get(i);
    }

    /**
     * Gets the lowest score (Best score)
     * @return the score
     */
    public String getLowest() {
        sortTheList();
        if (this.getSize() > 0) {
            return allCurrentUserScores.get(this.getSize() - 1);
        }else
            return "100000";
    }
    public void removeDuplicates(){

        Set<String> remover = new HashSet<>(allCurrentUserScores);
        allCurrentUserScores.clear();
        allCurrentUserScores.addAll(remover);
        sortTheList();
    }



    //adapted from https://stackoverflow.com/questions/15289863/java-sort-a-string-array-whose-strings-represent-int

    /**
     * Custom comparator to sort the score lists
     */
    public class CustomComparator implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            Integer int1;
            Integer int2;

            int1 = Integer.parseInt(s1);

            int2 = Integer.parseInt(s2);

            return int1.compareTo(int2);
        }

    }
}
