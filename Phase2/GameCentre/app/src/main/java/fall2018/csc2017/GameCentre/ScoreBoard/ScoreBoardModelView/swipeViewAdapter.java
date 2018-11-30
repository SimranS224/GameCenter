package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.UserScores;


/**
 * This is the controller for the swipeView. In short, each time we change pages, ie a swipe,
 * a new DemoFragment is created. In order to set the TextViews on this new demo fragment,
 * a bundle is used to store the information, which is then taken out in DemoFragment.
 */
public class swipeViewAdapter extends FragmentStatePagerAdapter {
    private final String[] type = new String[6];
    private final String[] size = new String[4];
    private final String[] scoreType = new String[2];
    private ArrayList<UserScores> allScores;
    private Context mContext;
    private String curUserName;

    swipeViewAdapter(FragmentManager fragmentManager) { //TODO look into making these varibles final
        super(fragmentManager);
        type[0] = "Now Displaying Global Rankings For Sliding Tiles";
        type[1] = "Now Displaying Your Best Score For Sliding Tiles";
        type[2] = "Now Displaying Your Global Rankings For Sequencer";
        type[3] = "Now Displaying Your Best Score For Sequencer";
        type[4] = "Now Displaying Your Global Rankings For TicTacToe";
        type[5] = "Now Your Best Score For TicTacToe";
        size[0] = "For 3x3:";
        size[1] = "For 4x4:";
        size[2] = "For 5x5:";
        size[3] = ""; // for games where size doesnt matter.
        scoreType[0] = "p";
        scoreType[1] = "g";

    }

    public int getCount() {
        return 10;

    }

    @Override
    public Fragment getItem(int position) {
        DemoFragment demoFragment = new DemoFragment();
        Bundle bundle = new Bundle();
        demoFragment.setContext(mContext);
        demoFragment.setList(allScores);

        position = position + 1;
        String curr_type;
        String curr_size;
        String scoreViewType;
        Integer current;

        if (position == 1) {
            curr_type = type[0];
            curr_size = size[0];
            current = 0;
            scoreViewType = scoreType[1];
        } else if (position == 2) {
            curr_type = type[0];
            curr_size = size[1];
            current = 1;
            scoreViewType = scoreType[1];
        } else if (position == 3) {
            curr_type = type[0];
            curr_size = size[2];
            current = 2;
            scoreViewType = scoreType[1];
        } else if (position == 4) {
            curr_type = type[1];
            curr_size = size[0];
            current = 0;
            scoreViewType = scoreType[0];

        } else if (position == 5) {
            curr_type = type[1];
            curr_size = size[1];
            current = 1;
            scoreViewType = scoreType[0];
        } else if (position == 6) { // all the sliding tiles ones first
            curr_type = type[1];
            curr_size = size[2];
            current = 2;
            scoreViewType = scoreType[0];

        } else if (position == 7) {
            curr_type = type[2];
            curr_size = size[3];
            current = 3;
            scoreViewType = scoreType[1];

        } else if (position == 8) {
            curr_type = type[3];
            curr_size = size[3];
            current = 3;
            scoreViewType = scoreType[0];

        } else if (position == 9) {
            curr_type = type[4];
            curr_size = size[3];
            current = 4;
            scoreViewType = scoreType[1];

        } else {
            curr_type = type[5];
            curr_size = size[3];
            current = 4;
            scoreViewType = scoreType[0];

        }
        bundle.putString("currentUser", curUserName);
        bundle.putInt("index", current);
        bundle.putString("publicorglobal", scoreViewType);
        bundle.putString("type", curr_type);
        bundle.putString("size", curr_size);

        demoFragment.setArguments(bundle);

        return demoFragment;

    }

    /**
     * Sets the list of UserScors
     *
     * @param contents the highscores of the games
     */
    public void addToGameScoresList(ArrayList<UserScores> contents) {
        allScores = new ArrayList<>();
        allScores.addAll(contents);

    }

    /**
     * sets the context
     *
     * @param swipeTest the context
     */
    public void setContext(Context swipeTest) {
        this.mContext = swipeTest;
    }

    /**
     * the current user's name
     *
     * @param name current user names
     */
    public void setName(String name) {
        this.curUserName = name;
    }

    /**
     * Gets the current user's name
     *
     * @return current user's name
     */
    public String getName() {
        return curUserName;
    }
}
