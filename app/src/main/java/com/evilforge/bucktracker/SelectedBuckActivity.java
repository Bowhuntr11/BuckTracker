package com.evilforge.bucktracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SelectedBuckActivity extends AppCompatActivity {

    ImageView defaultImage;
    TextView buckNameView;
    Bucks buck;
    String buckName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_buck);

        defaultImage = findViewById(R.id.defaultBuckImage);
        buckNameView = findViewById(R.id.selected_buck_name);

        Intent intent = getIntent();
        buck = (Bucks) intent.getSerializableExtra("Bucks");
        String picUrl = buck.getDefaultPhotoURL();
        Picasso.with(this)
                .load(picUrl)
                .placeholder(R.drawable.loading)
                .into(defaultImage);

        buckName = buck.getBuckName();
        buckNameView.setText(buckName);

    }
}
