package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter adapter;
    private String tag = "NotificationsActivity";
    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        TextView notificationsText = findViewById(R.id.notifications_text);
        notificationsText.setTypeface(typeface);

        ImageView exit = findViewById(R.id.exit_notifications_popup);
        exit.setOnClickListener(view -> finish());

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    Log.d(tag, "DocumentSnapshot data: " + document.getData());
                    // Display the notifications
                    mDatabase.collection("users").document(document.getId())
                            .update("newNotifications", false);
                    listView = findViewById(R.id.notifications_list);
                    String[] notificationsArray = Objects.requireNonNull(document.get("notifications")).toString()
                            .replaceAll("\\[", "").replaceAll("]", "").split(", ");
                    List<String> notifications = new ArrayList<>();
                    for (String notification : notificationsArray) {
                        if (!notification.isEmpty()) {
                            notifications.add(notification);
                        }
                    }
                    List<String> reversedNotifications = Lists.reverse(notifications);
                    adapter = new ArrayAdapter<>(NotificationsActivity.this, R.layout.list_white_text, reversedNotifications);
                    listView.setAdapter(adapter);
                } else {
                    Log.d(tag, "No such document");
                }
            } else {
                Log.d(tag, "get failed with ", task.getException());
            }
        });
    }

    }
