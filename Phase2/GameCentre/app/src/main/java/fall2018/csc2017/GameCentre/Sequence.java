package fall2018.csc2017.GameCentre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Sequence implements Serializable {
    private ArrayList<Integer> sequence;
    private Random sequencNumber;
    int position = 0;

    public Sequence() {
        sequence = new ArrayList<>();
        sequencNumber = new Random();
        for (int i = 0; i < 100; i++) {
            sequence.add(sequencNumber.nextInt(SequencerBoard.NUM_COLS * SequencerBoard.NUM_ROWS));
        }
    }
    public int get(){
        position += 1;
        System.out.println("---------------  " + position + "  ----------------------");

        return sequence.get(position - 1);
    }
    public void resetPos() {
        position = 0;
        System.out.println("---------------  " + position + "  ----------------------");
    }
}
