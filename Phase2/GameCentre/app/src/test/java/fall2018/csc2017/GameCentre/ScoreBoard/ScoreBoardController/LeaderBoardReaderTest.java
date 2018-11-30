package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LeaderBoardReaderTest {

    private LeaderBoardReader testOne;
    private ArrayList<UserScores> listTemp;

    @Before
    public void setUp() {
        UserScores newOne = new UserScores();
        Scores sample = new Scores("Jeff", "10");
        newOne.add(sample);
        UserScores anotherOne = new UserScores();
        Scores anotherSampleOne = new Scores("Bob", "Dlyan");
        anotherOne.add(anotherSampleOne);
        listTemp.add(anotherOne);
        listTemp.add(newOne);

    }

    /**
     * Tests to make sure the
     */
    @Test
    public void addAllToContents() {
        setUp();
        testOne.addAllToContents(listTemp);
        assertEquals(2, testOne.getSize());

    }

    @Test
    public void getContents() {
    }

    @Test
    public void getSize() {
    }

    @Test
    public void add() {
    }
}