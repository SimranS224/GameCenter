package fall2018.csc2017.GameCentre;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;


public class swipeViewAdapter extends FragmentStatePagerAdapter{

   swipeViewAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    public int getCount() {
        return 6;

    }
    @Override
    public Fragment getItem(int position) {
        DemoFragment demoFragment = new DemoFragment();
        Bundle bundle = new Bundle();
        position = position+1;
        bundle.putString("message", "hello from page : "+position);

        demoFragment.setArguments(bundle);
        return demoFragment;

    }
}
