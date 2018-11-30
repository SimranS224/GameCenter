package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.Scores;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.UserScores;

/**
 * This is an individual page in a sense. Every time a new page is swiped to, a new
 * DemoFragement is created.
 */
public class DemoFragment extends Fragment {

    public ArrayList<UserScores> listOfGameScores;
    private Context theContext;

    public DemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        TextView textView1 = view.findViewById(R.id.GlobalRanktext);
        TextView textView2 = view.findViewById(R.id.globalRanktext);
        ListView listLead = view.findViewById(R.id.listLead);
        String type = getArguments().getString("type");
        String size = getArguments().getString("size");
        String curUser = getArguments().getString("currentUser");
        String scoreType = getArguments().getString("publicorglobal");
        if (scoreType == null) {
            scoreType = "";
        }
        Integer index = getArguments().getInt("index");

        //TODO Get your list/array lists back from the bundle.

        textView1.setText(type);
        textView2.setText(size);


        if (listOfGameScores.size() != 0) {
            UserScores theCurrentView = listOfGameScores.get(index);


            LeaderBoardCustomListAdapter adapter;
            if (scoreType.equals("p")) {

                UserScores onlyOne = new UserScores();
                Scores bestOne = theCurrentView.getUser(curUser);
                onlyOne.add(bestOne);
                adapter = new LeaderBoardCustomListAdapter(theContext, onlyOne);
            } else {
                adapter = new LeaderBoardCustomListAdapter(theContext, theCurrentView);
            }


            listLead.setAdapter(adapter);

        }



        return view;
    }

    public void setList(ArrayList<UserScores> allScores) {

        listOfGameScores = new ArrayList<>();
        if (allScores != null) {
            listOfGameScores.addAll(allScores);
        }
    }

    public void setContext(Context mContext) {
        this.theContext = mContext;
    }
}
