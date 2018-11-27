package fall2018.csc2017.GameCentre;


import java.util.ArrayList;

public class LeaderBoardController {


    /**
     * The board
     */
    private Manager boardManager;

    /**
     * List of User/scores
     */
    private UserScores listOfScores = new UserScores();


    /**
     * Boolean representing if any data changed
     */
    private boolean dataChange = false;

    /**
     * The current player
     */
    private String currPlayerName;


    LeaderBoardController() {
    }

    /**
     * Sets the name to be the name of the current player
     *
     * @param newPlayer the name of the current player
     */
    public void setPlayerName(String newPlayer) { // TODO can use this to set the name Abdullah
        this.currPlayerName = newPlayer;
    }

    /**
     * Returns the name of the current player.
     *
     * @return the name of the current player
     */
    public String getPLayerName() {
        return this.currPlayerName;
    }

    /**
     * Remove all the elemens from the listOfScores
     */
    public void clear() {
        listOfScores.empty();
    }

    /**
     * Returns true if data has changed in the database.
     *
     * @return true if data has chenged in the database.
     */
    public boolean getDataChange() {
        return this.dataChange;
    }

    /**
     * Set the value of dataChange to newValue.
     *
     * @param newVal new value of dataChange
     */
    public void setDataChange(boolean newVal) {
        this.dataChange = newVal;
    }

    /**
     * Adds a score to the list of scores
     *
     * @param score Score to add
     */
    public void add(Scores score) {
        listOfScores.add(score);
    }

    /**
     * Sets the boardManger to the current boardManger.
     *
     * @param boardManger the current board manger
     */
    public void setBoard(Manager boardManger) {
        this.boardManager = boardManger;
    }

    /**
     * Returns the name of the key to add to listOFScores to firebase with.
     *
     * @return the name of the key to add to listOFScores to firebase with
     */
    public String getName() {
        return boardManager.getSpecificName();
    }

    /**
     * Sets the listOfScores to a list containing data from firebase
     *
     * @param list
     */
    public void setFromFireBaseList(ArrayList<Object> list) {
        for (int i = 0; i < list.size(); i++) {
            listOfScores.add(list.get(i));

        }


    }

    /**
     * Return the data to write to the database.
     *
     * @return an arraylist corresonding to the data in listOfscores.
     */
    public ArrayList<Scores> getWriteData() {
        return listOfScores.getArray();
    }

    /**
     * Updates the listOfscores.
     *
     * @param boardManager of the current game
     */
    public void updateScores(Manager boardManager) {
        Long winningScore = boardManager.getScore();
        addWinningScore(winningScore);

    }


    /**
     * Adds the winning score to listOfscores
     *
     * @param score the score that the user finished with
     */
    private void addWinningScore(Long score) {
        String theScore = score.toString();
        String name = this.getPLayerName();
        Scores userScore = new Scores();
        userScore.setName(name);
        userScore.setScore(theScore);
        if (!listOfScores.contains(userScore)) {
            this.listOfScores.add(userScore);
        } else {
            this.listOfScores.addLowerScore(userScore);
        }
    }


    /**
     * Returns true if the size of list of scores is 0.
     *
     * @return true or false
     */
    public boolean isEmpty() {
        return this.listOfScores.size() == 0;
    }
}
