package com.example.orphanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Define the EventAdapter class
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private List<Event> eventList;

    // Constructor for EventAdapter
    public EventAdapter(List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the views
        Event event = eventList.get(position);
        holder.titleTextView.setText(event.getTitle());
        holder.dateTextView.setText(event.getDate());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // Define the ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle); // Replace with your IDs
            dateTextView = itemView.findViewById(R.id.textViewDate);   // Replace with your IDs
        }
    }
}
