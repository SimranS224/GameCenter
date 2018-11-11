package fall2018.csc2017.GameCentre;

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

/**
 * Activity where the user chooses the Game to play.
 */
public class GameChoiceActivity extends AppCompatActivity {

    /**
     * Firebase Database reference pointing to the current user
     */
    private DatabaseReference mUserDatabase;

    /**
     * String representing the id of the current user in the firebase database
     */
    private String userID;

    /**
     * The Welcome Text
     */
    private TextView mWelcomeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_choice);

        getUserDatabaseReference();

        mWelcomeText = findViewById(R.id.welcomeText);

        getUserInfoFromDatabase();

        addSlidingTilesButtonListener();

        addLogoutButtonListener();
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
                Intent intent = new Intent(GameChoiceActivity.this,FirstActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Activate Sliding Tiles Button
     */
    private void addSlidingTilesButtonListener() {
        Button mSlidingTilesButton = findViewById(R.id.SlidingTiles);
        mSlidingTilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameChoiceActivity.this,StartingActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Get database reference from the Firebase Database pointing to the current user
     */
    private void getUserDatabaseReference() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID);
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
