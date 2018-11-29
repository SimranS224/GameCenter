package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.UserScores;

public class LeaderBoardReader {

    private ArrayList<UserScores> contents;

    public LeaderBoardReader(){
        this.contents = new ArrayList<>();
    }
    public void addAllToContents(ArrayList list){
        UserScores curList = new UserScores();
        for (int i=0; i< list.size();i++ ){
            curList.add(list.get(i));
        }
        contents.add(curList);

    }

    public ArrayList<UserScores> getContents(){
        return this.contents;
    }

    public int getSize() {
        return contents.size();
    }

    public void add(UserScores emptyOne) {
        this.contents.add(emptyOne);
    }
}
