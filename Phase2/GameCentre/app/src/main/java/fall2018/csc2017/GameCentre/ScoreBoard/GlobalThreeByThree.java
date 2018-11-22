package fall2018.csc2017.GameCentre.ScoreBoard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

// firebase imports
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import fall2018.csc2017.GameCentre.R;

/**
 * Used to update and store the Three by Three global scores.
 */
public class GlobalThreeByThree extends AppCompatActivity {

    //ref pointing to user database where we get id from
    private DatabaseReference mUserDatabase;
    // the listview
    private ListView listLead;
    private UserScores allUsers = new UserScores(); // all the users

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_global_3);

        listLead = findViewById(R.id.listLead3);
        switchToMyBestScores();
        switchTo4x4();
        switchTo5x5();
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
                Intent intent = new Intent(GlobalThreeByThree.this, LeaderBoardMain.class);
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
                Intent intent = new Intent(GlobalThreeByThree.this, GlobalFiveByFive.class);
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
                Intent intent = new Intent(GlobalThreeByThree.this, LeaderBoardCurrentUser.class);
                startActivity(intent);
                finish();

            }
        });
    }

    /**
     * Reads the data from the databse from Firebase.
     *
     * @param d snapshot
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
     * Returns wining score from Firebase map.
     *
     * @param map from Firebase
     * @return Winning score
     */
    public String returnWinningScore(Map<String, Object> map) {
        if (map.get("First_Best_Time3x3") != null) {
            return map.get("First_Best_Time3x3").toString();
        } else {
            return "NA";
        }
    }

    /**
     * Gets reference from Firebase database
     */
    private void getUserDatabaseReference() {
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users").child("userId");
    }

}