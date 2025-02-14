package com.example.orphanage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChildManagementActivity extends AppCompatActivity {

    private EditText etName, etAge, etHealth, etAdmissionDate, etEducation;
    private Button btnSave, btnDelete;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_management);

        // Initialize Views
        etName = findViewById(R.id.et_child_name);
        etAge = findViewById(R.id.et_child_age);
        etHealth = findViewById(R.id.et_child_health);
        etAdmissionDate = findViewById(R.id.et_child_admission_date);
        etEducation = findViewById(R.id.et_child_education);
        btnSave = findViewById(R.id.btn_save_child);
        btnDelete = findViewById(R.id.btn_delete_child);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Children");

        // Save Child Details
        btnSave.setOnClickListener(v -> saveChildDetails());

        // Delete Child Details
        btnDelete.setOnClickListener(v -> deleteChildDetails());
    }

    private void saveChildDetails() {
        String name = etName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String health = etHealth.getText().toString().trim();
        String admissionDate = etAdmissionDate.getText().toString().trim();
        String education = etEducation.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty() || health.isEmpty() || admissionDate.isEmpty() || education.isEmpty()) {
            Toast.makeText(this, "Please fill all details.", Toast.LENGTH_SHORT).show();
            return;
        }

        String childId = databaseReference.push().getKey();

        if (childId != null) {
            HashMap<String, String> childDetails = new HashMap<>();
            childDetails.put("name", name);
            childDetails.put("age", age);
            childDetails.put("health", health);
            childDetails.put("admissionDate", admissionDate);
            childDetails.put("education", education);

            databaseReference.child(childId).setValue(childDetails).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Child details saved successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to save details.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteChildDetails() {
        String childId = "child_id_to_delete"; // You should get the correct childId to delete the right record

        if (childId != null) {
            // Delete the child data from Firebase Database
            databaseReference.child(childId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Child details deleted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to delete child details.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
