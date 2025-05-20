package com.example.mini_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView noAccountText;
    Button loginButton;
    EditText emailEditText, passwordEditText;
    DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        noAccountText = findViewById(R.id.noAccountText);
        loginButton = findViewById(R.id.loginButton);

        dbHelper = new DBHelper(this);  // Initialize DBHelper

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = dbHelper.getUserByEmail(email);

            if (user != null && user.getPassword().equals(password)) {
                // Credentials matched
                String role = user.getRole();
                if ("admin".equalsIgnoreCase(role)) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                } else if ("user".equalsIgnoreCase(role)) {
                    startActivity(new Intent(MainActivity.this, Users.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "User role unknown", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Email or password incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        noAccountText.setOnClickListener(v -> {
            Intent backToSignUp = new Intent(MainActivity.this, CreateAccount.class);
            startActivity(backToSignUp);
        });
    }
}
