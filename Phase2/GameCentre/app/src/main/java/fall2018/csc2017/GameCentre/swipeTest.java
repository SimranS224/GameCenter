package fall2018.csc2017.GameCentre;

import android.hardware.usb.UsbRequest;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This is the actual activity that manages the swipeView; Nothing needs to be
 * changed in here.
 */
public class swipeTest extends FragmentActivity {
    static final int num_list = 6;

    swipeViewAdapter mAdapter;

    ViewPager mPager;

    Integer theCounter = 0;

    LeaderBoardReader tempStorage = new LeaderBoardReader();
    private DatabaseReference mGamesDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipeview_test);
        mAdapter = new swipeViewAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager_test);
        mPager.setAdapter(mAdapter);
        mAdapter.setContext(this);
        getDataBaseReference();
        theCounter++;


        saveCountOnDataBase();

        mGamesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> addedAlready = new ArrayList<>();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, ArrayList> map = (Map<String, ArrayList>) dataSnapshot.getValue();
                    assert map != null;

                    if (map.get("SlidingTilesThree") != null && !addedAlready.contains("SlidingTilesThree")) {
                        ArrayList theCurrentList = (map.get("SlidingTilesThree"));

                        tempStorage.addAllToContents(theCurrentList);
                        addedAlready.add("SlidingTilesThree");

                    }
                    if (map.get("SlidingTilesFour") != null && !addedAlready.contains("SlidingTilesFour")) {
                        ArrayList theCurrentList = (map.get("SlidingTilesFour"));
                        tempStorage.addAllToContents(theCurrentList);
                        addedAlready.add("SlidingTilesFour");

                    }
                    if (map.get("SlidingTilesFive") != null && !addedAlready.contains("SlidingTilesFive")) {
                        ArrayList theCurrentList = (map.get("SlidingTilesFive"));
                        tempStorage.addAllToContents(theCurrentList);
                        addedAlready.add("SlidingTilesFive");

                    }
                    if (map.get("Sequncer") != null && !addedAlready.contains("Sequncer")) {
                        ArrayList theCurrentList = (map.get("Sequncer"));
                        tempStorage.addAllToContents(theCurrentList);
                        addedAlready.add("Sequncer");

                    }
                    if (map.get("TicTacToe") != null && !addedAlready.contains("TicTacToe")) {
                        ArrayList theCurrentList = (map.get("TicTacToe"));
                        tempStorage.addAllToContents(theCurrentList);
                        addedAlready.add("TicTacToe");
                    }
                    while (tempStorage.getSize() < 6) {
                        UserScores emptyOne = new UserScores();
                        tempStorage.add(emptyOne);
                    }
                    mAdapter.addToGameScoresList(tempStorage.getContents());

                }
            }
//                for (DataSnapshot d : dataSnapshot.getChildren()) {
//                    if (d.exists() && d.getChildrenCount() > 0) {
//                        Map<String, ArrayList> map = (Map<String, ArrayList>) d.getValue();
//                        assert map != null;
//                        LeaderBoardReader tempStorage = new LeaderBoardReader();
//                        if (map.get("SlidingTilesThree") != null && !addedAlready.contains("SlidingTilesThree")){
//                            ArrayList theCurrentList = (map.get("SlidingTilesThree"));
//                            tempStorage.addAllToContents(theCurrentList);
//                            addedAlready.add("SlidingTilesThree");
//
//                        }else if (map.get("SlidingTilesFour") != null && !addedAlready.contains("SlidingTilesFour")){
//                            ArrayList theCurrentList = (map.get("SlidingTilesFour"));
//                            tempStorage.addAllToContents(theCurrentList);
//                            addedAlready.add("SlidingTilesFour");
//
//                        }else if (map.get("SlidingTilesFive") != null && !addedAlready.contains("SlidingTilesFive")){
//                            ArrayList theCurrentList = (map.get("SlidingTilesFive"));
//                            tempStorage.addAllToContents(theCurrentList);
//                            addedAlready.add("SlidingTilesFive");
//
//                        }else if (map.get("Sequncer") != null && !addedAlready.contains("Sequncer")){
//                            ArrayList theCurrentList = (map.get("Sequncer"));
//                            tempStorage.addAllToContents(theCurrentList);
//                            addedAlready.add("Sequncer");
//
//                        }else if (map.get("TicTacToe") != null && !addedAlready.contains("TicTacToe")){
//                            ArrayList theCurrentList = (map.get("TicTacToe"));
//                            tempStorage.addAllToContents(theCurrentList);
//                            addedAlready.add("TicTacToe");
//                        }
//                        mAdapter.addToGameScoresList(tempStorage.getContents());


//                        if (map.get("Name") != null) {
//                            String name = map.get("Name").toString();
//                            String score = returnWinningScore(map);
//                            Scores userScore = new Scores();
//                            userScore.setName(name);
//
//                            userScore.setScore(score);
//                            if (!userScore.getScore().equals("NA")) {
//                                allUsers.add(userScore);
//                            }
//                        }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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


        mAdapter.addToGameScoresList(testList);

    }
    private void saveCountOnDataBase() {
        Integer counter = theCounter;

        // String lastSavedUndoCount = textView.getText().toString();
        Map<String, Object> newMap = new HashMap<>();
        newMap.put("counter", counter);
        mGamesDatabase.updateChildren(newMap);
    }


    public void getDataBaseReference(){
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
//                    userScore.setScore(score);
//                    if (!userScore.getScore().equals("NA")) {
//                        allUsers.add(userScore);
//                    }
//                }
//
//            }
//        }
//
//        LeaderBoardCustomListAdapter adapter = new LeaderBoardCustomListAdapter(this, allUsers);
//        listLead.setAdapter(adapter);