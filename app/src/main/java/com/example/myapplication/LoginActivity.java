package com.example.myapplication;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEt, passwordEt;
    Button btnLogin;
    TextView RegisterSwitch;
    UserDBHelper db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 🔗 Link UI elements
        RegisterSwitch = findViewById(R.id.RegisterSwitch);
        usernameEt = findViewById(R.id.LoginUsername);
        passwordEt = findViewById(R.id.LoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        db = new UserDBHelper(this);
        sharedPreferences = getSharedPreferences("MyLoginAppPrefs", Context.MODE_PRIVATE);

        // 🔘 LOGIN button click
        btnLogin.setOnClickListener(v -> {
            String username = usernameEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            String dbPass = db.getPassword(username);
            if (dbPass == null) {
                Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
            } else if (dbPass.equals(password)) {
                // ✅ Successful login
                sharedPreferences.edit().putString("username", username).apply();

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        });

        // 🔄 Switch to Register
        RegisterSwitch.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }
}
