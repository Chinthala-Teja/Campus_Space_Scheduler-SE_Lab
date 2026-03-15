package com.example.campus_space_scheduler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BookingDetailsActivity";
    private TextView bookedByTextView;
    private TextView approvedByTextView;
    private TextView textViewRemarks;
    private String bookingId, scheduleId, timeSlot, slotStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        // Get views
        ImageView buttonBack = findViewById(R.id.buttonBack);
        TextView spaceNameTextView = findViewById(R.id.textViewSpaceName);
        TextView statusTextView = findViewById(R.id.textViewStatus);
        TextView dateTimeTextView = findViewById(R.id.textViewDateTime);
        TextView purposeTextView = findViewById(R.id.textViewPurpose);
        TextView descriptionTextView = findViewById(R.id.textViewDescription);
        TextView requestedOnTextView = findViewById(R.id.textViewRequestedOn);
        bookedByTextView = findViewById(R.id.textViewBookedBy);
        approvedByTextView = findViewById(R.id.textViewApprovedBy);
        textViewRemarks = findViewById(R.id.textViewRemarks);
        MaterialButton buttonViewLor = findViewById(R.id.buttonViewLor);
        MaterialButton buttonCancelBooking = findViewById(R.id.buttonCancelBooking);

        // Get data from intent
        bookingId = getIntent().getStringExtra("BOOKING_ID");
        scheduleId = getIntent().getStringExtra("SCHEDULE_ID");
        slotStart = getIntent().getStringExtra("SLOT_START");
        String spaceName = getIntent().getStringExtra("SPACE_NAME");
        String bookedById = getIntent().getStringExtra("BOOKED_BY_ID");
        String date = getIntent().getStringExtra("DATE");
        timeSlot = getIntent().getStringExtra("TIME_SLOT");
        String purpose = getIntent().getStringExtra("PURPOSE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String status = getIntent().getStringExtra("STATUS");
        String remarks = getIntent().getStringExtra("REMARKS");
        String actionBy = getIntent().getStringExtra("ACTION_BY");
        String requestedOn = getIntent().getStringExtra("REQUESTED_ON");
        boolean hasLor = getIntent().getBooleanExtra("HAS_LOR", false);
        String lorUrl = getIntent().getStringExtra("LOR_UPLOAD");
        boolean showCancelButton = getIntent().getBooleanExtra("SHOW_CANCEL_BUTTON", false);

        // Set initial data to views
        spaceNameTextView.setText(spaceName != null ? spaceName : "N/A");
        statusTextView.setText(status != null ? status : "PENDING");
        dateTimeTextView.setText((date != null ? date : "") + " | " + (timeSlot != null ? timeSlot : ""));
        purposeTextView.setText(purpose != null ? purpose : "N/A");
        descriptionTextView.setText(description != null ? description : "No description provided.");
        requestedOnTextView.setText(requestedOn != null ? requestedOn : "N/A");
        
        if (bookedById != null) {
            fetchUserName(bookedById);
        } else {
            bookedByTextView.setText("N/A");
        }

        // UI styling for status
        updateStatusUI(status, statusTextView);

        // Handle Approval/Rejection and Remarks
        if (status != null && !status.equalsIgnoreCase("Pending")) {
            String label = status.equalsIgnoreCase("Approved") || status.equalsIgnoreCase("Accepted") ? "Approved by: " : "Rejected by: ";
            approvedByTextView.setText(label + (actionBy != null ? actionBy : "Authority"));
            approvedByTextView.setVisibility(View.VISIBLE);

            if (remarks != null && !remarks.isEmpty()) {
                textViewRemarks.setText("Remarks: " + remarks);
                textViewRemarks.setVisibility(View.VISIBLE);
            } else {
                textViewRemarks.setVisibility(View.GONE);
            }
        } else {
            approvedByTextView.setVisibility(View.GONE);
            textViewRemarks.setVisibility(View.GONE);
        }

        if (hasLor && lorUrl != null && !lorUrl.isEmpty()) {
            buttonViewLor.setVisibility(View.VISIBLE);
            buttonViewLor.setOnClickListener(v -> {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(lorUrl));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid LOR Link", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            buttonViewLor.setVisibility(View.GONE);
        }

        // Handle Cancellation Button
        if (showCancelButton && status != null && !status.equalsIgnoreCase("Rejected")) {
            buttonCancelBooking.setVisibility(View.VISIBLE);
            buttonCancelBooking.setOnClickListener(v -> showCancelConfirmationDialog());
        } else {
            buttonCancelBooking.setVisibility(View.GONE);
        }

        buttonBack.setOnClickListener(v -> finish());
    }

    private void showCancelConfirmationDialog() {
        new MaterialAlertDialogBuilder(this, R.style.Theme_CampusSpaceScheduler_Dialog_Custom)
                .setTitle("Confirm Cancellation")
                .setMessage("Are you sure you want to cancel this booking? This action cannot be undone.")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> performCancellation())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void performCancellation() {
        if (bookingId == null || scheduleId == null) {
            Toast.makeText(this, "Error: Missing IDs", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings").child(bookingId);
        DatabaseReference slotsRef = FirebaseDatabase.getInstance().getReference("schedules").child(scheduleId).child("slots");

        // 1. Delete the booking
        bookingRef.removeValue().addOnSuccessListener(aVoid -> {
            // 2. Make the slot AVAILABLE again in schedules
            slotsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean updated = false;
                    for (DataSnapshot slotSnapshot : snapshot.getChildren()) {
                        String start = slotSnapshot.child("start").getValue(String.class);
                        if (start != null) {
                            String normalizedStart = start.replace(":", "");
                            if (normalizedStart.equals(slotStart)) {
                                slotSnapshot.getRef().child("status").setValue("AVAILABLE");
                                updated = true;
                                break;
                            }
                        }
                    }
                    
                    // Fallback to timeSlot string matching if slotStart is missing
                    if (!updated && timeSlot != null) {
                        for (DataSnapshot slotSnapshot : snapshot.getChildren()) {
                            String start = slotSnapshot.child("start").getValue(String.class);
                            String end = slotSnapshot.child("end").getValue(String.class);
                            if (start != null && end != null) {
                                String label = start + " - " + end;
                                if (timeSlot.contains(label)) {
                                    slotSnapshot.getRef().child("status").setValue("AVAILABLE");
                                    break;
                                }
                            }
                        }
                    }
                    
                    Toast.makeText(BookingDetailsActivity.this, "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Cancel failed: " + error.getMessage());
                    Toast.makeText(BookingDetailsActivity.this, "Failed to update schedule", Toast.LENGTH_SHORT).show();
                }
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(BookingDetailsActivity.this, "Failed to cancel booking", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchUserName(String uid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String rollNumber = snapshot.child("roolno").getValue(String.class);
                    if (rollNumber == null) rollNumber = snapshot.child("rollnumber").getValue(String.class);
                    if (rollNumber == null) rollNumber = snapshot.child("rollNumber").getValue(String.class);
                    
                    if (name != null) {
                        if (rollNumber != null && !rollNumber.isEmpty()) {
                            bookedByTextView.setText(name + " (" + rollNumber + ")");
                        } else {
                            bookedByTextView.setText(name);
                        }
                    } else {
                        bookedByTextView.setText("Unknown User");
                    }
                } else {
                    bookedByTextView.setText("Unknown User");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bookedByTextView.setText("Error loading user info");
            }
        });
    }

    private void updateStatusUI(String status, TextView statusTextView) {
        if (status == null) return;
        
        switch (status.toUpperCase()) {
            case "ACCEPTED":
            case "APPROVED":
                statusTextView.setBackgroundResource(R.drawable.status_accepted_bg);
                break;
            case "REJECTED":
                statusTextView.setBackgroundResource(R.drawable.status_rejected_bg);
                break;
            default:
                statusTextView.setBackgroundResource(R.drawable.status_pending_bg);
                break;
        }
    }
}
