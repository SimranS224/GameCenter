package fall2018.csc2017.GameCentre.TicTacToe;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fall2018.csc2017.GameCentre.R;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class TicTacGameActivity extends AppCompatActivity implements Observer {


    /**
     * The board manager.
     */
    private TicTacBoardManager boardManager;

    /**
     * Firebase Database reference pointing to the current user
     */
    private DatabaseReference mUserDatabase;

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
     * ArrayList where the parts of the image are going to be stored.
     */
    ArrayList<Bitmap> chunckedImages;

    /**
     * Checks the type the Board class is set as.
     *
     * @return whether the Board type is set to Image Tile
     */
    private boolean isImage() {
        return TicTacBoard.getType().equals("Image tiles");
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new TicTacCustomAdapter(markerButtons, columnWidth, columnHeight));

        /*//autosave
        saveToFile(StartingActivity.SAVE_FILENAME);
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);*/
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
        //uploadUserBoard(fileName);
    }

    //@Override
    public void update(Observable o, Object arg) {
        System.out.println("TicTacGameActivity.update()==========");
        display();
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
                System.out.println("Add ROW: " + row + "  COL: " + col);
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
            System.out.println("ROW: " + row + "  COL: " + col);
            b.setBackgroundResource(board.getMarker(row, col).getBackground());
            nextPos++;
        }
        //final TextView score = findViewById(R.id.score);
        //score.setText(String.valueOf(boardManager.getScore()));
        //saveUserInformationOnDatabase();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getUserDatabaseReference();
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        int gametype = -1; // or other values
        int depth = -1;
        if(b != null) {
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
        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Con4GameFragment.newInstance())
                    .commitNow();
        }*/

        createTileButtons(this);
        // set to tictac_game_activity
        setContentView(R.layout.tictac_game_activity);
        // Add View to activity
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
    }

}


