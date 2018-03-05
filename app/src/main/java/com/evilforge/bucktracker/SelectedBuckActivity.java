package com.evilforge.bucktracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SelectedBuckActivity extends AppCompatActivity {

    ImageView defaultImage;
    TextView buckNameView;
    ListView buckSightings;
    FloatingActionButton newSighting;
    Bucks buck;
    String buckName;
    Long buckLastSeen;
    private FirebaseListAdapter<BuckSighting> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_buck);

        defaultImage = findViewById(R.id.defaultBuckImage);
        buckNameView = findViewById(R.id.selected_buck_name);
        buckSightings = findViewById(R.id.list_of_sightings);
        newSighting = findViewById(R.id.fab_buck_sighting);

        Intent intent = getIntent();
        buck = (Bucks) intent.getSerializableExtra("Bucks");
        String picUrl = buck.getDefaultPhotoURL();
        Picasso.with(this)
                .load(picUrl)
                .placeholder(R.drawable.loading)
                .into(defaultImage);

        buckName = buck.getBuckName();
        buckLastSeen = buck.getLastSeen();
        buckNameView.setText(buckName);

        displaySightings();

        newSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedBuckActivity.this, NewBuckSighting.class);
                intent.putExtra("Bucks", buck);
                startActivity(intent);
            }
        });
    }

    private void displaySightings(){
        //Suppose you want to retrieve "chats" in your Firebase DB:
        Query query = FirebaseDatabase.getInstance().getReference().child("bucks").child(buckName).child("buckSightings");
        //The error said the constructor expected FirebaseListOptions - here you create them:
        FirebaseListOptions<BuckSighting> options = new FirebaseListOptions.Builder<BuckSighting>()
                .setQuery(query, BuckSighting.class)
                .setLayout(R.layout.buck_sightings)
                .build();

        adapter = new FirebaseListAdapter<BuckSighting>(options) {
            @Override
            protected void populateView(View v, BuckSighting buckSighting, int position) {
                // Get references to the views of buck_sightings.xml
                TextView standName = v.findViewById(R.id.stand_seen);
                TextView seenDate = v.findViewById(R.id.seen_date);

                standName.setText(buckSighting.getStandName());

                // Format the date before showing it
                long val = buckSighting.getWhenSeen();
                Date date=new Date(val);
                SimpleDateFormat df2 = new SimpleDateFormat("MMM d yyyy (HH:mm)", Locale.getDefault());
                String dateText = df2.format(date);
                seenDate.setText(dateText);
            }
        };

        buckSightings.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
