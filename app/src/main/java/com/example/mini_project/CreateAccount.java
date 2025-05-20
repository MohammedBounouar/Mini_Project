package com.example.mini_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        MaterialButton createAccountButton = findViewById(R.id.createAccountButton);
        TextInputEditText usernameEditText = findViewById(R.id.usernameEditText);
        TextInputEditText emailEditText = findViewById(R.id.emailEditText);
        TextInputEditText passwordEditText = findViewById(R.id.passwordEditText);
        TextInputEditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        createAccountButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(CreateAccount.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!password.equals(confirmPassword)) {
                Toast.makeText(CreateAccount.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(CreateAccount.this);

            if(dbHelper.getUserIdByEmail(email) != -1) {
                Toast.makeText(CreateAccount.this, "Email already registered", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = dbHelper.insertUser(username, email, password); // Assuming your insertUser takes these params

            if(result != -1) {
                Toast.makeText(CreateAccount.this, "Account created successfully", Toast.LENGTH_LONG).show();
                // Redirect to login
                Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(CreateAccount.this, "Failed to create account", Toast.LENGTH_SHORT).show();
            }
        });

        TextView alreadyHaveAccountText = findViewById(R.id.alreadyHaveAccountText);

        alreadyHaveAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
