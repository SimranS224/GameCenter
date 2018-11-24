package fall2018.csc2017.GameCentre;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;


/**
 * This is the actual activity that manages the swipeView; Nothing needs to be
 * changed in here.
 */
public class swipeTest extends FragmentActivity {
    static final int num_list = 6;

    swipeViewAdapter mAdapter;

    ViewPager mPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipeview_test);
        mAdapter = new swipeViewAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager_test);
        mPager.setAdapter(mAdapter);



    }


}
