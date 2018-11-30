package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController;

import java.util.ArrayList;


/**
 * A LeaderBoardReaderClass
 */
public class LeaderBoardReader {

    /**
     * The contents of LeaderBoardReader.
     */
    private ArrayList<UserScores> contents;

    public LeaderBoardReader() {
        this.contents = new ArrayList<>();
    }

    /**
     * Adds a list of hashed highscores for all games from firebase to the contents of LeaderBoardReader.
     *
     * @param list the hashed lists of highscores (i.e A Hashed UserScores object )
     */
    public void addAllToContents(ArrayList list) {
        UserScores curList = new UserScores();
        for (int i = 0; i < list.size(); i++) {
            curList.add(list.get(i));
        }
        contents.add(curList);

    }

    /**
     * Return the list of all highscores in a non-hashed format.
     *
     * @return the contents of LeaderBoardReader
     */
    public ArrayList<UserScores> getContents() {
        return this.contents;
    }

    /**
     * Return the number of elements in contents.
     *
     * @return the size of the contents of LeaderBoardReader
     */
    public int getSize() {
        return contents.size();
    }

    /**
     * Adds a list of highscores to contents.
     *
     * @param newOne a list of User Scores to add.
     */
    public void add(UserScores newOne) {
        this.contents.add(newOne);
    }
}
