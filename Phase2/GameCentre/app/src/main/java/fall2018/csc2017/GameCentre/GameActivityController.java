package fall2018.csc2017.GameCentre;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView.LeaderBoardFrontEnd;
import fall2018.csc2017.GameCentre.Sequencer.SequencerBoardManager;
import fall2018.csc2017.GameCentre.Sequencer.SequencerStartingActivity;
import fall2018.csc2017.GameCentre.SlidingTiles.BoardManager;

import static android.content.Context.MODE_PRIVATE;

public class GameActivityController {

    /**
     * The board manager
     */
    public Manager boardManager;

    /**
     * Refererence to the list of games database.
     */
    private DatabaseReference mGamesDatabase;

    /**
     * Checks if data has been changed
     */
    private boolean dataChange;

    /**
     * Name of the current user
     */
    private String currentUserName;
    /**
     * The LeaderBoard for this Game.
     */
    private LeaderBoardFrontEnd leaderBoardFrontEnd = new LeaderBoardFrontEnd();
    /**
     * Firebase Database reference pointing to the current user
     */
    private DatabaseReference mUserDatabase;
    public GameActivityController() {
    }

//    private void setBoardManagerType(Manager boardManager) {
//        if (boardManager instanceof SequencerBoardManager){
//            this.boardManager = sequencerBoardManager;
//        }
//        else if(boardManager instanceof BoardManager) {
//            this.boardManager = slidingtilesBoardManager;
//        }
//    }
    public SequencerBoardManager getSequencerBoardManager(){
            return (SequencerBoardManager) this.boardManager;

    }

    public BoardManager getSlidingtilesBoardManager() {
        return (BoardManager) this.boardManager;
    }


    /**
     * Save the theScore to the leaderboard when the game finishes.
     */
    public void databaseScoreSave() {
        mGamesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (boardManager.isOver() && !dataChange) {
                    leaderBoardFrontEnd.empty();
                    System.out.println(currentUserName);
                    leaderBoardFrontEnd.setmNameCurrentUser(currentUserName);
                    leaderBoardFrontEnd.saveScoreToLeaderBoard(dataSnapshot, boardManager); // move to leaderboard front endTODO

                    dataChange = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /**
     * Get database reference from the Firebase Database pointing to the current user
     */
    public void getUserDatabaseReference(String gameName) {

        // Firebase User Authorisation
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        this.mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child(gameName);
        this.mGamesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Games");

    }


    /**
     * Get Current User's Saved Information from the database to the application
     */
    public void updateLeaderBoard() {

        Objects.requireNonNull(this.mUserDatabase.getParent()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    assert map != null;
                    if (map.get("Name") != null) {
//                        String name = map.get("Name").toString();
                        currentUserName = map.get("Name").toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Saves the Current User's settings to the database
     */
    public void saveUserInformationOnDatabase(TextView theScore) {


        String lastSavedScore = theScore.getText().toString();
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("last_Saved_Score", lastSavedScore);

        this.mUserDatabase.updateChildren(userInfo);

    }

    /**
     * Saves a counter variable to the database that firebase listens for theScore changing.
     */
    public void saveScoreCountOnDataBase(TextView theScore) {

        String lastSavedScore = theScore.getText().toString();
        Map<String, Object> newMap = new HashMap<>();
        newMap.put("last_Saved_Score", lastSavedScore);
        mGamesDatabase.updateChildren(newMap);
    }





    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    public void loadFromFile(String fileName, Context context) {

        //String dbFileName= downloadUserBoard(fileName);

        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                if(fileName.equals(SequencerStartingActivity.SAVE_FILENAME) || fileName.equals(SequencerStartingActivity.TEMP_SAVE_FILENAME)) {
                    boardManager = (SequencerBoardManager) input.readObject();
                }

                else {
                    boardManager = (BoardManager) input.readObject();
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName, Context context) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
