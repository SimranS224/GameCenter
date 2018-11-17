package fall2018.csc2017.GameCentre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Leader board class
 */
public class LeaderBoardMain extends AppCompatActivity {

    /**
     * Firebase Database reference pointing to the current user
     */
    private DatabaseReference mUserDatabase;
    /**
     * The TextView representing number of Undos left in the current game
     */
    TextView textView;

    // the listview
    private ListView listLead;
    private UserScores allUsers = new UserScores(); // all the users

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        listLead = findViewById(R.id.listLead);
        switchToMyBestScores();
        switchTo3x3();
        switchTo5x5();
        swipe_test();
        getUserDatabaseReference();

        // gets called when activity starts, and whenever a change is made to the database
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (allUsers.isEmpty()) {
                    readData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void swipe_test() {
        Button test_button = findViewById(R.id.swipe_test);
        test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoardMain.this, swipeTest.class);
                startActivity(intent);
                finish();

            }
        });
    }

    /**
     * Activate the switch to 3x3 button.
     */
    private void switchTo3x3() {
        Button globalRankButton = findViewById(R.id.globalHigh3);
        globalRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoardMain.this, GlobalThreeByThree.class);
                startActivity(intent);
                finish();

            }
        });
    }

    /**
     * Activate the switch to 5x5 button.
     */
    private void switchTo5x5() {
        Button globalRankButton = findViewById(R.id.globalHigh5);
        globalRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoardMain.this, GlobalFiveByFive.class);
                startActivity(intent);
                finish();

            }
        });
    }

    /**
     * Activate the switch to my best rankings button.
     */
    private void switchToMyBestScores() {
        Button switchMyScore = findViewById(R.id.switchMyScores);
        switchMyScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoardMain.this, LeaderBoardCurrentUser.class);
                startActivity(intent);
                finish();

            }
        });
    }

    /**
     * Reads the data from the DataSnapshot from Firebase
     *
     * @param d The snapshot
     */
    private void readData(DataSnapshot d) {
        for (DataSnapshot dataSnapshot : d.getChildren()) {
            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                assert map != null;
                if (map.get("Name") != null) {
                    String name = map.get("Name").toString();
                    String score = returnWinningScore(map);
                    Scores userScore = new Scores();
                    userScore.setName(name);

                    userScore.setScore(score);
                    if (!userScore.getScore().equals("NA")) {
                        allUsers.add(userScore);
                    }
                }

            }
        }

        LeaderBoardCustomListAdapter adapter = new LeaderBoardCustomListAdapter(this, allUsers);
        listLead.setAdapter(adapter);
    }

    /**
     * Get the winning score for this leaderboard
     *
     * @param map from Firebase
     * @return returns the score in String form
     */
    public String returnWinningScore(Map<String, Object> map) {
        if (map.get("First_Best_Time4x4") != null) {
            return map.get("First_Best_Time4x4").toString();
        } else {
            return "NA";
        }
    }

    /**
     * Gets user reference from Firebase.
     */
    private void getUserDatabaseReference() {
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users").child("userId");
    }

}