package fall2018.csc2017.GameCentre;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Adopted from https://abhiandroid.com/programming/splashscreen also activity_splash.xml adopted from the same source
 * The splash screen when the application is launched
 */
public class SplashActivity extends AppCompatActivity {


    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,FirstActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
