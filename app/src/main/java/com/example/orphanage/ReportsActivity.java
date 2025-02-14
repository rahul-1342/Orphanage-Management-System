package com.example.orphanage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportsActivity extends AppCompatActivity {

    private TextView tvTotalDonations, tvChildrenAdded, tvInventoryUsage, tvActivitiesReport, tvChildrenAdopted;
    private Button btnRefreshReports;

    private DatabaseReference donationsRef, adoptionsRef, inventoryRef, activitiesRef, childCountRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // Initialize Views
        tvTotalDonations = findViewById(R.id.tvTotalDonations);
        tvChildrenAdded = findViewById(R.id.tvChildrenAdded);
        tvChildrenAdopted = findViewById(R.id.tvChildrenAdopted);
        tvInventoryUsage = findViewById(R.id.tvInventoryUsage);
      //  tvActivitiesReport = findViewById(R.id.tvActivitiesReport);
        btnRefreshReports = findViewById(R.id.btnRefreshReports);

        // Initialize Firebase references
        donationsRef = FirebaseDatabase.getInstance().getReference("Donations");
        adoptionsRef = FirebaseDatabase.getInstance().getReference("totalAdopted");
        inventoryRef = FirebaseDatabase.getInstance().getReference("Inventory");
        activitiesRef = FirebaseDatabase.getInstance().getReference("Activities");
        childCountRef = FirebaseDatabase.getInstance().getReference("ChildCount");

        // Load and display reports on activity load
        fetchReports();

        // Refresh data on button click
        btnRefreshReports.setOnClickListener(v -> fetchReports());
    }

    private void fetchReports() {
        fetchTotalDonations();
        fetchChildrenAdded();
        fetchInventoryUsage();
        //fetchActivitiesReport();
        fetchChildrenAdopted();

    }

    private void fetchTotalDonations() {
        donationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalDonations = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String donationAmountStr = snapshot.child("donationAmount").getValue(String.class);
                    try {
                        if (donationAmountStr != null) {
                            totalDonations += Double.parseDouble(donationAmountStr);
                        }
                    } catch (NumberFormatException e) {
                        Log.e("ReportsActivity", "Invalid donation amount: " + donationAmountStr);
                    }
                }
                tvTotalDonations.setText("Total Donations Received: Rs " + totalDonations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReportsActivity.this, "Failed to load donations data.", Toast.LENGTH_SHORT).show();
                Log.e("ReportsActivity", "DatabaseError in fetchTotalDonations: " + databaseError.getMessage());
            }
        });
    }

    private void fetchChildrenAdded() {
        childCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long childCount = dataSnapshot.getValue(Long.class);
                if (childCount != null) {
                    tvChildrenAdded.setText("Number of Children Added: " + childCount);
                } else {
                    tvChildrenAdded.setText("No children added yet.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReportsActivity.this, "Failed to load child count.", Toast.LENGTH_SHORT).show();
                Log.e("ReportsActivity", "DatabaseError in fetchChildrenAdded: " + databaseError.getMessage());
            }
        });
    }

    private void fetchInventoryUsage() {
        inventoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder inventoryReport = new StringBuilder("Inventory Usage:\n");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    Long quantity = snapshot.child("quantity").getValue(Long.class);
                    if (name != null && quantity != null) {
                        inventoryReport.append(name).append(": ").append(quantity).append("\n");
                    }
                }
                tvInventoryUsage.setText(inventoryReport.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReportsActivity.this, "Failed to load inventory data.", Toast.LENGTH_SHORT).show();
                Log.e("ReportsActivity", "DatabaseError in fetchInventoryUsage: " + databaseError.getMessage());
            }
        });
    }

//    private void fetchActivitiesReport() {
//        activitiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                StringBuilder activitiesReport = new StringBuilder("Activities Report:\n");
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String activityName = snapshot.child("name").getValue(String.class);
//                    String date = snapshot.child("date").getValue(String.class);
//                    if (activityName != null && date != null) {
//                        activitiesReport.append(activityName).append(" on ").append(date).append("\n");
//                    }
//                }
//                tvActivitiesReport.setText(activitiesReport.toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(ReportsActivity.this, "Failed to load activities data.", Toast.LENGTH_SHORT).show();
//                Log.e("ReportsActivity", "DatabaseError in fetchActivitiesReport: " + databaseError.getMessage());
//            }
//        });
//    }

    private void fetchChildrenAdopted() {
        adoptionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Fetch the number of children adopted
                Long totalAdopted = dataSnapshot.getValue(Long.class);
                if (totalAdopted != null) {
                    tvChildrenAdopted.setText("Number of Children Adopted: " + totalAdopted);  // Corrected here
                } else {
                    tvChildrenAdopted.setText("No children adopted yet.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReportsActivity.this, "Failed to load child adoption data.", Toast.LENGTH_SHORT).show();
                Log.e("ReportsActivity", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }


}



