package com.example.myapplication;




import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editName, editUsername, editPassword;
    Button btnRegister;
    TextView loginSwitch;
    UserDBHelper db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyLoginAppPrefs", Context.MODE_PRIVATE);

        // ✅ Check if user is already logged in → open WelcomeActivity
        String savedUsername = sharedPreferences.getString("username", null);
        if (savedUsername != null) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 🧩 Link UI components
        editName = findViewById(R.id.editName);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.btnRegister);
        loginSwitch = findViewById(R.id.loginSwitch);

        db = new UserDBHelper(this);

        // 🟢 Register button click
        btnRegister.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            // 🔴 Empty field check
            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔴 Password length check
            if (password.length() < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔁 User already exists → go to WelcomeActivity
            if (db.userExists(username)) {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();

                editUsername.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_YES);
                editPassword.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_YES);

                sharedPreferences.edit().putString("username", username).apply();

                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // ✅ Register new user
                boolean success = db.registerUser(name, username, password);
                if (success) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    sharedPreferences.edit().putString("username", username).apply();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 🔄 Optional: "Already have account? Login" switch
        loginSwitch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
