package fall2018.csc2017.GameCentre;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.LeaderBoardController;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.Scores;

import static org.junit.Assert.assertEquals;

public class LeaderBoardControllerTest {


    /**
     * The leaderboardcontroller
     */
    private LeaderBoardController newOne;


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        this.newOne = null;
    }

    @Test
    public void setPlayerName() {
        LeaderBoardController newOne = new LeaderBoardController();
        newOne.setPlayerName("TESTNAME");
        assertEquals(newOne.getPLayerName(), (String) "TESTNAME");
    }

    @Test
    public void clear() {
        LeaderBoardController newOne = new LeaderBoardController();
        Scores testOne = new Scores();
        testOne.setScore("4");
        testOne.setName("TESTNAME");
        newOne.add(testOne);
        assertEquals(newOne.isEmpty(),  (boolean) false);
        newOne.clear();
        assertEquals(newOne.isEmpty(), (boolean) true);

    }

    @Test
    public void getDataChange() {
        LeaderBoardController newOne = new LeaderBoardController();
        newOne.setDataChange(false);
        assertEquals(newOne.getDataChange(),  (boolean) false);
    }

    @Test
    public void setDataChange() {
        LeaderBoardController newOne = new LeaderBoardController();
        newOne.setDataChange(true);
        assertEquals(newOne.getDataChange(),  (boolean) true);
    }

    @Test
    public void add() {
        LeaderBoardController newOne = new LeaderBoardController();
        Scores testOne = new Scores();
        testOne.setScore("4");
        testOne.setName("TESTNAME");
        newOne.add(testOne);
        assertEquals(newOne.isEmpty(),  (boolean) false);
    }

    @Test
    public void setBoard() {
    }

    @Test
    public void getName() {
    }

    @Test
    public void setFromFireBaseList() {
    }

    @Test
    public void getWriteData() {
    }

    @Test
    public void updateScores() {
    }
}
