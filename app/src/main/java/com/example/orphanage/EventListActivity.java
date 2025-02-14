package com.example.orphanage;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    private RecyclerView rvEventList;
    private EventAdapter eventAdapter;
    private ArrayList<Event> eventList;
    private DatabaseReference eventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // Initialize RecyclerView and Firebase reference
        rvEventList = findViewById(R.id.rvEventList);
        rvEventList.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        rvEventList.setAdapter(eventAdapter);

        eventsRef = FirebaseDatabase.getInstance().getReference("Events");

        // Retrieve event data from Firebase
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    eventList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventListActivity.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
