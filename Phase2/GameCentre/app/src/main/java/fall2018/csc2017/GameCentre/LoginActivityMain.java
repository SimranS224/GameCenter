package fall2018.csc2017.GameCentre;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The Activity where the user signs in.
 */
public class LoginActivityMain extends AppCompatActivity {

    /**
     * The Text Field for the email and password of the user
     */
    private EditText mEmail, mPassword;

    /**
     * Firebase Authentication
     */
    private FirebaseAuth mAuth;

    /**
     * Firebase Authentication State Listener
     */
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addFirebaseAuthStateListener();

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        addNewUserListener();
        addLoginButtonListener();


    }


    /**
     * Activate Login Button
     */
    private void addLoginButtonListener() {


        Button mLogin = findViewById(R.id.login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();


                //If user leaves email and password box empty
                if (TextUtils.isEmpty(mEmail.getText()) || TextUtils.isEmpty(mPassword.getText())) {


                    if (TextUtils.isEmpty(mEmail.getText())) {
                        mEmail.setError("Email is required!");
                    }

                    if (TextUtils.isEmpty(mPassword.getText())) {
                        mPassword.setError("Password is Required!");
                    }

                    Toast.makeText(LoginActivityMain.this, "Sign In Error", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivityMain.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivityMain.this, "Sign In Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Activate New User Button
     */
    private void addNewUserListener() {
        Button mRegistration = findViewById(R.id.registration);

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivityMain.this, RegistrationActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }

    /**
     * Activate Firebase Authorisation Check
     */
    private void addFirebaseAuthStateListener() {

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivityMain.this, GameChoiceActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}