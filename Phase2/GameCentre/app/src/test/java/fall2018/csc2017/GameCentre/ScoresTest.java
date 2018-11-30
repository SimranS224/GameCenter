package fall2018.csc2017.GameCentre;

import org.junit.Before;
import org.junit.Test;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.Scores;

import static org.junit.Assert.assertEquals;

public class ScoresTest {
    /**
     * A Score object used for testing.
     */
    private Scores score;

    /**
     * Creates a new Score object.
     */
    @Before
    public void setUp() {
        score = new Scores("Frank", "1");
    }

    /**
     * Tests to make sure the correct score is returned.
     */
    @Test
    public void testGetScore() {
        setUp();
        String scoretest;
        scoretest = score.getScore();
        assertEquals("1", scoretest);
    }

    /**
     * Tests to make sure the correct name is returned.
     */
    @Test
    public void testGetName() {
        setUp();
        String nametest;
        nametest = score.getName();
        assertEquals("Frank", nametest);
    }

    /**
     * Tests to make sure the int value is correctly returned.
     */
    @Test
    public void testGetIntValue() {
        setUp();
        int scoretest;
        scoretest = score.getIntValue();
        assertEquals(1, scoretest);
    }

    /**
     * Tests to ensure the name is correctly set.
     */
    @Test
    public void testSetName() {
        Scores newscore = new Scores();
        newscore.setName("Frank");
        assertEquals("Frank", newscore.getName());
    }

    /**
     * Tests to make sure the name is correctly set.
     */
    @Test
    public void testSetScore() {
        Scores newscore = new Scores();
        newscore.setScore("1");
        assertEquals("1", newscore.getScore());
    }

    /**
     * Tests to make sure Scores are compared correctly.
     */
    @Test
    public void testCompareTo() {
        Scores compare = new Scores("Me", "0");
        assertEquals(1, score.compareTo(compare));
    }

    /**
     * Tests to make sure the string value of a Score is in the correct format.
     */
    @Test
    public void testToString() {
        assertEquals("Frank, 1", score.toString());
    }
}