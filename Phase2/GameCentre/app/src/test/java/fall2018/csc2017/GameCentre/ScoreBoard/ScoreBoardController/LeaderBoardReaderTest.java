package fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.LeaderBoardReader;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.Scores;

import static org.junit.Assert.*;

/**
 * The LeaderBoardReader Test Class
 */
public class LeaderBoardReaderTest {

    /**
     * A LeaderBoardReader for testing.
     */
    private LeaderBoardReader testOne;
    /**
     * An temporary arraylist of UserScores (i.e highscores)
     */
    private ArrayList<Scores> listTemp;

    /**
     * Creates a LeaderBoardReader, and temporary list.
     */
    @Before
    public void setUp() {
        testOne = new LeaderBoardReader();
        Scores sample = new Scores("Jeff", "10");
        Scores anotherSampleOne = new Scores("Bob", "10");
        listTemp = new ArrayList<>();
        listTemp.add(sample);
        listTemp.add(anotherSampleOne);

    }

    /**
     * Tests to make sure the content is updated.
     */
    @Test
    public void addAllToContents() {
        setUp();
        testOne.addAllToContents(listTemp);
        System.out.println(testOne.getContents());
        assertEquals(1, testOne.getSize());

    }

    /**
     * Test to make sure the contents of LeaderBoardReader are returned as desired.
     */
    @Test
    public void getContents() {
        setUp();
        testOne.addAllToContents(listTemp);
        assertNotNull(testOne.getContents());

    }

    /**
     * Test to make sure the size of LeaderBoardReader is correct.
     */
    @Test
    public void getSize() {
        setUp();
        testOne.addAllToContents(listTemp);
        assertEquals(1, testOne.getSize());
    }

    /**
     * Tests to make sure UserScore object can be added to the contents of LeaderBoardReader
     */
    @Test
    public void add() {
        setUp();
        testOne.addAllToContents(listTemp);
        assertEquals(1, testOne.getSize());
    }
}