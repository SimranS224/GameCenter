package fall2018.csc2017.GameCentre.Sequencer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class Sequence implements Serializable {
    private ArrayList<Integer> sequence;
    private Random sequencNumber;
    int speakPos = 0;
    int listenPos = 0;
    int round = 1;

    public Sequence() {
        sequence = new ArrayList<>();
        sequencNumber = new Random();
        for (int i = 0; i < 100; i++) {
            sequence.add(sequencNumber.nextInt(SequencerBoardManager.NUM_COLS * SequencerBoardManager.NUM_ROWS));
        }
    }
    public int speakGet(){
        speakPos += 1;
        System.out.println("---------------  Speak Position: " + speakPos + "  ----------------------");
        return sequence.get(speakPos - 1);
    }
    public int listenGet(){
        listenPos += 1;
        System.out.println("---------------  Listen Position: " + listenPos + "  ----------------------");
        return sequence.get(listenPos - 1);
    }
    public void resetSpeak() {
        speakPos = 0;
    }
    public void resetListen() {
        listenPos = 0;
    }

}
