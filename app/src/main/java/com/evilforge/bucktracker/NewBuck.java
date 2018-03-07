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

public class NewBuck extends AppCompatActivity {

    private EditText buckName;
    private CheckBox isShooter;
    private Button datePicker;
    private Spinner standSpinner;
    private Button uploadBuckPic;
    private Button saveBuck;

    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;

    private Long dateSeen;
    private String photoURL;
    private String stand;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    final Calendar myCalendar = Calendar.getInstance();

    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_buck);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        buckName = findViewById(R.id.buck_name);
        isShooter = findViewById(R.id.shooter_checkbox);
        datePicker = findViewById(R.id.dateSeenButton);
        standSpinner = findViewById(R.id.standSpinner);
        uploadBuckPic = findViewById(R.id.uploadBuckPic);
        saveBuck = findViewById(R.id.saveNewBuck);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        uploadBuckPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

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

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(NewBuck.this, android.R.layout.simple_spinner_item, standNames);
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
                try {
                    uploadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveBuck() {
        boolean isShooterSelected = isShooter.isChecked();
        Calendar calendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
        dateSeen = calendar.getTimeInMillis();
        if (standSpinner.getSelectedItem().toString() != null) {
            stand = standSpinner.getSelectedItem().toString();
        } else {
            stand = "NotSelected";
        }
        String buckNames = buckName.getText().toString();
        FirebaseDatabase.getInstance().getReference("bucks")
                .child(buckNames)
                .setValue(new Bucks(buckNames, isShooterSelected, dateSeen, stand, photoURL));
        mDatabase = FirebaseDatabase.getInstance().getReference("bucks")
                .child(buckNames).child("pictures");
        mDatabase.push().setValue(photoURL);
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() throws IOException {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            Bitmap newBitMap = getResizedBitmap(bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();


            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(NewBuck.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoURL = taskSnapshot.getDownloadUrl().toString();
                    progressDialog.dismiss();
                    saveBuck();
                    Toast.makeText(NewBuck.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }

    public Bitmap getResizedBitmap(Bitmap bitmap) {
        final int maxSize = 960;
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
    }
}

