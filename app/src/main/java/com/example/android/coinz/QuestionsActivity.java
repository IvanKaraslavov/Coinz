package com.example.android.coinz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class QuestionsActivity extends Activity {
    private String tag = "QuestionsActivity";
    public static boolean testing = false;

    @SuppressLint({"LogNotTimber", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //Resizing the activity

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, (int)(height * .90));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        List<Question> questions = Question.getQuestions();
        int number;
        if(testing) {
            number = 9;
        } else {
            number = (int) (Math.random() * ( questions.size()));
        }
        Question question = questions.get(number);
        TextView title = findViewById(R.id.question_title);
        title.setText(question.getTitle());

        TextView firstAnswer = findViewById(R.id.first_answer);
        firstAnswer.setText(question.getAnswers().get(0));
        if(number == 2) {
            firstAnswer.setTextSize(20);
        }

        TextView secondAnswer = findViewById(R.id.second_answer);
        secondAnswer.setText(question.getAnswers().get(1));
        if(number == 2) {
            secondAnswer.setTextSize(20);
        }

        TextView thirdAnswer = findViewById(R.id.third_answer);
        thirdAnswer.setText(question.getAnswers().get(2));
        if(number == 2) {
            thirdAnswer.setTextSize(20);
        }

        //Update the amount of gold coins if the answer is correct

        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = mDatabase.collection("users").document(Objects.requireNonNull(currentUser).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                long steps = (long) document.get("steps");
                double currCoinValue = (double) document.get("currCoinValue");
                if (Objects.requireNonNull(document).exists()) {
                    firstAnswer.setOnClickListener(v -> {
                        if (question.getAnswers().get(0).equals(question.getCorrectAnswer())) {
                            firstAnswer.setBackgroundResource(R.drawable.correct);
                            firstAnswer.setTextColor(Color.parseColor("#FFFFFF"));
                            double goldCoinsValue = (double) document.get("goldCoinsAmount");
                            double bonus = goldCoinsValue + (steps / 10) * currCoinValue;
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("goldCoinsAmount", bonus);
                            Toast.makeText(getApplicationContext(), "Correct answer! Based on your travelled distance and the value of the coin your get " + String.format("%.2f", (steps / 10) * currCoinValue)
                                            + " bonus gold coins!",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                     else {
                        firstAnswer.setBackgroundResource(R.drawable.wrong);
                        firstAnswer.setTextColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(getApplicationContext(), "Wrong answer!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                    secondAnswer.setOnClickListener(v -> {
                        if(question.getAnswers().get(1).equals(question.getCorrectAnswer())) {
                            secondAnswer.setBackgroundResource(R.drawable.correct);
                            secondAnswer.setTextColor(Color.parseColor("#FFFFFF"));
                            double goldCoinsValue = (double) document.get("goldCoinsAmount");
                            double bonus = goldCoinsValue + (steps / 10) * currCoinValue;
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("goldCoinsAmount", bonus);
                            Toast.makeText(getApplicationContext(), "Correct answer! Based on your travelled distance and the value of the coin your get " + String.format("%.2f", (steps / 10) * currCoinValue)
                                            + " bonus gold coins!",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            secondAnswer.setBackgroundResource(R.drawable.wrong);
                            secondAnswer.setTextColor(Color.parseColor("#FFFFFF"));
                            Toast.makeText(getApplicationContext(), "Wrong answer!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    });

                    thirdAnswer.setOnClickListener(v -> {
                        if(question.getAnswers().get(2).equals(question.getCorrectAnswer())) {
                            thirdAnswer.setBackgroundResource(R.drawable.correct);
                            thirdAnswer.setTextColor(Color.parseColor("#FFFFFF"));
                            double goldCoinsValue = (double) document.get("goldCoinsAmount");
                            double bonus = goldCoinsValue + (steps / 10) * currCoinValue;
                            mDatabase.collection("users").document(currentUser.getUid())
                                    .update("goldCoinsAmount", bonus);
                            Toast.makeText(getApplicationContext(), "Correct answer! Based on your travelled distance and the value of the coin your get " + String.format("%.2f", (steps / 10) * currCoinValue)
                                            + " bonus gold coins!",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            thirdAnswer.setBackgroundResource(R.drawable.wrong);
                            thirdAnswer.setTextColor(Color.parseColor("#FFFFFF"));
                            Toast.makeText(getApplicationContext(), "Wrong answer!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    });
                } else {
                    Log.d(tag, "No such document");
                }
            } else {
                Log.d(tag, "get failed with ", task.getException());
            }
        });

        ImageView exit = findViewById(R.id.exit_questions_popup);
        exit.setOnClickListener(view -> finish());
    }
}
