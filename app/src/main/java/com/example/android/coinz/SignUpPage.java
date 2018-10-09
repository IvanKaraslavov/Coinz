package com.example.android.coinz;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpPage extends AppCompatActivity {

    private Button logInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        logInButton = findViewById(R.id.loginbutton);
        logInButton.setPaintFlags(logInButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMainPage();
            }
        });
    }

    private void openActivityMainPage() {
        Intent intent = new Intent(this,LoginPage.class);
        startActivity(intent);
    }
}
