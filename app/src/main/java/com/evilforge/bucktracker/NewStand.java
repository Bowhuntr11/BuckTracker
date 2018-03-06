package com.evilforge.bucktracker;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.BinderThread;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

public class NewStand extends AppCompatActivity {

    EditText standName;
    Button location;
    Button saveStand;
    String latitude = "null";
    String longitude = "null";


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
                Intent intent = new Intent(NewStand.this, MapsActivity.class);
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

