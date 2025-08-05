package com.example.myapplication;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    TextView welcomeText;
    Button btnLogout;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeText = findViewById(R.id.txtWelcome);
        btnLogout = findViewById(R.id.btnLogout);

        // 🔓 Load SharedPreferences
        sharedPreferences = getSharedPreferences("MyLoginAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        // 🛑 If user not logged in, go to Register screen
        if (username == null) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
            return;
        }

        // ✅ Show welcome message with username
        welcomeText.setText("Welcome, " + username + "!");

        // 🔁 Logout: Clear saved username & go to Register screen
        btnLogout.setOnClickListener(v -> {
            sharedPreferences.edit().remove("username").apply();
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        });
    }
}
