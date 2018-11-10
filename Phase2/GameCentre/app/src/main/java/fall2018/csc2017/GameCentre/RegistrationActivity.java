package fall2018.csc2017.GameCentre;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


/**
 * The actvity where new users can register
 */
public class RegistrationActivity extends AppCompatActivity {

    /**
     * The registration name field
     */
    private EditText mName;

    /**
     * The registration email field
     */
    private EditText mEmail;

    /**
     * The registration password field
     */
    private EditText mPassword;

    /**
     * Firebase Authentication
     */
    private FirebaseAuth mAuth;

    /**
     * Firebase Authentication State Listener
     */
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    /**
     * Minimum Password Length
     */
    private final int MINIMUM_PASSWORD_LENGTH =6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        addFirebaseAuthStateListener();

        mName= findViewById(R.id.name);
        mEmail= findViewById(R.id.email);
        mPassword= findViewById(R.id.password);

        addRegistrationButtonListener();
        addAlreadyAUserButtonListener();

    }

    /**
     * Activate Old User Button
     */
    private void addAlreadyAUserButtonListener() {

        Button mAlreadyAUser = findViewById(R.id.alreadyAUser);
        mAlreadyAUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(RegistrationActivity.this,LoginActivityMain.class);
                startActivity(intent2);
                finish();
            }
        });
    }

    /**
     * Activate Registration Button
     */
    private void addRegistrationButtonListener() {

        Button mRegister = findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();

                //If user leaves email and password box empty or the password is less than 6 characters
                if( TextUtils.isEmpty(mName.getText())|| TextUtils.isEmpty(mEmail.getText()) || TextUtils.isEmpty(mPassword.getText()) || password.length()<MINIMUM_PASSWORD_LENGTH) {


                    if(TextUtils.isEmpty(mName.getText())) {
                        mName.setError("Name is required!");
                    }

                    if(TextUtils.isEmpty(mEmail.getText())) {
                        mEmail.setError("Email is required!");
                    }

                    if(TextUtils.isEmpty(mPassword.getText())) {
                        mPassword.setError("Password is Required!");
                    }

                    if(password.length()<MINIMUM_PASSWORD_LENGTH) {
                        mPassword.setError("Password must be at least 6 characters long!");
                    }

                    Toast.makeText(RegistrationActivity.this, "Sign In Error", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                            } else {

                                String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("userId").child(user_id).child("Name");
                                current_user_db.setValue(name);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Activate Firebase Authentication Check
     */
    private void addFirebaseAuthStateListener() {

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null) {
                    Intent intent = new Intent(RegistrationActivity.this,StartingActivity.class);
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
