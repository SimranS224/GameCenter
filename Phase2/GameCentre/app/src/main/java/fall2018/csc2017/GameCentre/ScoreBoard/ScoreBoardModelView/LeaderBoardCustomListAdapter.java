package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardModelView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.Scores;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.UserScores;

/*
Model/View code
 */

/**
 * The List Adapter for the global scores.
 * Adopted From
 * https://stackoverflow.com/questions/34518421/adding-a-scoreboard-to-an-android-studio-
 * application/34519967?fbclid=IwAR2OB9SQi4GB9xzmL0MM3t5uq8OWr5Nuc1krltWhFJIh-upg2LM4Zm6lX5s#34519967
 */
public class LeaderBoardCustomListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private UserScores itemsItems;


    /**
     * Adapter for leaderboard
     *
     * @param context    the context
     * @param itemsItems user scores
     */
    LeaderBoardCustomListAdapter(Context context, UserScores itemsItems) {
        this.mContext = context;
        this.itemsItems = itemsItems;

    }

    @Override
    public int getCount() {
        return itemsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return itemsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View scoreView, ViewGroup parent) {
        ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (scoreView == null) {

            assert inflater != null;
            scoreView = inflater.inflate(R.layout.activity_rows, parent, false);
            holder = new ViewHolder();
            holder.name = scoreView.findViewById(R.id.name);
            holder.score = scoreView.findViewById(R.id.score);

            scoreView.setTag(holder);

        } else {
            holder = (ViewHolder) scoreView.getTag();
        }

        final Scores m = itemsItems.get(position);
        holder.name.setText(m.getName());
        holder.score.setText(m.getScore());

        return scoreView;
    }

    /**
     * Holder for views
     */
    static class ViewHolder {

        TextView name;
        TextView score;

    }

}