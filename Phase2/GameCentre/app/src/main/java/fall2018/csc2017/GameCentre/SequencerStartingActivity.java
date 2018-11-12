package fall2018.csc2017.GameCentre;

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


public class SequencerStartingActivity extends AppCompatActivity {

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
    private BoardManager boardManager;

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

        //every user has a different save file
        SAVE_FILENAME = userID + "save_file.ser";
        TEMP_SAVE_FILENAME= userID + "save_file_tmp.ser";

        getUserInfoFromDatabase();
        boardManager = new BoardManager();
        getUserInfoFromDatabase();
        String type1 = Board.getType();
        System.out.println(type1);


        saveToFile(TEMP_SAVE_FILENAME);

        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
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
                Intent intent = new Intent(SequencerStartingActivity.this,LeaderBoardMain.class);
                startActivity(intent);
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
                Intent intent = new Intent(SequencerStartingActivity.this,SettingsActivity.class);
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
                Intent intent = new Intent(SequencerStartingActivity.this,FirstActivity.class);
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
     * Switch to the GameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, GameActivity.class);
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
                    if (map.get("Name")!=null) {
                        String name = map.get("Name").toString();
                        mWelcomeText.setText("Welcome Back "+name);
                    }
                    if (map.get("Board Size")!=null) {
                        String boardSize = map.get("Board Size").toString();
                        switch (boardSize) {
                            case "3x3":
                                oldBoardSize = 3;
                                Board.setBoardSize(oldBoardSize);
                                break;
                            case "4x4":
                                oldBoardSize = 4;
                                Board.setBoardSize(oldBoardSize);

                                break;
                            case "5x5":
                                oldBoardSize = 5;
                                Board.setBoardSize(oldBoardSize);
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
                        int lastSavedScore = Integer.parseInt(map.get("last_Saved_Score").toString());
                        boardManager.setScore(lastSavedScore);
                    }

                    if(map.get("Board_Type")!= null) {
                        String lastSavedBoardType = map.get("Board_Type").toString();
                        Board.setType(lastSavedBoardType);

                    }
                    if(map.get("requested_image")!= null) {
                        String lastSavedBoardImage = map.get("requested_image").toString();
                        Board.setIMAGE(lastSavedBoardImage);
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
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child("Sequencer");
    }

//    private String downloadUserBoard(String fileName) {
//
//        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Saved_Games").child(userID);
//        StorageReference riversRef = filePath.child("last_saved_game/");
//
//        if (riversRef ==null) {
//            return fileName;
//        }
//        riversRef = riversRef.child(fileName);
//
//
//            File localFile = null;
//            if (fileName.equals(SAVE_FILENAME)) {
//                localFile = new File(StartingActivity.this.getFilesDir().getAbsolutePath() + "/" + SAVE_FILENAME);
//            } else if (fileName.equals(TEMP_SAVE_FILENAME)) {
//                localFile = new File(StartingActivity.this.getFilesDir().getAbsolutePath() + "/" + TEMP_SAVE_FILENAME);
//            }
//            // Log.d(localFile.getAbsolutePath(), "downloadUserBoard: ");
//
//
//            riversRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    // Local temp file has been created
//                    Log.d("load passed", "onSuccess: ");
//
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle any errors
//                    Log.d("load failed", "onFailure: ");
//                    finish();
//                    return;
//                }
//            });
//
//
//        return localFile.getName();
//
//
//    }

//    private void uploadUserBoard(final String fileName) {
//        // Create a storage reference from our app
//        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Saved_Games").child(userID);
//
//        Uri file = Uri.fromFile(new File(StartingActivity.this.getFilesDir().getAbsolutePath()+"/"+fileName));
//        final StorageReference riversRef = filePath.child("last_saved_game/"+file.getLastPathSegment());
//        UploadTask uploadTask = riversRef.putFile(file);
//
//        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
////                finish();
////                return;
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> downloadUrl = riversRef.getDownloadUrl();
//                Log.d(downloadUrl.toString(), "onSuccess: ");
//
//
//
//                Map newImage = new HashMap();
//
//                if(fileName.equals(SAVE_FILENAME)) {
//                    newImage.put("SAVE URI", downloadUrl.toString());
//                }
//                if (fileName.equals(TEMP_SAVE_FILENAME)) {
//                    newImage.put("TEMP_SAVE_URI",downloadUrl.toString());
//                }
//                mUserDatabase.updateChildren(newImage);
//
//
//            }
//        });
//    }






}
