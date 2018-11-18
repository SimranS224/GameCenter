package fall2018.csc2017.GameCentre.TicTacToe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fall2018.csc2017.GameCentre.R;

public class TicTacMainActivity extends AppCompatActivity {
    private Button connect4button;
    /**
     * The board manager.
     */
    private TicTacBoardManager boardManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.tictac_activity_main);

        connect4button = findViewById(R.id.connect4button);
        connect4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }
    public void startGame() {
        Intent intent = new Intent(this, TicTacGameActivity.class);
        startActivity(intent);
    }
}
