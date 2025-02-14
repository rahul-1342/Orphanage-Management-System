package com.example.orphanage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddStaffActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private DatabaseReference childCountRef;

    private EditText etName, etAge, etHealth, etAdmissionDate, etEducation;
    private Button btnSave, btnDelete;
    private TextView tvApplications;
    private Button btnAccept, btnReject;

    private int childCount = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Children");
        childCountRef = FirebaseDatabase.getInstance().getReference("ChildCount");

        // Initialize Views
        etName = findViewById(R.id.et_child_name);
        etAge = findViewById(R.id.et_child_age);
        etHealth = findViewById(R.id.et_child_health);
        etAdmissionDate = findViewById(R.id.et_child_admission_date);
        etEducation = findViewById(R.id.et_child_education);
        btnSave = findViewById(R.id.btn_save_child);
        btnDelete = findViewById(R.id.btn_delete_child);
        btnAccept = findViewById(R.id.btn_accept);
        btnReject = findViewById(R.id.btn_reject);

        // Set initial text for Applications

        // Set button actions
        btnSave.setOnClickListener(v -> saveChildDetails());
        btnDelete.setOnClickListener(v -> deleteChildDetails());

        // Set click listener for Accept button
        btnAccept.setOnClickListener(v -> {
            Toast.makeText(AddStaffActivity.this, "Application Accepted", Toast.LENGTH_SHORT).show();
            tvApplications.setText("Application accepted successfully.");
        });

        // Set click listener for Reject button
        btnReject.setOnClickListener(v -> {
            Toast.makeText(AddStaffActivity.this, "Application Rejected", Toast.LENGTH_SHORT).show();
            tvApplications.setText("Application rejected successfully.");
        });
    }

    private void saveChildDetails() {
        String name = etName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String health = etHealth.getText().toString().trim();
        String admissionDate = etAdmissionDate.getText().toString().trim();
        String education = etEducation.getText().toString().trim();

        if (!validateInputs(name, age, health, admissionDate, education)) return;

        String childId = databaseReference.push().getKey();
        if (childId == null) {
            Toast.makeText(this, "Failed to generate child ID", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> childDetails = new HashMap<>();
        childDetails.put("name", name);
        childDetails.put("age", age);
        childDetails.put("health", health);
        childDetails.put("admissionDate", admissionDate);
        childDetails.put("education", education);

        databaseReference.child(childId).setValue(childDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        incrementChildCount();
                        Toast.makeText(this, "Child details saved successfully.", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(this, "Failed to save child details.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteChildDetails() {
        String name = etName.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter child name to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.orderByChild("name").equalTo(name).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            snapshot.getRef().removeValue().addOnCompleteListener(deleteTask -> {
                                if (deleteTask.isSuccessful()) {
                                    Toast.makeText(this, "Child details deleted successfully.", Toast.LENGTH_SHORT).show();
                                    clearFields();
                                } else {
                                    Toast.makeText(this, "Failed to delete child details.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(this, "No child found with this name.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void incrementChildCount() {
        childCountRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                childCount = snapshot.exists() && snapshot.getValue(Integer.class) != null
                        ? snapshot.getValue(Integer.class)
                        : 0;
                childCountRef.setValue(childCount + 1);
            } else {
                Toast.makeText(this, "Failed to get child count.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        etName.setText("");
        etAge.setText("");
        etHealth.setText("");
        etAdmissionDate.setText("");
        etEducation.setText("");
    }

    private boolean validateInputs(String name, String age, String health, String admissionDate, String education) {
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return false;
        }
        if (age.isEmpty() || !age.matches("\\d+") || Integer.parseInt(age) <= 0 || Integer.parseInt(age) > 18) {
            etAge.setError("Age must be between 1 and 18");
            etAge.requestFocus();
            return false;
        }
        if (health.isEmpty()) {
            etHealth.setError("Health status is required");
            etHealth.requestFocus();
            return false;
        }
        if (admissionDate.isEmpty() || !admissionDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            etAdmissionDate.setError("Admission date must be in YYYY-MM-DD format");
            etAdmissionDate.requestFocus();
            return false;
        }
        if (education.isEmpty()) {
            etEducation.setError("Education details are required");
            etEducation.requestFocus();
            return false;
        }
        return true;
    }
}
