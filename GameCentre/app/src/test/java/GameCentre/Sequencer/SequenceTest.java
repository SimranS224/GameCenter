package fall2018.csc2017.GameCentre.Sequencer;

import org.junit.Test;
import fall2018.csc2017.GameCentre.Sequencer.*;
import static org.junit.Assert.assertEquals;

public class SequenceTest {
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

}
