package fall2018.csc2017.GameCentre;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.Scores;

import static org.junit.Assert.assertEquals;

public class ScoresTest {

    Scores score;

    @Before
    public void setUp() {
        score = new Scores("Frank","1");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetScore() {
        setUp();
        String scoretest;
        scoretest = score.getScore();
        assertEquals("1", scoretest);
    }

    @Test
    public void testGetName() {
        setUp();
        String nametest;
        nametest = score.getName();
        assertEquals("Frank", nametest);
    }

    @Test
    public void getIntValue() {
        setUp();
        int scoretest;
        scoretest = score.getIntValue();
        assertEquals(1, scoretest);
    }

    @Test
    public void setName() {
        Scores newscore = new Scores();
        newscore.setName("Frank");
        assertEquals("Frank", newscore.getName());
    }

    @Test
    public void setScore() {
        Scores newscore = new Scores();
        newscore.setScore("1");
        assertEquals("1", newscore.getScore());
    }

    @Test
    public void testCompareTo() {
        Scores compare = new Scores("Me", "0");
        assertEquals(1, score.compareTo(compare));
    }

    @Test
    public void testToString() {
        assertEquals("Frank, 1", score.toString());
    }
}