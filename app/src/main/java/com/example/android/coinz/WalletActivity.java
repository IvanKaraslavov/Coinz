package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class WalletActivity extends AppCompatActivity {

    private String tag = "WalletActivity";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_wallet);

            ViewPager viewPager = findViewById(R.id.viewPager);
            setupViewPager(viewPager);

            Typeface typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
            TextView yourCoins = findViewById(R.id.your_coins_text);
            yourCoins.setTypeface(typefaceBold);

            Typeface typefaceLight = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Light.ttf");
            TextView coinsLeftText = findViewById(R.id.number_coins_left);
            coinsLeftText.setTypeface(typefaceLight);

            TextView coinsLeftNumber = findViewById(R.id.coins_number);
            coinsLeftNumber.setTypeface(typefaceLight);

            updateLayout();

            ImageView exit = findViewById(R.id.exit_wallet);
            exit.setOnClickListener(view -> finish());

        }

    @SuppressLint("LogNotTimber")
    private void updateLayout() {
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    Log.d(tag, "DocumentSnapshot data: " + document.getData());
                    TextView coinsNumber= findViewById(R.id.coins_number);
                    String str = Objects.requireNonNull(document.get("coinsLeft")).toString();
                    double coinsLeftDouble = Double.parseDouble(str);
                    int coinsLeft = (int) Math.floor(coinsLeftDouble);
                    coinsNumber.setText(String.valueOf(coinsLeft));
                } else {
                    Log.d(tag, "No such document");
                }
            } else {
                Log.d(tag, "get failed with ", task.getException());
            }
        });
    }

        //Setting View Pager
        private void setupViewPager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFrag(new GridViewFragmentWallet());
            viewPager.setAdapter(adapter);
        }
    }

