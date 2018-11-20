package fall2018.csc2017.GameCentre.TicTacToe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

import fall2018.csc2017.GameCentre.FirstActivity;
import fall2018.csc2017.GameCentre.GameChoiceActivity;
import fall2018.csc2017.GameCentre.R;

public class TicTacMainActivity extends AppCompatActivity {
    private Button connect4button;
    private Button randomAIbutton;
    private Button minimaxAIbutton;
    /**
     * The board manager.
     */
    private TicTacBoardManager boardManager;
    public static int PLAYER_TO_PLAYER = 0;
    public static int PLAYER_TO_RANDOM = 1;
    public static int PLAYER_TO_AI = 2;

    /**
     * Firebase Database reference pointing to the current user
     */
    private DatabaseReference mUserDatabase;


    /**
     * The Welcome Text
     */
    private TextView mWelcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.tictac_activity_main);

        getUserDatabaseReference();

        addLogoutButtonListener();
        addMainMenuButtonListener();
        mWelcomeText = findViewById(R.id.welcomeText);
        getUserNameFromDatabase();

        //Player vs Player button
        connect4button = findViewById(R.id.connect4button);
        connect4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(PLAYER_TO_PLAYER, 0);
            }
        });

        //RandomAIButton

        randomAIbutton = findViewById(R.id.randomAI_button);
        randomAIbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(PLAYER_TO_RANDOM, 0);
            }
        });

        //MinimaxAIButton
        minimaxAIbutton = findViewById(R.id.minimaxAI_button);
        minimaxAIbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(PLAYER_TO_AI, 4);
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
                Intent intent = new Intent(TicTacMainActivity.this,GameChoiceActivity.class);
                startActivity(intent);
                finish();
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
                Intent intent = new Intent(TicTacMainActivity.this,FirstActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void startGame(int gametype, int depth) {

        Intent intent = new Intent(this, TicTacGameActivity.class);
        Bundle b = new Bundle();
        b.putInt("gametype", gametype); //Your id
        b.putInt("depth", depth); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
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

        // Firebase User Authorisation
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID).child("tic_tac_toe");
    }
}
