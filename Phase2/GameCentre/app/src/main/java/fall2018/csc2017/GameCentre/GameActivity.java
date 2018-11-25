package fall2018.csc2017.GameCentre;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
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
public class GameActivity extends AppCompatActivity implements Observer {

    /**
     * The Score of the User's Current Game
     */
    private TextView score;

    /**
     * The TextView representing number of Undos left in the current game
     */
    TextView textView;


    /**
     * The board manager.
     */
    private BoardManager boardManager;

    /**
     * The LeaderBoard for this Game.
     */
    private LeaderBoardFrontEnd leaderBoardFrontEnd;
    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * The GridView of the Game
     */
    private GestureDetectGridView gridView;

    /**
     * The calculated column width and Height Based on Device Size
     */
    private static int columnWidth, columnHeight;

    /**
     * Firebase Database reference pointing to the current user
     */
    private DatabaseReference mUserDatabase;


    /**
     * ArrayList where the parts of the image are going to be stored.
     */
    ArrayList<Bitmap> chunckedImages;

    //    public ArrayList<Object> leaderBoard;
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
     * Checks the type the Board class is set as.
     *
     * @return whether the Board type is set to Image Tile
     */
    private boolean isImage() {
        return Board.getType().equals("Image tiles");
    }

    /**
     * Gets the image depending on what is set up in Board.
     *
     * @return A bitmap of the image selected.
     */
    private Bitmap getImage() {
        if (Board.getIMAGE().equals("American Pie")) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.american_pie);
        } else if (Board.getIMAGE().equals("U of T")) {
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
        saveToFile(StartingActivity.SAVE_FILENAME);
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserDatabaseReference();



        loadFromFile(StartingActivity.TEMP_SAVE_FILENAME);
        int unn = MoveStack.NUM_UNDOS;
        System.out.println(unn);
        createTileButtons(this);
        setContentView(R.layout.activity_main);
        if (isImage()) {
            chunckedImages = splitImage(getImage());
        }
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(Board.NUM_COLS);
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

                        columnWidth = displayWidth / Board.NUM_COLS;
                        columnHeight = displayHeight / Board.NUM_ROWS;

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
        // saves score on database


            leaderBoardFrontEnd = new LeaderBoardFrontEnd();
            updateLeaderBoard();
            System.out.println(currentUserName);
            this.dataChange = false;



        saveUserInformationOnDatabase();
        saveScoreCountOnDataBase();
        mGamesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (boardManager.puzzleSolved()) {

                if (boardManager.puzzleSolved() && !dataChange) {
                    leaderBoardFrontEnd.empty();
                    System.out.println(currentUserName);
                    leaderBoardFrontEnd.setmNameCurrentUser(currentUserName);
                    leaderBoardFrontEnd.saveScoreToLeaderBoard(dataSnapshot, boardManager); // move to leaderboard front endTODO

                    dataChange = true;
//                    saveScoreCountOnDataBase(0);
                }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /*
    Scoreboard code which reads scores as the game when the game ends
     */


    /**
     * Get database reference from the Firebase Database pointing to the current user
     */
    private void getUserDatabaseReference() {

        // Firebase User Authorisation
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child("sliding_tiles");
        mGamesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Games");
//        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child("sliding_tiles");

    }


    /**
     * Get Current User's Saved Information from the database to the application
     */
    private void updateLeaderBoard() {

        getUserDatabaseReference();
        mUserDatabase.getParent().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0) {
                    Map<String, Object> map = (Map<String,Object>) dataSnapshot.getValue();
                    assert map != null;
                    if (map.get("Name")!=null) {
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


        String lastSavedScore = score.getText().toString();
        String lastSavedUndoCount = textView.getText().toString();
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("last_Saved_Score", lastSavedScore);
        userInfo.put("last_saved_undo_count", lastSavedUndoCount);

        mUserDatabase.updateChildren(userInfo);

    }

    private void saveScoreCountOnDataBase() {

        String lastSavedScore = score.getText().toString();
        // String lastSavedUndoCount = textView.getText().toString();
        Map<String, Object> newMap = new HashMap<>();
        newMap.put("last_Saved_Score", lastSavedScore);
        mGamesDatabase.updateChildren(newMap);
    }



    /*
    Saving functions
     */

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
//        saveScoreToLeaderBoard(boardManager);
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
                boardManager = (BoardManager) input.readObject();
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
//        saveScoreToLeaderBoard(boardManager);
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
//        saveScoreToLeaderBoard(boardManager);
        saveScoreCountOnDataBase();
        saveUserInformationOnDatabase();
    }

    /*
    Buttons
     */

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
                Board board = boardManager.getBoard();
                if (boardManager.stack.canUndo()) {
                    Integer[] lastMoves = boardManager.stack.remove();
                    board.swapTiles(lastMoves[0], lastMoves[1], lastMoves[2], lastMoves[3]);
                    TextView textView = findViewById(R.id.UndoCounter);

                    textView.setText(boardManager.stack.getUndos());
                    boardManager.setScore(boardManager.getScore() + 1);

                }
//                saveScoreToLeaderBoard(boardManager);


            }
        });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board board = boardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != Board.NUM_ROWS; row++) {
            for (int col = 0; col != Board.NUM_COLS; col++) {
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
        Board board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / Board.NUM_ROWS;
            int col = nextPos % Board.NUM_COLS;
            if (isImage()) {
                int i = board.getTile(row, col).getId() - 1;
                if (i == 24 || i >= chunckedImages.size()) {
                    b.setBackgroundResource(board.getTile(row, col).getBackground());
                } else {
                    Drawable d = new BitmapDrawable(getResources(), chunckedImages.get(i));
                    b.setBackground(d);
                }
            } else {
                b.setBackgroundResource(board.getTile(row, col).getBackground());
            }

            nextPos++;
        }
        final TextView score = findViewById(R.id.score);
        score.setText(String.valueOf(boardManager.getScore()));
//        saveScoreToLeaderBoard(boardManager);
    }

    /*
    Bitmap code for using Images
     */

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
        int chunkNumbers = Board.NUM_COLS * Board.NUM_ROWS;

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


}
