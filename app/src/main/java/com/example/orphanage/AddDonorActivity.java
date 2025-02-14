package com.example.orphanage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class AddDonorActivity extends AppCompatActivity {

    private EditText etDonorName, etDonorEmail, etDonorPhone, etDonationAmount;
    private Spinner spinnerCategory;
    private Button btnDonate;
    private TextView tvStatus;
    private DatabaseReference donationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donor);

        // Initialize views
        etDonorName = findViewById(R.id.etDonorName);
        etDonorEmail = findViewById(R.id.etDonorEmail);
        etDonorPhone = findViewById(R.id.etDonorPhone);
        etDonationAmount = findViewById(R.id.etDonationAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnDonate = findViewById(R.id.btnDonate);
        tvStatus = findViewById(R.id.tvStatus);

        // Firebase Database reference
        donationsRef = FirebaseDatabase.getInstance().getReference("Donations");

        // Handle donate button click
        btnDonate.setOnClickListener(v -> handleDonation());
    }

    private void handleDonation() {
        Log.d("DonateButton", "handleDonation() triggered");

        // Collect input values
        String donorName = etDonorName.getText().toString().trim();
        String donorEmail = etDonorEmail.getText().toString().trim();
        String donorPhone = etDonorPhone.getText().toString().trim();
        String donationAmount = etDonationAmount.getText().toString().trim();
        String donationCategory = spinnerCategory.getSelectedItem().toString();

        // Validate inputs
        if (!validateInputs(donorName, donorEmail, donorPhone, donationAmount)) {
            return;
        }

        // Log inputs
        Log.d("Inputs", "Name: " + donorName + ", Email: " + donorEmail + ", Phone: " + donorPhone + ", Amount: " + donationAmount);

        // Save donation details to Firebase
        String donationId = donationsRef.push().getKey();
        HashMap<String, String> donationData = new HashMap<>();
        donationData.put("donorName", donorName);
        donationData.put("donorEmail", donorEmail);
        donationData.put("donorPhone", donorPhone);
        donationData.put("donationAmount", donationAmount);
        donationData.put("donationCategory", donationCategory);

        donationsRef.child(donationId).setValue(donationData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText("Donation successful! Thank you.");
                Toast.makeText(this, "Donation recorded successfully.", Toast.LENGTH_SHORT).show();

                // Generate PDF receipt
                generatePDFReceipt(donorName, donorEmail, donorPhone, donationAmount, donationCategory);
            } else {
                Toast.makeText(this, "Failed to record donation.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String donorName, String donorEmail, String donorPhone, String donationAmount) {
        if (donorName.isEmpty()) {
            etDonorName.setError("Name is required");
            etDonorName.requestFocus();
            return false;
        }

        if (donorEmail.isEmpty()) {
            etDonorEmail.setError("Email is required");
            etDonorEmail.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(donorEmail).matches()) {
            etDonorEmail.setError("Enter a valid email address");
            etDonorEmail.requestFocus();
            return false;
        }

        if (donorPhone.isEmpty()) {
            etDonorPhone.setError("Phone number is required");
            etDonorPhone.requestFocus();
            return false;
        }
        if (!donorPhone.matches("\\d{10}")) {
            etDonorPhone.setError("Enter a valid 10-digit phone number");
            etDonorPhone.requestFocus();
            return false;
        }

        if (donationAmount.isEmpty()) {
            etDonationAmount.setError("Donation amount is required");
            etDonationAmount.requestFocus();
            return false;
        }
        try {
            double amount = Double.parseDouble(donationAmount);
            if (amount <= 0) {
                etDonationAmount.setError("Enter a valid donation amount greater than zero");
                etDonationAmount.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etDonationAmount.setError("Enter a valid numeric amount");
            etDonationAmount.requestFocus();
            return false;
        }

        return true;
    }

    private void generatePDFReceipt(String donorName, String donorEmail, String donorPhone, String donationAmount, String donationCategory) {
        PdfDocument pdfDocument = new PdfDocument();

        // Page setup
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Drawing content
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setTextSize(14);

        // Header
        canvas.drawText("Donation Receipt", 90, 50, paint);

        // Content
        canvas.drawText("Donor Name: " + donorName, 10, 100, paint);
        canvas.drawText("Donor Email: " + donorEmail, 10, 130, paint);
        canvas.drawText("Donor Phone: " + donorPhone, 10, 160, paint);
        canvas.drawText("Donation Amount: $" + donationAmount, 10, 190, paint);
        canvas.drawText("Donation Category: " + donationCategory, 10, 220, paint);

        // Footer
        canvas.drawText("Thank you for your support!", 10, 270, paint);

        // Finish page
        pdfDocument.finishPage(page);

        // Save to Downloads folder
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "DonationReceipt.pdf");
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF saved to Downloads folder", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Close document
        pdfDocument.close();
    }
}
