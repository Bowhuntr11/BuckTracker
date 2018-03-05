package com.evilforge.bucktracker;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.BinderThread;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

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
    Button saveStand;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_stand);

        standName = findViewById(R.id.stand_name);
        saveStand = findViewById(R.id.saveNewStand);

        saveStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStand();
            }
        });
    }

    private void saveStand() {
        String standNames = standName.getText().toString();
        String latitude = "null";
        String longitude = "null";
        FirebaseDatabase.getInstance().getReference("stands")
                .child(standNames)
                .setValue(new Stands(standNames, latitude, longitude));
        finish();
    }
}

