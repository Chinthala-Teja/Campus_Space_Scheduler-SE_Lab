package com.example.campus_space_scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class StudentRoomDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_room_dashboard);

        TextView roomNameTextView = findViewById(R.id.textViewRoomName);
        CalendarView calendarView = findViewById(R.id.calendarView);

        String spaceName = getIntent().getStringExtra("SPACE_NAME");
        String role = getIntent().getStringExtra("ROLE");
        String spaceType = getIntent().getStringExtra("SPACE_TYPE");

        if (spaceName != null) {
            roomNameTextView.setText(spaceName);
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                Intent intent = new Intent(StudentRoomDashboardActivity.this, AvailableTimeSlotsActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("DATE", selectedDate);
                intent.putExtra("ROLE", role);
                intent.putExtra("SPACE_TYPE", spaceType);
                startActivity(intent);
            }
        });

        Button cancelButton = findViewById(R.id.buttonCancelRequest);
        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomDashboardActivity.this, CancelRequestActivity.class);
            intent.putExtra("SPACE_NAME", spaceName);
            intent.putExtra("ROLE", role);
            startActivity(intent);
        });

        ImageView profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomDashboardActivity.this, ProfileActivity.class);
            intent.putExtra("ROLE", role);
            startActivity(intent);
        });

        ImageView historyIcon = findViewById(R.id.historyIcon);
        historyIcon.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRoomDashboardActivity.this, BookingHistoryActivity.class);
            intent.putExtra("SPACE_NAME", spaceName);
            startActivity(intent);
        });
    }
}
