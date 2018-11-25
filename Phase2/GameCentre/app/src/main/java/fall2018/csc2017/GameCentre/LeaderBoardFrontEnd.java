package fall2018.csc2017.GameCentre;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;



public class LeaderBoardFrontEnd {

//    /**
//     * Reference to the name of the current user
//     */
//    private DatabaseReference mCurrentUserNameReference;




    /**
     * The Name of the Current user
     */
    private String mNameCurrentUser="";


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
    /**
     * A hash for wrinting data to firebase
     */
    private Map<String, Object> mapForWritingData = new HashMap<>();
    private DatabaseReference mUserDatabase;

//    /**
//     * name of key in the firebase database to add
//     */
//    private String nameToBeWritten = null;
//    /**
//     * data to be written to that key
//     */s
//    private ArrayList<Scores> dataToBeWritten = null;



    LeaderBoardFrontEnd(){
        // the factory chooses which leaderboard controller to return
        //LeaderBoardFactory leaderBoardFactory = new LeaderBoardFactory();
        //leaderBoard = leaderBoardFactory.getType(leaderBoardType);
        //leaderBoard = new SlidingTilesLeaderBoard(); // temp for the controller.
        getUserDatabaseReference();
//        leaderBoard.setPlayerName(mNameCurrentUser);
        tempStorage = new ArrayList<>();
    }

    public void setmNameCurrentUser(String mNameCurrentUser) {
        this.mNameCurrentUser = mNameCurrentUser;
        leaderBoard.setPlayerName(mNameCurrentUser);
    }

    /**
     * Set the boardManger and writes newly changed list to the data to the database.
     * @param boardManger the boardManger of the current game
     */
    public void saveBoard(BoardManager boardManger, ArrayList leaderboard) {
//        Log.d("in saveboard ", "listofarrays=============== " + leaderBoard.getWriteData());
        if (!this.leaderBoard.getDataChange()){ // if nothing has been changed yet do this
            this.numCalls = 0;
            this.leaderBoard.setBoard(boardManger);
            this.leaderBoard.setFromFireBaseList(leaderboard);
            update(boardManger);
        }
        this.leaderBoard.setDataChange(true);

        //writeDataToDataBase();
    }

//    public static void setmNameCurrentUser(String mNameCurrentUser) {
//        LeaderBoardFrontEnd.mNameCurrentUser = mNameCurrentUser;
//    }

    /**
     * Updates the boardManger and checks if any change i.e someone beating a highscore
     * happens and then writes the data to the database
     * @param boardManager the boardManger of the current game
     */
    public void update(BoardManager boardManager) {
//        Log.d("in update ", "listofarrays=============== " + leaderBoard.getWriteData());
        if (this.numCalls < 1){
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
//        mCurrentUserNameReference = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child("Name");
//        mNameCurrentUser = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child("Name").toString();
        mGamesDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Games");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("userId");
    }

    public void empty() {
        leaderBoard.clear();
    }


    public void saveScoreToLeaderBoard(DataSnapshot dataSnapshot, BoardManager boardManager) {
        tempStorage.clear();
        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
            Map<String, ArrayList> map = (Map<String, ArrayList>) dataSnapshot.getValue();
            assert map != null;
            // Reads preexisting values from the database to be stored for
            // later use.
            if (map.get(boardManager.getSpecificName()) != null) {
                Log.d("List from firebase", "listofuserscores=============== " + map.get(boardManager.getSpecificName()));
                ArrayList theCurrentList = (map.get(boardManager.getSpecificName()));
                //Object  theCurrentList = (ArrayList<UserScores>) (map.get(leaderBoard.getName()));
                Log.d("in readdata ", "thefirebaselist=============== " + theCurrentList);

                tempStorage.addAll(theCurrentList);
            }


        }
        ArrayList emptyOne = new ArrayList();
        if (tempStorage.size() == 0) {
            this.saveBoard(boardManager, emptyOne);
        } else {
            this.saveBoard(boardManager, tempStorage);
        }
    }

//    public void saveCurrentUserName() {
//        mCurrentUserNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                    assert map != null;
//                    if (map.get("Name") != null) {
//                        String name = map.get("Name").toString();
//                        mNameCurrentUser = name;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


    /**
     * Takes in data when update is called and writes the lists corresponding to the type of game
     * to the database.
     */
    public void writeDataToDataBase() {
//        Log.d("in wrtiedatatodatabase ", "listofarrays=============== " + leaderBoard.getWriteData());

        ArrayList theContent = leaderBoard.getWriteData();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(leaderBoard.getName(), theContent);

        mGamesDatabase.updateChildren(userInfo);
        this.leaderBoard.setDataChange(false);
        // after writing data set this to false.
//        leaderBoard.setDataChange(false);
    }



//    public void saveScore(int score) {
//        leaderBoard.addScore(score);
//    }


//    public String getNameToWrite(){
//        nameToBeWritten = leaderBoard.getWriteName();
//        return nameToBeWritten;
//    }

}
