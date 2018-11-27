package fall2018.csc2017.GameCentre;


import java.util.ArrayList;

public class LeaderBoardController {


    /**
     * The board
     */
    private BoardManager boardManager;

    /**
     * List of User/scores
     */
    private UserScores listOfScores = new UserScores();


    /**
     * Boolean representing if any data changed
     */
    private boolean dataChange = false;

    /**
     * The current player
     */
    private String currPlayerName;


    LeaderBoardController() {
    }

    public void setPlayerName(String newPlayer) { // TODO can use this to set the name Abdullah
        this.currPlayerName = newPlayer;
    }

    public String getPLayerName(){
        return this.currPlayerName;
    }

    /**
     * remove all the elemens from the listOfScores
     */
    public void clear() {
        listOfScores.empty();
    }

    public boolean getDataChange() {
        return this.dataChange;
    }

    public void setDataChange(boolean newVal) {
        this.dataChange = newVal;
    }

    public void add(Scores score) {
        listOfScores.add(score);
    }

    public void setBoard(BoardManager boardManger) {
        this.boardManager = boardManger;
//        Log.d("in setboardtheboard is udated", "listofarrays=============== " + listOfScores.array);
//        updateScores(boardManager);
    }

    public String getName() {
        return boardManager.getSpecificName();
    }

    public void setFromFireBaseList(ArrayList<Object> list) {
//        Log.d("insetfromfirebaselistthe before list", "listofarrays=============== " + listOfScores.array);
//        Log.d("oneelemeentbeofre list", "3x3=============== " + listOfScores.array.get(0).getScore());
        //listOfScores.array.clear();
        for (int i = 0; i < list.size(); i++) {

            // if (!listOfScores.contains((list.get(i)))){
            listOfScores.add(list.get(i));
            //}
        }


    }

    public ArrayList<Scores> getWriteData() {
//        Log.d("in getwritedata ", "listofarrays=============== " + listOfScores.array);

        return listOfScores.getArray();
    }

    public void updateScores(BoardManager boardManager) {
//        Log.d("inupdateScores", "listofarrays=============== " + listOfScores.array);

        //LeaderBoardController leaderBoardController = new LeaderBoardController(boardManager);

        Long winningScore = boardManager.getScore();
        Long worstCur = null;
        addWinningScore(winningScore);

    }


    private void addWinningScore(Long score) {
//            Log.d("in addwinningscore is udated", "listofarrays=============== " + listOfScores.array);

        String theScore = score.toString();
        String name = this.getPLayerName();
        Scores userScore = new Scores();
        userScore.setName(name);
        userScore.setScore(theScore);
//            Log.d("in addwinningscore calling add score", "3x3===============userscorename "+ userScore.getName());
//            Log.d("in addwinningscore calling add score", "3x3===============userSCORE"+ userScore.getScore());
        if (!listOfScores.contains(userScore)) {
            this.listOfScores.add(userScore);
        } else {
            this.listOfScores.addLowerScore(userScore);
        }
        // remove and add higherscore if higher /TODO

    }


    public boolean isEmpty() {
        return this.listOfScores.size() == 0;
    }
}
