package fall2018.csc2017.GameCentre.Sequencer;

import fall2018.csc2017.GameCentre.CustomAdapter;
import fall2018.csc2017.GameCentre.LeaderBoardFrontEnd;
import fall2018.csc2017.GameCentre.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import java.util.Observable;
import java.util.Observer;

/**
 * The game activity.
 */
public class SequencerGameActivity extends AppCompatActivity implements Observer {
    /**
     * The board manager.
     */
    private SequencerBoardManager boardManager;

    /**
     * The buttons to update.
     */
    private ArrayList<Button> tileButtons;

    /**
     * The GridView of the Game
     */
    private SequencerGestureDetectGridView gridView;

    /**
     * The calculated column width and Height Based on Device Size
     */
    private static int columnWidth, columnHeight;

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
    private LeaderBoardFrontEnd leaderBoardFrontEnd;
    /**
     * Firebase Database reference pointing to the current user
     */
    private DatabaseReference mUserDatabase;
    /**
     * The Score of the User's Current Game
     */
    private TextView theScore;

    /**
     * Updates the game by checking whether the game is over, whether the round is over,
     * and updating the theScore.
     */
    public void update() {
        // Updates the score counter
        final TextView score = findViewById(R.id.score);
        score.setText(String.valueOf(boardManager.getScore()));
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));

        // Saves to the file (Autosave)
        saveToFile(SequencerStartingActivity.SAVE_FILENAME);
        saveToFile(SequencerStartingActivity.TEMP_SAVE_FILENAME);
        score.setText(String.valueOf(boardManager.getScore()));

        // Checks whether tha game is over, if it is then it saves the theScore and terminates the game.
        if (boardManager.isOver()) {
            Intent intent = new Intent(SequencerGameActivity.this,SequencerStartingActivity.class);
            startActivity(intent);
        }
        // Checks if the round is over.
        // If it is, then it increases the round, and shows its pattern.
        if (boardManager.sequence.position == boardManager.sequence.round) {
            boardManager.sequence.round += 1;
            boardManager.sequence.reset();
            boardManager.talking = true;
            Speak();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFromFile(SequencerStartingActivity.TEMP_SAVE_FILENAME);
        createTileButtons(this);
        setContentView(R.layout.activity_sequencer_game);
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(SequencerBoardManager.NUM_COLS);
        gridView.setBoardManager(boardManager);
        boardManager.addObserver(this);
        // Observer sets up desired dimensions as well as calls our update function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / SequencerBoardManager.NUM_COLS;
                        columnHeight = displayHeight / SequencerBoardManager.NUM_ROWS;

                        update();
                    }
                });
        theScore = findViewById(R.id.score);
        theScore.setText(String.valueOf(boardManager.getScore()));
        Speak();
        saveUserInformationOnDatabase();
        saveScoreCountOnDataBase();
        databaseScoreSave();
    }

     /*
    Scoreboard code which reads scores as the game when the game ends
     */

    /**
     * Save the theScore to the leaderboard when the game finishes.
     */
    private void databaseScoreSave() {
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
    private void getUserDatabaseReference() {

        // Firebase User Authorisation
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child("sliding_tiles");
        mGamesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Games");

    }


    /**
     * Get Current User's Saved Information from the database to the application
     */
    private void updateLeaderBoard() {

        getUserDatabaseReference();
        mUserDatabase.getParent().addValueEventListener(new ValueEventListener() {
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
    private void saveUserInformationOnDatabase() {


        String lastSavedScore = theScore.getText().toString();
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("last_Saved_Score", lastSavedScore);

        mUserDatabase.updateChildren(userInfo);

    }

    /**
     * Saves a counter variable to the database that firebase listens for theScore changing.
     */
    private void saveScoreCountOnDataBase() {

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
    private void createTileButtons(Context context) {
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
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(SequencerStartingActivity.TEMP_SAVE_FILENAME);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        //String dbFileName= downloadUserBoard(fileName);

        try {
            InputStream inputStream = this.openFileInput(fileName);
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
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        update();
    }

    private void lightUp() {
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setRepeatCount(1);
        anim.setRepeatMode(Animation.REVERSE);
        Button b = tileButtons.get(boardManager.sequence.get());
        b.startAnimation(anim);

    }
    private void Speak() {
        int round = boardManager.sequence.round;
        Handler handler = new Handler();
        for (int i = 0; i < round; i++) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    lightUp();
                }
            }, 1000 * i);
        }
        handler.postDelayed(new Runnable() {
            public void run() {
                boardManager.sequence.reset();
                boardManager.talking = false;
            }
        }, 1000 * round - 1);
    }
}
