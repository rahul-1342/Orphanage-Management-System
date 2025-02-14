package com.example.orphanage;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryActivity extends AppCompatActivity {

    private EditText etItemName, etItemQuantity;
    private Button btnAddItem;
    private RecyclerView rvInventoryList;

    private InventoryAdapter inventoryAdapter;
    private ArrayList<InventoryItem> inventoryList;

    private DatabaseReference inventoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Initialize views
        etItemName = findViewById(R.id.etItemName);
        etItemQuantity = findViewById(R.id.etItemQuantity);
        btnAddItem = findViewById(R.id.btnAddItem);
        rvInventoryList = findViewById(R.id.rvInventoryList);

        // Initialize RecyclerView
        rvInventoryList.setLayoutManager(new LinearLayoutManager(this));
        inventoryList = new ArrayList<>();
        inventoryAdapter = new InventoryAdapter(inventoryList);
        rvInventoryList.setAdapter(inventoryAdapter);

        // Firebase Database reference
        inventoryRef = FirebaseDatabase.getInstance().getReference("Inventory");

        // Fetch inventory data
        fetchInventory();

        // Add new item to inventory
        btnAddItem.setOnClickListener(v -> addItemToInventory());
    }

    private void fetchInventory() {
        inventoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inventoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    InventoryItem item = snapshot.getValue(InventoryItem.class);
                    if (item != null) {
                        inventoryList.add(item);

                        // Check for low stock
                        if (item.getQuantity() < 10) {
                            Toast.makeText(InventoryActivity.this, "Low stock alert: " + item.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                inventoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(InventoryActivity.this, "Failed to load inventory.", Toast.LENGTH_SHORT).show();
                Log.e("InventoryActivity", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    private void addItemToInventory() {
        String itemName = etItemName.getText().toString().trim();
        String itemQuantityStr = etItemQuantity.getText().toString().trim();

        if (itemName.isEmpty() || itemQuantityStr.isEmpty()) {
            Toast.makeText(this, "Please enter item name and quantity.", Toast.LENGTH_SHORT).show();
            return;
        }

        int itemQuantity = Integer.parseInt(itemQuantityStr);

        String itemId = inventoryRef.push().getKey();
        InventoryItem newItem = new InventoryItem(itemId, itemName, itemQuantity);

        if (itemId != null) {
            inventoryRef.child(itemId).setValue(newItem).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Item added to inventory.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to add item.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
