package com.example.campus_space_scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AvailableTimeSlotsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_time_slots);

        TextView spaceNameTextView = findViewById(R.id.textViewSpaceName);
        TextView dateTextView = findViewById(R.id.textViewDate);

        String spaceName = getIntent().getStringExtra("SPACE_NAME");
        String date = getIntent().getStringExtra("DATE");
        String role = getIntent().getStringExtra("ROLE");
        String spaceType = getIntent().getStringExtra("SPACE_TYPE");

        spaceNameTextView.setText(spaceName);
        dateTextView.setText(date);

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            String timeSlot = b.getText().toString();

            Intent intent = new Intent(AvailableTimeSlotsActivity.this, BookingFormActivity.class);
            intent.putExtra("SPACE_NAME", spaceName);
            intent.putExtra("DATE", date);
            intent.putExtra("TIME_SLOT", timeSlot);
            intent.putExtra("ROLE", role);
            intent.putExtra("SPACE_TYPE", spaceType);
            startActivity(intent);
        };

        findViewById(R.id.buttonSlot1).setOnClickListener(listener);
        findViewById(R.id.buttonSlot2).setOnClickListener(listener);
        findViewById(R.id.buttonSlot3).setOnClickListener(listener);
    }
}
