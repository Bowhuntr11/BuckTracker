package com.evilforge.bucktracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class NewStand extends AppCompatActivity {

    private EditText standName;
    private Button location;
    private Button saveStand;
    private String latitude = "null";
    private String longitude = "null";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_stand);

        standName = findViewById(R.id.stand_name);
        location = findViewById(R.id.select_location_button);
        saveStand = findViewById(R.id.saveNewStand);

        Log.d("NewStand", "intent started");

        Intent intent = getIntent();
        if(intent.hasExtra("standName")){
            String standNames = intent.getStringExtra("standName");
            Log.d("NewStand", standNames);
            standName.setText(standNames);
            standName.invalidate();
        }
        if(intent.hasExtra("latitude")){
            latitude = intent.getStringExtra("latitude");
            Log.d("NewStand", latitude);
        }
        if(intent.hasExtra("longitude")){
            longitude = intent.getStringExtra("longitude");
            Log.d("NewStand", longitude);
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewStand.this, StandsMapsActivity.class);
                String standNames = standName.getText().toString();
                if (!standNames.matches("")) {
                    intent.putExtra("standName", standNames);
                }
                finish();
                startActivity(intent);
            }
        });


        saveStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStand();
            }
        });

    }

    private void saveStand() {
        String standNames = standName.getText().toString();
        if (!standNames.equalsIgnoreCase("") || latitude.equalsIgnoreCase("null") || longitude.equalsIgnoreCase("null")) {
            FirebaseDatabase.getInstance().getReference("stands")
                    .child(standNames)
                    .setValue(new Stands(standNames, latitude, longitude));
            finish();
        } else {
            Toast.makeText(NewStand.this, "Fill out all information", Toast.LENGTH_SHORT).show();
        }
    }
}

