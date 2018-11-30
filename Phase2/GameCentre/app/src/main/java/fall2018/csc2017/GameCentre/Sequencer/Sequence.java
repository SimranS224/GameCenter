package fall2018.csc2017.GameCentre.Sequencer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Sequence implements Serializable {
    // Where the sequence is going to be stored.
    private ArrayList<Integer> sequence;
    // The initial Position
    public int position = 0;

    Sequence() {
        sequence = new ArrayList<>();
        Random sequenceNumber = new Random();
        for (int i = 0; i < 100; i++) {
            sequence.add(sequenceNumber.nextInt(SequencerBoardManager.NUM_COLS * SequencerBoardManager.NUM_ROWS));
        }
    }

    /**
     * Gets the value of the sequence at the current position and then gets ready for the next call
     * by increasing the position by 1.
     * @return The value of the sequence at the current position (which button is supposed to be pressed)
     */
    public int get(){
        position += 1;
        return sequence.get(position - 1);
    }

    /**
     * Resets the position back to 0
     */
    void reset() {
        position = 0;
    }

}
