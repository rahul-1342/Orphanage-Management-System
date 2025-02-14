package com.example.orphanage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Updated layout to match your description

        // Find the logout icon and set its click listener
        ImageView logoutIcon = findViewById(R.id.logoutIcon); // Ensure this ID matches the logout icon in your XML
        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Toast.makeText(HomepageActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomepageActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
                startActivity(intent);
                finish(); // Finish current activity
            }
        });

        // Find the CardViews by their IDs
        CardView childrenCard = findViewById(R.id.Children);
        CardView donorCard = findViewById(R.id.Donor);
        CardView staffCard = findViewById(R.id.staff);
        CardView eventsCard = findViewById(R.id.events);
        CardView inventoryCard = findViewById(R.id.inventory);
        CardView reportCard = findViewById(R.id.report);
        CardView contactCard = findViewById(R.id.contact);
        CardView feedbackCard = findViewById(R.id.feeback);

        // Set OnClickListeners for each CardView to navigate to the respective activity
        childrenCard.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, ManageAdoptionActivity.class);
            startActivity(intent);
        });

        donorCard.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, ManageDonorsActivity.class);
            startActivity(intent);
        });

        staffCard.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, ManageStaffActivity.class);
            startActivity(intent);
        });

        eventsCard.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, ManageEventsActivity.class);
            startActivity(intent);
        });

        inventoryCard.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, InventoryActivity.class);
            startActivity(intent);
        });

        reportCard.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, ReportsActivity.class);
            startActivity(intent);
        });

        contactCard.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, ContactActivity.class);
            startActivity(intent);
        });

        feedbackCard.setOnClickListener(view -> {
            Intent intent = new Intent(HomepageActivity.this, FeedbackSupportActivity.class);
            startActivity(intent);
        });

        // Handle back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
            }
        });
    }

    private void showExitDialog() {
        // Create a confirmation dialog
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false) // Prevent the dialog from being dismissed by tapping outside
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // Close the activity, this will exit the app if it's the last activity
                    }
                })
                .setNegativeButton("No", null) // Do nothing if "No" is clicked
                .show();
    }
}
