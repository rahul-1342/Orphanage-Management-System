package com.example.orphanage;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FeedbackSupportActivity extends AppCompatActivity {

    private EditText etFeedback;
    private Button btnSubmitFeedback;
    private RatingBar ratingBar;

    private DatabaseReference feedbackRef, supportRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_support);

        // Initialize views
        etFeedback = findViewById(R.id.etFeedback);
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);

        // Initialize Firebase references
        feedbackRef = FirebaseDatabase.getInstance().getReference("Feedback");
        supportRef = FirebaseDatabase.getInstance().getReference("SupportTickets");

        // Handle feedback submission
        btnSubmitFeedback.setOnClickListener(v -> submitFeedback());
        float rating = ratingBar.getRating();
        String feedback = etFeedback.getText().toString();



    }

    private void submitFeedback() {
        String feedback = etFeedback.getText().toString().trim();

        if (TextUtils.isEmpty(feedback)) {
            Toast.makeText(this, "Please enter your feedback.", Toast.LENGTH_SHORT).show();
            return;
        }

        String feedbackId = feedbackRef.push().getKey();
        HashMap<String, String> feedbackData = new HashMap<>();
        feedbackData.put("id", feedbackId);
        feedbackData.put("feedback", feedback);

        if (feedbackId != null) {
            feedbackRef.child(feedbackId).setValue(feedbackData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                    etFeedback.setText(""); // Clear input field
                } else {
                    Toast.makeText(this, "Failed to submit feedback. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}


