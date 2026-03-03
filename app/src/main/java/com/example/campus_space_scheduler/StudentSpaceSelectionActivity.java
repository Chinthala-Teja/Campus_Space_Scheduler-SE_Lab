package com.example.campus_space_scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class StudentSpaceSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_space_selection);

        MaterialCardView cardClassrooms = findViewById(R.id.cardClassrooms);
        MaterialCardView cardLabs = findViewById(R.id.cardLabs);
        MaterialCardView cardHalls = findViewById(R.id.cardHalls);

        cardClassrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentSpaceSelectionActivity.this, ClassroomListActivity.class));
            }
        });

        cardLabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentSpaceSelectionActivity.this, LabListActivity.class));
            }
        });

        cardHalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentSpaceSelectionActivity.this, HallListActivity.class));
            }
        });
    }
}
