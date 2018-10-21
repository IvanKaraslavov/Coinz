package com.example.android.coinz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class CreateProfilePage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        ImageButton mapButton = findViewById(R.id.arrowbutton);
        TextView username = findViewById(R.id.usernamefield);
        mapButton.setOnClickListener(view -> {
            if(username.getText().toString().equals("")) {
                Toast.makeText(CreateProfilePage.this, "Please enter a nickname.",
                        Toast.LENGTH_LONG).show();
            } else {
                FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                assert currentUser != null;
                mDatabase.collection("users").document(currentUser.getUid())
                        .update("username", username.getText().toString());
                openActivityMainActivity();
            }
        });
    }

    private void openActivityMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
