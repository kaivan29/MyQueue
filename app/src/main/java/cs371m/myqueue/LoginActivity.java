package cs371m.myqueue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erinjensby on 5/1/17.
 */

public class LoginActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final String TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // app has never been opened
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences
                (getBaseContext());
        boolean previouslyStarted = sharedPrefs.getBoolean
                (getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = sharedPrefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), true);
            edit.commit();
            startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
        }

        // device already logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            restoreUserSources();
            restoreUserQueue();
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            LoginActivity.this.finish();
        }

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.login_layout);

        // login button onClick
        final Button login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText email = (EditText) findViewById(R.id.editEmail);
                EditText password = (EditText) findViewById(R.id.editPassword);
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Enter a valid username or password", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (login_button.getText().equals("CREATE ACCOUNT")) {
                        createAccount(email.getText().toString().trim(), password.getText().toString().
                                trim());
                    } else {
                        signIn(email.getText().toString().trim(), password.getText().toString().trim());
                    }
                }
            }
        });

        // switch mode button onClick
        final Button switch_mode_button = (Button) findViewById(R.id.switch_mode);
        switch_mode_button.setPaintFlags(switch_mode_button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        switch_mode_button.setOnClickListener(new View.OnClickListener() {
            EditText password = (EditText) findViewById(R.id.editPassword);
            public void onClick(View v) {
                if (switch_mode_button.getText().toString().equals(getString
                        (R.string.have_account))) {
                    switch_mode_button.setText(getString(R.string.new_user));
                    login_button.setText("LOGIN");
                    password.setHint("Password");
                } else {
                    switch_mode_button.setText(getString(R.string.have_account));
                    login_button.setText("CREATE ACCOUNT");
                    password.setHint("Password (must be at least 6 characters)");
                }
            }
        });

        // authListener for firebase login
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };



    }

    @Override
    public void onBackPressed() {}

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" +
                                task.isSuccessful());
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this,
                                    SelectSourcesActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);

                            // create default user service choices
                            SharedPreferences sharedPrefs = PreferenceManager.
                                    getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor edit = sharedPrefs.edit();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance()
                                    .getReference();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();
                            mDatabase.child("users").child(userId).child("services").child
                                    (getString(R.string.netflix_source)).setValue(true);
                            edit.putBoolean(getString(R.string.netflix_source), true);
                            mDatabase.child("users").child(userId).child("services").child
                                    (getString(R.string.hulu_source)).setValue(false);
                            edit.putBoolean(getString(R.string.hulu_source), false);
                            mDatabase.child("users").child(userId).child("services").child
                                    (getString(R.string.hbo_source)).setValue(false);
                            edit.putBoolean(getString(R.string.hbo_source), false);
                            mDatabase.child("users").child(userId).child("services").child
                                    (getString(R.string.amazon_source)).setValue(false);
                            edit.putBoolean(getString(R.string.amazon_source), false);
                            edit.commit();
                            LoginActivity.this.finish();
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.create_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            // pull user info from firebase
                            restoreUserSources();
                            restoreUserQueue();

                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("LoginActivity", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void restoreUserSources() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        String userId = user.getUid();

        mDatabase.child("users").child(userId).child("services").addListenerForSingleValueEvent
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        SharedPreferences sharedPrefs = PreferenceManager.
                                getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor edit = sharedPrefs.edit();

                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            switch (itemSnapshot.getKey()) {
                                case "hulu":
                                    Log.d(TAG, "adding hulu to sharedPrefs");
                                    edit.putBoolean(getString(R.string.hulu_source),
                                            (Boolean) itemSnapshot.getValue());
                                    edit.commit();
                                    break;
                                case "netflix":
                                    Log.d(TAG, "adding netflix to sharedPrefs");
                                    edit.putBoolean(getString(R.string.netflix_source),
                                            (Boolean) itemSnapshot.getValue());
                                    edit.commit();
                                    break;
                                case "hbo":
                                    Log.d(TAG, "adding hbo to sharedPrefs");
                                    edit.putBoolean(getString(R.string.hbo_source),
                                            (Boolean) itemSnapshot.getValue());
                                    edit.commit();
                                    break;
                                case "amazon_prime":
                                    Log.d(TAG, "adding amazon to sharedPrefs");
                                    edit.putBoolean(getString(R.string.amazon_source),
                                            (Boolean) itemSnapshot.getValue());
                                    edit.commit();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void restoreUserQueue() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = user.getUid();

        mDatabase.child("users").child(userId).child("queue").addListenerForSingleValueEvent
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, dataSnapshot.toString());
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            List<String> movie = new ArrayList<String>();
                            for (DataSnapshot detailSnapshot : itemSnapshot.getChildren()) {
                                movie.add(detailSnapshot.getValue().toString());
                            }
                            Queue.get().addMovie(Long.parseLong(itemSnapshot.getKey()), movie.get(0),
                                    movie.get(1));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}