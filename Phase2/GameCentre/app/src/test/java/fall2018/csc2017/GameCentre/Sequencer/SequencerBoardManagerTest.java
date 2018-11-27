package fall2018.csc2017.GameCentre.Sequencer;

import org.junit.Test;

import static org.junit.Assert.*;

public class SequencerBoardManagerTest {

    @Test
    public void increaseScore() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertEquals(b.getScore(), (Long) 1L);
        b.increaseScore();
        b.increaseScore();
        assertEquals(b.getScore(), (Long) 3L);
    }

    @Test
    public void isValidTap() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertEquals(false, b.isValidTap(20));
    }

    @Test
    public void setGameOver() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertEquals(false, b.isOver());
        b.setGameOver();
        assertEquals(true, b.isOver());
    }

    @Test
    public void getSpecificName() {
        SequencerBoardManager b = new SequencerBoardManager();
        assertEquals("Sequencer", b.getSpecificName());
    }
}