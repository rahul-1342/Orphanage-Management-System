package com.example.orphanage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateAdoptionStatusActivity extends AppCompatActivity {

    private TextView tvAdoptionDetails;
    private Button btnApprove, btnReject;

    private String applicationId;
    private DatabaseReference adoptionRequestsRef, staffRequestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_adoption_status);

        // Get application ID from intent
        applicationId = getIntent().getStringExtra("applicationId");
        adoptionRequestsRef = FirebaseDatabase.getInstance().getReference("AdoptionRequests").child(applicationId);
        staffRequestsRef = FirebaseDatabase.getInstance().getReference("StaffRequests");

        // Initialize UI components
        tvAdoptionDetails = findViewById(R.id.tvAdoptionDetails);
        btnApprove = findViewById(R.id.btnApprove);
        btnReject = findViewById(R.id.btnReject);

        // Load adoption request details
        loadAdoptionDetails();

        // Set button actions
        btnApprove.setOnClickListener(v -> updateStatus("Approved"));
        btnReject.setOnClickListener(v -> updateStatus("Rejected"));
    }

    private void loadAdoptionDetails() {
        adoptionRequestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String adopterName = task.getResult().child("adopterName").getValue(String.class);
                String contact = task.getResult().child("contact").getValue(String.class);
                String childPreference = task.getResult().child("childPreference").getValue(String.class);
                String status = task.getResult().child("status").getValue(String.class);

                tvAdoptionDetails.setText("Adopter Name: " + adopterName + "\nContact: " + contact
                        + "\nChild Preference: " + childPreference + "\nStatus: " + status);
            } else {
                Toast.makeText(this, "Failed to load adoption details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatus(String newStatus) {
        adoptionRequestsRef.child("status").setValue(newStatus).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();

                if ("Approved".equals(newStatus)) {
                    addToStaffRequests();
                } else {
                    finish();
                }
            } else {
                Toast.makeText(this, "Failed to update status.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToStaffRequests() {
        adoptionRequestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String adopterName = task.getResult().child("adopterName").getValue(String.class);
                String contact = task.getResult().child("contact").getValue(String.class);
                String childPreference = task.getResult().child("childPreference").getValue(String.class);

                String staffId = staffRequestsRef.push().getKey();
                if (staffId != null) {
                    staffRequestsRef.child(staffId).setValue(new StaffRequest(adopterName, contact, childPreference, "Pending"))
                            .addOnCompleteListener(addTask -> {
                                if (addTask.isSuccessful()) {
                                    Toast.makeText(this, "Application added to staff management.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Failed to add to staff management.", Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            });
                } else {
                    Toast.makeText(this, "Failed to generate staff ID.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to fetch application details for staff management.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper class to structure staff data
    static class StaffRequest {
        public String name;
        public String contact;
        public String role;
        public String status;

        public StaffRequest(String name, String contact, String role, String status) {
            this.name = name;
            this.contact = contact;
            this.role = role;
            this.status = status;
        }
    }
}
