package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.LeaderBoardReader;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.Scores;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.UserScores;


/**
 * This is the actual activity that manages the swipeView.
 */
public class swipeTest extends FragmentActivity {
    /**
     * the swipe view apapter
     */
    swipeViewAdapter mAdapter;
    /**
     * the pager for the view
     */
    ViewPager mPager;
    /**
     * stores a list of the different scores from all the games
     */
    LeaderBoardReader tempStorage;
    /**
     * the Games database reference
     */
    private DatabaseReference mGamesDatabase;
    /**
     * the User database reference
     */
    private DatabaseReference mUserDatabase;
    /**
     * the name of the current user
     */
    private String currentUserName;
    /**
     * the handler
     */
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipeview_test);
        mAdapter = new swipeViewAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.pager_test);
        mPager.setAdapter(mAdapter);
        mAdapter.setContext(this);
        getDataBaseReference();
        tempStorage = new LeaderBoardReader();
        getCurrUserName();


        Runnable update = new Runnable() {
            @Override
            public void run() {
                mGamesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> addedAlready = new ArrayList<>();
                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            Map<String, ArrayList> map = (Map<String, ArrayList>) dataSnapshot.getValue();
                            if (map == null) throw new AssertionError();
                            ArrayList<String> listGames = new ArrayList<>(Arrays.asList("SlidingTilesThree", "SlidingTilesFour", "SlidingTilesFive", "Sequencer", "TicTacToe"));
                            for (int i = 0; i < listGames.size(); i++) {
                                String toCheck = listGames.get(i);
                                if (map.get(toCheck) != null) {
                                    ArrayList theCurrentList = (map.get(toCheck));

                                    tempStorage.addAllToContents(theCurrentList);
                                    System.out.println(tempStorage);

                                } else {
                                    UserScores emptyOne = new UserScores();
                                    tempStorage.add(emptyOne);
                                }
                            }
                            mAdapter.addToGameScoresList(tempStorage.getContents());

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                handler.postDelayed(this, 1);
            }

        };
        handler.post(update);

    }

    /**
     * Get Current User's Saved Information from the database to the application
     */
    private void getCurrUserName() {

        getDataBaseReference();
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    assert map != null;
                    if (map.get("Name") != null) {
                        currentUserName = map.get("Name").toString();
                        mAdapter.setName(currentUserName);
                        System.out.println(currentUserName);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Gets the references to the database elements.
     */
    public void getDataBaseReference() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID);
        mGamesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Games");
    }

}