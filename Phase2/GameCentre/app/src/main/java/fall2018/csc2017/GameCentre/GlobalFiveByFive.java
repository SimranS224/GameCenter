package fall2018.csc2017.GameCentre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

// firebase imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Used to display and store the Five by Five global scores.
 */
public class GlobalFiveByFive extends AppCompatActivity {

    //ref pointing to user database where we get id from
    private DatabaseReference mUserDatabase;
    // the listview
    private ListView listLead;
    private UserScores allUsers = new UserScores(); // all the users

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_global_5);

        listLead = findViewById(R.id.listLead5);
        switchToMyBestScores();
        switchTo3x3();
        switchTo4x4();
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

    /**
     * Activate the switch to 4x4b utton.
     */
    private void switchTo4x4() {
        Button globalRankButton = findViewById(R.id.globalHigh4);
        globalRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GlobalFiveByFive.this, LeaderBoardMain.class);
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
                Intent intent = new Intent(GlobalFiveByFive.this, GlobalThreeByThree.class);
                startActivity(intent);
                finish();

            }
        });
    }

    /**
     * Activate the swtich to my best rankings button.
     */
    private void switchToMyBestScores() {
        Button switchMyScore = findViewById(R.id.switchMyScores);
        switchMyScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GlobalFiveByFive.this, LeaderBoardCurrentUser.class);
                startActivity(intent);
                finish();

            }
        });
    }

    /**
     * Reads the data from snapshot from Firebase.
     *
     * @param d Snapshot
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
     * Gets winning score from Firebase for 5 by 5 board size.
     *
     * @param map from Firebase
     * @return Winning score
     */
    public String returnWinningScore(Map<String, Object> map) {
        if (map.get("First_Best_Time5x5") != null) {
            return map.get("First_Best_Time5x5").toString();
        } else {
            return "NA";
        }
    }

    /**
     * Gets reference ro database objects
     */
    private void getUserDatabaseReference() {
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users").child("userId");
    }

}