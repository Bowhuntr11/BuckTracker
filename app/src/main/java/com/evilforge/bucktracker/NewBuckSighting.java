package com.evilforge.bucktracker;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class NewBuckSighting extends AppCompatActivity {

    private TextView buckName;
    private String buckNameString;
    private Button datePicker;
    private Spinner standSpinner;
    private Button saveBuck;
    private Bucks buck;

    private Long dateSeen;
    private String stand;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;


    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_buck_sighting);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        buckName = findViewById(R.id.buck_name);
        datePicker = findViewById(R.id.dateSeenButton);
        standSpinner = findViewById(R.id.standSpinner);
        saveBuck = findViewById(R.id.saveNewBuck);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        Intent intent = getIntent();
        buck = (Bucks) intent.getSerializableExtra("Bucks");
        buckNameString = buck.getBuckName();
        buckName.setText(buckNameString);

        mDatabase.child("stands").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> standNames = new ArrayList<String>();

                for (DataSnapshot standSnapshot : dataSnapshot.getChildren()) {
                    Stands stand = standSnapshot.getValue(Stands.class);
                    String standName = null;
                    if (stand != null) {
                        standName = stand.getStandName();
                    } else {
                        standName = "None Loaded";
                    }
                    standNames.add(standName);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(NewBuckSighting.this, android.R.layout.simple_spinner_item, standNames);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                standSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        saveBuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBuck();
            }
        });
    }

    private void saveBuck() {
        Calendar calendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
        dateSeen = calendar.getTimeInMillis();
        if (standSpinner.getSelectedItem().toString() != null) {
            stand = standSpinner.getSelectedItem().toString();
        } else {
            stand = "NotSelected";
        }

        if (dateSeen > buck.getLastSeen()) {
            FirebaseDatabase.getInstance().getReference("bucks")
                    .child(buckNameString)
                    .child("lastSeen")
                    .setValue(dateSeen);
        }

        FirebaseDatabase.getInstance().getReference("bucks")
                .child(buckNameString)
                .child("buckSightings")
                .push()
                .setValue(new BuckSighting(stand, dateSeen));
        finish();
    }

    private void timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        updateLabel();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void datePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void updateLabel() {
        String myFormat = "MMM d yyyy (HH:mm)"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        datePicker.setText(sdf.format(myCalendar.getTime()));
    }
}

