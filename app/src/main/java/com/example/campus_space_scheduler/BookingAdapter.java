package com.example.campus_space_scheduler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Booking booking);
    }

    public BookingAdapter(List<Booking> bookingList, OnItemClickListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.textViewDate.setText(booking.getDate());
        holder.textViewTimeSlot.setText(booking.getTimeSlot());
        holder.textViewPurpose.setText("Purpose: " + booking.getPurpose());
        holder.textViewStatus.setText(booking.getStatus());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(booking));

        switch (booking.getStatus()) {
            case "Accepted":
            case "Booked":
                holder.textViewStatus.setBackgroundColor(0xFF4CAF50); // Green
                break;
            case "Pending":
                holder.textViewStatus.setBackgroundColor(0xFFFFC107); // Amber
                break;
            default:
                holder.textViewStatus.setBackgroundColor(0xFF757575); // Grey
                break;
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewTimeSlot, textViewPurpose, textViewStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTimeSlot = itemView.findViewById(R.id.textViewTimeSlot);
            textViewPurpose = itemView.findViewById(R.id.textViewPurpose);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
