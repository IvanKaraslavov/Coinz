package com.example.android.coinz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;



public class CreateProfilePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    static boolean firstTimeUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        ImageButton mapButton = findViewById(R.id.arrowbutton);
        TextView username = findViewById(R.id.usernamefield);
        mapButton.setOnClickListener(view -> {
            //Choosing a unique username for the user
            if(username.getText().toString().equals("")) {
                Toast.makeText(CreateProfilePage.this, "Please enter a nickname.",
                        Toast.LENGTH_LONG).show();
            } else {
                FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                assert currentUser != null;
                CollectionReference users = mDatabase.collection("users");
                Query usernameQuery = users.whereEqualTo("username", username.getText().toString());
                Task<QuerySnapshot> usernameSnapshot = usernameQuery.get();
                usernameSnapshot.addOnCompleteListener(taskSend -> {
                    List<DocumentSnapshot> queryDocuments = Objects.requireNonNull(usernameSnapshot.getResult()).getDocuments();
                    if (!queryDocuments.isEmpty()) {
                        Toast.makeText(CreateProfilePage.this, "There is already a player with this username!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        mDatabase.collection("users").document(currentUser.getUid())
                                .update("username", username.getText().toString());
                        firstTimeUser = true;
                        openActivityMainActivity();
                    }
                });
            }
        });
    }

    private void openActivityMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
