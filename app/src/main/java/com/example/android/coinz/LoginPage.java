package com.example.android.coinz;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity {

    TextView customFontTextView;

    Typeface typeface;

    private Button signUpButton;
    private ImageButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);
        signUpButton = findViewById(R.id.signupbutton);
        signUpButton.setPaintFlags(signUpButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivitySignUpPage();
            }
        });

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateProfile();
            }
        });

        typeface = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        customFontTextView = findViewById(R.id.logoCoinz);
        customFontTextView.setTypeface(typeface);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();


    }

    private void openCreateProfile() {
        Intent intent = new Intent(this,CreateProfilePage.class);
        startActivity(intent);
    }

    private void openActivitySignUpPage() {
        Intent intent = new Intent(this,SignUpPage.class);
        startActivity(intent);
    }

}
