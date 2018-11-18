package fall2018.csc2017.GameCentre.TicTacToe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import fall2018.csc2017.GameCentre.R;

public class TicTacMainActivity extends AppCompatActivity {
    private Button connect4button;
    private Button randomAIbutton;
    private Button minimaxAIbutton;
    /**
     * The board manager.
     */
    private TicTacBoardManager boardManager;
    public static int PLAYER_TO_PLAYER = 0;
    public static int PLAYER_TO_RANDOM = 1;
    public static int PLAYER_TO_AI = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tictac_activity_main);

        //Player vs Player button
        connect4button = findViewById(R.id.connect4button);
        connect4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(PLAYER_TO_PLAYER, 0);
            }
        });

        //RandomAIButton

        randomAIbutton = findViewById(R.id.randomAI_button);
        randomAIbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(PLAYER_TO_RANDOM, 0);
            }
        });

        //MinimaxAIButton
        minimaxAIbutton = findViewById(R.id.minimaxAI_button);
        minimaxAIbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(PLAYER_TO_AI, 4);
            }
        });
    }

    public void startGame(int gametype, int depth) {

        Intent intent = new Intent(this, TicTacGameActivity.class);
        Bundle b = new Bundle();
        b.putInt("gametype", gametype); //Your id
        b.putInt("depth", depth); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }
}
