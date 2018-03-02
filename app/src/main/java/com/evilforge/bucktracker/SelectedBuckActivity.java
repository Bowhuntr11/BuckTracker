package com.evilforge.bucktracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class SelectedBuckActivity extends AppCompatActivity {

    ImageView defaultImage;
    Bucks buck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_buck);

        defaultImage = findViewById(R.id.defaultBuckImage);

        Intent intent = getIntent();
        buck = (Bucks) intent.getSerializableExtra("Bucks");
        String picUrl = buck.getDefaultPhotoURL();
        Picasso.with(this).load(picUrl).into(defaultImage);
    }
}
