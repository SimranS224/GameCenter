package fall2018.csc2017.GameCentre.SlidingTiles;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import fall2018.csc2017.GameCentre.FirstActivity;
import fall2018.csc2017.GameCentre.GameChoiceActivity;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView.swipeTest;
/*
Model/View Code
 */
/**
 * The initial activity for the sliding puzzle tile game.
 */
public class StartingActivity extends AppCompatActivity {
    /**
     * The RelativeLayout.
     */
    private RelativeLayout rellayout;
    /**
     * The main save file.
     */
    public static String SAVE_FILENAME = "save_file.ser";
    /**
     * A temporary save file.
     */
    public static String TEMP_SAVE_FILENAME = "save_file_tmp.ser";

    /**
     * The board manager.
     */
    private SlidingTilesBoardManager slidingTilesBoardManager;

    /**
     * The Welcome Text
     */
    private TextView mWelcomeText;

    /**
     * Firebase Database reference pointing to the current user
     */
    private DatabaseReference mUserDatabase;

    /**
     * String representing the id of the current user in the firebase database
     */
    private String userID;

    /**
     * The size of the previously saved board
     */
    private int oldBoardSize;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserDatabaseReference();
        getUserNameFromDatabase();

        //every user has a different save file
        SAVE_FILENAME = userID + "save_file.ser";
        TEMP_SAVE_FILENAME= userID + "save_file_tmp.ser";

        getUserInfoFromDatabase();
        slidingTilesBoardManager = new SlidingTilesBoardManager();
        getUserInfoFromDatabase();
        String type1 = SlidingTilesBoard.getType();
        System.out.println(type1);


        saveToFile(TEMP_SAVE_FILENAME);

        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
        addMainMenuButtonListener();
        switchToLeaderBoard();



        //getUserDatabaseReference();

        addLogoutButtonListener();


        //update welcome text with currentUserName
        mWelcomeText = findViewById(R.id.welcomeText);
        getUserInfoFromDatabase();

       addBackgroundTapListener();
    }

    /**
     * Switch to the leaderboard view LeaderBoard
     */
    private void switchToLeaderBoard(){

        Button mLeaderBoard = findViewById(R.id.LeaderBoardButton);
        mLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingActivity.this,swipeTest.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Activate Main Menu Button
     */
    private void addMainMenuButtonListener() {

        Button mMainMenuButton = findViewById(R.id.MainMenu);
        mMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartingActivity.this,GameChoiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingActivity.this,SettingsActivity.class);
                startActivity(intent);

            }
        });
    }

    /**
     * Activate Background Tap Color Change
     */
    private void addBackgroundTapListener() {
        // background changing code on tap
        final Random randNum = new Random();
        rellayout = findViewById(R.id.gradient_backg);
        rellayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String[] backgroundOne = {"#e1eec3", "#f05053", "#6190e8"};
                String[] backgroundTwo = {"#ffffff", "#1d2671", "#6f0000"};

                GradientDrawable gradDraw = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{Color.parseColor(backgroundOne[randNum.nextInt(2)]),
                                Color.parseColor(backgroundTwo[randNum.nextInt(2)])});

                rellayout.setBackground(gradDraw);
            }
        });
    }

    /**
     * Activate Logout Button
     */
    private void addLogoutButtonListener() {

        Button mLogoutButton = findViewById(R.id.LogoutButton);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(StartingActivity.this,FirstActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFromFile(SAVE_FILENAME);
                getUserInfoFromDatabase();
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastLoadedText();
                switchToGame();
            }
        });
    }

    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveToFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastSavedText();
            }
        });
    }

    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(TEMP_SAVE_FILENAME);
    }

    /**
     * Switch to the SlidingTilesGameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, SlidingTilesGameActivity.class);
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
        startActivity(tmp);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {


           //String dbFileName =downloadUserBoard(fileName);


        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                slidingTilesBoardManager = (SlidingTilesBoardManager) input.readObject();

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
            outputStream.writeObject(slidingTilesBoardManager);
            outputStream.close();

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
      //  uploadUserBoard(fileName);



    }

    /**
     * Get Current User's Saved Information from the database to the application
     */
    private void getUserInfoFromDatabase() {

        getUserDatabaseReference();
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0) {
                    Map<String, Object> map = (Map<String,Object>) dataSnapshot.getValue();
                    assert map != null;
                    if (map.get("SlidingTilesBoard Size")!=null) {
                        String boardSize = map.get("SlidingTilesBoard Size").toString();
                        switch (boardSize) {
                            case "3x3":
                                oldBoardSize = 3;
                                SlidingTilesBoard.setBoardSize(oldBoardSize);
                                break;
                            case "4x4":
                                oldBoardSize = 4;
                                SlidingTilesBoard.setBoardSize(oldBoardSize);

                                break;
                            case "5x5":
                                oldBoardSize = 5;
                                SlidingTilesBoard.setBoardSize(oldBoardSize);
                                break;
                        }
                    }
                    if (map.get("last_saved_undo_count")!=null) {
                        String lastSavedUndoCount = map.get("last_saved_undo_count").toString();
                        switch (lastSavedUndoCount) {
                            case "Undo uses left: 0":
                                MoveStack.setNumUndos(0);
                                break;
                            case "Undo uses left: 1":
                                MoveStack.setNumUndos(1);
                                break;
                            case "Undo uses left: 2":
                                MoveStack.setNumUndos(2);
                                break;
                            case "Undo uses left: 3":
                                MoveStack.setNumUndos(3);
                                break;
                            case "Unlimited":
                                MoveStack.setNumUndos(-1);
                                break;
                        }

                    }
                    if (map.get("last_Saved_Score")!=null) {
                        Long lastSavedScore = Long.parseLong(map.get("last_Saved_Score").toString());
                        slidingTilesBoardManager.setScore(lastSavedScore);
                    }

                    if(map.get("Board_Type")!= null) {
                        String lastSavedBoardType = map.get("Board_Type").toString();
                        SlidingTilesBoard.setType(lastSavedBoardType);

                    }
                    if(map.get("requested_image")!= null) {
                        String lastSavedBoardImage = map.get("requested_image").toString();
                        SlidingTilesBoard.setIMAGE(lastSavedBoardImage);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    /**
     * Get Current User's Saved Name from the database to the application
     */
    private void getUserNameFromDatabase() {

        getUserDatabaseReference();
        mUserDatabase.getParent().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0) {
                    Map<String, Object> map = (Map<String,Object>) dataSnapshot.getValue();
                    assert map != null;
                    if (map.get("Name")!=null) {
                        String name = map.get("Name").toString();
                        mWelcomeText.setText("Welcome Back "+name);
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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child("sliding_tiles");
    }


}
