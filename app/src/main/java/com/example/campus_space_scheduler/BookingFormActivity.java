package com.example.campus_space_scheduler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookingFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form);

        String spaceName = getIntent().getStringExtra("SPACE_NAME");
        String date = getIntent().getStringExtra("DATE");
        String timeSlot = getIntent().getStringExtra("TIME_SLOT");
        String role = getIntent().getStringExtra("ROLE");
        String spaceType = getIntent().getStringExtra("SPACE_TYPE");

        TextView spaceNameTextView = findViewById(R.id.textViewSpaceName);
        spaceNameTextView.setText(spaceName);

        TextView lorLabel = findViewById(R.id.textViewLorLabel);
        Button uploadLorButton = findViewById(R.id.buttonUploadLor);
        TextView noFileTextView = findViewById(R.id.textViewNoFile);
        Button downloadLorFormatButton = findViewById(R.id.buttonDownloadLorFormat);

        lorLabel.setVisibility(View.GONE);
        uploadLorButton.setVisibility(View.GONE);
        noFileTextView.setVisibility(View.GONE);
        downloadLorFormatButton.setVisibility(View.GONE);


        if ("student".equals(role)) {
            if ("Lab".equals(spaceType) || "Hall".equals(spaceType)) {
                lorLabel.setText("Upload LOR *");
                lorLabel.setVisibility(View.VISIBLE);
                uploadLorButton.setVisibility(View.VISIBLE);
                noFileTextView.setVisibility(View.VISIBLE);
                downloadLorFormatButton.setVisibility(View.VISIBLE);
            }

        } else if ("faculty".equals(role)) {
            if ("Lab".equals(spaceType) || "Hall".equals(spaceType)) {
                lorLabel.setText("Upload LOR");
                lorLabel.setVisibility(View.VISIBLE);
                uploadLorButton.setVisibility(View.VISIBLE);
                noFileTextView.setVisibility(View.VISIBLE);
                downloadLorFormatButton.setVisibility(View.VISIBLE);
            }

        }

        downloadLorFormatButton.setOnClickListener(v -> {
            Toast.makeText(BookingFormActivity.this, "LOR format downloaded", Toast.LENGTH_SHORT).show();
        });

        Button submitButton = findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(v -> {
            Toast.makeText(BookingFormActivity.this, "Booking request submitted successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
