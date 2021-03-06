package fall2018.csc2017.GameCentre.SlidingTiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.GameCentre.GameActivityModel;
import fall2018.csc2017.GameCentre.R;
/*
 * Model/View Code since it creates the buttons and display for the game.
 */

/**
 * The game activity.
 */
public class SlidingTilesGameActivity extends AppCompatActivity implements Observer {

    /**
     * The Score of the User's Current Game
     */
    private TextView score;

    /**
     * The TextView representing number of Undos left in the current game
     */
    TextView textView;


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
     * ArrayList where the parts of the image are going to be stored.
     */
    ArrayList<Bitmap> chunckedImages;


    /**
     * The controller of the game activity
     */
    private GameActivityModel controller;


    /**
     * Checks the type the SlidingTilesBoard class is set as.
     *
     * @return whether the SlidingTilesBoard type is set to Image Tile
     */
    private boolean isImage() {
        return SlidingTilesBoard.getType().equals("Image tiles");
    }

    /**
     * Gets the image depending on what is set up in SlidingTilesBoard.
     *
     * @return A bitmap of the image selected.
     */
    private Bitmap getImage() {
        if (SlidingTilesBoard.getIMAGE().equals("American Pie")) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.american_pie);
        } else if (SlidingTilesBoard.getIMAGE().equals("U of T")) {
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
        controller.saveToFile(StartingActivity.SAVE_FILENAME, this);
        controller.saveToFile(StartingActivity.TEMP_SAVE_FILENAME, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new GameActivityModel();
        controller.getUserDatabaseReference("sliding_tiles");


        controller.loadFromFile(StartingActivity.TEMP_SAVE_FILENAME, this);
        createTileButtons(this);
        setContentView(R.layout.activity_main);
        if (isImage()) {
            chunckedImages = splitImage(getImage());
        }
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(SlidingTilesBoard.NUM_COLS);
        gridView.setSlidingTilesBoardManager(controller.getSlidingtilesBoardManager());
        controller.getSlidingtilesBoardManager().getSlidingTilesBoard().addObserver(this);
        controller.getSlidingtilesBoardManager().addObserver(this);

        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / SlidingTilesBoard.NUM_COLS;
                        columnHeight = displayHeight / SlidingTilesBoard.NUM_ROWS;

                        display();
                    }
                });
        textView = findViewById(R.id.UndoCounter);
        textView.setText(controller.getSlidingtilesBoardManager().stack.getUndos());
        // Reads the score by id
        score = findViewById(R.id.score);
        score.setText(String.valueOf(controller.getSlidingtilesBoardManager().getCurrGameScore()));
        LinearLayout rellayout = findViewById(R.id.main_activity);
        rellayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                score.setText(String.valueOf(controller.getSlidingtilesBoardManager().getCurrGameScore()));
            }
        });
        addUndoButtonListener();


        // saves score on database
        controller.updateLeaderBoard();
        controller.saveUserInformationOnDatabase(score);
        controller.saveScoreCountOnDataBase(score);
        controller.databaseScoreSave();

    }


    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        controller.saveToFile(StartingActivity.TEMP_SAVE_FILENAME, this);
    }


    @Override
    public void update(Observable o, Object arg) {
        display();
        controller.saveScoreCountOnDataBase(score);
        controller.saveUserInformationOnDatabase(score);
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
                if (controller.getSlidingtilesBoardManager().stack.canUndo()) {
                    Integer[] lastMoves = controller.getSlidingtilesBoardManager().stack.remove();
                    controller.getSlidingtilesBoardManager().swapTiles(lastMoves[0], lastMoves[1], lastMoves[2], lastMoves[3]);
                    TextView textView = findViewById(R.id.UndoCounter);

                    textView.setText(controller.getSlidingtilesBoardManager().stack.getUndos());
                    controller.getSlidingtilesBoardManager().setScore(controller.getSlidingtilesBoardManager().getCurrGameScore() + 1);

                }

            }
        });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        SlidingTilesBoard slidingTilesBoard = controller.getSlidingtilesBoardManager().getSlidingTilesBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != SlidingTilesBoard.NUM_ROWS; row++) {
            for (int col = 0; col != SlidingTilesBoard.NUM_COLS; col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(slidingTilesBoard.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        SlidingTilesBoard slidingTilesBoard = controller.getSlidingtilesBoardManager().getSlidingTilesBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / SlidingTilesBoard.NUM_ROWS;
            int col = nextPos % SlidingTilesBoard.NUM_COLS;
            if (isImage()) {
                int i = slidingTilesBoard.getTile(row, col).getId() - 1;
                if (i == 24 || i >= chunckedImages.size()) {
                    b.setBackgroundResource(slidingTilesBoard.getTile(row, col).getBackground());
                } else {
                    Drawable d = new BitmapDrawable(getResources(), chunckedImages.get(i));
                    b.setBackground(d);
                }
            } else {
                b.setBackgroundResource(slidingTilesBoard.getTile(row, col).getBackground());
            }

            nextPos++;
        }
        final TextView score = findViewById(R.id.score);
        score.setText(String.valueOf(controller.getSlidingtilesBoardManager().getCurrGameScore()));
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
        int chunkNumbers = SlidingTilesBoard.NUM_COLS * SlidingTilesBoard.NUM_ROWS;

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
