package com.evilforge.bucktracker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class StandsMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Marker marker = null;
    private FloatingActionButton save_btn;
    private double latitude = 33;
    private double longitude = -100;
    private String standName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stands_maps);

        Intent intent = getIntent();
        if(intent.hasExtra("standName")){
                standName = intent.getStringExtra("standName");
            Log.d("NewStand", standName);
        }


        save_btn = findViewById(R.id.fab_save_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StandsMapsActivity.this, NewStand.class);
                intent.putExtra("longitude", String.valueOf(longitude));
                intent.putExtra("latitude", String.valueOf(latitude));
                intent.putExtra("standName", standName);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng startLocation = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions()
                                    .position(startLocation)
                                    .draggable(true)
                                    .title("Stand Location");


        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 12));
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng arg0) {
                if(marker != null) {
                    marker.remove();
                }
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(arg0).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                marker = map.addMarker(markerOption);


                LatLng position = marker.getPosition();
                latitude = position.latitude;
                longitude = position.longitude;

                Log.d("Latitude", String.valueOf(latitude));
                Log.d("Longitude", String.valueOf(longitude));
            }
        });

    }
}
