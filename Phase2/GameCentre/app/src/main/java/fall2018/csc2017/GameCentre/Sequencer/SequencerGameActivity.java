package fall2018.csc2017.GameCentre.Sequencer;

import fall2018.csc2017.GameCentre.CustomAdapter;
import fall2018.csc2017.GameCentre.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
     * Updates the game by checking whether the game is over, whether the round is over,
     * and updating the score.
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

        // Checks whether tha game is over, if it is then it saves the score and terminates the game.
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
        TextView score = findViewById(R.id.score);
        score.setText(String.valueOf(boardManager.getScore()));
        Speak();
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
