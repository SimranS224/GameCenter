package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
/*
Model/View code
 */

/**
 * This is an individual page in a sense. Every time a new page is swiped to, a new
 * Fragment is created.
 */
public class DemoFragment extends Fragment {
    /**
     * The list of all the highscores for every page of the swipeView.
     * The index we get back from the attached bundle is the position
     * of the list to display for the current fragment.
     */
    public ArrayList<UserScores> listOfGameScores;

    /**
     * Context used for the customListAdapter.
     */
    private Context theContext;

    /**
     * This a required constructor for all fragments in a swipeView.
     */
    public DemoFragment() {
        // Empty constructor
    }


    @Override
    //This is the method that is run every time we create (or refresh)
    //a fragment. We change the textViews/listViews depending on which
    //position we are at in the swipeViewAdapter.
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //The view, and all the textViews/listLeads that are changed
        //corresponding to a particular fragment.
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        TextView textView1 = view.findViewById(R.id.GlobalRanktext);
        TextView textView2 = view.findViewById(R.id.globalRanktext);
        ListView listLead = view.findViewById(R.id.listLead);

        assert getArguments() != null; //We specify the bundle in every case.
        String type = getArguments().getString("type");
        String size = getArguments().getString("size");
        String curUser = getArguments().getString("currentUser");
        String scoreType = getArguments().getString("publicorglobal");
        Integer index = getArguments().getInt("index");

        if (scoreType == null) {
            scoreType = "";
        }

        //Set the textViews corresponding to the information found in the
        //bundle.
        textView1.setText(type);
        textView2.setText(size);

        //This code is what determines how many highScores to show
        //on the listView. A scoreType of "p" means only the users highest
        //personal score is shown, scoreType of "g" means all of the the
        //global highSCores.
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

    /**
     * A simple setter of the list of the currentFragment, occurs when we
     * get our data back form the datasnapshot.
     */
    public void setList(ArrayList<UserScores> allScores) {

        listOfGameScores = new ArrayList<>();
        if (allScores != null) {
            listOfGameScores.addAll(allScores);
        }
    }

    /**
     * Setter for the context corresponding to the activity.
     */
    public void setContext(Context mContext) {
        this.theContext = mContext;
    }
}
