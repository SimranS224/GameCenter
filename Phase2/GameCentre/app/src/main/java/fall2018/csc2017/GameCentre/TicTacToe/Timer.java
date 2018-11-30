package fall2018.csc2017.GameCentre.TicTacToe;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

/**
 * The class for timer adapted from https://www.youtube.com/watch?v=zmjfAcnosS0&feature=share&fbclid=IwAR1glno3C3do99FweI38bzS6Opi_6-tzzSE1VmeYUcFthzEqInj2f5E0wrQ
 */
public class Timer {
    /**
     * the start time in millis
     */
    private static final long START_TIME_IN_MILLIS = 100000;

    /**
     * Textview for keeping track of text in xml
     */
    static TextView mTextViewCountDown;

    /**
     * Countdown timer
     */
    static CountDownTimer mCountDownTimer;

    /**
     * Variable to see if timer is running
     */
    static boolean mTimerRunning;

    /**
     * Time left in millis
     */
    static long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    /**
     * Starts timer
     */
    static void startTimer() {
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

    /**
     * pauses timer
     */
    static void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    /**
     * resets timer
     */
    static void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }

    /**
     * updates text as timer goes down
     */
    static void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    /**
     * Gets time left in millis
     * @return the time left on the timer in millis
     */
    static long getmTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    /**
     * get if the timer is still running
     * @return if the timer is running
     */
    static boolean getmTimerRunning() {
        return mTimerRunning;
    }
}
