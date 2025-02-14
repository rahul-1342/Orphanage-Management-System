package com.example.orphanage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button signupButton;
    private TextView loginLink;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        signupButton = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.loginLink);

        // Signup Button Click
        signupButton.setOnClickListener(v -> {
            // Get input values from the fields
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            // Validation checks
            if (name.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                return;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(SignupActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                return;
            } else if (password.length() < 6) {
                Toast.makeText(SignupActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                return;
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Account created successfully
                            Toast.makeText(SignupActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                            // Sign out the user
                            FirebaseAuth.getInstance().signOut();

                            // Redirect to LoginActivity
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Display specific error
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(SignupActivity.this, "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Login Link Click
        loginLink.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
