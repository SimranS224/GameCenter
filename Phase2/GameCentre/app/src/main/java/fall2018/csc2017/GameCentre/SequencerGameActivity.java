package fall2018.csc2017.GameCentre;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
     * The buttons to display.
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
     * Firebase Database reference pointing to the current user
     */
    private DatabaseReference mUserDatabase;


    /**
     * The Score of the User's Current Game
     */
    private TextView score;

    /**
     * The TextView representing number of Undos left in the current game
     */
    TextView textView;
    /**
     * The current users top 3 scores for the 4x4 board size.
     */
    private ScoresFourByFour allScoresFourByFour = new ScoresFourByFour();
    /**
     * The current users top 3 scores for the 3x3 board size.
     */
    private ScoresThreeByThree allScoresThreeByThree = new ScoresThreeByThree();
    /**
     * The current users top 3 scores for the 5x5 board size.
     */
    private ScoresFiveByFive allScoresFiveByFive = new ScoresFiveByFive();

    /**
     * ArrayList where the parts of the image are going to be stored.
     */
    ArrayList<Bitmap> chunckedImages;

    /**
     * Checks the type the SequencerBoard class is set as.
     *
     * @return whether the SequencerBoard type is set to Image Tile
     */
    private boolean isImage() {
        return SequencerBoard.getType().equals("Image tiles");
    }

    /**
     * Gets the image depending on what is set up in SequencerBoard.
     *
     * @return A bitmap of the image selected.
     */
    private Bitmap getImage() {
        if (SequencerBoard.getIMAGE().equals("American Pie")) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.american_pie);
        } else if (SequencerBoard.getIMAGE().equals("U of T")) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.uoft);
        }
        return BitmapFactory.decodeResource(getResources(), R.drawable.flower);

    }


    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));

        //autosave
        saveToFile(SequencerStartingActivity.SAVE_FILENAME);
        saveToFile(SequencerStartingActivity.TEMP_SAVE_FILENAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserDatabaseReference();

        loadFromFile(SequencerStartingActivity.TEMP_SAVE_FILENAME);
        createTileButtons(this);
        setContentView(R.layout.activity_sequencer_game);
        if (isImage()) {
            chunckedImages = splitImage(getImage());
        }
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(SequencerBoard.NUM_COLS);
        gridView.setBoardManager(boardManager);
        boardManager.getBoard().addObserver(this);

        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / SequencerBoard.NUM_COLS;
                        columnHeight = displayHeight / SequencerBoard.NUM_ROWS;

                        display();
                    }
                });
        textView = findViewById(R.id.UndoCounter);
        textView.setText(boardManager.stack.getUndos());
        // Reads the score by id
        score = findViewById(R.id.score);
        score.setText(String.valueOf(boardManager.getScore()));
        LinearLayout rellayout = findViewById(R.id.main_activity);
        rellayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                score.setText(String.valueOf(boardManager.getScore()));
            }
        });
        addUndoButtonListener();
        saveUserInformationOnDatabase();
        Speak(5);
        boardManager.sequence.resetPos();
        System.out.println(boardManager.sequence.position);
    }

    /**
     * Provides the functionality for the undo button.
     * If an undo can be made (At least one undo is left or the board is not in the beginning configuration)
     * then the previous move is removed from the move stack and then played.
     */
    private void addUndoButtonListener() {
        Button startButton = findViewById(R.id.UndoButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "UNDO BUTTON PRESSED", Toast.LENGTH_SHORT).show();
                SequencerBoard board = boardManager.getBoard();
                if (boardManager.stack.canUndo()) {
                    Integer[] lastMoves = boardManager.stack.remove();
                    board.swapTiles(lastMoves[0], lastMoves[1], lastMoves[2], lastMoves[3]);
                    TextView textView = findViewById(R.id.UndoCounter);

                    textView.setText(boardManager.stack.getUndos());
                    boardManager.setScore(boardManager.getScore() + 1);

                }
                saveUserInformationOnDatabase();


            }
        });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        SequencerBoard board = boardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != SequencerBoard.NUM_ROWS; row++) {
            for (int col = 0; col != SequencerBoard.NUM_COLS; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        SequencerBoard board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / Board.NUM_ROWS;
            int col = nextPos % Board.NUM_COLS;
            b.setBackgroundResource(R.drawable.green);
            nextPos++;
        }
        final TextView score = findViewById(R.id.score);
        score.setText(String.valueOf(boardManager.getScore()));
        saveUserInformationOnDatabase();
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
        //uploadUserBoard(fileName);
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
    }


    /**
     * Get database reference from the Firebase Database pointing to the current user
     */
    private void getUserDatabaseReference() {

        // Firebase User Authorisation
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID);
    }

    /**
     * Saves the Current User's settings to the database
     */
    private void saveUserInformationOnDatabase() {
        String winningScore = null;

        Map<String, Object> userInfo = new HashMap<>();

        String boardSize = SequencerBoard.NUM_ROWS + "x" + SequencerBoard.NUM_ROWS;

        //read the current users best scores into a list for sorting and displaying
        readUserScores(boardSize);


        if (boardManager.puzzleSolved()) {
            winningScore = score.getText().toString();
        }
        // Depending on the board size add the winning score to the correct class.
        switch (boardSize) {
            case "3x3":
                addWinningScore(winningScore, allScoresThreeByThree, userInfo, boardSize);
                break;
            case "4x4":
                addWinningScore(winningScore, allScoresFourByFour, userInfo, boardSize);
                break;
            case "5x5":
                addWinningScore(winningScore, allScoresFiveByFive, userInfo, boardSize);
                break;
        }

        String lastSavedScore = score.getText().toString();
        String lastSavedUndoCount = textView.getText().toString();

        userInfo.put("last_Saved_Score", lastSavedScore);
        userInfo.put("last_saved_undo_count", lastSavedUndoCount);

        mUserDatabase.updateChildren(userInfo);
    }

    /**
     * Adds winning score to the current list
     *
     * @param winningScore Winning score
     * @param lst          The current user score list
     * @param userInfo     information about the user (map) from Firebase
     * @param boardSize    Size of the board.
     */
    private void addWinningScore(String winningScore, ScoresForAllUserData lst, Map<String, Object> userInfo, String boardSize) {
        lst.removeDuplicates();

        if (winningScore != null) {
            Integer curGameWinScore = Integer.parseInt(winningScore);
            Integer worstCur = Integer.parseInt(lst.getLowest());
            if (lst.getSize() < 5 || worstCur > curGameWinScore) {
                lst.add(winningScore);
                putScoresIntoDatabase(userInfo, lst, boardSize);
            }
        }
    }

    /**
     * Puts the newly updated scores into the database.
     *
     * @param uinfo     the hashmap used to access the database
     * @param uScores   the class which contains the user scores for a particular board size.
     * @param boardSize the size of the board currently.
     */
    public void putScoresIntoDatabase(Map<String, Object> uinfo, ScoresForAllUserData uScores, String boardSize) {
        String theBoard = boardSize + "";
        uScores.sortTheList();
        // depending on the board size adds a key to the database which is mapped to a
        // a vlaue in uScores.
        if (uScores.getSize() == 1) {
            uinfo.put("First_Best_Time" + theBoard, uScores.getAtIndex(0));
        } else if (uScores.getSize() == 2) {
            uinfo.put("First_Best_Time" + theBoard, uScores.getAtIndex(0));
            uinfo.put("Second_Best_Time" + theBoard, uScores.getAtIndex(1));
        } else if (uScores.getSize() == 3) {
            uinfo.put("First_Best_Time" + theBoard, uScores.getAtIndex(0));
            uinfo.put("Second_Best_Time" + theBoard, uScores.getAtIndex(1));
            uinfo.put("Third_Best_Time" + theBoard, uScores.getAtIndex(2));
        }
    }

    /**
     * Reads the user scores from Firebase databse and updates allCurrentScores accordingly.
     *
     * @param x Size of the board
     */
    public void readUserScores(String x) {
        final String theBoard = x + "";
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    assert map != null;
                    // Reads preexisting values from the database to be stored for
                    // later use.
                    ScoresForAllUserData toAddTo = getStorage();
                    if (map.get("First_Best_Time" + theBoard) != null) {
                        String score = map.get("First_Best_Time" + theBoard).toString();
                        Log.d("adding more", "firstbesttimes " + score);

                        toAddTo.add(score);
                    }
                    if (map.get("Second_Best_Time" + theBoard) != null) {
                        String score = map.get("Second_Best_Time" + theBoard).toString();
                        toAddTo.add(score);
                    }
                    if (map.get("Third_Best_Time" + theBoard) != null) {
                        String score = map.get("Third_Best_Time" + theBoard).toString();
                        toAddTo.add(score);
                    }
                }
            }

            private ScoresForAllUserData getStorage() {
                switch (theBoard) {
                    case "3":
                        return allScoresThreeByThree;
                    case "5":
                        return allScoresFiveByFive;
                    default:
                        return allScoresFourByFour;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Returns the image split into smaller sub images.
     *
     * @param bitmap used to store the image
     * @return an arraylist containing the image spit into its corresponding sub images
     */
    private ArrayList<Bitmap> splitImage(Bitmap bitmap) {

        // Modified from: http://www.chansek.com/splittingdividing-image-into-smaller/

        //For the number of rows and columns of the grid to be displayed
        int rows, cols;

        //For height and width of the small image chunks
        int chunkHeight, chunkWidth;
        int chunkNumbers = SequencerBoard.NUM_COLS * SequencerBoard.NUM_ROWS;

        //To store all the small image chunks in bitmap format in this list
        ArrayList<Bitmap> chunkedImages = new ArrayList<>(chunkNumbers);

        //Getting the scaled bitmap of the source image
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        rows = cols = (int) Math.sqrt(chunkNumbers);
        chunkHeight = bitmap.getHeight() / rows;
        chunkWidth = bitmap.getWidth() / cols;

        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        for (int x = 0; x < rows; x++) {
            int xCoord = 0;
            for (int y = 0; y < cols; y++) {
                chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }

        /* Now the chunkedImages has all the small image chunks in the form of Bitmap class.
         * You can do what ever you want with this chunkedImages as per your requirement.
         * I pass it to a new Activity to show all small chunks in a grid for demo.
         * You can get the source code of this activity from my Google Drive Account.
         */
        //Start a new activity to show these chunks into a grid
        return chunkedImages;
    }
    private void lightUp() {
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setRepeatCount(1);
        anim.setRepeatMode(Animation.REVERSE);
        Button b = tileButtons.get(boardManager.sequence.get());
        b.startAnimation(anim);

    }
    private void Speak(int round) {
        Handler handler = new Handler();
        for (int i = 0; i < round; i++) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    lightUp();
                }
            }, 2000 * i);   //5 seconds
        }
        boardManager.sequence.resetPos();
    }
}
