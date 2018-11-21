package fall2018.csc2017.GameCentre.ScoreBoard;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;

import fall2018.csc2017.GameCentre.ScoreBoard.DemoFragment;


/**
 * This is the controller for the swipeView. In short, each time we change pages, ie a swipe,
 * a new DemoFragment is created. In order to set the TextViews on this new demo fragment,
 * a bundle is used to store the information, which is then taken out in DemoFragment.
 */
public class swipeViewAdapter extends FragmentStatePagerAdapter{
    String[] type = new String[2];
    String[] size = new String[3];
    swipeViewAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        type[0] = "Now Displaying Global Rankings";
        type[1] = "Now Displaying Your Best Scores";
        size[0] = "For 3x3:";
        size[1] = "For 4x4:";
        size[2] = "For 5x5:";

    }

    public int getCount() {
        return 6;

    }
    @Override
    public Fragment getItem(int position) {
        DemoFragment demoFragment = new DemoFragment();
        Bundle bundle = new Bundle();
        position = position+1;
        String curr_type;
        String curr_size;

        if (position == 1) {
            curr_type = type[0];
            curr_size = size[0];
        }
        else if (position == 2) {
            curr_type = type[0];
            curr_size = size[1];
        }
        else if (position == 3) {
            curr_type = type[0];
            curr_size = size[2];
        }
        else if (position == 4) {
            curr_type = type[1];
            curr_size = size[0];
        }
        else if (position == 5) {
            curr_type = type[1];
            curr_size = size[1];
        }
        else {
            curr_type = type[1];
            curr_size = size[2];
        }

        bundle.putString("type", curr_type);
        bundle.putString("size", curr_size);
        //TODO This is where you add your arraylists/lists to the bundle,
        //TODO based on position. No need to change the ListView directly in here,
        //TODO that should be done in DemoFragment.
        demoFragment.setArguments(bundle);

        return demoFragment;

    }
}
