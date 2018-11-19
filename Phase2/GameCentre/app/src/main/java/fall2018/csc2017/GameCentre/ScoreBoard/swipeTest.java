package fall2018.csc2017.GameCentre.ScoreBoard;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import fall2018.csc2017.GameCentre.R;


public class swipeTest extends FragmentActivity {
    static final int num_list = 6;

    swipeViewAdapter mAdapter;

    ViewPager mPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e("Test", "1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipeview_test);
        Log.e("Test", "2");
        mAdapter = new swipeViewAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager_test);
        mPager.setAdapter(mAdapter);
        Log.e("Test", "3");



    }


}
