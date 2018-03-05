package com.evilforge.bucktracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SelectedStandActivity extends AppCompatActivity {

    TextView standNameView;
    Stands stand;
    String standName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_stand);

        standNameView = findViewById(R.id.selected_stand_name);

        Intent intent = getIntent();
        stand = (Stands) intent.getSerializableExtra("Stands");

        standName = stand.getStandName();
        standNameView.setText(standName);

    }
}
