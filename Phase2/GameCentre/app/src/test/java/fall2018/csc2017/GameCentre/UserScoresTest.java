package fall2018.csc2017.GameCentre;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.Scores;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.UserScores;

import static org.junit.Assert.assertEquals;

public class UserScoresTest {
    private ArrayList<Scores> array;
    private UserScores userscores;
    private Scores score = new Scores("frank", "0");
    @Before
    public void setUp() {
        userscores = new UserScores();
        array = new ArrayList<>();
        userscores.add(score);
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
        assertEquals(true, (boolean) userscores.contains("frank"));
        assertEquals(false, userscores.contains("fr"));
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

    }

    @Test
    public void testIsEmpty() {
        setUp();
        assertEquals(false, userscores.isEmpty());
        //TODO add a false scenario

    }

    @Test
    public void addLowerScore() {
    }

    @Test
    public void testEmpty() {
        setUp();
        userscores.empty();
        assertEquals(true, userscores.size() == 0);
    }

    @Test
    public void getUser() {
    }
}