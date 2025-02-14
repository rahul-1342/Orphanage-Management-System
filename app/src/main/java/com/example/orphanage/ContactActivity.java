package com.example.orphanage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactActivity extends AppCompatActivity {

    // Firebase database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Initialize Firebase database reference
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Messages");

        // Initialize UI elements
        TextView tvPhone = findViewById(R.id.tv_phone);
        TextView tvEmail = findViewById(R.id.tv_email);
        Button btnCall = findViewById(R.id.btn_call);
        Button btnEmail = findViewById(R.id.btn_email);
        Button btnMap = findViewById(R.id.btn_map);
        EditText etName = findViewById(R.id.et_name);
        EditText etEmail = findViewById(R.id.et_email);
        EditText etMessage = findViewById(R.id.et_message);
        Button btnSubmit = findViewById(R.id.btn_submit);

        // Call button action
        btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+919021620400")); // Replace with actual phone number
            startActivity(intent);
        });

        // Email button action
        btnEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:contact@orphanage.com")); // Replace with actual email address
            intent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");
            startActivity(intent);
        });

        // Map button action
        btnMap.setOnClickListener(v -> {
            Uri mapUri = Uri.parse("geo:0,0?q=JQ7F+W3R,+Rahatani Link Rd, Laxman Nagar, Thergaon,+Pimpri-Chinchwad, ,+Maharashtra"); // Replace with actual location
            Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
            startActivity(intent);
        });

        // Submit button action
        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String message = etMessage.getText().toString().trim();

            // Validate fields
            if (name.isEmpty()) {
                etName.setError("Name is required");
                etName.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                etEmail.requestFocus();
                return;
            }

            // Validate email format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Please enter a valid email address");
                etEmail.requestFocus();
                return;
            }

            if (message.isEmpty()) {
                etMessage.setError("Message is required");
                etMessage.requestFocus();
                return;
            }

            // Save message to Firebase
            saveMessageToFirebase(name, email, message);
        });
    }

    // Method to save message to Firebase
    private void saveMessageToFirebase(String name, String email, String message) {
        // Generate a unique ID for the message
        String messageId = databaseReference.push().getKey();

        // Create a Message object
        Message messageObject = new Message(name, email, message);

        if (messageId != null) {
            // Store the message under the unique ID
            databaseReference.child(messageId).setValue(messageObject)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ContactActivity.this, "Thank you for reaching out! We'll get back to you soon.", Toast.LENGTH_SHORT).show();

                            // Clear input fields after successful submission
                            ((EditText) findViewById(R.id.et_name)).setText("");
                            ((EditText) findViewById(R.id.et_email)).setText("");
                            ((EditText) findViewById(R.id.et_message)).setText("");
                        } else {
                            Toast.makeText(ContactActivity.this, "Failed to send message. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Error: Could not generate a unique ID.", Toast.LENGTH_SHORT).show();
        }
    }

    // Message class to map data for Firebase
    public static class Message {
        public String name;
        public String email;
        public String message;

        // Default constructor required for Firebase
        public Message() {}

        // Parameterized constructor
        public Message(String name, String email, String message) {
            this.name = name;
            this.email = email;
            this.message = message;
        }
    }
}
