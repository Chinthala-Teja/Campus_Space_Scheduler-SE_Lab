package com.example.campus_space_scheduler;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CancelRequestActivity extends AppCompatActivity {

    private List<Booking> bookingList;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_request);

        TextView textViewSpaceName = findViewById(R.id.textViewSpaceName);
        RecyclerView recyclerViewBookings = findViewById(R.id.recyclerViewBookings);

        String spaceName = getIntent().getStringExtra("SPACE_NAME");
        textViewSpaceName.setText(spaceName);

        // Dummy data for booking requests
        bookingList = new ArrayList<>();
        bookingList.add(new Booking("12 Aug", "10:00–11:00", "Mid-term exam", "Pending"));
        bookingList.add(new Booking("15 Aug", "14:00–15:00", "Project discussion", "Accepted"));
        bookingList.add(new Booking("20 Aug", "09:00–10:00", "Guest lecture", "Booked"));

        adapter = new BookingAdapter(bookingList, booking -> {
            showCancelConfirmationDialog(booking);
        });

        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBookings.setAdapter(adapter);
    }

    private void showCancelConfirmationDialog(Booking booking) {
        String spaceName = getIntent().getStringExtra("SPACE_NAME");
        String message = "Are you sure you want to cancel the booking for " + spaceName + " on " + booking.getDate() + " (" + booking.getTimeSlot() + ")?";

        new AlertDialog.Builder(this)
                .setTitle("Cancel Booking Confirmation")
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> {
                    int position = bookingList.indexOf(booking);
                    if (position != -1) {
                        bookingList.remove(position);
                        adapter.notifyItemRemoved(position);
                        // Also notify for item range change to update subsequent item positions
                        adapter.notifyItemRangeChanged(position, bookingList.size());
                        Toast.makeText(CancelRequestActivity.this, "Booking request cancelled successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
