package fall2018.csc2017.GameCentre;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import fall2018.csc2017.GameCentre.R;

/**
 * This is an individual page in a sense. Every time a new page is swiped to, a new
 * DemoFragement is created.
 */
public class DemoFragment extends Fragment {

    private TextView textView1;
    private TextView textView2;
    private ListView listView;
    public DemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        textView1 = view.findViewById(R.id.GlobalRanktext);
        textView2 = view.findViewById(R.id.globalRanktext);

        String type = getArguments().getString("type");
        String size = getArguments().getString("size");

        //TODO Get your list/array lists back from the bundle.

        textView1.setText(type);
        textView2.setText(size);

//        LeaderBoardCustomListAdapter adapter = new LeaderBoardCustomListAdapter(this, allUsers);
//        listLead.setAdapter(adapter); - todo implemnt this here


        //TODO "Set" the listview to be that list you got back from the bundle.
        return view;
    }

}
