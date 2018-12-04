package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class AvatarsActivity extends AppCompatActivity {

    private String tag = "AvatarsActivity";
    private final int priceOfAvatars = 7000;
    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatars);

        Typeface typefaceLight = Typeface.createFromAsset(getAssets(), "fonts/Oswald-SemiBold.ttf");
        TextView shopText = findViewById(R.id.shop_text);
        shopText.setTypeface(typefaceLight);

        ImageView exit = findViewById(R.id.exit_shop);
        exit.setOnClickListener(view -> finish());

        //Displaying the avatars from the database and providing buy functionality

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    Log.d(tag, "DocumentSnapshot data: " + document.getData());
                    boolean dogBought = (boolean) document.get("dogBought");
                    boolean catBought = (boolean) document.get("catBought");
                    boolean koalaBought = (boolean) document.get("koalaBought");
                    double goldCoins = (double) document.get("goldCoinsAmount");

                    ImageButton rabbitButton = findViewById(R.id.red_buton);
                    rabbitButton.setOnClickListener(view -> {
                        mDatabase.collection("users").document(currentUser.getUid())
                                .update("currentAvatar",  1);
                        Toast.makeText(AvatarsActivity.this, "The avatar is changed.",
                                Toast.LENGTH_LONG).show();
                    });

                    ImageButton dogButton = findViewById(R.id.blue_button);
                    if(dogBought) {
                        dogButton.setImageResource(R.drawable.blue_button_change);
                    } else {
                        dogButton.setImageResource(R.drawable.blue_button_buy);
                    }
                    dogButton.setOnClickListener(view -> {
                        if(dogBought) {
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("currentAvatar",  2);
                            Toast.makeText(AvatarsActivity.this, "The avatar is changed.",
                                    Toast.LENGTH_LONG).show();
                        } else if(goldCoins < priceOfAvatars) {
                            Toast.makeText(AvatarsActivity.this, "Not enough gold coins.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AvatarsActivity.this, "The avatar is bought.",
                                    Toast.LENGTH_LONG).show();
                            dogButton.setImageResource(R.drawable.blue_button_change);
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("dogBought", true);
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("goldCoinsAmount",  goldCoins - priceOfAvatars);
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("currentAvatar",  2);
                        }

                    });

                    ImageButton catButton = findViewById(R.id.yellow_button);
                    if(catBought) {
                        catButton.setImageResource(R.drawable.yellow_button_change);
                    } else {
                        catButton.setImageResource(R.drawable.yellow_button_buy);
                    }
                    catButton.setOnClickListener(view -> {
                        if(catBought) {
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("currentAvatar",  3);
                            Toast.makeText(AvatarsActivity.this, "The avatar is changed.",
                                    Toast.LENGTH_LONG).show();
                        } else if(goldCoins < priceOfAvatars) {
                            Toast.makeText(AvatarsActivity.this, "Not enough gold coins.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AvatarsActivity.this, "The avatar is bought.",
                                    Toast.LENGTH_LONG).show();
                            catButton.setImageResource(R.drawable.yellow_button_change);
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("catBought", true);
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("goldCoinsAmount",  goldCoins - priceOfAvatars);
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("currentAvatar",  3);
                        }
                    });

                    ImageButton koalaButton = findViewById(R.id.green_button);
                    if(koalaBought) {
                        koalaButton.setImageResource(R.drawable.green_button_change);
                    } else {
                        koalaButton.setImageResource(R.drawable.green_button_buy);
                    }
                    koalaButton.setOnClickListener(view -> {
                        if(koalaBought) {
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("currentAvatar", 4);
                            Toast.makeText(AvatarsActivity.this, "The avatar is changed.",
                                    Toast.LENGTH_LONG).show();
                        } else if(goldCoins < priceOfAvatars) {
                            Toast.makeText(AvatarsActivity.this, "Not enough gold coins.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AvatarsActivity.this, "The avatar is bought.",
                                    Toast.LENGTH_LONG).show();
                            koalaButton.setImageResource(R.drawable.green_button_change);
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("koalaBought", true);
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("goldCoinsAmount",  goldCoins - priceOfAvatars);
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("currentAvatar", 4);
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
