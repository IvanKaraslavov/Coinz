package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView password;
    private TextView email;
    private TextView passwordConfirm;
    private int coinsLeft = 25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up_page);
        password = findViewById(R.id.passwordField);
        email = findViewById(R.id.emailField);
        passwordConfirm = findViewById(R.id.passwordconfirm);
        ImageButton signUpButton = findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(view -> {
            if(password.getText().toString().equals(passwordConfirm.getText().toString())) {
                signUp(email.getText().toString(), password.getText().toString());
            } else {
                Toast.makeText(SignUpPage.this, "Passwords do not match.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        Button logInButton = findViewById(R.id.loginbutton);
        logInButton.setPaintFlags(logInButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        logInButton.setOnClickListener(view -> openLoginPage());

    }

    private void openLoginPage() {
        Intent intent = new Intent(this,LoginPage.class);
        startActivity(intent);
        finish();
    }

    private void openCreateProfile() {
        Intent intent = new Intent(this,CreateProfilePage.class);
        startActivity(intent);
        finish();
    }

    //Create an account

    @SuppressLint("LogNotTimber")
    private void signUp(String email, String password) {
        if(email.isEmpty()) {
            Toast.makeText(SignUpPage.this, "Enter an email.",
                    Toast.LENGTH_SHORT).show();
        } else if(password.isEmpty()) {
            Toast.makeText(SignUpPage.this, "Enter a password.",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user.
                            Log.w("TESTING", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpPage.this, "Authentication failed." + Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TESTING", "createUserWithEmail:success");
                            Toast.makeText(SignUpPage.this, "Successfully created an account!",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
                            mAuth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            assert currentUser != null;
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .set(new Player(email, password,0, coinsLeft));
                            openCreateProfile();
                        }
                    });
        }

    }
}
