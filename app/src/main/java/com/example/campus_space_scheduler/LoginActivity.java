package com.example.campus_space_scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPassword = findViewById(R.id.editTextPassword);

        TextView forgotPasswordTextView = findViewById(R.id.textViewForgotPassword);
        if (forgotPasswordTextView != null) {
            forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                    startActivity(intent);
                }
            });
        }

        Button googleLoginButton = findViewById(R.id.buttonLoginViaGoogle);
        if (googleLoginButton != null) {
            googleLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Mock Google Login as Student
                    Intent intent = new Intent(LoginActivity.this, StudentSpaceSelectionActivity.class);
                    intent.putExtra("ROLE", "student");
                    startActivity(intent);
                }
            });
        }

        Button loginButton = findViewById(R.id.buttonLogin);
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextPassword != null && editTextPassword.getText() != null) {
                        String password = editTextPassword.getText().toString();
                        if (password.equals("student")) {
                            Intent intent = new Intent(LoginActivity.this, StudentSpaceSelectionActivity.class);
                            intent.putExtra("ROLE", "student");
                            startActivity(intent);
                        } else if (password.equals("faculty")) {
                            Intent intent = new Intent(LoginActivity.this, FacultySpaceSelectionActivity.class);
                            intent.putExtra("ROLE", "faculty");
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid password (use 'student' or 'faculty')", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
