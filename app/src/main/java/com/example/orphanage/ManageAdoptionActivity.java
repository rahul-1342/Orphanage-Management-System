package com.example.orphanage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManageAdoptionActivity extends AppCompatActivity {

    private ListView lvAdoptions;
    private Button btnRefreshAdoptions, btnAddAdoption;
    private ArrayList<String> adoptionList;
    private ArrayAdapter<String> adapter;

    private DatabaseReference adoptionRequestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_adoptions);

        // Initialize Firebase Database reference
        adoptionRequestsRef = FirebaseDatabase.getInstance().getReference("AdoptionRequests");

        // Initialize UI components
        lvAdoptions = findViewById(R.id.lvAdoptions);
        btnRefreshAdoptions = findViewById(R.id.btnRefreshAdoptions);
        btnAddAdoption = findViewById(R.id.btnAddAdoption);

        // Initialize adoption list and adapter
        adoptionList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adoptionList);
        lvAdoptions.setAdapter(adapter);

        // Load adoption requests
        loadAdoptionRequests();

        // Handle Refresh button click
        btnRefreshAdoptions.setOnClickListener(v -> loadAdoptionRequests());

        // Handle Add Adoption button click
        btnAddAdoption.setOnClickListener(v -> {
            Intent intent = new Intent(ManageAdoptionActivity.this, AdoptionManagementActivity.class);
            startActivity(intent);
        });

        // Handle ListView item click
        lvAdoptions.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedRequest = adoptionList.get(position);
            String[] parts = selectedRequest.split(" - ");
            String applicationId = parts[0]; // Assuming the first part is the application ID

            Intent intent = new Intent(ManageAdoptionActivity.this, UpdateAdoptionStatusActivity.class);
            intent.putExtra("applicationId", applicationId);
            startActivity(intent);
        });
    }

    private void loadAdoptionRequests() {
        adoptionList.clear();

        adoptionRequestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String applicationId = snapshot.getKey();
                    String adopterName = snapshot.child("adopterName").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);

                    if (applicationId != null && adopterName != null && status != null) {
                        String requestDetails = applicationId + " - " + adopterName + " - Status: " + status;
                        adoptionList.add(requestDetails);
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to load adoption requests.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
