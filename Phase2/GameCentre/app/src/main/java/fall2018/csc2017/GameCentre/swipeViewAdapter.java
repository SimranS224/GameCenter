package fall2018.csc2017.GameCentre;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;


/**
 * This is the controller for the swipeView. In short, each time we change pages, ie a swipe,
 * a new DemoFragment is created. In order to set the TextViews on this new demo fragment,
 * a bundle is used to store the information, which is then taken out in DemoFragment.
 */
public class swipeViewAdapter extends FragmentStatePagerAdapter{
    String[] type = new String[2];
    String[] size = new String[3];
    public ArrayList<UserScores> allScores; // TODO make it size 6 and index in the fragments part
    private Context mContext;

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
        demoFragment.setContext(mContext);
        demoFragment.setList(allScores);
        // todo make a method in demo fragment which sets its list to be the arraylist of 6 lists of highscores for the
        // todo different games and then access the list by index that is sent from the if loops below,
        // todo so only send an int value
        position = position+1;
        String curr_type;
        String curr_size;
        Integer current;
        // List curlist //todo
        if (position == 1) {
            curr_type = type[0];
            curr_size = size[0];
            current = 0;
            //curr_list = list.get(0)//list x  todo set this to be the list that u want to show in each fragment
            //list // slidingtiles floblal 3x3

        }
        else if (position == 2) {
            curr_type = type[0];
            curr_size = size[1];
            current = 1;

        }
        else if (position == 3) {
            curr_type = type[0];
            curr_size = size[2];
            current = 2;

        }
        else if (position == 4) {
            curr_type = type[1];
            curr_size = size[0];
            current = 3;

        }
        else if (position == 5) {
            curr_type = type[1];
            curr_size = size[1];
            current = 4;

        }
        else {
            curr_type = type[1];
            curr_size = size[2];
            current = 5;

        }
//        bundle.putParcelableArrayList();
        bundle.putInt("index", current);
        bundle.putString("type", curr_type);
        bundle.putString("size", curr_size);
        //TODO This is where you add your arraylists/lists to the bundle,
        //TODO based on position. No need to change the ListView directly in here,
        //TODO that should be done in DemoFragment.
        demoFragment.setArguments(bundle);

        return demoFragment;

    }

    public void addToGameScoresList(ArrayList<UserScores> contents) {
        allScores = new ArrayList<>();
        if (contents!=null) {

            for (int i = 0; i < contents.size(); i++) {
                allScores.add(contents.get(i));
            }
            //allScores.addAll(contents);
        }
    }

    public void setContext(Context swipeTest) {
        this.mContext = swipeTest;
    }
}
