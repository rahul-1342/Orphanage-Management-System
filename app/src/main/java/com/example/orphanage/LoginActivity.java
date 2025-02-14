package com.example.orphanage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView signupLink;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Validate input fields
            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(LoginActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                // Attempt to log in the user
                loginUser(email, password);
            }
        });

        // Signup link click listener
        signupLink.setOnClickListener(v -> {
            Log.d("LoginActivity", "Navigating to SignupActivity");
            // Navigate to SignupActivity
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // Redirect to HomepageActivity
                        Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Login failed
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Login failed";
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
