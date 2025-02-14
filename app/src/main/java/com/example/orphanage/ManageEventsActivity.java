package com.example.orphanage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class ManageEventsActivity extends AppCompatActivity {

    private ListView lvEvents;
    private Button btnAddEvent;
    private ArrayList<String> eventList;
    private ArrayAdapter<String> adapter;

    // Firebase Database reference
    private DatabaseReference eventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);

        // Initialize UI components
        lvEvents = findViewById(R.id.lvEvents);
        btnAddEvent = findViewById(R.id.btnAddEvent);

        // Firebase reference to the "Events" node
        eventsRef = FirebaseDatabase.getInstance().getReference("Events");

        // Initialize event list and adapter
        eventList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventList);
        lvEvents.setAdapter(adapter);

        // Fetch events from Firebase
        fetchEventsFromFirebase();

        // Handle Add New Event button click
        btnAddEvent.setOnClickListener(view -> {
            Intent intent = new Intent(ManageEventsActivity.this, AddEventActivity.class);
            startActivity(intent);
        });

        // Handle ListView item click
        lvEvents.setOnItemClickListener((adapterView, view, position, id) -> {
            String eventDetails = eventList.get(position);
            Toast.makeText(ManageEventsActivity.this, "Selected: " + eventDetails, Toast.LENGTH_SHORT).show();
        });
    }

    // Fetch events from Firebase and update the ListView
    private void fetchEventsFromFirebase() {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();  // Clear the existing list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String event = snapshot.child("title").getValue(String.class);
                    eventList.add(event);  // Add the event to the list
                }
                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Toast.makeText(ManageEventsActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
