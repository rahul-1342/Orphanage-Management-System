package com.example.orphanage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Navigate to HomepageActivity after the splash time-out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, HomepageActivity.class);
                startActivity(intent);
                finish(); // Remove the SplashScreenActivity from the back stack
            }
        }, SPLASH_TIME_OUT);
    }
}
