package com.example.orphanage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageStaffActivity extends AppCompatActivity {

    private ListView lvStaff;
    private Button btnAcceptStaff, btnRejectStaff, btnAddChildren;
    private ArrayList<String> staffList;
    private ArrayList<String> staffIds; // To keep track of staff IDs
    private ArrayAdapter<String> adapter;

    private DatabaseReference staffRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        // Initialize Firebase Database Reference
        staffRef = FirebaseDatabase.getInstance().getReference("StaffRequests");

        // Initialize UI components
        lvStaff = findViewById(R.id.lvStaff);
        btnAcceptStaff = findViewById(R.id.btn_accept);
        btnRejectStaff = findViewById(R.id.btn_reject);
        btnAddChildren = findViewById(R.id.btnAddChildren);

        // Initialize Lists
        staffList = new ArrayList<>();
        staffIds = new ArrayList<>();

        // Setup Adapter and ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, staffList);
        lvStaff.setAdapter(adapter);

        // Load staff data from Firebase
        loadStaffData();

        // Handle Manage Children button click
        btnAddChildren.setOnClickListener(v -> {
            Intent intent = new Intent(ManageStaffActivity.this, ChildManagementActivity.class);
            startActivity(intent);
        });

        // Handle Accept Staff button click
        btnAcceptStaff.setOnClickListener(view -> handleStaffAction("Accepted"));

        // Handle Reject Staff button click
        btnRejectStaff.setOnClickListener(view -> handleStaffAction("Rejected"));

        // Handle ListView item click
        lvStaff.setOnItemClickListener((adapterView, view, position, id) -> {
            String staffDetails = staffList.get(position);
            Toast.makeText(this, "Selected: " + staffDetails, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Loads staff data from Firebase and updates the ListView.
     */
    private void loadStaffData() {
        staffRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                staffList.clear();
                staffIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String staffId = snapshot.getKey();
                    HashMap<String, String> staffDetails = (HashMap<String, String>) snapshot.getValue();

                    if (staffDetails != null) {
                        String staffInfo = staffDetails.get("name") + ", Gender: " + staffDetails.get("role");
                        staffList.add(staffInfo);
                        staffIds.add(staffId);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ManageStaffActivity.this, "Failed to load staff data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handles accepting or rejecting staff members.
     *
     * @param action "Accepted" or "Rejected"
     */
    private void handleStaffAction(String action) {
        if (!staffList.isEmpty()) {
            String selectedStaff = staffList.get(0);
            String staffId = staffIds.get(0);

            // Remove from the list and update UI
            staffList.remove(0);
            staffIds.remove(0);
            adapter.notifyDataSetChanged();

            // Update status in Firebase
            staffRef.child(staffId).child("status").setValue(action).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, action + ": " + selectedStaff, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update status in database.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Application to " + action.toLowerCase() + ".", Toast.LENGTH_SHORT).show();
        }
    }
}
