package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * A class to keep track on a single users individual score. Many of these can
 * be created per user.
 */
public class Scores implements Comparable<Scores> {

    /**
     * The instance variables for the Users name and score for the highscores
     * page. The users score is given as number of moves, NOT amount of time.
     */
    private String name, score;

    /**
     * Defaults the score to be 0 with no user; Just the default "highscore".
     */
    public Scores() {
        this.score = "0";
    }

    /**
     * The 'proper' Constructor for scores; Takes a users name and score.
     *
     * @param name  The users name.
     * @param score The users score.
     */
    public Scores(String name, String score) {

        this.name = name;
        this.score = score;

    }

    /**
     * Constructor used for converting hashmap from firebase to a Scores object.
     *
     * @param theScore the hashmap from firebase.
     */
    public Scores(Object theScore) {
        if (theScore instanceof Scores) {
            this.name = ((Scores) theScore).getName();
            this.score = ((Scores) theScore).getScore();
        } else if (theScore instanceof HashMap) {
            HashMap newHashScore = (HashMap) theScore;
            this.score = newHashScore.get("score").toString();
            this.name = newHashScore.get("name").toString();

        }

    }

    /**
     * A getter for the users score.
     *
     * @return The users score.
     */
    public String getScore() {
        return score;
    }

    /**
     * A getter for the users name.
     *
     * @return The users name.
     */
    public String getName() {
        return name;
    }

    /**
     * The integer value of the Scores object
     *
     * @return The integer value of the Scores object
     */
    public Integer getIntValue() {
        return Integer.parseInt(this.getScore());
    }

    /**
     * A setter for the users name.
     *
     * @param name The new users name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A setter for the uses score.
     *
     * @param newTime The new users score.
     */
    public void setScore(String newTime) {
        this.score = newTime;
    }


    @Override
    public int compareTo(@NonNull Scores scoreToCompare) {
        return (Integer.parseInt(this.score) - Integer.parseInt(scoreToCompare.score));
    }

    @Override
    public String toString() {
        return this.name + ", " + this.score;
    }
}
