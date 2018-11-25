package fall2018.csc2017.GameCentre;

import org.junit.Test;
import fall2018.csc2017.GameCentre.Sequencer.*;
import static org.junit.Assert.assertEquals;

public class SequencerTest {
    // ------------------------- Sequence ----------------------------
    /**
     * Test whether all the elements of the sequence are between the correct bounds (0 and  15)
     */
    @Test
    public void testSequenceBound() {
        Sequence s = new Sequence();
        boolean allBounded = true;
        for (int i = 0; i < 100; i++) {
            int num = s.get();
            System.out.println(num);
            if (num < 0 || num > 15) {
                allBounded = false;
            }
        }
        assertEquals(true, allBounded);
    }
    /**
     * Test whether get() changes the listen position
     */
    @Test
    public void testPosition() {
        Sequence s = new Sequence();
        s.get();
        s.get();
        s.get();
        assertEquals(s.position, 3);
    }
    /**
     * Tests reset()
     */
    @Test
    public void testReset(){
        Sequence s = new Sequence();
        s.get();
        s.get();
        s.get();
        s.reset();
        assertEquals(s.position, 0);
    }
    // -------------------- SequencerBoardManager ----------------------------

    /**
     * Tests both the initial score and the increaseScore method.
     */
    @Test
    public void testIncreaseScore() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertEquals(b.getScore(), (Long) 1L);
        b.increaseScore();
        b.increaseScore();
        assertEquals(b.getScore(), (Long) 3L);
    }
    /**
     * Test the correctness of taps
     */
    @Test
    public void testIsValidTap() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertEquals(false, b.isValidTap(20));
    }
    /**
     * Test isOver and setGameOver
     */
    @Test
    public void testSetGameOver() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertEquals(false, b.isOver());
        b.setGameOver();
        assertEquals(true, b.isOver());
    }
    /**
     * Tests the specified name
     */
    @Test
    public void testSpecificName() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertEquals("Sequencer", b.getSpecificName());
    }
    // ---------------------- SequencerGameActivity -------------------------

}
