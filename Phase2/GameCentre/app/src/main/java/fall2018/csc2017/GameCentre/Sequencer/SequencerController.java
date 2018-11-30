package fall2018.csc2017.GameCentre.Sequencer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView.LeaderBoardFrontEnd;
import fall2018.csc2017.GameCentre.R;

import static android.content.Context.MODE_PRIVATE;

public class SequencerController {

    /**
     * The board manager.
     */
    SequencerBoardManager boardManager;

    /**
     * The buttons to update.
     */
    ArrayList<Button> tileButtons;
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

    /**
     * Save the theScore to the leaderboard when the game finishes.
     */
    void databaseScoreSave() {
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
    void getUserDatabaseReference() {
        // Firebase User Authorisation
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        this.mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child("sequncer");
        this.mGamesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Games");
    }


    /**
     * Get Current User's Saved Information from the database to the application
     */
    public void updateLeaderBoard() {

        getUserDatabaseReference();
        this.mUserDatabase.getParent().addValueEventListener(new ValueEventListener() {
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
    void saveUserInformationOnDatabase(TextView theScore) {


        String lastSavedScore = theScore.getText().toString();
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("last_Saved_Score", lastSavedScore);

        this.mUserDatabase.updateChildren(userInfo);

    }

    /**
     * Saves a counter variable to the database that firebase listens for theScore changing.
     */
    void saveScoreCountOnDataBase(TextView theScore) {

        String lastSavedScore = theScore.getText().toString();
        Map<String, Object> newMap = new HashMap<>();
        newMap.put("last_Saved_Score", lastSavedScore);
        mGamesDatabase.updateChildren(newMap);
    }




    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    void createTileButtons(Context context) {
        tileButtons = new ArrayList<>();
        for (int row = 0; row != SequencerBoardManager.NUM_ROWS; row++) {
            for (int col = 0; col != SequencerBoardManager.NUM_COLS; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(R.drawable.green);
                this.tileButtons.add(tmp);
            }
        }
    }
    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    void loadFromFile(String fileName, Context context) {

        //String dbFileName= downloadUserBoard(fileName);

        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (SequencerBoardManager) input.readObject();
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
