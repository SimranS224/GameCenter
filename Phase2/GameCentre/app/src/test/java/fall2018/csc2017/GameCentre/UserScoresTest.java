package fall2018.csc2017.GameCentre;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.Scores;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.UserScores;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class UserScoresTest {
    private ArrayList<Scores> array;
    private UserScores userscores;
    private Scores score = new Scores("frank", "10");

    @Before
    public void setUp() {
        userscores = new UserScores();
        array = new ArrayList<>();
        userscores.add(score);
    }

    @After
    public void tearDown() {
        userscores.empty();
    }

    @Test
    public void testSetArray() {
        setUp();
        userscores.setArray(array);
        //  assertEquals(, userscores.getArray());
    }

    @Test
    public void getArray() {

    }

    @Test
    public void testContains() {
        setUp();
        assertTrue(userscores.contains(score));
        assertFalse(userscores.contains("fr"));
    }

    @Test
    public void size() {
        setUp();
        assertEquals(1, userscores.array.size());
    }

    @Test
    public void get() {

    }

    @Test
    public void add() {
        assertEquals(1, userscores.size());
        Scores newOne = new Scores("Joe", "12");
        userscores.add(newOne);
        assertEquals(2, userscores.size());

    }

    @Test
    public void testIsEmpty() {
        setUp();
        assertFalse(userscores.isEmpty());
    }

    @Test
    public void addLowerScore() {
        tearDown();
        setUp();
        Scores newScore = new Scores("frank", "0");
        userscores.addLowerScore(newScore);
        assertEquals(1, userscores.size());
    }

    @Test
    public void testEmpty() {
        setUp();
        userscores.empty();
        assertEquals(0, userscores.size());
    }

    @Test
    public void getUser() {
        tearDown();
        setUp();
        assertEquals(score.getName(), userscores.getUser("frank").getName());
        assertEquals(score.getScore(), userscores.getUser("frank").getScore());


    }
}