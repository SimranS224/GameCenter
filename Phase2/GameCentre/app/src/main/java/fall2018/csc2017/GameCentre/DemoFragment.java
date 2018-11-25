package fall2018.csc2017.GameCentre;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import fall2018.csc2017.GameCentre.R;

/**
 * This is an individual page in a sense. Every time a new page is swiped to, a new
 * DemoFragement is created.
 */
public class DemoFragment extends Fragment {

    private TextView textView1;
    private TextView textView2;
    private ListView listView;
    public ArrayList<UserScores> listOfGameScores;
    private Context theContext;

    public DemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        textView1 = view.findViewById(R.id.GlobalRanktext);
        textView2 = view.findViewById(R.id.globalRanktext);
        ListView listLead = view.findViewById(R.id.listLead);
        String type = getArguments().getString("type");
        String size = getArguments().getString("size");
        Integer index = getArguments().getInt("index");

        //TODO Get your list/array lists back from the bundle.

        textView1.setText(type);
        textView2.setText(size);


//        LeaderBoardCustomListAdapter adapter = new LeaderBoardCustomListAdapter(this, allUsers);
//        listLead.setAdapter(adapter); - todo implemnt this here
        UserScores theCurrentView = listOfGameScores.get(index);


//        LeaderBoardCustomListAdapter adapter = null;
////        if (index > 2){
////            if (theCurrentView.size() != 0) {
////                UserScores onlyOne = new UserScores();
////                Scores bestOne = theCurrentView.get(0);
////                onlyOne.add(bestOne);
////                adapter = new LeaderBoardCustomListAdapter(theContext, onlyOne);;
////            }else{
////                adapter = new LeaderBoardCustomListAdapter(theContext, theCurrentView);
////            }
////        }else{
//        LeaderBoardCustomListAdapter adapter = new LeaderBoardCustomListAdapter(theContext, theCurrentView);
//        }


        LeaderBoardCustomListAdapter adapter = new LeaderBoardCustomListAdapter(theContext, theCurrentView);
        listLead.setAdapter(adapter);
        //TODO "Set" the listview to be that list you got back from the bundle.
        return view;
    }

    public void setList(ArrayList<UserScores> allScores) {
        listOfGameScores = new ArrayList<>();
        listOfGameScores.addAll(allScores);
    }

    public void setContext(Context mContext) {
        this.theContext = mContext;
    }
}
