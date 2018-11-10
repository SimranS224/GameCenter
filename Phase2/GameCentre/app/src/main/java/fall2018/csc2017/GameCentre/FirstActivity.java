package fall2018.csc2017.GameCentre;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The Activity where the user can choose to signin or register..
 */
public class FirstActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        addSignInListener();
        addNewUserListener();

        //if no internet connection
        if (!isConnected(FirstActivity.this)) {
            showDialog();
        }

    }


    /**
     * Activate SignIn Button.
     */
    private void addSignInListener() {

        Button mSignIn = findViewById(R.id.signIn);

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, LoginActivityMain.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Activate New User Button.
     */
    private void addNewUserListener() {
        Button mRegister = findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    FirebaseAuth.getInstance().signOut();
                }
                Intent intent2 = new Intent(FirstActivity.this, RegistrationActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }

    /**
     * Checks whether the device has Internet Connection or not.
     * adopted from https://stackoverflow.com/questions/25685755/ask-user-to-connect-to-internet-or-quit-app-android
     *
     * @param context The current context.
     * @return True if the device is connected to the Internet else returns false.
     */
    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected());
    }


    /**
     * Displays a Dialogue Box that asks the user to quit or connect to the Internet via WiFi.
     * adopted from https://stackoverflow.com/questions/25685755/ask-user-to-connect-to-internet-or-quit-app-android
     */
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Connect to wifi or quit")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
