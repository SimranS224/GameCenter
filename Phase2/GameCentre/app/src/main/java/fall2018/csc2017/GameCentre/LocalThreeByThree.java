package fall2018.csc2017.GameCentre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


/**
 * The local leaderBoard of the current user for a 3x3 Board
 */
public class LocalThreeByThree extends AppCompatActivity {


    /**
     * Reference pointing to the database of the current user
     */
    private DatabaseReference mUserDatabase;

    /**
     * The listView of the scores
     */
    private ListView listLeadCurrentUser;

    /**
     * The ArrayList containing all the scores of the current user
     */
    private ArrayList<String> allCurrScores = new ArrayList<>(); // all the users


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboardcurrentuser_3);

        listLeadCurrentUser = findViewById(R.id.listLeadCurrentUser3);
        switchToGlobalRankings();
        switchTo4x4();
        switchTo5x5();
        getUserDatabaseReference();

        // gets called when activity starts, and whenever a change is made to the database
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (allCurrScores.isEmpty()) {
                    readData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Reads the 5 best scores of the user for a 3x3 board
     *
     * @param d Datasnapshot of the firebase database
     */
    private void readData(DataSnapshot d) {
        if (d.exists() && d.getChildrenCount() > 0) {
            Map<String, Object> map = (Map<String, Object>) d.getValue();
            assert map != null;
            if (map.get("First_Best_Time3x3") != null) {
                String score = map.get("First_Best_Time3x3").toString();
                allCurrScores.add(score);

            }
            if (map.get("Second_Best_Time3x3") != null) {
                String score = map.get("Second_Best_Time3x3").toString();
                allCurrScores.add(score);
            }
            if (map.get("Third_Best_Time3x3") != null) {
                String score = map.get("Third_Best_Time3x3").toString();
                allCurrScores.add(score);
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allCurrScores);
        listLeadCurrentUser.setAdapter(adapter);
    }


    /**
     * Activate the swtich to my best rankings button.
     */
    private void switchToGlobalRankings() {
        Button globalRankButton = findViewById(R.id.globalrank);
        globalRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocalThreeByThree.this, LeaderBoardMain.class);
                startActivity(intent);
                finish();

            }
        });
    }


    /**
     * Activate the switch to 4x4 button.
     */
    private void switchTo5x5() {
        Button globalRankButton = findViewById(R.id.UserHigh5);
        globalRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocalThreeByThree.this, LocalFiveByFive.class);
                startActivity(intent);
                finish();

            }
        });
    }

    /**
     * Activate the switch to 3x3 button.
     */
    private void switchTo4x4() {
        Button globalRankButton = findViewById(R.id.UserHigh4);
        globalRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocalThreeByThree.this, LeaderBoardCurrentUser.class);
                startActivity(intent);
                finish();

            }
        });
    }


    /**
     * Get the reference pointing to the database of the current user
     */
    private void getUserDatabaseReference() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // the current user's id
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users").child("userId").child(userID);
    }


}


