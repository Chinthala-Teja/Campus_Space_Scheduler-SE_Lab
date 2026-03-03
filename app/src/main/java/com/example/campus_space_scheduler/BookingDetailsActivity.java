package com.example.campus_space_scheduler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookingDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        // Get views
        TextView spaceNameTextView = findViewById(R.id.textViewSpaceName);
        TextView bookedByTextView = findViewById(R.id.textViewBookedBy);
        TextView dateTextView = findViewById(R.id.textViewDate);
        TextView timeSlotTextView = findViewById(R.id.textViewTimeSlot);
        TextView purposeTextView = findViewById(R.id.textViewPurpose);
        TextView descriptionTextView = findViewById(R.id.textViewDescription);
        TextView statusTextView = findViewById(R.id.textViewStatus);
        TextView approvedRejectedByTextView = findViewById(R.id.textViewApprovedRejectedBy);
        Button viewLorButton = findViewById(R.id.buttonViewLor);

        // Get data from intent
        String spaceName = getIntent().getStringExtra("SPACE_NAME");
        String bookedBy = getIntent().getStringExtra("BOOKED_BY");
        String date = getIntent().getStringExtra("DATE");
        String timeSlot = getIntent().getStringExtra("TIME_SLOT");
        String purpose = getIntent().getStringExtra("PURPOSE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String status = getIntent().getStringExtra("STATUS");
        String approvedOrRejectedBy = getIntent().getStringExtra("APPROVED_OR_REJECTED_BY");
        boolean hasLor = getIntent().getBooleanExtra("HAS_LOR", false);

        // Set data to views
        spaceNameTextView.setText(spaceName);
        bookedByTextView.setText("Booked by: " + bookedBy);
        dateTextView.setText(date);
        timeSlotTextView.setText(timeSlot);
        purposeTextView.setText("Purpose: " + purpose);
        descriptionTextView.setText("Description: " + description);

        if (description != null && !description.isEmpty()) {
            descriptionTextView.setVisibility(View.VISIBLE);
        } else {
            descriptionTextView.setVisibility(View.GONE);
        }

        statusTextView.setText("Status: " + status);

        // Handle conditional visibility
        if ("Approved".equals(status)) {
            approvedRejectedByTextView.setText("Approved by: " + approvedOrRejectedBy);
            approvedRejectedByTextView.setVisibility(View.VISIBLE);
        } else if ("Rejected".equals(status)) {
            approvedRejectedByTextView.setText("Rejected by: " + approvedOrRejectedBy);
            approvedRejectedByTextView.setVisibility(View.VISIBLE);
        } else {
            approvedRejectedByTextView.setVisibility(View.GONE);
        }

        if (hasLor) {
            viewLorButton.setVisibility(View.VISIBLE);
            viewLorButton.setOnClickListener(v -> {
                Toast.makeText(BookingDetailsActivity.this, "Viewing LOR...", Toast.LENGTH_SHORT).show();
            });
        } else {
            viewLorButton.setVisibility(View.GONE);
        }
    }
}
