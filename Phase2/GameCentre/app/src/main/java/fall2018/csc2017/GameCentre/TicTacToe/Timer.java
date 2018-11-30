package fall2018.csc2017.GameCentre.TicTacToe;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

public class Timer {
    /**
     * the start time in millis
     */
    private static final long START_TIME_IN_MILLIS = 100000;

    public static TextView mTextViewCountDown;
    public static CountDownTimer mCountDownTimer;
    public static boolean mTimerRunning;

    public static long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    public static void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();
        mTimerRunning = true;
    }

    public static void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    public static void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }

    public static void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    public static long getmTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    public static boolean getmTimerRunning() {
        return mTimerRunning;
    }
}
