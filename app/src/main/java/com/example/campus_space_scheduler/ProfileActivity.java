package com.example.campus_space_scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LinearLayout rollNumberSection = findViewById(R.id.rollNumberSection);
        TextView resetPassword = findViewById(R.id.textViewResetPassword);
        TextView logout = findViewById(R.id.textViewLogout);

        String role = getIntent().getStringExtra("ROLE");

        // Show Roll Number only for students
        if ("student".equals(role)) {
            rollNumberSection.setVisibility(View.VISIBLE);
        } else {
            rollNumberSection.setVisibility(View.GONE);
        }

        resetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            // Clear the activity stack and start a new task for the login screen
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
