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

import fall2018.csc2017.GameCentre.GameActivityController;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView.LeaderBoardFrontEnd;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.Sequencer.SequencerStartingActivity;
import fall2018.csc2017.GameCentre.SlidingTiles.StartingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class TicTacGameActivity extends AppCompatActivity implements Observer {

    /**
     * the start time in millis
     */
    private static final long START_TIME_IN_MILLIS = 100000;

    /**
     * Textview for the countdown timer
     */
    private static TextView mTextViewCountDown;

    /**
     * The countdown timer
     */
    private static CountDownTimer mCountDownTimer;

    /**
     * Tells whether timer is running or not.
     */
    private static boolean mTimerRunning;

    /**
     * The starting time.
     */
    public static long mTimeLeftInMillis = START_TIME_IN_MILLIS;



    /**
     * The controller of the game activity
     */
    private GameActivityController controller;


    /**
     * The value of the Counter
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

    @Override
    public void update(Observable o, Object arg) {
        display();
        moveCounter = controller.getTicTacBoardManager().getMoveCounter();
        controller.getUserDatabaseReference("tic_tac_toe");
        controller.saveUserInformationOnDatabase(moveCounter);
        controller.saveScoreCountOnDataBase(moveCounter);
        controller.updateLeaderBoard();
        controller.databaseScoreSave();
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        TicTacBoard board = controller.getTicTacBoardManager().getBoard();
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
        TicTacBoard board = controller.getTicTacBoardManager().getBoard();
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


        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();

        controller = new GameActivityController();
        controller.getUserDatabaseReference("tic_tac_toe");


        if (createNewGame(b)) return;


        setContentView(R.layout.tictac_game_activity);


        createTileButtons(this);

        // set to tictac_game_activity
        setContentView(R.layout.tictac_game_activity);


        // Add View to activity
        mTextViewCountDown = findViewById(R.id.text_count_Down);
        gridView = findViewById(R.id.gridView);
        gridView.setBoardManager(controller.getTicTacBoardManager());
        gridView.setNumColumns(TicTacBoard.NUM_COLS);
        controller.getTicTacBoardManager().getBoard().addObserver(this);

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
                        resetTimer();
                        startTimer();
                    }
                });
        moveCounter = controller.getTicTacBoardManager().getMoveCounter();

    }

    /**
     * Create a new game depending on the user choice. Game could be against easy AI, hard AI or two player Game.
     * @param b The game type
     * @return returns false if the game type was chosen.
     */
    private boolean createNewGame(Bundle b) {
        int gameType = -1; // or other values
        int depth = -1;
        if (b != null) {
            gameType = b.getInt("gametype");
            depth = b.getInt("depth");
        }
        if (gameType == -1) {
            return true;

        }
        //PLAYER TO PLAYER CASE
        else if (gameType == TicTacMainActivity.PLAYER_TO_PLAYER) {

            controller.createTicTacBoardManager("PlayerToPlayer");
        }
        //PLAYER TO RANDOM AI CASE
        else if (gameType == TicTacMainActivity.PLAYER_TO_RANDOM) {
            controller.createTicTacBoardManager("PlayerToRandom");

        }

        //PLAYER TO MINIMAX AI CASE
        else if (gameType == TicTacMainActivity.PLAYER_TO_AI) {
            controller.createTicTacBoardManager("PlayerToAI");

        }
        return false;
    }

    /**
     * Starts the count down timer
     */
    public static void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();
        mTimerRunning = true;
    }

    /**
     * Pause the count down timer
     */
    public static void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    /**
     * Reset the countdown timer
     */
    public static void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }

    /**
     * Updates the countdown timer text
     */
    public static void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    /**
     * Gets the time left
     * @return The time left
     */
    public static long getmTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    /**
     * Check whether there is time remaining
     * @return True if time is remaining else false
     */
    public static boolean getMTimerRunning() {
        return mTimerRunning;
    }


    /**
     * Adopted from https://stackoverflow.com/questions/4778754/how-do-i-kill-an-activity-when-the-back-button-is-pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pauseTimer();
        this.finish();

    }


}


