package fall2018.csc2017.GameCentre.Sequencer;

// Mode/View

import fall2018.csc2017.GameCentre.GameActivityModel;
import fall2018.csc2017.GameCentre.SlidingTiles.CustomAdapter;
import fall2018.csc2017.GameCentre.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * The game activity.
 */
public class SequencerGameActivity extends AppCompatActivity implements Observer {


    /**
     * The GridView of the Game
     */
    private SequencerGestureDetectGridView gridView;

    /**
     * The buttons to update.
     */
    ArrayList<Button> tileButtons;

    /**
     * The calculated column width and Height Based on Device Size
     */
    private static int columnWidth, columnHeight;

    /**
     * The controller of the game activity
     */
    private GameActivityModel controller;


    /**
     * Updates the game by checking whether the game is over, whether the round is over,
     * and updating the theScore.
     */
    public void update() {
        // Updates the score counter
        final TextView score = findViewById(R.id.score);
        score.setText(String.valueOf(controller.boardManager.getCurrGameScore()));
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));

        // Saves to the file (Autosave)
        controller.saveToFile(SequencerStartingActivity.SAVE_FILENAME, this);
        controller.saveToFile(SequencerStartingActivity.TEMP_SAVE_FILENAME, this);
        score.setText(String.valueOf(controller.boardManager.getCurrGameScore()));

        // Checks whether the game is over, if it is then it saves the theScore and terminates the game.
        if (controller.boardManager.isOver()) {
            Intent intent = new Intent(SequencerGameActivity.this,SequencerStartingActivity.class);
            startActivity(intent);
        }
        // Checks if the round is over.
        // If it is, then it increases the round, and shows its pattern.
        if (controller.getSequencerBoardManager().sequence.position == controller.getSequencerBoardManager().getRound()) {
            controller.getSequencerBoardManager().increaseRound();
            controller.getSequencerBoardManager().sequence.reset();
            controller.getSequencerBoardManager().talking = true;
            Speak();
        }
        controller.getUserDatabaseReference("sequncer");
        controller.updateLeaderBoard();
        controller.saveUserInformationOnDatabase(score);
        controller.saveScoreCountOnDataBase(score);
        controller.databaseScoreSave();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        controller = new GameActivityModel();

        controller.loadFromFile(SequencerStartingActivity.TEMP_SAVE_FILENAME, this);

        createTileButtons(this);
        setContentView(R.layout.activity_sequencer_game);
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(SequencerBoardManager.NUM_COLS);
        gridView.setBoardManager(controller.getSequencerBoardManager());
        controller.getSequencerBoardManager().addObserver(this);
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

        TextView theScore = findViewById(R.id.score);
        theScore.setText(String.valueOf(controller.boardManager.getCurrGameScore()));
        Speak();

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
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        controller.saveToFile(SequencerStartingActivity.TEMP_SAVE_FILENAME, this);
    }



    @Override
    public void update(Observable o, Object arg) {
        update();
    }

    /**
     * Lights up the correct tile depending on the current position in the sequence.
     */
    private void lightUp() {
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setRepeatCount(1);
        anim.setRepeatMode(Animation.REVERSE);
        Button b = tileButtons.get(controller.getSequencerBoardManager().sequence.get());
        b.startAnimation(anim);

    }

    /**
     * Lights up all the tiles in the sequence in this round with a delay (one after another)
     */
    private void Speak() {
        int round = controller.getSequencerBoardManager().getRound();
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
                controller.getSequencerBoardManager().sequence.reset();
                controller.getSequencerBoardManager().talking = false;
            }
        }, 1000 * round - 1);
    }
}
