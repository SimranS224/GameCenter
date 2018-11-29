package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fall2018.csc2017.GameCentre.Manager;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.LeaderBoardController;


public class LeaderBoardFrontEnd {

    /**
     * The path to the Games branch of the firebase database.
     */
    private DatabaseReference mGamesDatabase;
    /**
     * the numCalls to read data.
     */
    private int numCalls;

    /**
     * the current leaderboard being added to.
     */
    private LeaderBoardController leaderBoard = new LeaderBoardController();

    /**
     * List for storing read values changes when scores/game changes
     */
    private ArrayList<Object> tempStorage;


    public LeaderBoardFrontEnd() {
        getUserDatabaseReference();
        tempStorage = new ArrayList<>();
    }

    /**
     * Sets the name of the current user
     *
     * @param mNameCurrentUser the current user's name
     */
    public void setmNameCurrentUser(String mNameCurrentUser) {
        leaderBoard.setPlayerName(mNameCurrentUser);
    }

    /**
     * Set the boardManger and writes newly changed list to the data to the database.
     *
     * @param boardManger the boardManger of the current game
     */
    private void saveBoard(Manager boardManger, ArrayList<Object> leaderboard) {
        if (!this.leaderBoard.getDataChange()) { // if nothing has been changed yet do this
            this.numCalls = 0;
            this.leaderBoard.setBoard(boardManger);
            this.leaderBoard.setFromFireBaseList(leaderboard);
            update(boardManger);
        }
        this.leaderBoard.setDataChange(true);
    }


    /**
     * Updates the boardManger and checks if any change i.e someone beating a highscore
     * happens and then writes the data to the database
     *
     * @param boardManager the boardManger of the current game
     */
    private void update(Manager boardManager) {
        if (this.numCalls < 1) {
            numCalls++;
        }

        if (numCalls == 1) {
            this.leaderBoard.updateScores(boardManager);
            writeDataToDataBase();
        }

    }


    /**
     * Gets the Users name and also gets the path in the database to add to.
     */
    public void getUserDatabaseReference() {
        mGamesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Games");
    }

    /**
     * Removes all elements from internal storage.
     */
    public void empty() {
        leaderBoard.clear();
    }

    /**
     * Saves data from the database
     *
     * @param dataSnapshot the datasnapshot of database data
     * @param boardManager the current boardManger
     */
    public void saveScoreToLeaderBoard(DataSnapshot dataSnapshot, Manager boardManager) {
        tempStorage.clear();
        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
            Map<String, ArrayList> map = (Map<String, ArrayList>) dataSnapshot.getValue();
            assert map != null;
            // Reads preexisting values from the database to be stored for
            // later use.
            if (map.get(boardManager.getSpecificName()) != null) {
                ArrayList theCurrentList = (map.get(boardManager.getSpecificName()));

                tempStorage.addAll(theCurrentList);
            }


        }
        ArrayList<Object> emptyOne = new ArrayList<>();
        if (tempStorage.size() == 0) {
            this.saveBoard(boardManager, emptyOne);
        } else {
            this.saveBoard(boardManager, tempStorage);
        }
    }


    /**
     * Takes in data when update is called and writes the lists corresponding to the type of game
     * to the database.
     */
    private void writeDataToDataBase() {
        ArrayList theContent = new ArrayList();
        if (!leaderBoard.isEmpty()) {
            theContent = leaderBoard.getWriteData();
        }
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(leaderBoard.getName(), theContent);

        mGamesDatabase.updateChildren(userInfo);
        this.leaderBoard.setDataChange(false);
        // after writing data set this to false.
    }


}
