package com.example.campus_space_scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class FacultySpaceSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_space_selection);

        MaterialCardView cardLabs = findViewById(R.id.cardLabs);
        MaterialCardView cardHalls = findViewById(R.id.cardHalls);

        cardLabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultySpaceSelectionActivity.this, LabListActivity.class);
                intent.putExtra("IS_FACULTY", true);
                startActivity(intent);
            }
        });

        cardHalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultySpaceSelectionActivity.this, HallListActivity.class);
                intent.putExtra("IS_FACULTY", true);
                startActivity(intent);
            }
        });
    }
}
