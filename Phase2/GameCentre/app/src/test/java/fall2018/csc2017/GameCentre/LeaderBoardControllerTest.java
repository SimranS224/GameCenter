package fall2018.csc2017.GameCentre;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.LeaderBoardController;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.Scores;
import fall2018.csc2017.GameCentre.ScoreBoard.ScoreBoardController.UserScores;
import fall2018.csc2017.GameCentre.Sequencer.SequencerBoardManager;


import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class LeaderBoardControllerTest {


    /**
     * The leaderboardcontroller
     */
    private LeaderBoardController newOne;
    /**
     * A sample testScore
     */
    private Scores testOne;

    /**
     * Creates a LeaderBoardController and Score object instance.
     */
    @Before
    public void setUp() {
        newOne = new LeaderBoardController();
        testOne = new Scores("Bob", "10");


    }

    /**
     * Tests to make sure the the global LeaderBoardController and Score object are set to null.
     */
    @After
    public void tearDown() {
        this.newOne = null;
        this.testOne = null;
    }

    /**
     * Tests to make sure setting the name of the current player works as desired.
     */
    @Test
    public void testsetPlayerName() {
        newOne.setPlayerName("TESTNAME");
        assertEquals("TESTNAME", newOne.getPLayerName());
    }

    /**
     * Tests to make sure all elements from LeaderBoardControllers internal storage are removed.
     */
    @Test
    public void testClear() {
        testOne.setScore("4");
        testOne.setName("TESTNAME");
        newOne.add(testOne);
        assertFalse(newOne.isEmpty());
        newOne.clear();
        assertTrue(newOne.isEmpty());

    }

    /**
     * Gets the data change variable, ensures the variable is set properly when called.
     */
    @Test
    public void testGetDataChange() {
        newOne.setDataChange(false);
        assertFalse(newOne.getDataChange());
    }

    /**
     * Tests to make sure the data change variable is set, ensures the variable is set properly when called.
     */
    @Test
    public void testSetDataChange() {
        newOne.setDataChange(true);
        assertTrue(newOne.getDataChange());
    }

    /**
     * Tests to make sure LeaderBoardController adds a new Score to its local storager.
     */
    @Test
    public void testAdd() {
        tearDown();
        setUp();
        testOne.setScore("4");
        testOne.setName("TESTNAME");
        newOne.add(testOne);
        assertFalse(newOne.isEmpty());
    }

    /**
     * Sets the boardManger so that the name can be referenced.
     */
    @Test
    public void testSetBoard() {
        setUp();
        SequencerBoardManager newBoard = new SequencerBoardManager();
        newOne.setBoard(newBoard);
        assertNotNull(newOne.getName());
    }

    /**
     * Tests to make sure after setting the boardManger to the correct one that the name is correct.
     */
    @Test
    public void testGetName() {
        SequencerBoardManager newBoard = new SequencerBoardManager();
        newOne.setBoard(newBoard);
        assertEquals("Sequencer", newOne.getName());
    }

    /**
     * Tests to make sure an input list is stored properly in  the internal storage of LeaderBoardController.
     */
    @Test
    public void testSetFromFireBaseList() {
        UserScores listOfUserScores = new UserScores();
        Scores theNewScore = new Scores("Jeff", "10");
        listOfUserScores.add(theNewScore);
        ArrayList<Object> temp = new ArrayList<>();
        temp.add(listOfUserScores);
        newOne.setFromFireBaseList(temp);
        assertEquals(1, newOne.getWriteData().size());
    }

    /**
     * Tests to make sure the contents of LeaderBoardController to write to firebase are correct.
     */
    @Test
    public void testGetWriteData() {
        assertEquals(0, newOne.getWriteData().size());
        newOne.add(testOne);
        assertEquals(1, newOne.getWriteData().size());

    }

    /**
     * Tests to make sure when the internal storage of LeaderBoardController is updated that
     * it correctly updates.
     */
    @Test
    public void testUpdateScores() {
        SequencerBoardManager newBoard = new SequencerBoardManager();
        newOne.updateScores(newBoard);
        assertEquals(1, newOne.getWriteData().size());

    }
}
