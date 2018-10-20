package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginPage extends AppCompatActivity {

    TextView customFontTextView;

    Typeface typeface;

    private FirebaseAuth mAuth;


    private TextView password;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_log_in_page);
        Button signUpButton = findViewById(R.id.signupbutton);
        signUpButton.setPaintFlags(signUpButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signUpButton.setOnClickListener(view -> openActivitySignUpPage());
        password = findViewById(R.id.passwordField);
        email = findViewById(R.id.emailField);
        ImageButton loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            String emailString = email.getText().toString();
            String passwordString = password.getText().toString();
            login(emailString, passwordString);
        });

        typeface = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        customFontTextView = findViewById(R.id.logoCoinz);
        customFontTextView.setTypeface(typeface);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).hide();


    }

    private void openActivitySignUpPage() {
        Intent intent = new Intent(this,SignUpPage.class);
        startActivity(intent);
    }

    private void openMap() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @SuppressLint("LogNotTimber")
    private void login(String email, String password) {
        if(email.isEmpty()) {
            Toast.makeText(LoginPage.this, "Enter an email.",
                    Toast.LENGTH_SHORT).show();
        } else if(password.isEmpty()) {
            Toast.makeText(LoginPage.this, "Enter a password.",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TESTING", "signInWithEmail:success");
                            openMap();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TESTING", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            openMap();
        }
    }
}
