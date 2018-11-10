package fall2018.csc2017.GameCentre;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The settings activity where the new game settings are configured
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * The undo options to choose
     */
    private RadioGroup mUndoSettings;

    /**
     * The id of the undo options button
     */
    private int requestedUndoSettingId;
    /**
     * String representing the undo settings
     */
    private String requestedUndoSetting;

    /**
     * The board manager
     */
    private BoardManager boardManager;

    /**
     * The options for the board size
     */
    private RadioGroup mBoardSize;

    /**
     * The id of the board size button
     */
    private int requestedBoardSizeId;

    /**
     * String representing the board size chosen
     */
    private String requestedBoardSize;


    /**
     * Firebase Database reference pointing to the current user
     */
    private DatabaseReference mUserDatabase;

    /**
     * Image of the board if image- tile board size is selected.
     */
    private RadioGroup mImage;
    private int requestedImageId;
    private String requestedImage;

    private RadioGroup mType;
    private int requestedTypeId;
    private String requestedType;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        addUndoSettingsButtonListener();
        addBoardSizeButtonListener();
        addImageButtonListener();
        addTypeButtonListener();


        getUserDatabaseReference();
        getUserSettingsFromDatabase();

        addImageButtonListener();
        addTypeButtonListener();

        addConfirmButtonListener();
        addBackButtonListener();


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

    /**
     * Activate the undo options buttons
     */
    private void addUndoSettingsButtonListener() {
        mUndoSettings = findViewById(R.id.undoSettings);
        mUndoSettings.check(R.id.limitedUndo);
        requestedUndoSettingId = mUndoSettings.getCheckedRadioButtonId();
        final RadioButton selectedRadioButton = findViewById(requestedUndoSettingId);
        requestedUndoSetting= selectedRadioButton.getText().toString();
    }

    /**
     * Activate the Board Size Options Button
     */
    private void addBoardSizeButtonListener() {
        mBoardSize = findViewById(R.id.boardSize);
        mBoardSize.check(R.id.fourByFour);
        requestedBoardSizeId = mBoardSize.getCheckedRadioButtonId();
        final RadioButton selectedBoardSize = findViewById(requestedBoardSizeId);
        requestedBoardSize= selectedBoardSize.getText().toString();
    }
    private void addImageButtonListener() {
        mImage = findViewById(R.id.imageSettings);
        mImage.check(R.id.american_pie);
        requestedImageId = mImage.getCheckedRadioButtonId();
        final RadioButton selectedImage = findViewById(requestedImageId);
        requestedImage = selectedImage.getText().toString();
    }
    private void addTypeButtonListener() {
        mType = findViewById(R.id.boardType);
        mType.check(R.id.numberTiles);
        requestedTypeId = mType.getCheckedRadioButtonId();
        final RadioButton selectedType = findViewById(requestedTypeId);
        requestedType = selectedType.getText().toString();
    }


    /**
     * Activate the start game button
     */
    private void addConfirmButtonListener() {
        Button mConfirm = findViewById(R.id.confirm);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformationOnDatabase();
                createNewGame();
            }
        });
    }

    /**
     * Activate the back button
     */
    private void addBackButtonListener() {

        Button mBack = findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Create a New Game
     */
    private void createNewGame() {

        if (requestedUndoSetting.equals("3 Undo")) {
            MoveStack.setNumUndos(3);
        }
        if (requestedUndoSetting.equals("Unlimited Undo")) {
            MoveStack.setNumUndos(-1);
        }

        switch (requestedBoardSize) {
            case "3x3":
                Board.setBoardSize(3);
                break;
            case "4x4":
                Board.setBoardSize(4);
                break;
            case "5x5":
                Board.setBoardSize(5);

                break;
        }

        // Sets the correct type of board
        Board.setType(requestedType);

        // Sets the correct image
        Board.setIMAGE(requestedImage);


        startNewGame();

    }


    /**
     * Start a new game
     */
    private void startNewGame() {
        boardManager = new BoardManager();
        Intent tmp = new Intent(SettingsActivity.this, GameActivity.class);
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
        startActivity(tmp);
        finish();
    }


    /**
     * Imports Current User's Saved Settings from the database to the application
     */
    private void getUserSettingsFromDatabase() {
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0) {
                    Map<String, Object> map = (Map<String,Object>) dataSnapshot.getValue();
                    assert map != null;
                    if(map.get("undoSettings") !=null) {
                        requestedUndoSetting = map.get("undoSettings").toString();
                        if(requestedUndoSetting.equals("3 Undo")) {
                            requestedUndoSettingId= R.id.limitedUndo;
                        }
                        if(requestedUndoSetting.equals("Unlimited Undo")) {
                            requestedUndoSettingId= R.id.unlimitedUndo;
                        }
                        mUndoSettings.check(requestedUndoSettingId);
                    }

                    if(map.get("Board Size") !=null) {
                        requestedBoardSize = map.get("Board Size").toString();
                        if(requestedBoardSize.equals("3x3")) {
                            requestedBoardSizeId= R.id.threeByThree;
                        }
                        if(requestedBoardSize.equals("4x4")) {
                            requestedBoardSizeId= R.id.fourByFour;
                        }
                        if(requestedBoardSize.equals("5x5")) {
                            requestedBoardSizeId= R.id.fiveByFive;
                        }

                        mBoardSize.check(requestedBoardSizeId);
                    }
                    if (map.get("Board_Type") !=null) {
                        requestedType = map.get("Board_Type").toString();
                        if(requestedType.equals("Number tiles")) {
                            requestedTypeId = R.id.numberTiles;
                        }
                        if(requestedType.equals("Image tiles")) {
                            requestedTypeId = R.id.imageTiles;
                        }

                        mType.check(requestedTypeId);
                    }
                    if ((map.get("requested_image"))!= null) {
                        requestedImage = map.get("requested_image").toString();
                        if(requestedImage.equals("American Pie")) {
                            requestedImageId = R.id.american_pie;
                        }
                        if(requestedImage.equals("U of T")) {
                            requestedImageId = R.id.uoft;
                        }
                        if(requestedImage.equals("Flower")) {
                            requestedImageId = R.id.flower;
                        }
                        mImage.check(requestedImageId);

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

        //number of undos
        requestedUndoSettingId = mUndoSettings.getCheckedRadioButtonId();
        final RadioButton selectedRadioButton = findViewById(requestedUndoSettingId);
        requestedUndoSetting= selectedRadioButton.getText().toString();

        //board size
        requestedBoardSizeId = mBoardSize.getCheckedRadioButtonId();
        final RadioButton selectedBoardSize = findViewById(requestedBoardSizeId);
        requestedBoardSize= selectedBoardSize.getText().toString();


        //board type
        requestedTypeId = mType.getCheckedRadioButtonId();
        final RadioButton selectedBoardType = findViewById(requestedTypeId);
        requestedType = selectedBoardType.getText().toString();

        //image type
        requestedImageId = mImage.getCheckedRadioButtonId();
        final RadioButton selectedImage = findViewById(requestedImageId);
        requestedImage = selectedImage.getText().toString();



        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("undoSettings",requestedUndoSetting);

        //overwrite last saved undo count
        userInfo.put("last_saved_undo_count",requestedUndoSetting);

        userInfo.put("Board Size", requestedBoardSize);

        userInfo.put("Board_Type",requestedType);

        userInfo.put("requested_image",requestedImage);

        mUserDatabase.updateChildren(userInfo);
    }


    /**
     * Get database reference from the Firebase Database pointing to the current user
     */
    private void getUserDatabaseReference() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID);
    }





}


