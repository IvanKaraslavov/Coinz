package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class BankActivity extends Activity {

    private String tag = "BankActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        TextView goldCoinsText = findViewById(R.id.gold_coinz_text);
        goldCoinsText.setTypeface(typeface);

        TextView goldCoins = findViewById(R.id.gold_coins);
        goldCoins.setTypeface(typeface);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, (int)(height * .65));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
        loadData();

        ImageView exit = findViewById(R.id.exit_bank_popup);
        exit.setOnClickListener(view -> finish());
    }

    @SuppressLint({"LogNotTimber", "SetTextI18n", "DefaultLocale"})
    private void loadData() {
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    Log.d(tag, "DocumentSnapshot data: " + document.getData());
                    TextView username = findViewById(R.id.gold_coins);
                    String goldCoins = Objects.requireNonNull(document.get("goldCoinsAmount")).toString();
                    username.setText(String.format("%.2f", Double.parseDouble(goldCoins)));
                } else {
                    Log.d(tag, "No such document");
                    TextView username = findViewById(R.id.gold_coins);
                    username.setText(0);
                }
            } else {
                Log.d(tag, "get failed with ", task.getException());
            }
        });
    }
}
