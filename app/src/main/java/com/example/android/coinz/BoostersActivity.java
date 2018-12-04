package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class BoostersActivity extends AppCompatActivity {

    private String tag = "BoostersActivity";
    private final int priceOfBooster = 3000;
    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boosters);

        Typeface typefaceSemiBold = Typeface.createFromAsset(getAssets(), "fonts/Oswald-SemiBold.ttf");
        Typeface typefaceSemiMedium = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Medium.ttf");
        TextView boosterText = findViewById(R.id.boosters_text);
        boosterText.setTypeface(typefaceSemiBold);

        TextView note = findViewById(R.id.booster_note);
        note.setTypeface(typefaceSemiMedium);

        ImageView exit = findViewById(R.id.exit_booster);
        exit.setOnClickListener(view -> finish());

        //Displaying the booster from the database and providing buy functionality

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    Log.d(tag, "DocumentSnapshot data: " + document.getData());
                    boolean boosterBought = (boolean) document.get("boosterBought");
                    double goldCoins = (double) document.get("goldCoinsAmount");
                    ImageButton boosterButton = findViewById(R.id.booster_button);
                    boosterButton.setOnClickListener(view -> {
                        if(boosterBought) {
                            Toast.makeText(BoostersActivity.this, "You already purchased the booster today.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            if(goldCoins < priceOfBooster) {
                                Toast.makeText(BoostersActivity.this, "Not enough gold coins.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(BoostersActivity.this, "The booster is bought.",
                                        Toast.LENGTH_LONG).show();
                                mDatabase.collection("users").document(currentUser.getUid())
                                        .update("boosterBought", true);
                                mDatabase.collection("users").document(currentUser.getUid())
                                        .update("goldCoinsAmount",  goldCoins - priceOfBooster);
                            }
                        }
                    });
                } else {
                    Log.d(tag, "No such document");
                }
            } else {
                Log.d(tag, "get failed with ", task.getException());
            }
        });
    }
}
