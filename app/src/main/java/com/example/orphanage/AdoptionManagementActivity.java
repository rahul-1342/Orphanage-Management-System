package com.example.orphanage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class AdoptionManagementActivity extends AppCompatActivity {

    private EditText etAdopterName, etAdopterContact, etChildPreference;
    private Button btnSubmitApplication;
    private TextView tvApplicationStatus;

    private DatabaseReference adoptionRequestsRef, totalAdoptedRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption_management);

        // Initialize Firebase references
        adoptionRequestsRef = FirebaseDatabase.getInstance().getReference("AdoptionRequests");
        totalAdoptedRef = FirebaseDatabase.getInstance().getReference("totalAdopted");

        // Initialize Views
        etAdopterName = findViewById(R.id.et_adopter_name);
        etAdopterContact = findViewById(R.id.et_adopter_contact);
        etChildPreference = findViewById(R.id.et_child_preference);
        btnSubmitApplication = findViewById(R.id.btn_submit_application);
        tvApplicationStatus = findViewById(R.id.tv_application_status);

        // Initialize Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // Set up button actions
        btnSubmitApplication.setOnClickListener(v -> validateAndSubmitApplication());
    }

    private void validateAndSubmitApplication() {
        String name = etAdopterName.getText().toString().trim();
        String contact = etAdopterContact.getText().toString().trim();
        String childPreference = etChildPreference.getText().toString().trim();

        // Validate fields
        if (name.isEmpty() || contact.isEmpty() || childPreference.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate Adopter's Name (only alphabets and spaces)
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            Toast.makeText(this, "Adopter's name should contain only alphabets and spaces.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate Adopter's Contact (basic phone number validation)
        if (!contact.matches("^\\d{10}$")) {
            Toast.makeText(this, "Please enter a valid 10-digit phone number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress dialog
        progressDialog.setMessage("Submitting application...");
        progressDialog.show();

        // Save application to the database
        saveApplicationToDatabase(name, contact, childPreference);
    }

    private void saveApplicationToDatabase(String name, String contact, String childPreference) {
        String applicationId = adoptionRequestsRef.push().getKey();
        if (applicationId == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Failed to generate application ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create application details
        HashMap<String, Object> applicationDetails = new HashMap<>();
        applicationDetails.put("adopterName", name);
        applicationDetails.put("contact", contact);
        applicationDetails.put("childPreference", childPreference);
        applicationDetails.put("status", "Pending");
        applicationDetails.put("timestamp", ServerValue.TIMESTAMP); // Add timestamp

        // Save application and update total adoption count
        adoptionRequestsRef.child(applicationId).setValue(applicationDetails).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                incrementTotalAdopted();
                Toast.makeText(AdoptionManagementActivity.this, "Application submitted successfully.", Toast.LENGTH_SHORT).show();
                clearFields();
                tvApplicationStatus.setText("Application Status: Pending");
            } else {
                Toast.makeText(AdoptionManagementActivity.this, "Failed to submit application.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void incrementTotalAdopted() {
        totalAdoptedRef.setValue(ServerValue.increment(1)).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to update total adoption count: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearFields() {
        etAdopterName.setText("");
        etAdopterContact.setText("");
        etChildPreference.setText("");
    }
}
