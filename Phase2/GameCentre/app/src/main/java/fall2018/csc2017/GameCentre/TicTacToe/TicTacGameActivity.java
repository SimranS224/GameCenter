package fall2018.csc2017.GameCentre.TicTacToe;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView.LeaderBoardFrontEnd;
import fall2018.csc2017.GameCentre.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class TicTacGameActivity extends AppCompatActivity implements Observer {

    public Timer time = new Timer();
    /**
     * The board manager.
     */
    private TicTacBoardManager boardManager;

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
     * moveCounter
     */
    private Long moveCounter;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> markerButtons;

    /**
     * The calculated column width and Height Based on Device Size
     */
    private static int columnWidth, columnHeight;

    /**
     * The GridView of the Game
     */
    private TicTacGridView gridView;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new TicTacCustomAdapter(markerButtons, columnWidth, columnHeight));
    }

    //@Override
    public void update(Observable o, Object arg) {
        System.out.println("TicTacGameActivity.update()==========");
        display();
        moveCounter = boardManager.getMoveCounter();
        getUserDatabaseReference();
        saveUserInformationOnDatabase();
        saveScoreCountOnDataBase();
        updateLeaderBoard();
        databaseScoreSave();
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        TicTacBoard board = boardManager.getBoard();
        markerButtons = new ArrayList<>();
        for (int row = 0; row < TicTacBoard.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacBoard.NUM_COLS; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getMarker(row, col).getBackground());
                this.markerButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        TicTacBoard board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : markerButtons) {
            int row = nextPos / TicTacBoard.NUM_COLS;
            int col = nextPos % TicTacBoard.NUM_COLS;
            b.setBackgroundResource(board.getMarker(row, col).getBackground());
            nextPos++;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getUserDatabaseReference();
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();


        int gametype = -1; // or other values
        int depth = -1;
        if (b != null) {
            gametype = b.getInt("gametype");
            depth = b.getInt("depth");
        }
        if (gametype == -1) {
            //Toast.makeText(getApplicationContext(), "Invalid game type", Toast.LENGTH_SHORT).show();
            return;
        } else if (gametype == TicTacMainActivity.PLAYER_TO_PLAYER) {
            //PLAYER TO PLAYER CASE
            boardManager = new TicTacBoardManager(new TicTacEmptyStrategy(depth));

        } else if (gametype == TicTacMainActivity.PLAYER_TO_RANDOM) {
            //PLAYER TO RANDOM AI CASE
            boardManager = new TicTacBoardManager(new TicTacRandomStrategy(depth));
        } else if (gametype == TicTacMainActivity.PLAYER_TO_AI) {
            //PLAYER TO MINIMAX AI CASE
            boardManager = new TicTacBoardManager(new TicTacMinimaxStrategy(depth));
        }

        setContentView(R.layout.tictac_game_activity);

        createTileButtons(this);
        // set to tictac_game_activity
        setContentView(R.layout.tictac_game_activity);
        // Add View to activity
        time.mTextViewCountDown = findViewById(R.id.text_count_Down);
        gridView = findViewById(R.id.gridView);
        gridView.setBoardManager(boardManager);
        gridView.setNumColumns(TicTacBoard.NUM_COLS);
        boardManager.getBoard().addObserver(this);

        // Observer sets up desired dimensions as well as calls our update function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / TicTacBoard.NUM_COLS;
                        columnHeight = displayHeight / TicTacBoard.NUM_ROWS;

                        display();
                        time.resetTimer();
                        time.startTimer();
                    }
                });
        moveCounter = boardManager.getMoveCounter();
        leaderBoardFrontEnd = new LeaderBoardFrontEnd();
        this.dataChange = false;

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
                if (boardManager.isGetWinnerHasBeenCalled()) {
                    if (boardManager.isOver() && !dataChange) {
                        leaderBoardFrontEnd.empty();
                        System.out.println(currentUserName);
                        leaderBoardFrontEnd.setmNameCurrentUser(currentUserName);
                        leaderBoardFrontEnd.saveScoreToLeaderBoard(dataSnapshot, boardManager); // move to leaderboard front endTODO

                        dataChange = true;
                    }
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
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child("tic_tac_toe");
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


        String lastSavedScore = moveCounter.toString();
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("last_Saved_Score", lastSavedScore);

        mUserDatabase.updateChildren(userInfo);

    }

    /**
     * Saves a counter variable to the database that firebase listens for score changing.
     */
    private void saveScoreCountOnDataBase() {

        String lastSavedScore = moveCounter.toString();
        Map<String, Object> newMap = new HashMap<>();
        newMap.put("last_Saved_Score", lastSavedScore);
        mGamesDatabase.updateChildren(newMap);
    }

    /**
     * Adopted from https://stackoverflow.com/questions/4778754/how-do-i-kill-an-activity-when-the-back-button-is-pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        time.pauseTimer();
        this.finish();

    }


}