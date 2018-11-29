package fall2018.csc2017.GameCentre;

import android.hardware.usb.UsbRequest;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This is the actual activity that manages the swipeView; Nothing needs to be
 * changed in here.
 */
public class swipeTest extends FragmentActivity {
    static final int num_list = 6;

    swipeViewAdapter mAdapter;

    ViewPager mPager;

    Integer theCounter = 0;

    LeaderBoardReader tempStorage;
    private DatabaseReference mGamesDatabase;
    private DatabaseReference mUserDatabase;
    private String currentUserName;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipeview_test);
        mAdapter = new swipeViewAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager_test);
        mPager.setAdapter(mAdapter);
        mAdapter.setContext(this);
        getDataBaseReference();
        tempStorage = new LeaderBoardReader();
        getCurrUserName();
        mAdapter.setName(currentUserName);

//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        }, 1000, 1000);
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//            }
//        });

        Runnable update = new Runnable() {
            @Override
            public void run() {
                mGamesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> addedAlready = new ArrayList<>();
                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            Map<String, ArrayList> map = (Map<String, ArrayList>) dataSnapshot.getValue();
                            assert map != null;
                            ArrayList<String> listGames = new ArrayList<>(Arrays.asList("SlidingTilesThree", "SlidingTilesFour", "SlidingTilesFive", "Sequencer", "TicTacToe"));
                            for (int i = 0; i < listGames.size(); i++) {
                                String toCheck = listGames.get(i);
                                if (map.get(toCheck) != null) {
                                    ArrayList theCurrentList = (map.get(toCheck));

                                    tempStorage.addAllToContents(theCurrentList);
                                    System.out.println(tempStorage);
                                    //                            addedAlready.add(toCheck);

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


        ArrayList<UserScores> testList = new ArrayList<>();
        UserScores x = new UserScores();
        Scores y = new Scores();
        y.setName("");
        y.setScore("");
        x.add(y);
//        testList.add(x);
//        testList.add(x);
//        testList.add(x);
//        testList.add(x);
//        testList.add(x);
//        testList.add(x);


//        mAdapter.addToGameScoresList(testList);

    }

    @Override
    protected void onStart() {
        super.onStart();

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
//                        String name = map.get("Name").toString();
                        currentUserName = map.get("Name").toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        tempStorage = new LeaderBoardReader();
//    }

//    private void saveCountOnDataBase() {
//        Integer counter = theCounter;
//
//        // String lastSavedUndoCount = textView.getText().toString();
//        Map<String, Object> newMap = new HashMap<>();
//        newMap.put("last_Saved_Score", counter);
//        mGamesDatabase.updateChildren(newMap);
//    }


    public void getDataBaseReference() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(userID);
        mGamesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Games");
    }

}
// for (DataSnapshot dataSnapshot : d.getChildren()) {
//            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                assert map != null;
//                if (map.get("Name") != null) {
//                    String name = map.get("Name").toString();
//                    String score = returnWinningScore(map);
//                    Scores userScore = new Scores();
//                    userScore.setName(name);
//
//                    userScore.setMoveCounter(score);
//                    if (!userScore.getCurrGameScore().equals("NA")) {
//                        allUsers.add(userScore);
//                    }
//                }
//
//            }
//        }
//
//        LeaderBoardCustomListAdapter adapter = new LeaderBoardCustomListAdapter(this, allUsers);
//        listLead.setAdapter(adapter);


//                    if (map.get("SlidingTilesThree") != null && !addedAlready.contains("SlidingTilesThree")) {
//                        ArrayList theCurrentList = (map.get("SlidingTilesThree"));
//
//                        tempStorage.addAllToContents(theCurrentList);
//                        System.out.println(tempStorage);
//                        addedAlready.add("SlidingTilesThree");
//
//                    }
//                    if (map.get("SlidingTilesFour") != null && !addedAlready.contains("SlidingTilesFour")) {
//                        ArrayList theCurrentList = (map.get("SlidingTilesFour"));
//                        tempStorage.addAllToContents(theCurrentList);
//                        addedAlready.add("SlidingTilesFour");
//
//                    }
//                    if (map.get("SlidingTilesFive") != null && !addedAlready.contains("SlidingTilesFive")) {
//                        ArrayList theCurrentList = (map.get("SlidingTilesFive"));
//                        tempStorage.addAllToContents(theCurrentList);
//                        addedAlready.add("SlidingTilesFive");
//
//                    }
//                    if (map.get("Sequncer") != null && !addedAlready.contains("Sequncer")) {
//                        ArrayList theCurrentList = (map.get("Sequncer"));
//                        tempStorage.addAllToContents(theCurrentList);
//                        addedAlready.add("Sequncer");
//
//                    }
//                    if (map.get("TicTacToe") != null && !addedAlready.contains("TicTacToe")) {
//                        ArrayList theCurrentList = (map.get("TicTacToe"));
//                        tempStorage.addAllToContents(theCurrentList);
//                        addedAlready.add("TicTacToe");
//                    }
//                    while (tempStorage.getSize() < 6) {
//                        UserScores emptyOne = new UserScores();
//                        tempStorage.add(emptyOne);
//                    }