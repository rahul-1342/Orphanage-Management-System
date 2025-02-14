package com.example.orphanage;



import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ManageDonorsActivity extends AppCompatActivity {

    private ListView lvDonors;
    private Button btnAddDonor;
    private ArrayList<String> donorList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_donors);

        // Initialize UI components
        lvDonors = findViewById(R.id.lvDonors);
        btnAddDonor = findViewById(R.id.btnAddDonor);

        // Sample data for donors (this would come from a database in a real app)
        donorList = new ArrayList<>();
        donorList.add("Alice Johnson, Donation: $100");
        donorList.add("Bob Lee, Donation: $200");

        // Setup adapter and ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, donorList);
        lvDonors.setAdapter(adapter);

        // Handle Add New Donor button click
        btnAddDonor.setOnClickListener(view -> {
            Intent intent = new Intent(ManageDonorsActivity.this, AddDonorActivity.class);
            startActivity(intent);
        });

        // Handle ListView item click
        lvDonors.setOnItemClickListener((adapterView, view, position, id) -> {
            String donorDetails = donorList.get(position);
            Toast.makeText(ManageDonorsActivity.this, "Selected: " + donorDetails, Toast.LENGTH_SHORT).show();
        });
    }
}
