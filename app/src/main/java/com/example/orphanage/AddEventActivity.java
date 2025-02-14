package com.example.orphanage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddEventActivity extends AppCompatActivity {

    private EditText etEventTitle, etEventDescription, etEventDate, etEventTime;
    private Spinner spinnerEventCategory;
    private Button btnSubmitEvent;
    private DatabaseReference eventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Initialize Firebase Database reference
        eventsRef = FirebaseDatabase.getInstance().getReference("Events");

        // Initialize UI elements
        etEventTitle = findViewById(R.id.etEventTitle);
        etEventDescription = findViewById(R.id.etEventDescription);
        etEventDate = findViewById(R.id.etEventDate);
        etEventTime = findViewById(R.id.etEventTime);
        spinnerEventCategory = findViewById(R.id.spinnerEventCategory);
        btnSubmitEvent = findViewById(R.id.btnSubmitEvent);

        // Submit event button click
        btnSubmitEvent.setOnClickListener(v -> createEvent());
    }

    private void createEvent() {
        // Get values from the EditText and Spinner
        String title = etEventTitle.getText().toString().trim();
        String description = etEventDescription.getText().toString().trim();
        String date = etEventDate.getText().toString().trim();
        String time = etEventTime.getText().toString().trim();
        String category = spinnerEventCategory.getSelectedItem() != null ? spinnerEventCategory.getSelectedItem().toString() : "";

        // Log the values of the fields to check if any are empty
        Log.d("CreateEvent", "Title: " + title);
        Log.d("CreateEvent", "Description: " + description);
        Log.d("CreateEvent", "Date: " + date);
        Log.d("CreateEvent", "Time: " + time);
        Log.d("CreateEvent", "Category: " + category);

        // Validation checks
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter an event title.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (date.isEmpty()) {
            Toast.makeText(this, "Please enter a date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (time.isEmpty()) {
            Toast.makeText(this, "Please enter a time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (category.isEmpty()) {
            Toast.makeText(this, "Please select a category.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate date format (simple example: MM/dd/yyyy)
        if (!isValidDate(date)) {
            Toast.makeText(this, "Please enter a valid date (MM/dd/yyyy).", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate time format (simple example: HH:mm)
        if (!isValidTime(time)) {
            Toast.makeText(this, "Please enter a valid time (HH:mm).", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create unique event ID
        String eventId = eventsRef.push().getKey();
        if (eventId == null) {
            Log.e("CreateEvent", "Failed to generate event ID");
            return;
        }

        // Prepare data to save in Firebase
        HashMap<String, String> eventData = new HashMap<>();
        eventData.put("title", title);
        eventData.put("description", description);
        eventData.put("date", date);
        eventData.put("time", time);
        eventData.put("category", category);

        // Save the event data to Firebase
        eventsRef.child(eventId).setValue(eventData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("CreateEvent", "Event created successfully!");
                Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();

                // Navigate to ManageEventActivity after saving event
                Intent intent = new Intent(AddEventActivity.this, ManageEventActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            } else {
                Log.e("CreateEvent", "Failed to create event", task.getException());
                Toast.makeText(this, "Failed to create event.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to validate date format (simple MM/dd/yyyy format)
    private boolean isValidDate(String date) {
        String regex = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{4}$";
        return date.matches(regex);
    }

    // Method to validate time format (simple HH:mm format)
    private boolean isValidTime(String time) {
        String regex = "^(2[0-3]|[01]?[0-9]):([0-5]?[0-9])$";
        return time.matches(regex);
    }
}
