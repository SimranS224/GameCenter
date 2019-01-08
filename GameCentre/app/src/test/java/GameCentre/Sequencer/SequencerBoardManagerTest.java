package fall2018.csc2017.GameCentre.Sequencer;

import org.junit.Test;

import static org.junit.Assert.*;

public class SequencerBoardManagerTest {

    @Test
    public void increaseRound() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertEquals(1L, b.getRound());
        assertEquals((Long) 99L, b.getCurrGameScore());
        b.increaseRound();
        b.increaseRound();
        assertEquals(3L, b.getRound());
        assertEquals((Long) 97L, b.getCurrGameScore());
    }

    @Test
    public void isValidTap() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertFalse(b.isValidTap(20));
    }

    @Test
    public void setGameOver() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertFalse(b.isOver());
        b.setGameOver();
        assertTrue(b.isOver());
    }

    @Test
    public void getSpecificName() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertEquals("Sequencer", b.getSpecificName());
    }
}